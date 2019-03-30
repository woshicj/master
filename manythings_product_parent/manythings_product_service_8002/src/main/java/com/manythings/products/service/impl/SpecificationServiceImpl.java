package com.manythings.products.service.impl;

import com.manythings.domain.Specification;
import com.manythings.products.mapper.SpecificationMapper;
import com.manythings.products.service.ISpecificationService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author CJJ
 * @since 2019-03-06
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements ISpecificationService {

}
