package edu.nju.yummy.entity;

import edu.nju.yummy.enums.CanteenType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "canteen")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Canteen extends User {
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Address location;
    @ElementCollection
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Set<CanteenType> canteenTypes;
    private boolean isSubmit;
    private boolean isPass;
}
