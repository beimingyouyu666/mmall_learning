package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @Auther: yangkun
 * @Date: 2019/3/20 0020 23:46
 * @Description:
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, String path, Integer userId);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

}
