package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.pojo.Shipping;

public interface IShippingService {
    ServerResponse addShipping(Integer userId, Shipping shipping);

    ServerResponse delShipping(Integer userId,Integer shippingId);
    ServerResponse updateShipping(Integer userId,Shipping shipping);

    ServerResponse detailShipping(Integer userId, Integer shippingId);

    ServerResponse listShipping(Integer userId, Integer pageNum, Integer pageSize);
}
