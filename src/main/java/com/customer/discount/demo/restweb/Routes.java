package com.customer.discount.demo.restweb;

import com.customer.discount.demo.entity.Mongo.CustomerDatabase;
import com.customer.discount.demo.entity.Mongo.Discount;
import com.customer.discount.demo.entity.constant.ApiPath;
import com.customer.discount.demo.restweb.model.request.DiscountRequest;
import com.customer.discount.demo.restweb.model.request.PurchaseHistoryRequest;
import com.customer.discount.demo.serviceapi.getPointsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
public class Routes {
  @Autowired
  private getPointsHandler getPointsHandler;

  @RequestMapping(value = ApiPath.BASE_PATH_V1, method = RequestMethod.GET)
  public String dashboard(Model model) {
      model.addAttribute("dashboard", getPointsHandler.getAll());
      model.addAttribute("discount",getPointsHandler.findDiscount()*100);
    return "index";
  }
  @RequestMapping(value =ApiPath.BASE_PATH_V1+ApiPath.GET_POINTS, method = RequestMethod.GET)
  public String dashboardPoints(Model model,@RequestParam("keyword") String keyword) {
    if(keyword.length()>0){
      model.addAttribute("dashboard",getPointsHandler.getPoints(keyword));
    }
    else {
      model.addAttribute("dashboard", getPointsHandler.getAll());
    }
    return "indexGetPoints";
  }

  @RequestMapping(value = ApiPath.BASE_PATH_V1+ApiPath.ADD_CUSTOMER,method = RequestMethod.GET)
  public String addcustomerEndpoint( Model model) {
    CustomerDatabase customerDatabase=CustomerDatabase.builder().build();
    model.addAttribute("customer",customerDatabase);
     return "addCustomer";
  }
  @RequestMapping(value = "customer/save")
  public String save(@ModelAttribute("customer") CustomerDatabase getPointsResponse) {
    getPointsHandler.addcustomer(getPointsResponse);
     return "redirect:/harish-and-sons/v1";
  }

  @RequestMapping(value = ApiPath.BASE_PATH_V1+ApiPath.ADD_PURCHASE,method = RequestMethod.GET)
  public String addPurchaseEndpoint( Model model) {
    PurchaseHistoryRequest purchaseHistoryRequest=new PurchaseHistoryRequest();
    model.addAttribute("purchase",purchaseHistoryRequest);
    model.addAttribute("discount",getPointsHandler.findDiscount()*100);
    return "addPurchase";
  }

  @RequestMapping(value = "/purchase/save", method = RequestMethod.POST)
  public String addPurchase(@ModelAttribute("purchase") PurchaseHistoryRequest purchase) {
    getPointsHandler.addPurchase(purchase);
    return "redirect:/harish-and-sons/v1";
  }

  @RequestMapping(value = ApiPath.BASE_PATH_V1+ApiPath.VIEW_PURCHASE,method = RequestMethod.GET)
  public String viewPurchaseEndpoint(@RequestParam("mobile") String mobile, Model model) {
    model.addAttribute("purchase",getPointsHandler.findPurchasebyMobile(mobile));
    log.info(getPointsHandler.findPurchasebyMobile(mobile).toString());
    return "viewPurchase";
  }
  @RequestMapping(value = ApiPath.BASE_PATH_V1+ApiPath.DISCOUNT,method = RequestMethod.GET)
  public String changeDiscountEndpoint( Model model) {
  DiscountRequest discount=new DiscountRequest();
  model.addAttribute("discount",discount);
  model.addAttribute("discountRate",getPointsHandler.findDiscount()*100);
    return "discount";
  }

  @RequestMapping(value = "/discount/save", method = RequestMethod.POST)
  public String addPurchase(@ModelAttribute("discount") DiscountRequest discount) {
    getPointsHandler.addDiscount(discount);
    return "redirect:/harish-and-sons/v1";
  }

}
