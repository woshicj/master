package com.manythings.products.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.manything.util.PageList;
import com.manything.util.StrUtils;
import com.manythings.client.ProductEsClient;
import com.manythings.doc.ProductDoc;
import com.manythings.domain.Brand;
import com.manythings.domain.Product;
import com.manythings.domain.ProductExt;
import com.manythings.products.mapper.ProductExtMapper;
import com.manythings.products.mapper.ProductMapper;
import com.manythings.products.service.IProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.manythings.query.ProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    @Autowired
    private ProductExtMapper productExtMapper;
    @Autowired
    protected ProductMapper productMapper;
    @Autowired
    ProductEsClient productEsClient;


    @Override
    public boolean insert(Product entity) {
        boolean b = super.insert(entity);
        ProductExt productExt = entity.getProductExt();
        productExt.setProductId(entity.getId());
        productExtMapper.insert(productExt);
        return b;
    }


    @Override
    public PageList<Product> selectQuery(ProductQuery query) {
        PageList<Product> pageList = new PageList<>();
        long totalCount = productMapper.queryPageCount(query);
        if (totalCount>0){
            pageList.setTotal(totalCount);
            List<Product> products = productMapper.queryPage(query);
            pageList.setRows(products);
        }
        return pageList;
    }

    @Override
    public void onSale(String ids, Long opt) {
        List<Long> idlist = StrUtils.splitStr2LongArr(ids);
        //1:根据id修改数据库的状态和上架时间
        updateOnSaleBatch(idlist);
        //2:查询出数据库的数据  where id in (1,2,3)
        List<Product> productListDb =  productMapper.selectBatchIds(idlist);
        //3:把数据库的数据转换成:ProductDoc
        List<ProductDoc> list = getProductDocList(productListDb);
        //4:调用es的服务,上架
        productEsClient.addBatch(list);
    }

    private List<ProductDoc> getProductDocList(List<Product> productListDb) {
        List<ProductDoc> list = new ArrayList<>();
        for (Product product : productListDb) {
            ProductDoc doc = productToProductDoc(product);
            list.add(doc);
        }

        return list;
    }

    private ProductDoc productToProductDoc(Product product) {
        ProductDoc doc = new ProductDoc();
        doc.setId(product.getId());
        doc.setAll(product.getName() + " " + product.getSubName());
        doc.setBrandId(product.getBrandId());
        doc.setCommentCount(product.getCommentCount());
        doc.setMaxPrice(product.getMaxPrice());
        if (!StringUtils.isEmpty(product.getMedias())) {
            // List<String>
            String[] strings = StrUtils.splitStr2StrArr(product.getMedias());
            doc.setMedias(Arrays.asList(strings));//
        }

        doc.setMinPrice(product.getMinPrice());
        doc.setOnSaleTime(product.getOnSaleTime());
        doc.setProductTypeId(product.getProductTypeId());
        doc.setSaleCount(product.getSaleCount());
        doc.setViewCount(product.getViewCount());
        //ext表:
        ProductExt ext = new ProductExt();
        ext.setProductId(product.getId());
        // 关联查询: select * from t_product p left join t_product_ext e on p.id = e.productId where  id in (1,2,3)
        ProductExt productExt = productExtMapper.selectOne(ext);
        if(!StringUtils.isEmpty(productExt.getSkuProperties())){
            // productExt.getSkuProperties()
            // [{"id":33,"specName":"颜色","type":2,"productTypeId":9,"skuValues":["yellow","blank"]},
            // {"id":34,"specName":"尺寸","type":2,"productTypeId":9,"skuValues":["18","8"]}]
            String skuProperties = productExt.getSkuProperties();
            List<Map> maps= JSONArray.parseArray(skuProperties, Map.class);
            doc.setSkuProperties(maps);
        }

        if(!StringUtils.isEmpty(productExt.getViewProperties())){
            String viewProperties = productExt.getViewProperties();
            List<Map> maps= JSONArray.parseArray(viewProperties, Map.class);
            doc.setViewProperties(maps);
        }


        return doc;
    }

    private void updateOnSaleBatch(List<Long> idlist) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", idlist);
        params.put("onSaleTime", new Date().getTime());
        productMapper.updateOnSaleBatch(params);

    }

    @Override
    public void offSale(String ids, Long opt) {

    }
}
