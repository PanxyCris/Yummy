package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.CategoryDao;
import edu.nju.yummy.dao.FoodDao;
import edu.nju.yummy.dao.StockDao;
import edu.nju.yummy.entity.Category;
import edu.nju.yummy.entity.Food;
import edu.nju.yummy.entity.Stock;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.FoodService;
import edu.nju.yummy.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodDao foodDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private StockDao stockDao;

    @Override
    public List<Category> getCategoryList(String userId) {
        return categoryDao.findCategoriesByUserId(userId);
    }

    @Override
    public List<Food> getFoodList(long cid) {
        List<Food> originFood = foodDao.getFoodsByCategory_Cid(cid);
        List<Food> filtFood = new ArrayList<>();
        for (Food food : originFood)
            filtFood.add(checkFood(food.getFid()));
        return filtFood;
    }

    @Override
    public ResultMessage saveCategory(long cid, String name, String userId) {
        Category category = new Category();
        if (cid != -1)
            category = categoryDao.getOne(cid);
        category.setName(name);
        category.setUserId(userId);
        categoryDao.saveAndFlush(category);
        return ResultMessage.Success;
    }

    @Override
    public Food getFood(long fid) {
        return checkFood(fid);
    }

    @Override
    public ResultMessage saveFood(long fid, String name, long cid, double price, double boxPrice) {
        Food food = new Food();
        if (fid != -1)
            food = foodDao.getOne(fid);
        else
            food.setNumber(0);
        food.setName(name);
        food.setCategory(categoryDao.getOne(cid));
        food.setPrice(price);
        food.setBoxPrice(boxPrice);
        foodDao.saveAndFlush(food);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage addStock(long fid, int number, String startTime, int days) {
        Stock stock = new Stock();
        stock.setFood(foodDao.getOne(fid));
        stock.setNumber(number);
        stock.setLeftNumber(number);
        LocalDate localDate = LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        stock.setStartTime(localDate);
        stock.setDays(days);
        stock.setEndTime(localDate.plusDays(days));
        stock.setPass(false);
        Food food = foodDao.getOne(fid);
        food.setNumber(food.getNumber() + number);
        stockDao.saveAndFlush(stock);
        foodDao.saveAndFlush(food);
        return ResultMessage.Success;
    }

    @Override
    public List<Stock> getStockList(long fid) {
        return stockDao.findStocksByFood_Fid(fid);
    }


    /**
     * 对过期食物的检查
     *
     * @param fid
     * @return
     */
    public Food checkFood(long fid) {
        Food food = foodDao.getOne(fid);
        List<Stock> stocks = stockDao.findStocksByFood_FidAndPassIsFalse(fid);
        for (Stock stock : stocks) {
            if (stock.getEndTime().isBefore(LocalDate.now())) {
                stock.setPass(true);
                food.setNumber(food.getNumber() - stock.getLeftNumber());
                stockDao.saveAndFlush(stock);
            }
        }
        foodDao.saveAndFlush(food);
        return food;
    }
}
