package com.example.photo.bean;

public class ItemActionBean {
    private Long userId;
    private Long photoItemId;

    public Long getUserId() {
        return userId;
    }

    public Long getPhotoItemId() {
        return photoItemId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPhotoItemId(Long photoItemId) {
        this.photoItemId = photoItemId;
    }
}
