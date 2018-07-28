package com.medicine.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.medicine.service.IFileUploadService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/1/10/010.
 */

@Controller
@RequestMapping("/api/upload/")
public class FileUploadController {
    @Autowired
    private IFileUploadService iFileUploadService;

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request) {
        return iFileUploadService.upload(request);
    }
}
