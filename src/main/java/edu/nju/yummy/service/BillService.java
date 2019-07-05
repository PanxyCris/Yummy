package edu.nju.yummy.service;

import edu.nju.yummy.entity.Bill;
import edu.nju.yummy.entity.Cart;
import edu.nju.yummy.enums.BillState;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.SiftCondition;
import edu.nju.yummy.vo.BillCanteenVO;
import edu.nju.yummy.vo.BillMemberVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BillService {
    ResultMessage payForPreparation(long bid, long timeStamps, long aid);

    Bill getBill(long bid);

    Bill generateBill(String userId, String canteenId);

    int getMonthlyBillsNumber(String canteenId);

    List<BillCanteenVO> getCanteenBills(String userId, SiftCondition key, String value);

    List<BillMemberVO> getStateBills(String userId, BillState billState, SiftCondition key, String value);

    List<BillMemberVO> getHistoryBills(String userId, SiftCondition key, String value);

    List<Cart> getCartList(String userId, String canteenId);

    ResultMessage pay(long bid, String accountId, String password);

    ResultMessage confirmBill(long bid);

    ResultMessage cancelBill(long bid);

    ResultMessage rejectBill(long bid);

    Bill againBill(long bid);

    int[] getMonthlySales();

    double[] getWeekAmount();
}
