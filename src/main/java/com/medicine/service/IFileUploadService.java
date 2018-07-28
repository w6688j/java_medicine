package com.medicine.service;

import com.medicine.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IFileUploadService {
    ServerResponse<String> upload(HttpServletRequest request, HttpSession session);
}
