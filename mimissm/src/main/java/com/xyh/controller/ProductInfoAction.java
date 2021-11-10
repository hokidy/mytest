package com.xyh.controller;

import com.github.pagehelper.PageInfo;
import com.xyh.pojo.ProductInfo;
import com.xyh.pojo.vo.ProductInfoVo;
import com.xyh.service.ProductInfoService;
import com.xyh.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    public static final int PAGE_SIZE=5;
    String saveFileName = "";
    @Resource
    ProductInfoService productInfoService;
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }
    //显示第一页
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else{
        info = productInfoService.splitPage(1,PAGE_SIZE);}

        request.setAttribute("info",info);
        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session){
        PageInfo info = productInfoService.splitPageVo(vo,PAGE_SIZE);
        session.setAttribute("info",info);
    }

    //异步ajax文件上传处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //提起生成文件名UUID+上传图片的后缀
        saveFileName = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            pimage.transferTo(new File(path+File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject object = new JSONObject();
        object.put("imgurl",saveFileName);

        return object.toString();
    }

    //
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的5个数据，有异步ajax上来的图片名称数据，有上架时间的数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (num>0){
            request.setAttribute("msg","增加成功");
        }else{
            request.setAttribute("msg", "增加失败");
        }
        saveFileName="";
        //重新访问数据库
        return "forward:/prod/split.action";
    }
    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model,HttpSession session){
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod",info);
        session.setAttribute("prodVo",vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        if(!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }
        int num=-1;
        try {
            num=productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg","更新成功");
        }else{
            request.setAttribute("msg", "更新失败");
        }

        saveFileName="";
        return "forward:/prod/split.action";

    }

    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功");
            request.getSession().setAttribute("dProdVo",vo);

        }else{
            request.setAttribute("msg", "删除失败");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object deleteAjaxSplit(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("dProdVo");
        if (vo!=null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);

        }else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request){
        String []ps = pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0) {
                request.setAttribute("msg","批量删除成功");
            }else{
                request.setAttribute("msg", "批量删除失败");
            }
        } catch (Exception e) {
           request.setAttribute("msg", "当前商品不可删除！");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    //多条件查询
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list",list);
    }


}
