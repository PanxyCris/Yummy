package edu.nju.yummy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.nju.yummy.enums.BillState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bill")
@Data
@AllArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bid;
    private String userId;
    private String canteenId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<Cart> cartList;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Address address;//送餐地址
    private String userAccountId;
    private double boxPrice;
    private double reduction;
    private double systemRedution;
    private double totalPrice;
    private long timeStamps;
    private BillState billState;

    public Bill() {
        userId = "";
        canteenId = "";
        billState = BillState.NotInPay;
        cartList = new ArrayList<>();
    }

    public void addCart(Cart cart) {
        cartList.add(cart);
    }

    public void removeCart(long fid) {
        for (Cart cartTmp : cartList) {
            if (cartTmp.getFood().getFid() == fid) {
                cartList.remove(cartTmp);
                break;
            }
        }
    }

    public void changeCart(Food food, int number) {
        boolean isContain = false;
        for (Cart cartTmp : cartList) {
            if (cartTmp.getFood().getFid() == food.getFid()) {
                cartTmp.setNumber(cartTmp.getNumber() + number);
                if (cartTmp.getNumber() == 0)
                    cartList.remove(cartTmp);
                isContain = true;
                break;
            }
        }
        if (!isContain) {
            Cart cart = new Cart();
            cart.setNumber(number);
            cart.setFood(food);
            addCart(cart);
        }
    }
}
