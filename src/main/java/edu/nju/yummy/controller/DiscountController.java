package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Discount;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @RequestMapping("/getDiscountList")
    public @ResponseBody
    List<Discount> getDiscountList(String userId){
        return discountService.getDiscountList(userId);
    }

    @RequestMapping("/getDiscount")
    public @ResponseBody
    Discount getDiscount(long did){
        return discountService.getDiscount(did);
    }

    @RequestMapping("/saveDiscount")
    public @ResponseBody
    ResultMessage saveDiscount(long did, String userId, double fullPrice, double reduction){
        return discountService.saveDiscount(did, userId, fullPrice, reduction);
    }

    @RequestMapping("/deleteDiscount")
    public @ResponseBody
    ResultMessage deleteDiscount(long did){
        return discountService.deleteDiscount(did);
    }
}
