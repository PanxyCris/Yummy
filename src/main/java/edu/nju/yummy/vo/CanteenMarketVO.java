package edu.nju.yummy.vo;

import edu.nju.yummy.entity.Discount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CanteenMarketVO {
    private String id;
    private String name;
    private int sales;//销量
    private String time;
    private double distance;
    private List<Discount>  discounts;
}
