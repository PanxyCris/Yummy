package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Bill;
import edu.nju.yummy.entity.Cart;
import edu.nju.yummy.enums.BillState;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.SiftCondition;
import edu.nju.yummy.service.BillService;
import edu.nju.yummy.vo.BillCanteenVO;
import edu.nju.yummy.vo.BillMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @RequestMapping("/getBill")
    public @ResponseBody
    Bill getBill(long bid) {
        return billService.getBill(bid);
    }

    @RequestMapping("/generateBill")
    public @ResponseBody
    Bill generateBill(String userId, String canteenId) {
        return billService.generateBill(userId, canteenId);
    }

    @RequestMapping("/getCanteenBills")
    public @ResponseBody
    List<BillCanteenVO> getCanteenBills(String userId, SiftCondition key, String value) {
        return billService.getCanteenBills(userId, key,value);
    }

    @RequestMapping("/getStateBills")
    public @ResponseBody
    List<BillMemberVO> getStateBills(String userId, BillState billState, SiftCondition key, String value) {
        return billService.getStateBills(userId, billState,key,value);
    }

    @RequestMapping("/getHistoryBills")
    public @ResponseBody
    List<BillMemberVO> getHistoryBills(String userId, SiftCondition key, String value) {
        return billService.getHistoryBills(userId,key,value);
    }

    @RequestMapping("/getCartList")
    public @ResponseBody
    List<Cart> getCartList(String userId, String canteenId) {
        return billService.getCartList(userId, canteenId);
    }

    @RequestMapping("/payForPreparation")
    public @ResponseBody
    ResultMessage payForPreparation(long bid, long timeStamps, long aid) {
        return billService.payForPreparation(bid, timeStamps, aid);
    }

    @RequestMapping("/pay")
    public @ResponseBody
    ResultMessage pay(long bid, String accountId, String password) {
        return billService.pay(bid, accountId, password);
    }

    @RequestMapping("/confirmBill")
    public @ResponseBody
    ResultMessage confirmBill(long bid) {
        return billService.confirmBill(bid);
    }

    @RequestMapping("/cancelBill")
    public @ResponseBody
    ResultMessage cancelBill(long bid) {
        return billService.cancelBill(bid);
    }

    @RequestMapping("/rejectBill")
    public @ResponseBody
    ResultMessage rejectBill(long bid) {
        return billService.rejectBill(bid);
    }

    @RequestMapping("/againBill")
    public @ResponseBody
    Bill againBill(long bid) {
        return billService.againBill(bid);
    }

    @RequestMapping("/getMonthlySales")
    public @ResponseBody
    int[] getMonthlySales(){return billService.getMonthlySales();}

    @RequestMapping("/getWeekAmount")
    public @ResponseBody
    double[] getWeekAmount(){return billService.getWeekAmount();}
}
