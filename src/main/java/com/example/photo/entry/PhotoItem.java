package com.example.photo.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户上传图片项目表
 */
@Entity
@Table
public class PhotoItem {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoItemId;

    /**
     * 图片配文
     */
    @Column
    @NotNull
    private String photoContent;

    @Column
    @NotNull
    private Long userId;

    /**
     * 图片上传时间
     */
    @Column
    @NotNull
    private Long uploadTime;

    public Long getUserId() {
        return userId;
    }

    public Long getPhotoItemId() {
        return photoItemId;
    }

    public String getPhotoContent() {
        return photoContent;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPhotoContent(String photoContent) {
        this.photoContent = photoContent;
    }

    public void setPhotoItemId(long photoItemId) {
        this.photoItemId = photoItemId;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
