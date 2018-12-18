package com.em.service;

import com.em.pojo.Users;

/**
 * @author Administrator
 */
public interface UserService {

    /**
     * @param username
     * @return 判断用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 保存用户基本信息
     * @param user
     */
    void saveUser(Users user);

    /**'
     * 用户登录，根据用户名和密码查询用户
     * @param name
     * @param password
     * @return Users
     */
    Users queryUserForLogin(String name, String password);

}
