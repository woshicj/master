package com.manythings.service;


import com.manythings.doc.ProductDoc;

import java.util.List;

public interface IProductEsService {

    //添加一个
    void addOne(ProductDoc productDoc);

    //批量添加
    void addBatch(List<ProductDoc> productDocList);

    //删除一个
    void deleteOne(Long id);

    //批量删除
    void deleteBatch(List<Long> ids);

    //查询一个
    ProductDoc findOne(Long id);
}
