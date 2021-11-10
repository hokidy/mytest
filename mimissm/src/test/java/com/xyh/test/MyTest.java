package com.xyh.test;

import com.xyh.mapper.ProductInfoMapper;
import com.xyh.pojo.ProductInfo;
import com.xyh.pojo.vo.ProductInfoVo;
import com.xyh.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class MyTest {
    @Test
    public void testMD5(){
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }
    @Resource
    ProductInfoMapper mapper;

    @Test
    public void testSelectCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        vo.setPname("小米");
        vo.setLprice(2000);
        List<ProductInfo> productInfos = mapper.selectCondition(vo);
        productInfos.forEach(productInfo -> System.out.println(productInfo));
    }

}
