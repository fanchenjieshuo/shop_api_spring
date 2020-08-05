package com.fh.service;

import com.fh.common.exception.CountException;

import java.util.Map;

public interface OrderService {
    public Map addOrder(Integer addressId, Integer payType)throws CountException;

    Map addMeonyPhoto(Integer orderId , String totalMoney) throws Exception;

    Integer queryPayStatus(Integer orderId)throws Exception;
}
