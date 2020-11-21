package com.example.photo.bean;

import com.example.photo.entry.UserInfo;

import java.util.List;

public class PhotoItemBean {
    private String photoContent;
    private Long photoItemId;
    private String[] images;
    private UserInfo uploadUser;
    private int praiseCount;
    private int favoriteCount;
    private Long uploadTime;
    private Boolean isPraise = false;
    private Boolean isFavorite = false;

    public Boolean getFavorite() {
        return isFavorite;
    }

    public Boolean getPraise() {
        return isPraise;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setPraise(Boolean praise) {
        isPraise = praise;
    }

    public String getPhotoContent() {
        return photoContent;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public String[] getImages() {
        return images;
    }

    public Long getPhotoItemId() {
        return photoItemId;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public UserInfo getUploadUser() {
        return uploadUser;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public void setPhotoItemId(Long photoItemId) {
        this.photoItemId = photoItemId;
    }

    public void setPhotoContent(String photoContent) {
        this.photoContent = photoContent;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public void setUploadUser(UserInfo uploadUser) {
        this.uploadUser = uploadUser;
    }
}
