package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Category;
import edu.nju.yummy.entity.Food;
import edu.nju.yummy.entity.Stock;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @RequestMapping("/getCategoryList")
    public @ResponseBody
    List<Category> getCategoryList(String userId) {
        return foodService.getCategoryList(userId);
    }

    @RequestMapping("/getFoodList")
    public @ResponseBody
    List<Food> getFoodList(long cid) {
        return foodService.getFoodList(cid);
    }

    @RequestMapping("/saveCategory")
    public @ResponseBody
    ResultMessage saveCategory(long cid, String name, String userId) {
        return foodService.saveCategory(cid, name, userId);
    }

    @RequestMapping("/getFood")
    public @ResponseBody
    Food getFood(long fid) {
        return foodService.getFood(fid);
    }

    @RequestMapping("/saveFood")
    public @ResponseBody
    ResultMessage saveFood(long fid, String name, long cid, double price, double boxPrice) {
        return foodService.saveFood(fid, name, cid, price, boxPrice);
    }

    @RequestMapping("/addStock")
    public @ResponseBody
    ResultMessage addStock(long fid, int number, String startTime, int days) {
        return foodService.addStock(fid, number, startTime, days);
    }

    @RequestMapping("/getStockList")
    public @ResponseBody
    List<Stock> getStockList(long fid) {
        return foodService.getStockList(fid);
    }
}
