package com.example.photo.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户图片收藏表
 */
@Entity
@Table
public class PhotoFavorite {
    /**
     * 图片的Id
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoFavoriteId;

    @Column
    @NotNull
    private Long userId;

    /**
     * 图片项目Id
     */
    @Column
    @NotNull
    private Long photoItemId;

    /**
     * 图片收藏时间
     */
    @Column
    @NotNull
    private Long favoriteTime;

    public Long getFavoriteTime() {
        return favoriteTime;
    }

    public Long getPhotoItemId() {
        return photoItemId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPhotoFavoriteId() {
        return photoFavoriteId;
    }

    public void setPhotoItemId(long photoItemId) {
        this.photoItemId = photoItemId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setFavoriteTime(Long favoriteTime) {
        this.favoriteTime = favoriteTime;
    }
}
