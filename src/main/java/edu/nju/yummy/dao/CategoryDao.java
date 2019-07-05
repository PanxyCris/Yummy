package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Long> {
    List<Category> findCategoriesByUserId(String userId);
}
