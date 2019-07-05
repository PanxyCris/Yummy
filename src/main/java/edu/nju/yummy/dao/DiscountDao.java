package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountDao extends JpaRepository<Discount, Long> {
    List<Discount> findDiscountsByUserIdOrderByFullPrice(String userId);

    List<Discount> findDiscountsByUserIdAndFullPrice(String userId, double fullPrice);
}
