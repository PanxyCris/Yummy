package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockDao extends JpaRepository<Stock, Long> {
    List<Stock> findStocksByFood_Fid(long fid);

    @Query(value = "select * from stock where is_pass = false and food_fid=?1", nativeQuery = true)
    List<Stock> findStocksByFood_FidAndPassIsFalse(long fid);

    @Query(value = "select * from stock where is_pass = false and food_fid=?1 order by end_time asc", nativeQuery = true)
    List<Stock> findStocksByFood_FidAndPassIsFalseOrderByEndTimeAsc(long fid);
}
