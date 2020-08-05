package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Product;
import com.fh.model.vo.ProductCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ProductDao extends BaseMapper<Product> {
    public ProductCart queryProductCartById(Integer pid);
    public int updateProductCount(@Param("id") Integer pid, @Param("count") Integer count);
}
