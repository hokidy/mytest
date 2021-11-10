package com.xyh.service.impl;

import com.xyh.mapper.AdminMapper;
import com.xyh.pojo.Admin;
import com.xyh.pojo.AdminExample;
import com.xyh.service.AdminService;
import com.xyh.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
   @Resource
    AdminMapper adminMapper;
    @Override
    public Admin login(String name, String pwd) {
        //根据传入用户名到DB中查询相应用户对象
        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);
        List<Admin> list = adminMapper.selectByExample(example);

        //如查询到，进行密码比对
        if (list.size() > 0) {
            Admin admin = list.get(0);
            String miPwd = MD5Util.getMD5(pwd);
            if (miPwd.equals(admin.getaPass())) {
                return admin;
            }
        }

        return null;
    }
}
