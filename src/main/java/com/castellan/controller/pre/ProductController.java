package com.castellan.controller.pre;

import com.castellan.common.ServerResponse;
import com.castellan.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getList(@PathVariable String keyword,
                                  @PathVariable(value = "categoryId")Integer categoryId,
                                  @PathVariable(value = "pageNum",required = false )int pageNum,
                                  @PathVariable(value = "pageSize",required = false)int pageSize,
                                  @PathVariable(value = "orderBy",required = false)String orderBy){
        return iProductService.getProductsByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }


    @RequestMapping("list/category/{categoryId}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getList(
                                  @PathVariable(value = "categoryId")Integer categoryId,
                                  @PathVariable(value = "pageNum",required = false )Integer pageNum,
                                  @PathVariable(value = "pageSize",required = false)Integer pageSize,
                                  @PathVariable(value = "orderBy",required = false)String orderBy){

        if (pageNum == null){
            pageNum = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_desc";
        }
        return iProductService.getProductsByKeywordCategory(null,categoryId,pageNum,pageSize,orderBy);
    }

    @RequestMapping("list/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}")
    @ResponseBody
    public ServerResponse getList(@PathVariable String keyword,
                                  @PathVariable(value = "pageNum",required = false)Integer pageNum,
                                  @PathVariable(value = "pageSize",required = false)Integer pageSize,
                                  @PathVariable(value = "orderBy",required = false)String orderBy){
        if (pageNum == null){
            pageNum = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_desc";
        }
        return iProductService.getProductsByKeywordCategory(keyword,null,pageNum,pageSize,orderBy);
    }
}
