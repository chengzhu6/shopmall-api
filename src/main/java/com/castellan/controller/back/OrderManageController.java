package com.castellan.controller.back;

import com.github.pagehelper.PageInfo;
import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IOrderService;
import com.castellan.service.IUserService;
import com.castellan.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){


        //填充我们增加产品的业务逻辑
        return iOrderService.manageList(pageNum,pageSize);

    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo){
        //填充我们增加产品的业务逻辑
        return iOrderService.manageDetail(orderNo);

    }



    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        //填充我们增加产品的业务逻辑
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }



    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo){
        //填充我们增加产品的业务逻辑
        return iOrderService.manageSendGoods(orderNo);
    }








}
