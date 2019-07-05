package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Bill;
import edu.nju.yummy.enums.BillState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BillDao extends JpaRepository<Bill, Long> {

    List<Bill> findBillsByUserIdAndCanteenIdAndBillStateLessThanEqualOrderByTimeStampsDesc(String userId, String canteenId, BillState billState);

    List<Bill> findBillsByUserIdAndBillStateOrderByTimeStampsDesc(String userId, BillState billState);

    List<Bill> findBillsByUserIdAndBillStateAndTimeStampsGreaterThanEqual(String userId, BillState billState, long timeStamps);

    List<Bill> findBillsByUserIdAndBillStateAndTotalPriceLessThanEqualOrderByTimeStampsDesc(String userId, BillState billState, double totalPrice);

    List<Bill> findBillsByUserIdAndBillStateAndCanteenIdOrderByTimeStampsDesc(String userId, BillState billState, String canteenId);

    List<Bill> findBillsByUserIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(String userId, BillState billState);

    List<Bill> findBillsByUserIdAndBillStateGreaterThanEqualAndTimeStampsGreaterThanEqualOrderByTimeStampsDesc(String userId, BillState billState, long timeStamps);

    List<Bill> findBillsByUserIdAndBillStateGreaterThanEqualAndTotalPriceLessThanEqualOrderByTimeStampsDesc(String userId, BillState billState, double totalPrice);

    List<Bill> findBillsByUserIdAndBillStateGreaterThanEqualAndCanteenIdOrderByTimeStampsDesc(String userId, BillState billState, String canteenId);

    List<Bill> findBillsByCanteenIdAndBillStateGreaterThanEqualOrderByTimeStampsDesc(String canteenId,BillState billState);

    List<Bill> findBillsByCanteenIdAndBillStateGreaterThanEqualAndTimeStampsGreaterThanEqualOrderByTimeStamps(String canteenId, BillState billState, long timeStamps);

    List<Bill> findBillsByCanteenIdAndBillStateGreaterThanEqualAndTotalPriceLessThanEqualOrderByTimeStamps(String userId, BillState billState, double totalPrice);

    List<Bill> findBillsByCanteenIdAndBillStateGreaterThanEqualAndUserIdOrderByTimeStampsDesc(String canteenId, BillState billState, String userId);

    @Query(value = "select count(*) from bill b where b.canteen_id=?1 and time_stamps between ?2 and ?3 and bill_state=4", nativeQuery = true)
    int getMonthlyBillsNumber(String canteenId, long startTime, long endTime);

    @Query(value = "select count(*) from bill b where time_stamps between ?1 and ?2 and bill_state=4", nativeQuery = true)
    int getMonthlyNumber(long startTime, long endTime);

    @Query(value = "select sum(total_price) from bill b where time_stamps between ?1 and ?2 and bill_state=4", nativeQuery = true)
    Double getDaylyAmount(long startTime, long endTime);
}
