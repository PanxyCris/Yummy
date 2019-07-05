package edu.nju.yummy.controller;

import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/changeCart")
    public @ResponseBody
    ResultMessage changeCart(String userId, String canteenId, long fid, boolean isAdd) {
        return cartService.changeCart(userId, canteenId, fid, isAdd);
    }

    @RequestMapping("/removeFood")
    public @ResponseBody
    ResultMessage removeFood(String userId,String canteenId, long fid) {
        return cartService.removeFood(userId,canteenId, fid);
    }
}
