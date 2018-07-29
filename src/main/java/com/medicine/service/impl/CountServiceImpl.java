package com.medicine.service.impl;

import com.medicine.common.ServerResponse;
import com.medicine.dao.UploadFileMapper;
import com.medicine.dao.UserMapper;
import com.medicine.pojo.Count;
import com.medicine.service.ICountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangjian
 * User: root
 * Date: 2018/7/29
 * Time: 8:40
 */
@Service("iCountService")
public class CountServiceImpl implements ICountService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public ServerResponse<Count> getCount() {
        int userCount = userMapper.userCount();
        int uploadFIleCount = uploadFileMapper.uploadFIleCount();

        Count count = new Count();
        count.setUserCount(userCount);
        count.setUploadFIleCount(uploadFIleCount);

        return ServerResponse.createBySuccess(count);
    }
}
