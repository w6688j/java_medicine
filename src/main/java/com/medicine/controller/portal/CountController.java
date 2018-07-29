package com.medicine.controller.portal;

import com.medicine.common.ServerResponse;
import com.medicine.pojo.Count;
import com.medicine.service.ICountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangjian
 * User: root
 * Date: 2018/7/29
 * Time: 8:37
 */

@Controller
@RequestMapping("/api/count/")
public class CountController {
    @Autowired
    private ICountService iCountService;

    @RequestMapping(value = "get_count.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Count> getCount() {
        return iCountService.getCount();
    }
}
