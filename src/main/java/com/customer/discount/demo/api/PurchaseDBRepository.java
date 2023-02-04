package com.customer.discount.demo.api;

import com.customer.discount.demo.entity.Mongo.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseDBRepository extends MongoRepository<Purchase,String> {
  List<Purchase> findByMobile(String mobile);

}
