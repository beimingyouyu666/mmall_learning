package com.mmall.controller.protal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: yangkun
 * @Date: 2019/2/28 0028 23:37
 * @Description:
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    //购物车列表 list.do
    //根据用户id查询购物车中所有数据


    //购物车添加商品 add.do
    //传入 productid和数量 请注意这个字段，超过数量会返回这样的标识"limitQuantity" 失败的：LIMIT_NUM_FAIL 成功的：LIMIT_NUM_SUCCESS


    //更新购物车某个产品数量 update.do
    //传入productId,count  根据用户id和产品id 更新数量

    //移除购物车某个产品 delete_product.do
    //传入 productIds，根据产品id和用户id删除

    //购物车选中某个商品 select.do
    //传入productid 将状态设置为选中

    //购物车取消选中某个商品 un_select.do
    //注意返回值中的cartTotalPrice，如果反选之后总价的变化

    //查询在购物车里的产品数量
    //根据用户id，sum统计数量  ，mybatis使用ifnull函数防止查询不到数据null不能赋值给int

    //购物车全选 select_all.do
    // 注意返回值中的cartTotalPrice的变化

    //购物车取消全选 un_select_all.do
    // 注意返回值中的cartTotalPrice总价的变化


}
