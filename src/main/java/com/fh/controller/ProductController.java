package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.Product;
import com.fh.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("productController")
public class ProductController {

    @Autowired
    private ProductService productService;
    @ApiOperation("查询商品方法")
    @RequestMapping("findProductList")
    public JsonData findProductList(){

        List<Product> productList = productService.findProductList();
        return  JsonData.getJsonSuccess(productList);
    }
}
