package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yangkun
 * @Date: 2019/3/1 0001 21:53
 * @Description:
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    private CartVo cartCoreMethod(Integer userId){
        CartVo cartVo = new CartVo();
        List<CartProductVo> resultList = new ArrayList<>();
        List<Cart> cartList = cartMapper.queryCartByUserId(userId);
        BigDecimal totalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)){
            for (Cart cart : cartList) {
                CartProductVo cpv = new CartProductVo();
                cpv.setUserId(userId);
                cpv.setProductId(cart.getProductId());
                //将购物车商品数量与商品库存进行对比
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null){
                    if (cart.getQuantity() <= product.getStock()){
                        cpv.setQuantity(cart.getQuantity());
                        cpv.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        cpv.setQuantity(product.getStock());
                        cpv.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(cart1);
                    }
                    cpv.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cpv.getQuantity()));
                    cpv.setProductChecked(cart.getChecked());
                }
                if(cart.getChecked() == Const.Cart.CHECKED){
                    //如果已经勾选,增加到整个的购物车总价中
                    totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(),cpv.getProductTotalPrice().doubleValue());
                }
                resultList.add(cpv);
            }
        }
        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartProductVoList(resultList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;

    }

    private Boolean getAllCheckedStatus(Integer userId) {
        return cartMapper.queryCheckStatus(userId) == 0;
    }

    public ServerResponse<CartVo> list(Integer userId){
        return ServerResponse.createBySuccess(this.cartCoreMethod(userId));
    }

    public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
        //添加商品，如果购物车中有了就增加数量，如果没有就要添加
        Cart cart = cartMapper.queryCartByUserIdProductId(userId, productId);
        if(cart == null){
            //这个产品不在这个购物车里,需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.queryCartByUserIdProductId(userId, productId);
        if(cart == null){
            return ServerResponse.createByErrorMessage("此商品在购物车中不存在");
        }
        cart.setQuantity(count);
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    public ServerResponse<CartVo> delete(Integer userId,Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductId(userId,productId);
        return this.list(userId);
    }

    public ServerResponse<CartVo> select(Integer userId,Integer productId){
        cartMapper.updateCheckStatus(userId,productId,Const.Cart.CHECKED);
        return this.list(userId);
    }

    public ServerResponse<CartVo> unSelect(Integer userId,Integer productId){
        cartMapper.updateCheckStatus(userId,productId,Const.Cart.UN_CHECKED);
        return this.list(userId);
    }

    public ServerResponse<CartVo> selectAll(Integer userId){
        cartMapper.updateCheckStatus(userId,null,Const.Cart.CHECKED);
        return this.list(userId);
    }

    public ServerResponse<CartVo> unSelectAll(Integer userId){
        cartMapper.updateCheckStatus(userId,null,Const.Cart.UN_CHECKED);
        return this.list(userId);
    }

    public ServerResponse<Integer> productCount(Integer userId){
        return ServerResponse.createBySuccess(cartMapper.queryProductCount(userId));
    }

}
