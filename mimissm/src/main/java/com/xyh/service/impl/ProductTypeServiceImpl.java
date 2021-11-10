package com.xyh.service.impl;

import com.xyh.mapper.ProductTypeMapper;
import com.xyh.pojo.ProductType;
import com.xyh.pojo.ProductTypeExample;
import com.xyh.service.ProductTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {
    @Resource
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
