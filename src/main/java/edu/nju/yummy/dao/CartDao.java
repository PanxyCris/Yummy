package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartDao extends JpaRepository<Cart, Long> {
    List<Cart> findCartsByUserIdAndFood_Fid(String userId, long fid);

    @Query(value = "select * from cart where cart_list_bid = ?1", nativeQuery = true)
    List<Cart> findCartsByBid(long bid);
}
