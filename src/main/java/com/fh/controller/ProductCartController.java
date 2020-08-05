package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.service.ProductCartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("productCartController")
public class ProductCartController {

    @Autowired
    private ProductCartService productCartService;
    @RequestMapping("addCart")
    @ApiOperation("新增购物车")
    public JsonData addCart(@RequestParam("id") Integer proId, Integer count){
        Integer integer = productCartService.addProductToCart(proId, count);
        return JsonData.getJsonSuccess(integer);
    }

    @RequestMapping("addCart1")

    public JsonData addCart1(@RequestParam("id") Integer proId, Integer count){
        Integer integer = productCartService.addProductToCart1(proId, count);
        return JsonData.getJsonSuccess(integer);
    }


    @RequestMapping("findProductCartList")
    @ApiOperation("查询购物车商品方法")
    public JsonData findProductCartList(){

       List list=productCartService.findProductCartList();
        return JsonData.getJsonSuccess(list);
    }

    @RequestMapping("deleteProduct")
    @ApiOperation("删除购物车商品方法")
    public JsonData deleteProduct(String id){
        productCartService.delProduct(id);
        return JsonData.getJsonSuccess("删除成功");

    }


}
