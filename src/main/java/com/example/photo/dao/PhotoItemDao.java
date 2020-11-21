package com.example.photo.dao;

import com.example.photo.entry.PhotoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PhotoItemDao extends CrudRepository<PhotoItem,Long> {
    /**
     * 分页查询用户上传的图片
     * @param userId
     * @param pageable
     * @return
     */
    Page<PhotoItem> findAllByUserId(Long userId,Pageable pageable);

    /**
     * 通过照片id查询图片
     * @param photoItemId
     * @return
     */
    PhotoItem findFirstByPhotoItemId(Long photoItemId);

    /**
     * 分页查询所有用户上传图片
     * @param pageable
     * @return
     */
    Page<PhotoItem> findAll(Pageable pageable);
}
