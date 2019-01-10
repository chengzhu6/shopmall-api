package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.vo.CartVo;

public interface ICartService {
    ServerResponse addCartItem(Integer userId, Integer productId, Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse updateCart(Integer productId, Integer count, Integer userId);
    ServerResponse getCartList(Integer userId);

    ServerResponse selectOrUnselectProduct(Integer userId, Integer productId, Integer checked);



    ServerResponse  getProductNumber(Integer userId);
}
