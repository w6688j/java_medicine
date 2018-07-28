package com.medicine.controller.portal;

import com.medicine.common.ServerResponse;
import com.medicine.pojo.UploadFile;
import com.medicine.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public ServerResponse<String> upload(HttpServletRequest request, HttpSession session) {

        return iFileUploadService.upload(request, session);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return iFileUploadService.getUploadFileList(pageNum, pageSize);
    }
}
