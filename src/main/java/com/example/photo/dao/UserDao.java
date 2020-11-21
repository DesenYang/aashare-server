package com.example.photo.dao;

import com.example.photo.entry.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserDao extends CrudRepository<User,Long> {
    /**
     *  通过userName获取用户信息
     * @param userName
     * @return
     */
    User findFirstByUserName(String userName);

    /**
     * 通过用户Id获取用户信息
     * @param userId
     * @return
     */
    User findFirstByUserId(Long userId);
}
