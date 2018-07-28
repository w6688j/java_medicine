package com.medicine.service;

import com.medicine.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;

public interface IFileUploadService {
    ServerResponse<String> upload(HttpServletRequest request);
}
