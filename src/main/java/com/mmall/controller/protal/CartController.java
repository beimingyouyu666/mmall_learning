package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Auther: yangkun
 * @Date: 2019/2/28 0028 23:37
 * @Description:
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    ICartService iCartService;

    //购物车列表 list.do
    //根据用户id查询购物车中所有数据
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.list(user.getId()));
    }


    //购物车添加商品 add.do
    //传入 productid和数量 请注意这个字段，超过数量会返回这样的标识"limitQuantity" 失败的：LIMIT_NUM_FAIL 成功的：LIMIT_NUM_SUCCESS
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Integer productid,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.add(user.getId(),productid,count));
    }

    //更新购物车某个产品数量 update.do
    //传入productId,count  根据用户id和产品id 更新数量
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Integer productid,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.update(user.getId(),productid,count));
    }
    //移除购物车某个产品 delete_product.do
    //传入 productIds，根据产品id和用户id删除
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse deleteProduct(HttpSession session, Integer productid){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.delete(user.getId(),productid));
    }
    //购物车选中某个商品 select.do
    //传入productid 将状态设置为选中

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer productid){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.select(user.getId(),productid));
    }
    //购物车取消选中某个商品 un_select.do
    //注意返回值中的cartTotalPrice，如果反选之后总价的变化
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse unSelect(HttpSession session, Integer productid){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.unSelect(user.getId(),productid));
    }
    //查询在购物车里的产品数量
    //根据用户id，sum统计数量  ，mybatis使用ifnull函数防止查询不到数据null不能赋值给int

    @RequestMapping("productCount.do")
    @ResponseBody
    public ServerResponse productCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.productCount(user.getId()));
    }
    //购物车全选 select_all.do
    // 注意返回值中的cartTotalPrice的变化
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.selectAll(user.getId()));
    }

    //购物车取消全选 un_select_all.do
    // 注意返回值中的cartTotalPrice总价的变化
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(iCartService.unSelectAll(user.getId()));
    }

}
