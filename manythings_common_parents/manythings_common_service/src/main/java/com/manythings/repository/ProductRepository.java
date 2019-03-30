package com.manythings.repository;

import com.manythings.doc.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<ProductDoc,Long> {
}
