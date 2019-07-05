package edu.nju.yummy.enums;

public enum BillState {
    NotInPay("订单制定中"),
    NotPaid("订单尚未支付"),
    Sending("订单派送中"),
    Cancel("订单已取消"),
    Finish("订单已送达");

    private String value;

    BillState(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
