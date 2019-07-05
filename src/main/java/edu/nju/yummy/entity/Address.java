package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nju.yummy.enums.Label;
import edu.nju.yummy.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long aid;//地址ID
    private String contactName;
    private Sex sex;
    private String phoneNumber;
    private String location;
    private double longitude;
    private double latitude;
    private String houseNumber;//门牌号
    private Label label;//标签
}
