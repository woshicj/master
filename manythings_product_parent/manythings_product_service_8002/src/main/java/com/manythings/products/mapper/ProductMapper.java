package com.manythings.products.mapper;

import com.manythings.domain.Brand;
import com.manythings.domain.Product;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.manythings.query.ProductQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
public interface ProductMapper extends BaseMapper<Product> {

    long queryPageCount(ProductQuery query);

    List<Product> queryPage(ProductQuery query);

    void updateOnSaleBatch(Map<String, Object> params);
}
