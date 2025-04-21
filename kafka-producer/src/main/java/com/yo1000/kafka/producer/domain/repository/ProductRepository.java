package com.yo1000.kafka.producer.domain.repository;

import com.yo1000.kafka.producer.domain.vo.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
}
