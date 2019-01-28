package com.castellan.service.impl;

import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.dao.CartMapper;
import com.castellan.dao.ProductMapper;
import com.castellan.pojo.Cart;
import com.castellan.pojo.Product;
import com.castellan.service.ICartService;
import com.castellan.util.PropertiesUtil;
import com.castellan.vo.CartProductVo;
import com.castellan.vo.CartVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse addCartItem(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);
        if (cart == null) {
            // 将cart添加到购物车并且默认状态是选中的
            Cart cartNew = new Cart();
            cartNew.setChecked(Const.Cart.CHECKED);
            cartNew.setQuantity(count);
            cartNew.setUserId(userId);
            cartNew.setProductId(productId);
            cartMapper.insertSelective(cartNew);
        } else {
            cart.setQuantity(cart.getQuantity() + count);
            cart.setChecked(Const.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        // 将所有的cart中的内容进行封装返回
        return getCartList(userId);
    }

    public ServerResponse getCartList(Integer userId){
        CartVo cartVo = getCartVo(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse updateCart(Integer productId, Integer count, Integer userId){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.getCartList(userId);
    }

    public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return this.getCartList(userId);
    }




    public ServerResponse selectOrUnselectProduct(Integer userId, Integer productId, Integer checked) {
        cartMapper.updateCheckedByProductId(userId,productId,checked);
        return this.getCartList(userId);
    }

    public ServerResponse getProductNumber(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectProductNumByUserId(userId));
    }


    private CartVo getCartVo(Integer userId) {
        CartVo cartVo = new CartVo();
        cartVo.setCartProductVoList(Lists.newArrayList());
        cartVo.setCartTotalPrice(new BigDecimal("0"));
        List<Cart> carts = cartMapper.selectCardListByUserId(userId);
        if (carts != null) {
            for (Cart cartItem : carts) {
                CartProductVo cartProductVo = new CartProductVo();
                Integer productId = cartItem.getProductId();
                Product product = productMapper.selectByPrimaryKey(productId);
                if (product != null) {
                    // 对每个cartItem进行检查，看是否超过库存
                    Integer stock = product.getStock();
                    if (cartItem.getQuantity() > stock) {

                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        cartItem.setQuantity(stock);
                        // 更新数据库中的购物车信息
                        Cart cartUpdate = new Cart();
                        cartUpdate.setQuantity(stock);
                        cartUpdate.setId(cartItem.getId());
                        cartMapper.insertSelective(cartUpdate);
                    } else {
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIl);
                    }
                    // 将cart和product封装成CartProductVo 进行返回
                    cartProductVo.setId(cartItem.getId());
                    cartProductVo.setProductChecked(cartItem.getChecked());
                    cartProductVo.setProductId(cartItem.getProductId());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductTotalPrice(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setUserId(userId);
                    cartProductVo.setQuantity(cartItem.getQuantity());
                    // 如果cartItem处于选中状态将cart的总价进行更新
                    if (cartItem.getChecked() == Const.Cart.CHECKED){
                        cartVo.setCartTotalPrice(cartVo.getCartTotalPrice().add(cartProductVo.getProductTotalPrice()));
                    }
                }
                // 商品已经下架或者不存在也将其进行添加
                cartVo.getCartProductVoList().add(cartProductVo);
            }
        }
        cartVo.setAllChecked(getAllChecked(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }


    private Boolean getAllChecked(Integer userId){

        if (userId == null) {
            return false;
        }
        return cartMapper.selectAllCheckedByUserId(userId) == 0;
    }
}
