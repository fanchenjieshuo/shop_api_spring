package com.fh.service;

import java.util.List;

public interface ProductCartService {
    public Integer addProductToCart(Integer proId, Integer count);
    public Integer addProductToCart1(Integer proId, Integer count);
    List findProductCartList();

    void delProduct(String id);
}
