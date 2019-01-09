package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.pojo.Product;

public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse getDetail(Integer productId);

    ServerResponse getProductList(int pageNum,int pageSize);

    ServerResponse searchProduct(String productName,Integer productId,int pageNum,int pageSize);
}
