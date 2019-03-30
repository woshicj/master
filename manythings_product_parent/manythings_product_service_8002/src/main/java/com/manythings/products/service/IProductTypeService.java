package com.manythings.products.service;

import com.manythings.domain.ProductType;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品目录 服务类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
public interface IProductTypeService extends IService<ProductType> {

    List<ProductType> treeData();

}
