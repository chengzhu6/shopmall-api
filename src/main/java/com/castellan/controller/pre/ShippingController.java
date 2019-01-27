package com.castellan.controller.pre;


import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.Shipping;
import com.castellan.pojo.User;
import com.castellan.service.IShippingService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        return iShippingService.addShipping(user.getId(),shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        return iShippingService.delShipping(user.getId(),shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        return iShippingService.updateShipping(user.getId(),shipping);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Integer shippingId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        return iShippingService.detailShipping(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iShippingService.listShipping(user.getId(),pageNum,pageSize);
    }
}
