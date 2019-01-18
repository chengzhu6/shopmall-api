package com.castellan.controller.pre;

import com.castellan.common.ServerResponse;
import com.castellan.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "keyword",required = false) String keyword,
                                  @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                  @RequestParam(value = "pageNum",defaultValue = "0" )int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                  @RequestParam(value = "orderBy",required = false)String orderBy){
        return iProductService.getProductsByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

    @RequestMapping("all.do")
    @ResponseBody
    public ServerResponse getALlList(@RequestParam(value = "keyword",required = false) String keyword,
                                  @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                  @RequestParam(value = "pageNum",defaultValue = "0" )int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                  @RequestParam(value = "orderBy",required = false)String orderBy){
        return iProductService.getProductList(pageNum,pageSize);
    }


}
