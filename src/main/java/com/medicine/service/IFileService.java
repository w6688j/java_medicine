package com.medicine.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wangjian
 * User: root
 * Date: 2018/7/7
 * Time: 14:44
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
