package com.fh.service.impl;

import com.fh.dao.ProductDao;
import com.fh.model.Product;
import com.fh.service.ProductService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;
    @Override
    public List<Product> findProductList() {


        return productDao.selectList(null);
    }
}
