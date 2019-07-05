package edu.nju.yummy.service;

import edu.nju.yummy.entity.Category;
import edu.nju.yummy.entity.Food;
import edu.nju.yummy.entity.Stock;
import edu.nju.yummy.enums.ResultMessage;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface FoodService {
    List<Category> getCategoryList(String userId);

    List<Food> getFoodList(long cid);

    ResultMessage saveCategory(long cid, String name, String userId);

    Food getFood(long fid);

    ResultMessage saveFood(long fid, String name, long cid, double price, double boxPrice);

    ResultMessage addStock(long fid, int number, String startTime, int days);

    List<Stock> getStockList(long fid);

}
