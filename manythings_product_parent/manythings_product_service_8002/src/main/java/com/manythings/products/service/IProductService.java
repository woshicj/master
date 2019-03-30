package com.manythings.products.service;

import com.manything.util.PageList;
import com.manythings.domain.Product;
import com.baomidou.mybatisplus.service.IService;
import com.manythings.query.ProductQuery;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
public interface IProductService extends IService<Product> {

    PageList<Product> selectQuery(ProductQuery query);

    void onSale(String ids, Long opt);

    void offSale(String ids, Long opt);
}
