package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nju.yummy.enums.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "systemdiscount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SystemDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sdid;
    private String userId;
    private MemberGrade grade;
    private double reduction;
}
