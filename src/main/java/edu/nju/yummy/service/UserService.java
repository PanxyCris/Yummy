package edu.nju.yummy.service;

import edu.nju.yummy.entity.Member;
import edu.nju.yummy.enums.CanteenType;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //会员注册
    ResultMessage memberRegister(String email, String password);

    //餐厅注册
    String canteenRegister(String password);

    //邮箱验证
    ResultMessage checkEmail(String email, String code);

    ResultMessage deleteUser(String id, String password);

    ResultMessage login(String username, String password);

    ResultMessage memberInfo(String id, String name);

    Member getMember(String userId);

    ResultMessage canteenInfo(CanteenSaveVO canteenSaveVO);

    CanteenSaveVO getCanteenInfo(String userId);

    boolean ifSubmit(String id);

    List<CanteenSaveVO> getCheckCanteenList();

    List<MemberVO> getMemberListByCanteen(String userId);

    List<CanteenSaveVO> getCanteenListByMember(String userId);

    List<CanteenSaveVO> getCanteenListByType(CanteenType type);

    /**
     * 根据用户地点和餐厅类型来获取餐厅列表
     * @param type 类型
     * @param lng 用户的经度
     * @param lat 用户的纬度
     * @return
     */
    List<CanteenMarketVO> getCanteenListByTypeAndLocation(CanteenType type, double lng, double lat);

    ResultMessage checkCanteens(CanteenIdsVO canteenIdsVO);

    ResultMessage checkMemberGrade(String userId,double consumption);

    UserNumberVO getUserNumber();

}
