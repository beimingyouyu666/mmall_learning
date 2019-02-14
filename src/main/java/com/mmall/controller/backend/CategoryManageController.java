package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 后台品类接口
 */
@Controller
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    ICategoryService iCategoryService;

    /**
     * 获取品类直接子节点
     * @return
     */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse<List<Category>> getCategory(@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId
            , HttpSession httpSession){
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        //校验一下是否是管理员
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return iCategoryService.getCategory(categoryId);
    }


    /**
     * 增加品类
     * @return
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse<String> addCategory(@RequestParam(value = "parentId" ,defaultValue = "0") Integer  parentId
            ,String categoryName,HttpSession httpSession){
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        //校验一下是否是管理员
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return iCategoryService.addCategory(parentId,categoryName);
    }

    /**
     * 修改品类名字
     * @return
     */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse<String> setCategoryName(Integer categoryId,String categoryName,HttpSession httpSession){
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        //校验一下是否是管理员
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return iCategoryService.setCategoryName(categoryId,categoryName);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * @return
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId
        ,HttpSession httpSession){
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        //校验一下是否是管理员
        //TODO 使用过滤器过滤接口判断是否管理员或者是否登陆
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return iCategoryService.getDeepCategory(categoryId);
    }

}
