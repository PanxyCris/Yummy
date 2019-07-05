package edu.nju.yummy.service;

import edu.nju.yummy.entity.Cart;
import edu.nju.yummy.enums.ResultMessage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    ResultMessage changeCart(String userId, String canteenId, long fid, boolean isAdd);

    ResultMessage removeFood(String userId,String canteenId,long fid);

    ResultMessage consumeFood(List<Cart> carts);
}
