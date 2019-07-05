package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.*;
import edu.nju.yummy.entity.*;
import edu.nju.yummy.enums.BillState;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.SiftCondition;
import edu.nju.yummy.service.AccountService;
import edu.nju.yummy.service.BillService;
import edu.nju.yummy.service.CartService;
import edu.nju.yummy.service.UserService;
import edu.nju.yummy.util.BillTransfer;
import edu.nju.yummy.vo.BillCanteenVO;
import edu.nju.yummy.vo.BillMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class BillServiceImpl implements BillService {

    private static final int CANCEL_SPAN = 2;

    private static final double TRUE_BENEFIT_RATE_CANTEEN = 0.95;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BillDao billDao;

    @Autowired
    private CanteenDao canteenDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private DiscountDao discountDao;

    @Autowired
    private SystemDiscountDao systemDiscountDao;


    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AddressDao addressDao;

    @Override
    public ResultMessage payForPreparation(long bid, long timeStamps, long aid) {
        Address address = addressDao.getOne(aid);
        Bill bill = billDao.getOne(bid);
        bill.setAddress(address);
        bill.setBillState(BillState.NotPaid);
        bill.setTimeStamps(timeStamps);
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public Bill getBill(long bid) {
        return billDao.getOne(bid);
    }

    @Override
    public Bill generateBill(String userId, String canteenId) {
        List<Bill> bills = billDao.findBillsByUserIdAndCanteenIdAndBillStateLessThanEqualOrderByTimeStampsDesc(userId, canteenId, BillState.NotPaid);
        Bill bill = bills.get(0);
        double boxPrice = 0;
        double allPrice = 0;
        for (Cart cart : bill.getCartList()) {
            boxPrice += cart.getNumber() * cart.getFood().getBoxPrice();
            allPrice += cart.getNumber() * cart.getFood().getPrice();
        }
        bill.setBoxPrice(boxPrice);
        allPrice += boxPrice;
        double reduction = 0;
        List<Discount> discounts = discountDao.findDiscountsByUserIdOrderByFullPrice(canteenId);
        for (Discount discount : discounts) {
            if (discount.getFullPrice() <= allPrice) {
                reduction = discount.getReduction();
            }
        }
        bill.setReduction(reduction);
        double systemRedution = 0;
        List<SystemDiscount> systemDiscounts = systemDiscountDao.findSystemDiscountsByUserIdAndAndGrade(userId, memberDao.getOne(userId).getGrade());
        if (systemDiscounts != null && systemDiscounts.size() > 0) {
            SystemDiscount systemDiscount = systemDiscounts.get(0);
            systemRedution += systemDiscount.getReduction();
            systemDiscount.setReduction(0);
            systemDiscountDao.saveAndFlush(systemDiscount);
        }
        bill.setSystemRedution(systemRedution);
        allPrice -= (reduction + systemRedution);
        bill.setTotalPrice(allPrice);
        billDao.saveAndFlush(bill);
        return bill;

    }

    @Override
    public int getMonthlyBillsNumber(String canteenId) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = simpleDateFormat.parse(String.valueOf(year) + "-" + getMonthStr(month) + "-01 00:00:00");
            int newMonth = month + 1;
            if (newMonth == 13) {
                newMonth = 1;
                year++;
            }
            endDate = simpleDateFormat.parse(String.valueOf(year) + "-" + getMonthStr(newMonth) + "-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startTime = beginDate.getTime();
        long endTime = endDate.getTime();
        return billDao.getMonthlyBillsNumber(canteenId, startTime, endTime);
    }

    @Override
    public List<BillCanteenVO> getCanteenBills(String userId, SiftCondition key, String value) {
        List<Bill> bills;
        BillState billState = BillState.Sending;
        switch (key) {
            case NoLimit:
                bills = billDao.findBillsByCanteenIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(userId, billState);
                break;
            case time:
                long span = System.currentTimeMillis() - Long.parseLong(value) * 1000 * 60 * 60 * 24;
                bills = billDao.findBillsByCanteenIdAndBillStateGreaterThanEqualAndTimeStampsGreaterThanEqualOrderByTimeStamps(userId, billState, span);
                break;
            case money:
                bills = billDao.findBillsByCanteenIdAndBillStateGreaterThanEqualAndTotalPriceLessThanEqualOrderByTimeStamps(userId, billState, Double.parseDouble(value));
                break;
            case member:
                bills = billDao.findBillsByCanteenIdAndBillStateGreaterThanEqualAndUserIdOrderByTimeStampsDesc(userId, billState, value);
                break;
            default:
                bills = billDao.findBillsByCanteenIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(userId, billState);
                break;
        }
        List<BillCanteenVO> billCanteenVOS = new ArrayList<>();
        for (Bill bill : bills)
            billCanteenVOS.add(BillTransfer.canteenPoTovo(bill, memberDao.getOne(bill.getUserId()).getName()));
        return billCanteenVOS;
    }

    @Override
    public List<BillMemberVO> getStateBills(String userId, BillState billState, SiftCondition key, String value) {
        List<Bill> beforeBills;
        if (billState == BillState.NotPaid) {
            switch (key) {
                case NoLimit:
                    beforeBills = billDao.findBillsByUserIdAndBillStateOrderByTimeStampsDesc(userId, billState);
                    break;
                case time:
                    long span = System.currentTimeMillis() - Long.parseLong(value) * 1000 * 60 * 60 * 24;
                    beforeBills = billDao.findBillsByUserIdAndBillStateAndTimeStampsGreaterThanEqual(userId, billState, span);
                    break;
                case money:
                    beforeBills = billDao.findBillsByUserIdAndBillStateAndTotalPriceLessThanEqualOrderByTimeStampsDesc(userId, billState, Double.parseDouble(value));
                    break;
                case canteen:
                    beforeBills = billDao.findBillsByUserIdAndBillStateAndCanteenIdOrderByTimeStampsDesc(userId, billState, value);
                    break;
                default:
                    beforeBills = billDao.findBillsByUserIdAndBillStateOrderByTimeStampsDesc(userId, billState);
                    break;
            }

            for (Bill bill : beforeBills)
                checkBill(bill.getBid());
        }
        List<Bill> bills = billDao.findBillsByUserIdAndBillStateOrderByTimeStampsDesc(userId, billState);
        List<BillMemberVO> billMemberVOS = new ArrayList<>();
        for (Bill bill : bills)
            billMemberVOS.add(BillTransfer.memberPoTovo(bill, canteenDao.getOne(bill.getCanteenId()).getName()));
        return billMemberVOS;
    }

    @Override
    public List<BillMemberVO> getHistoryBills(String userId, SiftCondition key, String value) {
        List<Bill> bills;
        switch (key) {
            case NoLimit:
                bills = billDao.findBillsByUserIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(userId, BillState.Cancel);
                break;
            case time:
                long span = System.currentTimeMillis() - Long.parseLong(value) * 1000 * 60 * 60 * 24;
                bills = billDao.findBillsByUserIdAndBillStateGreaterThanEqualAndTimeStampsGreaterThanEqualOrderByTimeStampsDesc(userId, BillState.Cancel, span);
                break;
            case money:
                bills = billDao.findBillsByUserIdAndBillStateGreaterThanEqualAndTotalPriceLessThanEqualOrderByTimeStampsDesc(userId, BillState.Cancel, Double.parseDouble(value));
                break;
            case canteen:
                bills = billDao.findBillsByUserIdAndBillStateGreaterThanEqualAndCanteenIdOrderByTimeStampsDesc(userId, BillState.Cancel, value);
                break;
            default:
                bills = billDao.findBillsByUserIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(userId, BillState.Cancel);
                break;
        }
        List<BillMemberVO> billMemberVOS = new ArrayList<>();
        for (Bill bill : bills)
            billMemberVOS.add(BillTransfer.memberPoTovo(bill, canteenDao.getOne(bill.getCanteenId()).getName()));
        return billMemberVOS;
    }

    @Override
    public List<Cart> getCartList(String userId, String canteenId) {
        List<Bill> bills = billDao.findBillsByUserIdAndCanteenIdAndBillStateLessThanEqualOrderByTimeStampsDesc(userId, canteenId, BillState.NotPaid);
        if (bills == null || bills.size() == 0)
            return null;
        else
            return bills.get(0).getCartList();
    }

    @Override
    public ResultMessage pay(long bid, String accountId, String password) {
        ResultMessage message = checkBill(bid); //检查订单是否失效
        if (message != ResultMessage.Success)
            return message;
        Account account = accountDao.getOne(accountId);
        if (!account.getPassword().equals(password)) //检查密码
            return ResultMessage.Fail;
        Bill bill = billDao.getOne(bid);
        if (account.getBalance() < bill.getTotalPrice()) //查看账户余额
            return ResultMessage.NotEnough;
        message = cartService.consumeFood(bill.getCartList()); //食物量是否足够
        if (message != ResultMessage.Success)
            return message;
        accountService.moneyMovement(accountId, accountDao.findFirstByUserId("admin").getAccountId(), bill.getTotalPrice() + bill.getSystemRedution());
        bill.setUserAccountId(accountId);
        bill.setBillState(BillState.Sending);
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage confirmBill(long bid) {
        Bill bill = billDao.getOne(bid);
        bill.setBillState(BillState.Finish);
        billDao.saveAndFlush(bill);
        Account canteenAccount = accountDao.findFirstByUserId(bill.getCanteenId());
        Account adminAccount = accountDao.findFirstByUserId("admin");
        accountService.moneyMovement(adminAccount.getAccountId(), canteenAccount.getAccountId(),
                bill.getTotalPrice() * TRUE_BENEFIT_RATE_CANTEEN);
        userService.checkMemberGrade(bill.getUserId(), bill.getTotalPrice());
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage cancelBill(long bid) {
        Bill bill = billDao.getOne(bid);
        bill.setBillState(BillState.Cancel);
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage rejectBill(long bid) {
        Bill bill = billDao.getOne(bid);
        long startTime = bill.getTimeStamps();
        long endTime = System.currentTimeMillis();
        long minute = (endTime - startTime) / (1000 * 60);
        double rate = 0;
        if (minute > 10)
            return ResultMessage.Fail;
        else if (minute > 5 && minute <= 10) {
            rate = 0.3;
        } else if (minute > 1 && minute <= 5) {
            rate = 0.6;
        } else {
            rate = 0.9;
        }
        Account adminAccount = accountDao.findFirstByUserId("admin");
        Account canteenAccount = accountDao.findFirstByUserId(bill.getCanteenId());
        accountService.moneyMovement(adminAccount.getAccountId(), bill.getUserAccountId(), rate * bill.getTotalPrice());
        accountService.moneyMovement(adminAccount.getAccountId(), canteenAccount.getAccountId(), (1 - rate) * bill.getTotalPrice());
        bill.setBillState(BillState.Cancel);
        billDao.saveAndFlush(bill);
        return ResultMessage.Success;
    }

    @Override
    public Bill againBill(long bid) {
        Bill bill = billDao.getOne(bid);
        Bill anotherBill = new Bill();
        anotherBill.setUserId(bill.getUserId());
        anotherBill.setCanteenId(bill.getCanteenId());
        List<Cart> anotherCarts = new ArrayList<>();
        for (Cart cart : bill.getCartList()) {
            Cart newCart = new Cart();
            newCart.setFood(cart.getFood());
            newCart.setNumber(cart.getNumber());
            anotherCarts.add(newCart);
        }
        anotherBill.setCartList(anotherCarts);
        anotherBill.setBillState(BillState.NotInPay);
        billDao.saveAndFlush(anotherBill);
        return anotherBill;
    }

    @Override
    public int[] getMonthlySales() {
        int[] sales = new int[12];
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        for (int i = 0; i < sales.length; i++) {
            int month = i + 1;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date beginDate = null;
            Date endDate = null;
            int newYear = year;
            try {
                beginDate = simpleDateFormat.parse(String.valueOf(year) + "-" + getMonthStr(month) + "-01 00:00:00");
                int newMonth = month + 1;
                if (newMonth == 13) {
                    newMonth = 1;
                    newYear += 1;
                }
                endDate = simpleDateFormat.parse(String.valueOf(newYear) + "-" + getMonthStr(newMonth) + "-01 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long startTime = beginDate.getTime();
            long endTime = endDate.getTime();
            sales[i] = billDao.getMonthlyNumber(startTime, endTime);
        }
        return sales;
    }

    @Override
    public double[] getWeekAmount() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        double[] weekAmount = new double[7];
        for (int i = 0; i < weekAmount.length; i++) {
            LocalDate currentDate = localDate.minusDays(i);
            long startTime = currentDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endTime = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Double amount = billDao.getDaylyAmount(startTime, endTime);
            if (amount == null)
                amount = 0.0;
            weekAmount[6 - i] = amount;
        }
        return weekAmount;
    }

    private ResultMessage checkBill(long bid) {
        Bill bill = billDao.getOne(bid);
        if (bill.getBillState() == BillState.NotPaid) {
            long now = System.currentTimeMillis();
            if (now - bill.getTimeStamps() >= CANCEL_SPAN * 60 * 1000) {
                bill.setBillState(BillState.Cancel);
                billDao.saveAndFlush(bill);
                return ResultMessage.Fail;
            }
        } else if (bill.getBillState() == BillState.Cancel)
            return ResultMessage.Fail;
        return ResultMessage.Success;
    }

    private String getMonthStr(int month) {
        if (month < 10)
            return "0" + String.valueOf(month);
        else
            return String.valueOf(month);
    }
}
