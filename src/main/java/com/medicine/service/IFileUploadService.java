package com.medicine.service;

import com.github.pagehelper.PageInfo;
import com.medicine.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IFileUploadService {
    ServerResponse<String> upload(HttpServletRequest request, HttpSession session);

    ServerResponse<PageInfo> getUploadFileList(int pageNum, int pageSize);

    ServerResponse<String> ftpUpload(String fileIds);
}
