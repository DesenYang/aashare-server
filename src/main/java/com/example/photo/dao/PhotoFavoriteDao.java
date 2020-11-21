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
public interface PhotoFavoriteDao extends CrudRepository<PhotoFavorite,Long> {
    /**
     * 分页查询收藏图片
     * @param userId
     * @param pageable
     * @return
     */
    Page<PhotoFavorite> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 查询所有收藏图片
     * @param photoItemId
     * @return
     */
    List<PhotoFavorite> findAllByPhotoItemId(Long photoItemId);

    /**
     * 通过用户id和图片id查询第一个收藏图片
     * @param photoItemId
     * @param userId
     * @return
     */
    PhotoFavorite findFirstByPhotoItemIdAndUserId(Long photoItemId,Long userId);
}