package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.common.exception.CountException;
import com.fh.service.OrderService;
import com.fh.utils.RedisUse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("orderController")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @ApiOperation("新增订单")
    @RequestMapping("addOrder")
    public JsonData addOrder(Integer addressId,Integer payType,String flag)throws CountException {
        boolean exists = RedisUse.exists(flag);
        if(exists==true){

            return JsonData.getJsonError(300,"正在处理请稍后");

        }else {
            //时间
            RedisUse.set(flag,"",10);
        }
        Map map = orderService.addOrder(addressId, payType);
        return JsonData.getJsonSuccess(map);

    }


    @RequestMapping("addMeonyPhoto")

    public JsonData addMeonyPhoto(Integer orderId,String totalMoney) throws Exception {
        Map map = orderService.addMeonyPhoto(orderId, totalMoney);

        return JsonData.getJsonSuccess(map);
    }
    @RequestMapping("queryPayStatus")
    @ApiOperation("查询支付状态")
    public JsonData queryPayStatus(Integer orderId) throws Exception {
        Integer status = orderService.queryPayStatus(orderId);
        return JsonData.getJsonSuccess(status);
    }
}
