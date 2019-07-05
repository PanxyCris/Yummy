package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Canteen;
import edu.nju.yummy.enums.CanteenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface CanteenDao extends JpaRepository<Canteen, String> {
    @Query(value = "select * from canteen where is_pass = false and is_submit = true", nativeQuery = true)
    List<Canteen> getCheckCanteens();

    @Query(value = "select * from canteen c where c.id in (select b.canteen_id from bill b where b.user_id=?1)", nativeQuery = true)
    List<Canteen> getCanteensByMember(String userId);

    @Query(value = "select count(*) from canteen", nativeQuery = true)
    int getCanteensNumber();

    List<Canteen> findCanteensByCanteenTypes(CanteenType canteenType);
}
