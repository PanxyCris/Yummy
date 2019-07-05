package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long did;
    private String userId;
    private double fullPrice;
    private double reduction;
}
