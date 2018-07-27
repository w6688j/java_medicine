package com.medicine.service;

import com.medicine.common.ServerResponse;
import com.medicine.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by wangjian
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);

    ServerResponse<String> isLogin(HttpSession session);
}
