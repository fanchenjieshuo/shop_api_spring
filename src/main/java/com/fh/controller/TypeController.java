package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.model.Type;
import com.fh.service.TypeService;
import com.fh.utils.RedisUse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("typeController")
public class TypeController {

    @Autowired
    private TypeService typeService;
    @ApiOperation("查询类型方法")
    @RequestMapping("findTypeList")
    public JsonData findTypeList(){


        List<Type> typeList = typeService.findTypeList();
        return JsonData.getJsonSuccess(typeList);
    }

}
