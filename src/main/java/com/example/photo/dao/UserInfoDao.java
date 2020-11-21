package com.example.photo.dao;

import com.example.photo.entry.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * 查询用户信息，
 */
public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    UserInfo findFirstByUserId(Long userId);
}
