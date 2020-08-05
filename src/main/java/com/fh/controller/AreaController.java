package com.fh.controller;


import com.fh.common.JsonData;
import com.fh.model.AddRessInfo;
import com.fh.service.AreaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("areaController")
public class AreaController {
    
    @Autowired
    private AreaService areaService;


    @RequestMapping("findAreaList")
    public JsonData findAreaList(){
        
        List<AddRessInfo> areaList= areaService.findAreaList();
        return JsonData.getJsonSuccess(areaList);
    }
}
