package com.example.photo.controller;

import com.example.photo.base.RestResult;
import com.example.photo.dao.UserDao;
import com.example.photo.dao.UserInfoDao;
import com.example.photo.entry.User;
import com.example.photo.entry.UserInfo;
import com.example.photo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    UserInfoDao userInfoDao;

    /**
     * 注册账号
     * @param user
     * @return
     */
    @PostMapping("/register")
    RestResult<Long> register(
            @RequestBody User user
    ) {
        if (user == null || user.getUserName() == null) {
            return RestResult.failed("请填写账号名");
        }

        if (user.getPassword() == null ) {
            return RestResult.failed("请填写密码");
        }
        if (userDao.findFirstByUserName(user.getUserName()) != null) {
            return RestResult.failed("该账号已被注册");
        }else {
            try {
                user.setRegisterTime(System.currentTimeMillis());
                user = userDao.save(user);
                return RestResult.success(user.getUserId());
            }catch (Exception e) {
                e.printStackTrace();
                return RestResult.failed(e.getMessage());
            }
        }
    }

    /**
     * 登陆
     * @param user
     * @return
     */
    @PostMapping("/login")
    RestResult<UserInfo> login(
            @RequestBody User user
    ) {
        if (user == null || user.getUserName() == null) {
            return RestResult.failed("请填写账号名");
        }

        if (user.getPassword() == null ) {
            return RestResult.failed("请填写密码");
        }
        User temp = userDao.findFirstByUserName(user.getUserName());
        if ( temp != null) {
            if (!temp.getPassword().equals(user.getPassword())) {
                return RestResult.failed("密码错误");
            }
            UserInfo userInfo = userInfoDao.findFirstByUserId(temp.getUserId());
            if ( userInfo != null ) {
                if (!StringUtils.isEmpty(userInfo.getUserAvatar())) {
                    userInfo.setUsername(user.getUserName());
                    return RestResult.success(userInfo);
                }else {
                    return new RestResult<UserInfo>(201,"请填写资料",userInfo);
                }
            }else {
                userInfo = new UserInfo();
                userInfo.setUserNick("");
                userInfo.setBirthday(0L);
                userInfo.setUserAvatar("");
                userInfo.setGender(0);
                userInfo.setUserId(temp.getUserId());
                userInfo.setUsername(temp.getUserName());
                return new RestResult<UserInfo>(201,"请填写资料",userInfo);
            }

        }else {
            return RestResult.notFound();
        }
    }

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @PostMapping("/getUserInfo")
    RestResult<UserInfo> getUserInfo(
            @RequestBody User user
    ) {
        if (user == null || user.getUserId() == null) {
            return RestResult.failed("请填写用户id");
        }
        UserInfo userInfo = userInfoDao.findFirstByUserId(user.getUserId());
        User temp = userDao.findFirstByUserId(user.getUserId());
        if (userInfo == null || temp == null){
            return RestResult.notFound();
        }
        userInfo.setUsername(temp.getUserName());
        return RestResult.success(userInfo);

    }

    /**
     * 设置用户信息
     * @param userInfo
     * @return
     */
    @PostMapping("/setupUserInfo")
    RestResult<String> setupUserInfo(
            @RequestBody UserInfo userInfo
    ) {
        if (userInfo == null || userInfo.getUserId() == null) {
            return RestResult.failed("请填写正确的资料");
        }
        if (userInfo.getBirthday() == null) {
            return RestResult.failed("请填写生日");
        }
        if (userInfo.getGender() == null) {
            return RestResult.failed("请填写性别");
        }
        if (userInfo.getUserNick() == null) {
            return RestResult.failed("请填写昵称");
        }
        if (userInfo.getUserAvatar() == null) {
            userInfo.setUserAvatar("");
        }
        UserInfo temp = userInfoDao.findFirstByUserId(userInfo.getUserId());
        try {
            if (temp == null) {
                userInfo.setUserAvatar("");
            } else {
                userInfo.setUserAvatar(temp.getUserAvatar());
                userInfo.setUserId(temp.getUserId());
            }
            userInfoDao.save(userInfo);
            return RestResult.success();
        }catch (Exception e) {
            e.printStackTrace();
            return RestResult.failed(e.getMessage());
        }
    }

    /**
     * 上传头像
     * @param multipartRequest
     * @return
     */
    @PostMapping("/uploadAvatar")
    RestResult<UserInfo> uploadUserAvatar(
            MultipartHttpServletRequest multipartRequest
    ) {
        try {
            Long userId = Long.valueOf(multipartRequest.getParameter("userId"));
            MultipartFile multiFile = multipartRequest.getFile("avatar");
            if (userDao.findFirstByUserId(userId) == null) {
                return RestResult.failed("该用户不存在");
            }
            if (multiFile == null) {
                return RestResult.failed("请上传头像");
            }
            UserInfo userInfo = userInfoDao.findFirstByUserId(userId);
            if (userInfo == null) {
                return RestResult.failed("请先设置用户信息");
            }
            String name = multiFile.getOriginalFilename();
            //设置文件名+获取文件后缀
            name = userId + System.currentTimeMillis() + "." + FileUtils.getSuffix( name!= null? name : "");
            String path = "avatar/" + userId;
            File file = FileUtils.save(path,name,multiFile.getInputStream());
            if (file != null ) {
                userInfo.setUserAvatar(path + "/" + name);
                userInfoDao.save(userInfo);
                return RestResult.success(userInfo);
            }else {
                return RestResult.failed("保存失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return RestResult.failed(e.getMessage());
        }
    }
}
