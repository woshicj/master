package com.manythings.products.mapper;

import com.manythings.domain.Brand;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.manythings.query.BrandQuery;

import java.util.List;

/**
 * <p>
 * 品牌信息 Mapper 接口
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
public interface BrandMapper extends BaseMapper<Brand> {

    long queryPageCount(BrandQuery query);

    List<Brand> queryPage(BrandQuery query);
}
