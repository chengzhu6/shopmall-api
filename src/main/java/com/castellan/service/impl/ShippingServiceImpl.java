package com.castellan.service.impl;

import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.dao.ShippingMapper;
import com.castellan.pojo.Shipping;
import com.castellan.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;


    public ServerResponse addShipping(Integer userId,Shipping shipping){
        if (shipping == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        shipping.setUserId(userId);
        int resultCount = shippingMapper.insert(shipping);
        if (resultCount > 0) {
            Map<String ,Integer> map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess(map, "添加地址成功");
        }
        return ServerResponse.createByErrorMessage("添加地址失败");
    }

    public ServerResponse delShipping(Integer userId,Integer shippingId){
        if (shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage( "删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }


    public ServerResponse updateShipping(Integer userId,Shipping shipping){
        if (shipping == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShipping(shipping);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    public ServerResponse detailShipping(Integer userId, Integer shippingId){
        if (shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }

        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if (shipping != null){
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMessage("无法查询到该地址的详细信息");
    }

    public ServerResponse listShipping(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectListByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
