package com.manythings.products.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.manything.util.PageList;
import com.manythings.domain.Brand;
import com.manythings.domain.Product;
import com.manythings.products.mapper.BrandMapper;
import com.manythings.products.service.IBrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.manythings.query.BrandQuery;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {
    @Autowired
    BrandMapper brandMapper;

    public PageList<Brand> queryPage(BrandQuery query) {
        PageList<Brand> pageList = new PageList<>();
        long totalCount = brandMapper.queryPageCount(query);
        if (totalCount>0){
            pageList.setTotal(totalCount);
            List<Brand> brands = brandMapper.queryPage(query);
            pageList.setRows(brands);
        }
        return pageList;
    }
}
