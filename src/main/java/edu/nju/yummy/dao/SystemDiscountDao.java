package edu.nju.yummy.dao;

import edu.nju.yummy.entity.SystemDiscount;
import edu.nju.yummy.enums.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemDiscountDao extends JpaRepository<SystemDiscount, Long> {
    List<SystemDiscount> findSystemDiscountsByUserIdAndAndGrade(String userId, MemberGrade memberGrade);
}
