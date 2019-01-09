package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.pojo.Product;
import org.springframework.stereotype.Service;


public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    /**
     * 后台调用
     * @param productId
     * @return
     */
    ServerResponse getDetail(Integer productId);

    ServerResponse getProductList(int pageNum,int pageSize);

    ServerResponse searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    /**
     * 前台调用
     * @param productId
     * @return
     */
    ServerResponse getProductDetail(Integer productId);
    ServerResponse getProductsByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
