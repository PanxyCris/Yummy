package edu.nju.yummy.vo;

import edu.nju.yummy.enums.CanteenType;
import edu.nju.yummy.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanteenSaveVO {
    private String id;
    private String name;
    private String contactName;
    private Sex sex;
    private String address;
    private double longitude;
    private double latitude;
    private Set<CanteenType> canteenTypes;
    private boolean isPass;


}
