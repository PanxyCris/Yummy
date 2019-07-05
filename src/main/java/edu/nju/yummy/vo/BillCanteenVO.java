package edu.nju.yummy.vo;

import edu.nju.yummy.entity.Address;
import edu.nju.yummy.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BillCanteenVO {
    private long bid;
    private String canteenId;
    private String memberId;
    private String memberName;
    private List<Cart> cartList;
    private Address address;//送餐地址
    private double boxPrice;
    private double reduction;
    private double totalPrice;
    private String timeStamps;
    private String billState;
}
