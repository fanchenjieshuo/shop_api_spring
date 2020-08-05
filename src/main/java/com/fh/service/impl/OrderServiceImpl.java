package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.OrderEnum;
import com.fh.common.exception.CountException;
import com.fh.dao.OrderDao;
import com.fh.dao.OrderProductDao;
import com.fh.dao.ProductDao;
import com.fh.model.Order;
import com.fh.model.OrderProduct;
import com.fh.model.Product;
import com.fh.model.vo.ProductCart;
import com.fh.service.OrderService;
import com.fh.utils.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired

    private OrderDao orderDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderProductDao orderProductDao;
    @Override
    public Map addOrder(Integer addressId, Integer payType) throws CountException {

        HashMap<Object, Object> result = new HashMap<>();

        List<OrderProduct> list = new ArrayList<>();

        Order order = new Order();
        order.setAddressId(addressId);
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setPayStatus((OrderEnum.PAY_STATUS_INIT.getStatus()));

        Integer typeCount=0;
        BigDecimal bigDecimal = new BigDecimal(0);
        Map login_user = (Map) request.getAttribute("login_user");

        String iphone = (String) login_user.get("iphone");

        List<String> productsStr = RedisUse.hvals("cart" + iphone + "lbw");

        for (int i = 0; i < productsStr.size(); i++) {

            ProductCart productCart = JSONObject.parseObject(productsStr.get(i), ProductCart.class);


            if(productCart.isCheck()==true){

                Product product = productDao.selectById(productCart.getId());

                if(product.getCount()>=productCart.getCount()){

                    typeCount++;
                    bigDecimal=bigDecimal.add(productCart.getMoney());


                    OrderProduct orderProduct=new OrderProduct();
                    orderProduct.setCount(productCart.getCount());
                    orderProduct.setProductId(productCart.getId());

                    list.add(orderProduct);


                    int i1 = productDao.updateProductCount(product.getId(), productCart.getCount());
                    System.out.println(i1);
                    if(i1==0){
                        throw new CountException("商品编号为:"+productCart.getId()+"的库存不足   库存只有："+product.getCount());
                    }
                }else{
                    throw new CountException("商品编号为:"+productCart.getId()+"的库存不足   库存只有："+product.getCount());
                }
            }

        }

        order.setProTypeCount(typeCount);
        order.setTotalMoney(bigDecimal);
        order.setIpone(iphone);
        orderDao.insert(order);
        orderProductDao.batchAdd(list,order.getId());

        for (int i = 0; i < productsStr.size(); i++) {

            ProductCart productCart = JSONObject.parseObject(productsStr.get(i), ProductCart.class);
            if(productCart.isCheck()==true){
                RedisUse.hdel("cart" + iphone + "lbw",productCart.getId()+"");
            }
        }
        result.put("code",200);
        result.put("orderId",order.getId());
        result.put("totalMoney",bigDecimal);
        return result;
    }

    @Override
    public Map addMeonyPhoto(Integer orderId, String totalMoney) throws Exception {
        Map rs=new HashMap();

        Order order = orderDao.selectById(orderId);


        FeiConfig config = new FeiConfig();

        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();

        data.put("body", "飞狐电商666-订单支付");


        data.put("out_trade_no","weixin1_order_lbw_"+orderId);

        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));

        data.put("time_expire", dateStr);


        BigDecimal bigDecimal = new BigDecimal(100);

        int i = Integer.parseInt(totalMoney);

        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");

        data.put("trade_type", "NATIVE");

        Map<String, String> resp = wxpay.unifiedOrder(data);

        System.out.println(orderId+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            rs.put("code",200);
            rs.put("url",resp.get("code_url"));

            order.setPayStatus(OrderEnum.PAY_STATUS_ING.getStatus());
            orderDao.updateById(order);
        }else {
            rs.put("code",600);
            rs.put("info",resp.get("return_msg"));
        }
        return rs;
    }

    @Override
    public Integer queryPayStatus(Integer oid) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","weixin1_order_lbw_"+oid);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询结果："+JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新订单状态
                Order order=new Order();
                order.setId(oid);
                order.setPayStatus(OrderEnum.PAY_STATUS_SUCCESS.getStatus());
                orderDao.updateById(order);
                return 1;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }
        }
        return 0;
    }

}
