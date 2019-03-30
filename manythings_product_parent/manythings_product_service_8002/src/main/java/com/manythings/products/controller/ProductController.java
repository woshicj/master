package com.manythings.products.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.manything.util.AjaxResult;
import com.manything.util.PageList;
import com.manythings.client.ProductEsClient;
import com.manythings.doc.ProductDoc;
import com.manythings.domain.ProductExt;
import com.manythings.domain.Specification;
import com.manythings.products.service.IProductExtService;
import com.manythings.products.service.IProductService;
import com.manythings.domain.Product;
import com.manythings.query.ProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    public IProductService productService;
    @Autowired
    public IProductExtService productExtService;
    @Autowired
    public ProductEsClient productEsClient;

    /**
    * 保存和修改公用的
    * @param product  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Product product){
        try {
            if(product.getId()!=null){
                productService.updateById(product);
            }else{
                productService.insert(product);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMsg("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            productService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Product get(@PathVariable("id")Long id)
    {
        return productService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Product> list(){

        return productService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Product> json(@RequestBody ProductQuery query)
    {
//        Page<Product> page = new Page<Product>(query.getPage(),query.getRows());
//            page = productService.selectPage(page);
//            return new PageList<Product>(page.getTotal(),page.getRecords());
        return productService.selectQuery(query);

    }
    //// 把这个存到t_product_ext表的一个viewProperties,需要根据productId进行过滤:
    // 后台要接收多个参数,我们提交一般用post请求,那么可以通过RequestBody接收前台参数
    //但是RequestBody只能有一个识别才ok,我这里有多个参数,不行.应该考虑把我们参数的接收变成一个接收对象:
    //方案1:提供一个domain,设置前台传递的两个属性 方案2:使用map
    @RequestMapping(value = "/viewProperties", method = RequestMethod.POST)
    public AjaxResult saveViewProperties(@RequestBody Map<String, Object> map) {
        try {
            //目的是更新:t_product_ext的viewProperties
            //1:接收前台的数据  java.lang.Integer cannot be cast to java.lang.Long
            Object productId = map.get("productId");
            //前台传过来的添加的显示属性的list
            List<Specification> viewProperties = (List<Specification>) map.get("viewProperties");// List<>

            //2:viewProperties转换成json字符:
            String toJSONString = JSONArray.toJSONString(viewProperties);

            //3:更新到productExt的viewProperties中:需要的就是json的字符串
            ProductExt productExt = new ProductExt();
            productExt.setViewProperties(toJSONString);
            productExtService.update(productExt, new EntityWrapper<ProductExt>().eq("productId", productId));
            return AjaxResult.me().setMsg("显示属性保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMsg("显示属性保存失败:" + e.getMessage());
        }

    }
    /**
     * 上下架的操作
     *
     * @param map ids:操作的id   1,2,3    ;  optType:1 上架请求   2下架请求
     * @return
     */
    @RequestMapping(value = "/productSale", method = RequestMethod.POST)
    public AjaxResult productSale(@RequestBody Map<String, Object> map) {
        Object ids1 = map.get("ids");
        Object optType = map.get("optType");
        if (ids1 != null && optType != null) {
            try {
                String ids = ids1.toString();
                Long opt = Long.valueOf(optType.toString());
                if (opt == 1) {
                    productService.onSale(ids,opt);
                } else if (opt == 2) {
                    productService.offSale(ids,opt);
                }
                return AjaxResult.me().setSuccess(true).setMsg("上下架成功");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return AjaxResult.me().setSuccess(false).setMsg("上下架失败");
            }

        } else {
            return AjaxResult.me().setSuccess(false).setMsg("请传入正确请求参数");
        }

    }

    @RequestMapping(value = "/productSale33", method = RequestMethod.POST)
    public com.manythings.util.AjaxResult productSale232(@RequestBody Map<String, Object> map) {

        return productEsClient.addBatch(new ArrayList<ProductDoc>());
    }
}
