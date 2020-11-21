package com.example.photo.controller;

import com.example.photo.base.RestResult;
import com.example.photo.bean.ItemActionBean;
import com.example.photo.bean.PageBean;
import com.example.photo.bean.PhotoItemBean;
import com.example.photo.dao.*;
import com.example.photo.entry.*;
import com.example.photo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("photo")
public class PhotoController {
    @Autowired
    UserDao userDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    PhotoItemDao photoItemDao;
    @Autowired
    PhotoFavoriteDao photoFavoriteDao;
    @Autowired
    PhotoPraiseDao photoPraiseDao;

    //上传图片
    @PostMapping("/uploadItem")
    RestResult<PhotoItem> uploadItem(
            MultipartHttpServletRequest multipartRequest
    ) {
        try {
            Long userId = Long.valueOf(multipartRequest.getParameter("userId"));
            User user = userDao.findFirstByUserId(userId);
            if (user == null) {
                return RestResult.notFound();
            }
            String content = multipartRequest.getParameter("content");
            if (content == null) {
                content = "";
            }
            List<MultipartFile> files = multipartRequest.getFiles("file");
            if (files.size() == 0) {
                return RestResult.failed("请上传图片");
            }
            PhotoItem photoItem = new PhotoItem();
            photoItem.setPhotoContent(content);
            photoItem.setUserId(userId);
            photoItem.setUploadTime(System.currentTimeMillis());
            photoItem = photoItemDao.save(photoItem);
            int count = 0;
            for (MultipartFile file : files) {
                String name = file.getOriginalFilename();
                name = count + "." + FileUtils.getSuffix(name != null ? name : "");
                File temp = FileUtils.save("item/" + photoItem.getPhotoItemId(),name,file.getInputStream());
                if (temp != null) {
                    count += 1;
                }
            }
            if (count == files.size()) {
                return RestResult.success(photoItem);
            }else {
                FileUtils.deleteDir(FileUtils.SAVE_DIR + "item/"+ photoItem.getPhotoItemId());
                photoItemDao.delete(photoItem);
                return RestResult.failed("上传失败");
            }

        }catch (Exception e) {
            e.printStackTrace();
            return RestResult.failed(e.getMessage());
        }
    }

    //删除图片
    @PostMapping("/deleteItem")
    RestResult<String> deleteItem(
        @RequestBody ItemActionBean item
    ) {
        if (item == null || item.getPhotoItemId() == null || item.getUserId() == null) {
            return RestResult.failed("缺少部分信息");
        }
        PhotoItem photoItem = photoItemDao.findFirstByPhotoItemId(item.getPhotoItemId());
        if (photoItem == null) {
            return RestResult.notFound();
        }
        if (photoItem.getUserId().equals(item.getUserId())) {
            photoItemDao.delete(photoItem);
            FileUtils.deleteDir("item/" + photoItem.getPhotoItemId());
            return RestResult.success();
        }else {
            return RestResult.noAccess();
        }
    }

    /**
     * 分页查询图片
     * @param pageBean
     * @return
     */
    @PostMapping("getItems")
    RestResult<List<PhotoItemBean>> getItems(
            @RequestBody PageBean pageBean
    ) {
        if (pageBean == null || pageBean.getUserId() == null || pageBean.getPageNumber() == null) {
            return RestResult.failed("缺少部分信息");
        }
        PageRequest request = PageRequest.of(pageBean.getPageNumber(),20, Sort.by("uploadTime").descending());
        Page<PhotoItem> items = photoItemDao.findAll(request);
        ArrayList<PhotoItemBean> beans = new ArrayList<>();
        for (PhotoItem item : items) {
            PhotoItemBean bean = new PhotoItemBean();
            bean.setPhotoContent(item.getPhotoContent());
            bean.setUploadTime(item.getUploadTime());
            bean.setPhotoItemId(item.getPhotoItemId());
            List<PhotoFavorite> photoFavorites = photoFavoriteDao.findAllByPhotoItemId(item.getPhotoItemId());
            bean.setFavoriteCount(photoFavorites == null? 0 : photoFavorites.size());
            List<PhotoPraise> photoPraises = photoPraiseDao.findAllByPhotoItemId(item.getPhotoItemId());
            bean.setPraiseCount(photoPraises == null? 0 : photoPraises.size());
            UserInfo userInfo = userInfoDao.findFirstByUserId(item.getUserId());
            bean.setUploadUser(userInfo);
            bean.setImages(FileUtils.getFilesPath("item/" + item.getPhotoItemId()));
            for (int index = 0; index < bean.getImages().length; index++) {
                bean.getImages()[index] = "item/" + item.getPhotoItemId() + "/" + bean.getImages()[index];
            }
            if (photoPraiseDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setPraise(true);
            }
            if (photoFavoriteDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setFavorite(true);
            }
            beans.add(bean);
        }
        return RestResult.success(beans);

    }

    /**
     * 根据用户查询图片
     * @param pageBean
     * @return
     */
    @PostMapping("getItemsByUser")
    RestResult<List<PhotoItemBean>> getItemsByUser(
            @RequestBody PageBean pageBean
    ) {
        if (pageBean == null || pageBean.getUserId() == null || pageBean.getPageNumber() == null || pageBean.getPageUserId() == null) {
            return RestResult.failed("缺少部分信息");
        }
        PageRequest request = PageRequest.of(pageBean.getPageNumber(),20, Sort.by("uploadTime").descending());
        Page<PhotoItem> items = photoItemDao.findAllByUserId(pageBean.getPageUserId(),request);
        ArrayList<PhotoItemBean> beans = new ArrayList<>();
        for (PhotoItem item : items) {
            PhotoItemBean bean = new PhotoItemBean();
            bean.setPhotoContent(item.getPhotoContent());
            bean.setUploadTime(item.getUploadTime());
            bean.setPhotoItemId(item.getPhotoItemId());
            List<PhotoFavorite> photoFavorites = photoFavoriteDao.findAllByPhotoItemId(item.getPhotoItemId());
            bean.setFavoriteCount(photoFavorites == null? 0 : photoFavorites.size());
            List<PhotoPraise> photoPraises = photoPraiseDao.findAllByPhotoItemId(item.getPhotoItemId());
            bean.setPraiseCount(photoPraises == null? 0 : photoPraises.size());
            UserInfo userInfo = userInfoDao.findFirstByUserId(item.getUserId());
            bean.setUploadUser(userInfo);
            bean.setImages(FileUtils.getFilesPath("item/" + item.getPhotoItemId()));
            for (int index = 0; index < bean.getImages().length; index++) {
                bean.getImages()[index] = "item/" + item.getPhotoItemId() + "/" + bean.getImages()[index];
            }
            if (photoPraiseDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setPraise(true);
            }
            if (photoFavoriteDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setFavorite(true);
            }
            beans.add(bean);
        }
        return RestResult.success(beans);
    }

    /**
     * 查看点过赞的图片
     * @param pageBean
     * @return
     */
    @PostMapping("getPraiseItemsByUser")
    RestResult<List<PhotoItemBean>> getPraiseItemsByUser(
            @RequestBody PageBean pageBean
    ) {
        if (pageBean == null || pageBean.getPageUserId() == null || pageBean.getUserId() == null || pageBean.getPageNumber() == null) {
            return RestResult.failed("缺少部分信息");
        }
        PageRequest request = PageRequest.of(pageBean.getPageNumber(),20, Sort.by("praiseTime").descending());
        Page<PhotoPraise> items = photoPraiseDao.findAllByUserId(pageBean.getPageUserId(),request);
        ArrayList<PhotoItemBean> beans = new ArrayList<>();
        for (PhotoPraise item : items) {
            PhotoItemBean bean = new PhotoItemBean();
            PhotoItem photoItem = photoItemDao.findFirstByPhotoItemId(item.getPhotoItemId());
            bean.setPhotoItemId(item.getPhotoItemId());
            if (photoItem == null) {
                continue;
            }else {
                bean.setImages(FileUtils.getFilesPath("item/" + item.getPhotoItemId()));
                for (int index = 0; index < bean.getImages().length; index++) {
                    bean.getImages()[index] = "item/" + item.getPhotoItemId() + "/" + bean.getImages()[index];
                }
                List<PhotoFavorite> photoFavorites = photoFavoriteDao.findAllByPhotoItemId(item.getPhotoItemId());
                bean.setFavoriteCount(photoFavorites == null? 0 : photoFavorites.size());
                List<PhotoPraise> photoPraises = photoPraiseDao.findAllByPhotoItemId(item.getPhotoItemId());
                bean.setPraiseCount(photoPraises == null? 0 : photoPraises.size());
                UserInfo userInfo = userInfoDao.findFirstByUserId(photoItem.getUserId());
                bean.setUploadUser(userInfo);
                bean.setUploadTime(photoItem.getUploadTime());
                bean.setPhotoItemId(photoItem.getPhotoItemId());
            }
            if (photoPraiseDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setPraise(true);
            }
            if (photoFavoriteDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setFavorite(true);
            }
            beans.add(bean);
        }
        return RestResult.success(beans);
    }

    /**
     * 查看收藏的图片
     * @param pageBean
     * @return
     */
    @PostMapping("getFavoriteItemsByUser")
    RestResult<List<PhotoItemBean>> getFavoriteItemsByUser(
            @RequestBody PageBean pageBean
    ) {
        if (pageBean == null || pageBean.getPageUserId() == null || pageBean.getUserId() == null || pageBean.getPageNumber() == null) {
            return RestResult.failed("缺少部分信息");
        }
        PageRequest request = PageRequest.of(pageBean.getPageNumber(),20, Sort.by("favoriteTime").descending());
        Page<PhotoFavorite> items = photoFavoriteDao.findAllByUserId(pageBean.getPageUserId(),request);
        ArrayList<PhotoItemBean> beans = new ArrayList<>();
        for (PhotoFavorite item : items) {
            PhotoItemBean bean = new PhotoItemBean();
            PhotoItem photoItem = photoItemDao.findFirstByPhotoItemId(item.getPhotoItemId());
            if (photoItem == null) {
                continue;
            }else {
                bean.setImages(FileUtils.getFilesPath("item/" + item.getPhotoItemId()));
                for (int index = 0; index < bean.getImages().length; index++) {
                    bean.getImages()[index] = "item/" + item.getPhotoItemId() + "/" + bean.getImages()[index];
                }
                List<PhotoFavorite> photoFavorites = photoFavoriteDao.findAllByPhotoItemId(item.getPhotoItemId());
                bean.setFavoriteCount(photoFavorites == null? 0 : photoFavorites.size());
                List<PhotoPraise> photoPraises = photoPraiseDao.findAllByPhotoItemId(item.getPhotoItemId());
                bean.setPraiseCount(photoPraises == null? 0 : photoPraises.size());
                bean.setUploadTime(photoItem.getUploadTime());
                UserInfo userInfo = userInfoDao.findFirstByUserId(photoItem.getUserId());
                bean.setUploadUser(userInfo);
                bean.setPhotoItemId(photoItem.getPhotoItemId());
            }
            if (photoPraiseDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setPraise(true);
            }
            if (photoFavoriteDao.findFirstByPhotoItemIdAndUserId(item.getPhotoItemId(),pageBean.getUserId()) != null) {
                bean.setFavorite(true);
            }
            beans.add(bean);
        }
        return RestResult.success(beans);
    }

    //点赞图片
    @PostMapping("praiseItem")
    RestResult<Boolean> praiseItem(
            @RequestBody ItemActionBean bean
    ) {
        if (bean == null || bean.getUserId() == null || bean.getPhotoItemId() == null) {
            return RestResult.failed("信息不完整",false);
        }
        if (userDao.findFirstByUserId(bean.getUserId()) == null) {
            return RestResult.failed("此用户不存在",false);
        }
        if (photoItemDao.findFirstByPhotoItemId(bean.getPhotoItemId()) != null) {
            if (photoPraiseDao.findFirstByPhotoItemIdAndUserId(bean.getPhotoItemId(), bean.getUserId()) == null) {
                PhotoPraise item = new PhotoPraise();
                item.setPhotoItemId(bean.getPhotoItemId());
                item.setUserId(bean.getUserId());
                item.setPraiseTime(System.currentTimeMillis());
                item = photoPraiseDao.save(item);
            }
            return RestResult.success(true);
        }else {
            return RestResult.failed("此内容不存在",false);
        }
    }

    /**
     * 删除点赞
     * @param bean
     * @return
     */
    @PostMapping("unPraiseItem")
    RestResult<Boolean> unPraiseItem(
            @RequestBody ItemActionBean bean
    ) {
        if (bean == null || bean.getUserId() == null || bean.getPhotoItemId() == null) {
            return RestResult.failed("信息不完整",true);
        }
        if (userDao.findFirstByUserId(bean.getUserId()) == null) {
            return RestResult.failed("此用户不存在",true);
        }
        PhotoPraise item = photoPraiseDao.findFirstByPhotoItemIdAndUserId(bean.getPhotoItemId(),bean.getUserId());
        if (item != null) {
            photoPraiseDao.delete(item);
            return RestResult.success(false);
        }else {
            return RestResult.failed("此内容不存在",true);
        }
    }

    /**
     * 收藏图片
     * @param bean
     * @return
     */
    @PostMapping("favoriteItem")
    RestResult<Boolean> favoriteItem(
            @RequestBody ItemActionBean bean
    ) {
        if (bean == null || bean.getUserId() == null || bean.getPhotoItemId() == null) {
            return RestResult.failed("信息不完整",false);
        }
        if (userDao.findFirstByUserId(bean.getUserId()) == null) {
            return RestResult.failed("此用户不存在",false);
        }
        if (photoItemDao.findFirstByPhotoItemId(bean.getPhotoItemId()) != null) {
            if (photoFavoriteDao.findFirstByPhotoItemIdAndUserId(bean.getPhotoItemId(), bean.getUserId()) == null) {
                PhotoFavorite item = new PhotoFavorite();
                item.setPhotoItemId(bean.getPhotoItemId());
                item.setUserId(bean.getUserId());
                item.setFavoriteTime(System.currentTimeMillis());
                item = photoFavoriteDao.save(item);
            }
            return RestResult.success(true);
        }else {
            return RestResult.failed("此内容不存在",false);
        }
    }

    /**
     * 删除收藏图片
     * @param bean
     * @return
     */
    @PostMapping("unFavoriteItem")
    RestResult<Boolean> unFavoriteItem(
            @RequestBody ItemActionBean bean
    ) {
        if (bean == null || bean.getUserId() == null || bean.getPhotoItemId() == null) {
            return RestResult.failed("缺少信息",true);
        }
        if (userDao.findFirstByUserId(bean.getUserId()) == null) {
            return RestResult.failed("此用户不存在",true);
        }
        PhotoFavorite item = photoFavoriteDao.findFirstByPhotoItemIdAndUserId(bean.getPhotoItemId(),bean.getUserId());
        if (item != null) {
            photoFavoriteDao.delete(item);
            return RestResult.success(false);
        }else {
            return RestResult.failed("此内容不存在",true);
        }
    }

}
