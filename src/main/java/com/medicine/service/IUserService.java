package com.medicine.service;

import com.github.pagehelper.PageInfo;
import com.medicine.common.ServerResponse;
import com.medicine.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by wangjian
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> isLogin(HttpSession session);

    ServerResponse<PageInfo> getUserList(int pageNum, int pageSize);
}
