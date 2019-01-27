package com.castellan.service.impl;

import com.castellan.common.Const;
import com.castellan.common.RedisPool;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.dao.CategoryMapper;
import com.castellan.dao.ProductMapper;
import com.castellan.pojo.Category;
import com.castellan.pojo.Product;
import com.castellan.service.ICategoryService;
import com.castellan.service.IProductService;
import com.castellan.util.JsonUtil;
import com.castellan.vo.ProductDetailVo;
import com.castellan.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import com.castellan.util.PropertiesUtil;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;


    public ServerResponse saveOrUpdateProduct(Product product){
        if (product != null){
            {
                if(StringUtils.isNotBlank(product.getSubImages())){
                    String[] subImageArray = product.getSubImages().split(",");
                    if(subImageArray.length > 0){
                        product.setMainImage(subImageArray[0]);
                    }
                }

                if(product.getId() == null){
                    int resultCount = productMapper.insert(product);
                    if (resultCount > 0){
                        return ServerResponse.createBySuccessMessage("添加商品信息成功");
                    } else {
                        return ServerResponse.createByErrorMessage("添加商品信息失败");
                    }
                } else {
                    int resultCount = productMapper.updateByPrimaryKey(product);
                    if (resultCount > 0){
                        return ServerResponse.createBySuccessMessage("更新商品信息成功");
                    } else {
                        return ServerResponse.createByErrorMessage("更新商品信息失败");
                    }
                }
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
    }


    public ServerResponse setSaleStatus(Integer productId, Integer status){
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("更新商品状态成功");
        }
        return ServerResponse.createByErrorMessage("更新商品状态信息失败");
    }



    public ServerResponse getDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }


    public ServerResponse getProductList(int pageNum,int pageSize){

        List<Product> products = Lists.newArrayList();
        PageHelper.startPage(pageNum,pageSize);
        products = productMapper.selectAll();
        List<ProductListVo> productListVos = new ArrayList<>(products.size());
        for (Product productItem: products) {
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);


    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://39.96.47.168/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }


    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://39.96.47.168/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(product.getCreateTime().toString());
        productDetailVo.setUpdateTime(product.getUpdateTime().toString());
        return productDetailVo;
    }




    public ServerResponse searchProduct(String productName,Integer productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.selectProductByNameAndProductId(productId,productName);
        List<ProductListVo> productListVos = new ArrayList<>(products.size());
        for (Product productItem: products) {
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);
    }



    public ServerResponse getProductDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        Product product = null;
        product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }
        if (product.getStatus() != Const.ProductStatus.ON_SALE.getStatus()){
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);

    }

    public ServerResponse getProductsByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        if (StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getChildernCategoryById(categoryId).getData();

        }

        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }



        PageHelper.startPage(pageNum,pageSize);
        if (orderBy != null){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderBys = orderBy.split("_");
                PageHelper.orderBy(orderBys[0]+" "+orderBys[1]);
            }
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,
                                                                                categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVos = new ArrayList<>(productList.size());
        for (Product productItem: productList) {
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);


    }


}
