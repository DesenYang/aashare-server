package com.example.photo.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 图片点赞项目表
 */
@Table
@Entity
public class PhotoPraise {
    /**
     * 点赞图片的Id
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoPraiseId;

    /**
     * 用户Id
     */
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
     * 图片点赞时间
     */
    @Column
    @NotNull
    private Long praiseTime;

    public Long getPhotoItemId() {
        return photoItemId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPhotoPraiseId() {
        return photoPraiseId;
    }

    public Long getPraiseTime() {
        return praiseTime;
    }

    public void setPhotoItemId(long photoItemId) {
        this.photoItemId = photoItemId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPraiseTime(Long praiseTime) {
        this.praiseTime = praiseTime;
    }
}
