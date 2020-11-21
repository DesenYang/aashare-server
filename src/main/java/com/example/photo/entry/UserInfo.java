package com.example.photo.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class UserInfo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    @NotNull
    private String userNick;

    /**
     * 用户头像保存路径
     */
    @Column
    @NotNull
    private String userAvatar;

    /**
     * 用户性别
     */
    @Column
    @NotNull
    private Integer gender;

    @Column
    @NotNull
    private Long birthday;

    public String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getGender() {
        return gender;
    }

    public Long getBirthday() {
        return birthday;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
