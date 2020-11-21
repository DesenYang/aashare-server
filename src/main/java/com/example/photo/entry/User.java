package com.example.photo.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column
    @NotNull
    private String userName;

    @Column
    @NotNull
    private String password;

    /**
     * 注册时间
     */
    @Column
    @NotNull
    private Long registerTime;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public Long getUserId() {
        return userId;
    }
}
