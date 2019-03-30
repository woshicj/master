package com.manythings.products.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.manything.util.PageList;
import com.manythings.products.service.ISpecificationService;
import com.manythings.domain.Specification;
import com.manythings.query.SpecificationQuery;
import com.baomidou.mybatisplus.plugins.Page;
import com.manythings.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Autowired
    public ISpecificationService specificationService;

    /**
    * 保存和修改公用的
    * @param specification  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Specification specification){
        try {
            if(specification.getId()!=null){
                specificationService.updateById(specification);
            }else{
                specificationService.insert(specification);
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
            specificationService.deleteById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMsg("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Specification get(@PathVariable("id")Long id)
    {
        return specificationService.selectById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Specification> list(){

        return specificationService.selectList(null);
    }


    /**
    * 分页查询数据
    *
    */
    @RequestMapping(value = "/viewProperties/{productTypeId}",method = RequestMethod.POST)
    public PageList<Specification> viewProperties(@PathVariable("productTypeId") Long productTypeId)
    {
//        Page<Specification> page = new Page<Specification>(query.getPage(),query.getRows());
//            page = specificationService.selectPage(page);
//            return new PageList<Specification>(page.getTotal(),page.getRecords());
        Wrapper<Specification> wrapper = new EntityWrapper<>();
        wrapper.eq("product_type_id",productTypeId);
        List<Specification> specifications = specificationService.selectList(wrapper);

        return null;
    }


    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Specification> json(@RequestBody SpecificationQuery query)
    {
        Page<Specification> page = new Page<Specification>(query.getPage(),query.getRows());
            page = specificationService.selectPage(page);
            return new PageList<Specification>(page.getTotal(),page.getRecords());
    }
}

