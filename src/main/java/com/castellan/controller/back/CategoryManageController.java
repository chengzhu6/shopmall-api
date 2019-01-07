package com.castellan.controller.back;


import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.ICategoryService;

import com.castellan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/back/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(currentUser.getRole()).isSuccess()){
            // 管理员进行添加品类的操作

            return iCategoryService.addCategory(categoryName,parentId);
        }
        return ServerResponse.createByErrorMessage("权限不足");

    }


    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse resetCategoryName(HttpSession session, int categoryId, String categoryName){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(currentUser.getRole()).isSuccess()){
            return iCategoryService.resetCategoryName(categoryName,categoryId);
        }
        return ServerResponse.createByErrorMessage("权限不足");

    }

    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryID",defaultValue = "0") int categoryId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(currentUser.getRole()).isSuccess()){
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("权限不足");

    }

    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServerResponse getChildrenDeepCategory(HttpSession session, @RequestParam(value = "categoryID",defaultValue = "0") int categoryId){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(currentUser.getRole()).isSuccess()){
            return iCategoryService.getChildernCategoryById(categoryId);
        }
        return ServerResponse.createByErrorMessage("权限不足");

    }
}
