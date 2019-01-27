package com.castellan.controller.pre;


import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse cartList(HttpSession session, Integer productId, int count){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.addCartItem(currentUser.getId(),productId,count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Integer productId, int count){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.updateCart(productId,count,currentUser.getId());
    }


    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, String productIds){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.deleteProduct(currentUser.getId(),productIds);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse update(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.getCartList(currentUser.getId());
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.selectOrUnselectProduct(currentUser.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.selectOrUnselectProduct(currentUser.getId(),null,Const.Cart.UNCHECKED);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse selectProduct(HttpSession session,Integer productId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.selectOrUnselectProduct(currentUser.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse unSelectProduct(HttpSession session,Integer productId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        return iCartService.selectOrUnselectProduct(currentUser.getId(),productId,Const.Cart.UNCHECKED);
    }

    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse getProductNum(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getProductNumber(currentUser.getId());
    }

}
