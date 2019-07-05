package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.BillDao;
import edu.nju.yummy.dao.CartDao;
import edu.nju.yummy.dao.FoodDao;
import edu.nju.yummy.dao.StockDao;
import edu.nju.yummy.entity.Bill;
import edu.nju.yummy.entity.Cart;
import edu.nju.yummy.entity.Food;
import edu.nju.yummy.entity.Stock;
import edu.nju.yummy.enums.BillState;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.CartService;
import edu.nju.yummy.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private FoodService foodService;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private BillDao billDao;

    @Autowired
    private FoodDao foodDao;

    @Autowired
    private StockDao stockDao;

    @Override
    public ResultMessage changeCart(String userId, String canteenId, long fid, boolean isAdd) {
        List<Bill> bills = billDao.findBillsByUserIdAndCanteenIdAndBillStateLessThanEqualOrderByTimeStampsDesc(userId, canteenId, BillState.NotPaid);
        Bill bill;
        List<Cart> cartList;
        if (bills != null && bills.size() > 0) {
            bill = bills.get(0);
            cartList = bill.getCartList();
        } else {
            bill = new Bill();
            bill.setUserId(userId);
            bill.setCanteenId(canteenId);
            cartList = new ArrayList<>();
        }
        if (cartList == null || cartList.size() == 0) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setFood(foodDao.getOne(fid));
            cart.setNumber(1);
            bill.addCart(cart);
        } else {
            Food food = foodDao.getOne(fid);
            if (isAdd) {
                bill.changeCart(food, 1);
            } else {
                bill.changeCart(food, -1);
            }
        }
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage removeFood(String userId, String canteenId, long fid) {
        List<Bill> bills = billDao.findBillsByUserIdAndCanteenIdAndBillStateLessThanEqualOrderByTimeStampsDesc(userId, canteenId,BillState.NotPaid);
        Bill bill = bills.get(0);
        bill.removeCart(fid);
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage consumeFood(List<Cart> carts) {
        List<Stock> allStocks = new ArrayList<>();
        for (Cart cart : carts) {
            Food food = foodService.getFood(cart.getFood().getFid());//对过期食物检查
            if (food.getNumber() < cart.getNumber()) {
                return ResultMessage.Pass;
            } else {
                List<Stock> stocks = stockDao.findStocksByFood_FidAndPassIsFalseOrderByEndTimeAsc(food.getFid());
                int leftNumber = cart.getNumber();
                for (Stock stock : stocks) {
                    if (stock.getLeftNumber() < leftNumber) {
                        leftNumber -= stock.getLeftNumber();
                        stock.setLeftNumber(0);
                    } else {
                        stock.setLeftNumber(stock.getLeftNumber() - leftNumber);
                        break;
                    }
                }
                allStocks.addAll(stocks);
                food.setNumber(food.getNumber() - cart.getNumber());
            }
        }
        for (Stock stock : allStocks) {
            stockDao.saveAndFlush(stock);
        }
        for (Cart cart : carts)
            foodDao.saveAndFlush(cart.getFood());
        return ResultMessage.Success;
    }
}
