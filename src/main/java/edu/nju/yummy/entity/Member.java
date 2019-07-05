package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nju.yummy.enums.MemberGrade;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "member")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Member extends User implements Serializable {
    private String phone;
    private String email;
    private MemberGrade grade;
    private double consumption;//消费总额
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<Address> addresses;
    private boolean isCancel;
    private boolean isActive;//用户的激活状态
    private String activeCode;//用户的激活码
}
