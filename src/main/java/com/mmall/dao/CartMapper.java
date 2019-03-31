package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> queryCartByUserId(Integer userId);

    int queryCheckStatus(Integer userId);

    Cart queryCartByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    int deleteByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    int updateCheckStatus(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    int queryProductCount(Integer userId);
}