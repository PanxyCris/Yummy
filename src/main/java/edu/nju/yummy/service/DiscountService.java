package edu.nju.yummy.service;

import edu.nju.yummy.entity.Discount;
import edu.nju.yummy.enums.ResultMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DiscountService {

    List<Discount> getDiscountList(String userId);

    Discount getDiscount(long did);

    ResultMessage saveDiscount(long did, String userId, double fullPrice, double reduction);

    ResultMessage deleteDiscount(long did);
}
