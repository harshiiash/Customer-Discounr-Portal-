package com.customer.discount.demo.api;

import com.customer.discount.demo.entity.Mongo.Purchase;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Slf4j
@Repository
public class PurchaseDB {
  private static MongoTemplate mongoTemplate;

  @Autowired
  public PurchaseDB(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public static Purchase updateByMobile(String Id, Purchase purchase){
    Query query= new Query();
    query.addCriteria(Criteria.where("mobile").is(Id));
    Update update=new Update();
    update.set("amount",purchase.getAmount());
    update.set("date", LocalDateTime.now());
    log.info(mongoTemplate.find(query,Purchase.class).toString());
    mongoTemplate.upsert(query,update,Purchase.class);
    return mongoTemplate.findOne(query,Purchase.class);
  }
}
