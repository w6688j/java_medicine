package com.medicine.service;

import com.medicine.common.ServerResponse;
import com.medicine.pojo.Count;

/**
 * Created by wangjian
 * User: root
 * Date: 2018/7/29
 * Time: 8:38
 */
public interface ICountService {
    ServerResponse<Count> getCount();
}
