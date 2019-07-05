package edu.nju.yummy.vo;

import edu.nju.yummy.entity.Address;
import edu.nju.yummy.enums.CanteenType;
import edu.nju.yummy.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private String id;
    private String name;
}
