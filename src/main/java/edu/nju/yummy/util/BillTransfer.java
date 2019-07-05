package edu.nju.yummy.util;

import edu.nju.yummy.entity.Bill;
import edu.nju.yummy.enums.BillState;
import edu.nju.yummy.vo.BillCanteenVO;
import edu.nju.yummy.vo.BillMemberVO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BillTransfer {
    public static BillMemberVO memberPoTovo(Bill bill, String canteenName) {
        BillMemberVO billMemberVO = new BillMemberVO(bill.getBid(), bill.getUserId(), bill.getCanteenId(), canteenName, bill.getCartList(), bill.getAddress(), bill.getBoxPrice(),
                bill.getReduction(), bill.getTotalPrice(), timeStampDate(bill.getTimeStamps()), bill.getBillState().getValue());
        return billMemberVO;
    }

    public static BillCanteenVO canteenPoTovo(Bill bill, String memberName) {
        String billValue = bill.getBillState().getValue();
        if (bill.getBillState() == BillState.Sending)
            billValue = "订单制作中";
        BillCanteenVO billCanteenVO = new BillCanteenVO(bill.getBid(), bill.getCanteenId(), bill.getUserId(), memberName, bill.getCartList(), bill.getAddress(), bill.getBoxPrice(),
                bill.getReduction(), bill.getTotalPrice(), timeStampDate(bill.getTimeStamps()), billValue);
        return billCanteenVO;
    }

    public static String timeStampDate(long seconds) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }
}
