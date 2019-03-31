package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @Auther: yangkun
 * @Date: 2019/3/1 0001 21:53
 * @Description:
 */
public interface ICartService {

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> delete(Integer userId,Integer productId);

    ServerResponse<CartVo> select(Integer userId,Integer productId);

    ServerResponse<CartVo> unSelect(Integer userId,Integer productId);

    ServerResponse<CartVo> selectAll(Integer userId);

    ServerResponse<CartVo> unSelectAll(Integer userId);

    ServerResponse<Integer> productCount(Integer userId);

}
