package com.example.photo.dao;


import com.example.photo.entry.PhotoFavorite;
import com.example.photo.entry.PhotoPraise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface PhotoPraiseDao extends CrudRepository<PhotoPraise,Long> {
    /**
     * 分页查询点赞图片
     * @param userId
     * @param pageable
     * @return
     */
    Page<PhotoPraise> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 通过图片项目ID查询所有图片
     * @param photoItemId
     * @return
     */
    List<PhotoPraise> findAllByPhotoItemId(Long photoItemId);

    /**
     * 通过用户id和图片项目id查询图片
     * @param photoItemId
     * @param userId
     * @return
     */
    PhotoPraise findFirstByPhotoItemIdAndUserId(Long photoItemId, Long userId);
}
