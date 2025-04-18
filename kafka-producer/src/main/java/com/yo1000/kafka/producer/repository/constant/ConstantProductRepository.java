package com.yo1000.kafka.producer.repository.constant;

import com.yo1000.kafka.producer.domain.repository.ProductRepository;
import com.yo1000.kafka.producer.domain.vo.Product;

import java.util.List;

public class ConstantProductRepository implements ProductRepository {


    @Override
    public List<Product> findAll() {
        return List.of();
    }
}
