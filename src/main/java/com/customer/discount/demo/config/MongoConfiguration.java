package com.customer.discount.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfiguration {

  @Bean
  public MongoClient mongo1() {
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/harish-sons");
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .build();

    return MongoClients.create(mongoClientSettings);
  }

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() throws Exception {
    return new ReactiveMongoTemplate(mongo1(), "harish-sons");
  }

}