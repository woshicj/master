package com.manythings.client;

import com.manythings.doc.ProductDoc;
import com.manythings.util.AjaxResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductEsClientFactory implements FallbackFactory<ProductEsClient> {

    @Override
    public ProductEsClient create(Throwable throwable) {
        return new ProductEsClient() {
            @Override
            public AjaxResult addOne(ProductDoc productDoc) {
                return null;
            }

            @Override
            public AjaxResult addBatch(List<ProductDoc> productDocList) {
                return null;
            }

            @Override
            public AjaxResult deleteOne(Long id) {
                return null;
            }

            @Override
            public AjaxResult deleteBatch(List<Long> ids) {
                return null;
            }

            @Override
            public AjaxResult findOne(Long id) {
                return null;
            }
        };
    }
}
