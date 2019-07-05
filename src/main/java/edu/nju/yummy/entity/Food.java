package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

@Entity
@Table(name = "food")
@Data
@Scope("prototype")
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fid;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Category category;
    private int number;
    private double price;
    private double boxPrice;
}
