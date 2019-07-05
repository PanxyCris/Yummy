package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.DiscountDao;
import edu.nju.yummy.entity.Discount;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountDao discountDao;

    @Override
    public List<Discount> getDiscountList(String userId) {
        return discountDao.findDiscountsByUserIdOrderByFullPrice(userId);
    }

    @Override
    public Discount getDiscount(long did) {
        return discountDao.getOne(did);
    }

    @Override
    public ResultMessage saveDiscount(long did, String userId, double fullPrice, double reduction) {
        Discount discount = new Discount();
        if (did != -1)
            discount = discountDao.getOne(did);
        else {
            List<Discount> discounts = discountDao.findDiscountsByUserIdAndFullPrice(userId, fullPrice);
            if (discounts != null && discounts.size() > 0)
                return ResultMessage.Existed;
            else
                discount.setUserId(userId);
        }
        discount.setFullPrice(fullPrice);
        discount.setReduction(reduction);
        discountDao.saveAndFlush(discount);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage deleteDiscount(long did) {
        discountDao.deleteById(did);
        return ResultMessage.Success;
    }
}
