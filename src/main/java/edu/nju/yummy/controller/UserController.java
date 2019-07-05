package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Member;
import edu.nju.yummy.enums.CanteenType;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.UserService;
import edu.nju.yummy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping("/emailRegister")
    public @ResponseBody
    ResultMessage emailRegister(String email, String password) {
        return userService.memberRegister(email, password);
    }

    @RequestMapping("/canteenRegister")
    public @ResponseBody
    String canteenRegister(String password) {
        return userService.canteenRegister(password);
    }

    //激活邮箱
    @RequestMapping("/checkEmail")
    public @ResponseBody
    ResultMessage checkEmail(String email, String code) {
        return userService.checkEmail(email, code);
    }

    @RequestMapping("/deleteUser")
    public @ResponseBody
    ResultMessage deleteUser(String id, String password) {
        return userService.deleteUser(id, password);
    }

    @RequestMapping("/login")
    public @ResponseBody
    ResultMessage login(String username, String password) {
        return userService.login(username, password);
    }

    @RequestMapping("/memberInfo")
    public @ResponseBody
    ResultMessage memberInfo(String id,String name) {
        return userService.memberInfo(id,name);
    }

    @RequestMapping("/getMember")
    public @ResponseBody
    Member getMember(String userId) {
        return userService.getMember(userId);
    }

    @RequestMapping("/canteenInfo")
    public @ResponseBody
    ResultMessage canteenInfo(@RequestBody CanteenSaveVO canteenSaveVO) {
        return userService.canteenInfo(canteenSaveVO);
    }

    @RequestMapping("/getCanteenInfo")
    public @ResponseBody
    CanteenSaveVO getCanteenInfo(String userId){
        return userService.getCanteenInfo(userId);
    }

    @RequestMapping("/ifSubmit")
    public @ResponseBody
    boolean ifSubmit(String id) {
        return userService.ifSubmit(id);
    }

    @RequestMapping("/check")
    public @ResponseBody
    List<CanteenSaveVO> getCheckCanteenList() {
        return userService.getCheckCanteenList();
    }

    @RequestMapping("/getMemberListByCanteen")
    public @ResponseBody
    List<MemberVO> getMemberListByCanteen(String userId) {
        return userService.getMemberListByCanteen(userId);
    }

    @RequestMapping("/getCanteenListByMember")
    public @ResponseBody
    List<CanteenSaveVO> getCanteenListByMember(String userId) {
        return userService.getCanteenListByMember(userId);
    }

    @RequestMapping("/getCanteenListByType")
    public @ResponseBody
    List<CanteenSaveVO> getCanteenListByType(CanteenType type) {
        return userService.getCanteenListByType(type);
    }

    /**
     * 根据用户地点和餐厅类型来获取餐厅列表
     * @param type 类型
     * @param lng 用户的经度
     * @param lat 用户的纬度
     * @return
     */
    @RequestMapping("/getCanteenListByTypeAndLocation")
    public @ResponseBody
    List<CanteenMarketVO> getCanteenListByTypeAndLocation(CanteenType type, double lng, double lat){
        return userService.getCanteenListByTypeAndLocation(type, lng, lat);
    }

    @RequestMapping("/pass")
    public @ResponseBody
    ResultMessage checkCanteens(@RequestBody CanteenIdsVO canteenIdsVO) {
        return userService.checkCanteens(canteenIdsVO);
    }

    @RequestMapping("/getAllCanteenTypes")
    public @ResponseBody
    CanteenType[] getAllCanteenTypes() {
        return CanteenType.values();
    }


    @RequestMapping("/getUserNumber")
    public @ResponseBody
    UserNumberVO getUserNumber(){return userService.getUserNumber();}


}
