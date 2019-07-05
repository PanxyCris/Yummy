package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface FoodDao extends JpaRepository<Food,Long> {
    List<Food> getFoodsByCategory_Cid(long cid);
}
