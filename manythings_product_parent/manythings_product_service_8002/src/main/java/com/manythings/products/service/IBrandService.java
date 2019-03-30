package com.manythings.products.service;

import com.manything.util.PageList;
import com.manythings.domain.Brand;
import com.baomidou.mybatisplus.service.IService;
import com.manythings.query.BrandQuery;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
public interface IBrandService extends IService<Brand> {
    PageList<Brand> queryPage(BrandQuery query);
}
