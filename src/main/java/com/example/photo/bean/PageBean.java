package com.example.photo.bean;

public class PageBean {
    private Long userId;
    private Long pageUserId;
    private int pageNumber;

    public Long getUserId() {
        return userId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getPageUserId() {
        return pageUserId;
    }

    public void setPageUserId(Long pageUserId) {
        this.pageUserId = pageUserId;
    }
}
