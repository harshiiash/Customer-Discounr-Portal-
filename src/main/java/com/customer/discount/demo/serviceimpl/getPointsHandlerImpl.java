package com.customer.discount.demo.serviceimpl;

import com.customer.discount.demo.api.CustomerDB;
import com.customer.discount.demo.api.CustomerDBRepository;
import com.customer.discount.demo.api.DiscountDBRepository;
import com.customer.discount.demo.api.PurchaseDBRepository;
import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import com.customer.discount.demo.entity.Mongo.Discount;
import com.customer.discount.demo.entity.Mongo.Purchase;
import com.customer.discount.demo.restweb.model.request.DiscountRequest;
import com.customer.discount.demo.restweb.model.request.PurchaseHistoryRequest;
import com.customer.discount.demo.serviceapi.getPointsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
@Slf4j
@Service
public class getPointsHandlerImpl implements getPointsHandler {
  @Autowired
  private CustomerDBRepository customerDBRepository;

  @Autowired
  private PurchaseDBRepository purchaseDBRepository;

  @Autowired
  private DiscountDBRepository discountDBRepository;
  @Override
  public CustomerDatabase addcustomer(CustomerDatabase getPointsResponse) {
    getPointsResponse.setUid(getPointsResponse.getName().toLowerCase().substring(0,4).concat(getPointsResponse.getMobileNumber().substring(0,4)));
    customerDBRepository.save(getPointsResponse);
    return customerDBRepository.findByMobileNumber(getPointsResponse.getMobileNumber());
  }

  @Override
  public Purchase addPurchase(PurchaseHistoryRequest purchaseHistoryRequest) {
    return Objects.nonNull(customerDBRepository.findByMobileNumber(purchaseHistoryRequest.getMobile()))?
      PurchaseDB(purchaseHistoryRequest,customerDBRepository.findByMobileNumber(purchaseHistoryRequest.getMobile())):
      Objects.nonNull(addcustomer(CustomerDatabase.builder()
        .points((purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue())
        .uid((Objects.nonNull(purchaseHistoryRequest.getName()) && purchaseHistoryRequest.getName().length()>0)?
          purchaseHistoryRequest.getName().toLowerCase().substring(0,4).concat(purchaseHistoryRequest.getMobile().substring(0,4))
          :"hg"+purchaseHistoryRequest.getMobile())
        .mobileNumber(purchaseHistoryRequest.getMobile())
        .name(purchaseHistoryRequest.getName().length()>0?purchaseHistoryRequest.getName():"H&G")
        .build()))?purchaseDBRepository.save(Purchase.builder().amount(purchaseHistoryRequest.getAmount())
        .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).mobile(purchaseHistoryRequest.getMobile()).build()):null;
  }

  @Override
  public List<CustomerDatabase> getAll() {
    return customerDBRepository.findAll();
  }

  @Override
  public List<Purchase> findPurchasebyMobile(String mobile) {
    return purchaseDBRepository.findByMobile(mobile);
  }

  @Override
  public CustomerDatabase getPoints(String keyword) {
    return customerDBRepository.findByMobileNumber(keyword)!=null?customerDBRepository.findByMobileNumber(keyword)
      :customerDBRepository.findByUid(keyword);
  }

  @Override
  public Discount addDiscount(DiscountRequest discount) {
   return discountDBRepository.save(Discount.builder().discount(discount.getDiscount())
      .date(LocalDateTime.now()).build());
  }

  private Purchase PurchaseDB(PurchaseHistoryRequest purchaseHistoryRequest, CustomerDatabase customerDatabase) {
    CustomerDB.updateById(customerDatabase.getId(), CustomerDatabase.builder()
      .points(customerDatabase.getPoints() + (purchaseHistoryRequest.getAmount().multiply(BigDecimal.valueOf(findDiscount()))).setScale(0, RoundingMode.UP).intValue())
      .build());
    log.info("updating purchaseDB " + purchaseHistoryRequest.toString() + "  " + customerDatabase.toString());
    return purchaseDBRepository.save(Purchase.builder().amount(purchaseHistoryRequest.getAmount())
      .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).mobile(purchaseHistoryRequest.getMobile()).build());
  }
  @Override
  public Double findDiscount(){
    return (discountDBRepository.findAll(Sort.by(Sort.Direction.DESC,"date")).get(0).getDiscount())/100.0;
  }
}
