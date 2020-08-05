package com.fh.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.fh.dao.ProductDao;
import com.fh.model.vo.ProductCart;
import com.fh.service.ProductCartService;
import com.fh.utils.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ProductCartServiceImpl implements ProductCartService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ProductDao productDao;


    @Override
    public Integer addProductToCart(Integer proId, Integer count) {
        Map login_user = (Map) request.getAttribute("login_user");

        String iphone = (String) login_user.get("iphone");

        String product = RedisUse.hget("cart" + iphone + "lbw", proId + "");
        if (StringUtils.isEmpty(product)) {
            ProductCart productCart = productDao.queryProductCartById(proId);
            productCart.setCheck(true);
            productCart.setCount(count);

            BigDecimal money = productCart.getPrice().multiply(new BigDecimal(count));
            productCart.setMoney(money);

            String productCartString = JSONObject.toJSONString(productCart);

            RedisUse.hset("cart" + iphone + "lbw", proId + "", productCartString);
        } else {

            ProductCart productCart = JSONObject.parseObject(product, ProductCart.class);
            productCart.setCount(productCart.getCount() + count);
            BigDecimal money=productCart.getPrice().multiply(new BigDecimal(productCart.getCount()));
            productCart.setMoney(money);
            String productCartString = JSONObject.toJSONString(productCart);
            RedisUse.hset("cart" + iphone + "lbw", proId + "", productCartString);
        }
        long hlen = RedisUse.hlen("cart" + iphone + "lbw");


        return (int) hlen;
    }

    @Override
    public Integer addProductToCart1(Integer proId, Integer count) {

        Map login_user = (Map) request.getAttribute("login_user");

        String iphone = (String) login_user.get("iphone");

        String product = RedisUse.hget("cart" + iphone + "lbw", proId + "");

        ProductCart productCart = productDao.queryProductCartById(proId);
        productCart.setCheck(true);
        productCart.setCount(count);

        BigDecimal money = productCart.getPrice().multiply(new BigDecimal(count));
        productCart.setMoney(money);

        String productCartString = JSONObject.toJSONString(productCart);

        RedisUse.hset("cart" + iphone + "lbw", proId + "", productCartString);

        long hlen = RedisUse.hlen("cart" + iphone + "lbw");


        return (int) hlen;
    }

    @Override
    public List findProductCartList() {

        Map login_user = (Map) request.getAttribute("login_user");

        String iphone = (String) login_user.get("iphone");

        List<String> hvals = RedisUse.hvals("cart" + iphone + "lbw");

        return hvals;
    }

    @Override
    public void delProduct(String id) {

        Map login_user = (Map) request.getAttribute("login_user");
        String iphone = (String) login_user.get("iphone");

        RedisUse.hdel("cart" + iphone + "lbw",id);
    }
}
