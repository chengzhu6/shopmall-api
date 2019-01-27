package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IOrderService {

    ServerResponse pay(Integer userId, Long orderNo, String path);

    ServerResponse aliCallBack(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId,Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);

    void closeOrder(int hour);
}
