package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.dao.AreaDao;
import com.fh.model.AddRessInfo;
import com.fh.model.Area;
import com.fh.service.AreaService;
import com.fh.utils.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AreaDao areaDao;

    @Override
    public List<AddRessInfo> findAreaList() {

        //获取登录人的信息
        Map login_user = (Map) request.getAttribute("login_user");

        //获取登陆人手机号
        String iphone = (String) login_user.get("iphone");

        QueryWrapper<Area> wrapper = new QueryWrapper<>();

        wrapper.eq("vipId",iphone);
        //查询数据库数据
        List<Area> areas = areaDao.selectList(wrapper);

        ArrayList<AddRessInfo> list = new ArrayList<>();
        for (int i = 0; i <areas.size() ; i++) {

            AddRessInfo addRessInfo = new AddRessInfo();
            Area area = areas.get(i);
            addRessInfo.setId(area.getId());
            addRessInfo.setIphone(area.getIphone());
            addRessInfo.setName(area.getName());

            String areaIds = area.getAreaIds();
            String areaNames = RedisUse.getAreaNames(areaIds);
            addRessInfo.setAddress(areaNames+area.getDetailAdd());
            addRessInfo.setIscheck(area.getIscheck());

            list.add(addRessInfo);
        }
        return list;
    }
}
