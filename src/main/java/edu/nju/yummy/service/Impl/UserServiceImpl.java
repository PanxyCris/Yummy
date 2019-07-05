package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.*;
import edu.nju.yummy.entity.*;
import edu.nju.yummy.enums.CanteenType;
import edu.nju.yummy.service.BillService;
import edu.nju.yummy.util.*;
import edu.nju.yummy.vo.*;
import edu.nju.yummy.enums.MemberGrade;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class UserServiceImpl implements UserService {

    private static final int DISTANCE_SPAN = 4;

    @Autowired
    private MD5Util md5Util;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private BillService billService;
    @Autowired
    private CanteenDao canteenDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private DiscountDao discountDao;
    @Autowired
    private SystemDiscountDao systemDiscountDao;


    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${constant.codeKey}")
    private String key;
    @Value("${constant.codeLength}")
    private int codeLength;

    @Override
    public ResultMessage memberRegister(String email, String password) {
        if (memberDao.existsById(email)) {
            return ResultMessage.Existed;
        } else {
            Member member = new Member();
            member.setId(email);
            member.setEmail(email);
            member.setPassword(password);
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < this.codeLength; i++) {
                int random = (int) (Math.random() * 10);
                code.append(random);
            }
            String activeCode = md5Util.md5Encode(this.key + email + code.toString());
            member.setActiveCode(activeCode);
            member.setActive(false);
            if (!this.sendEmail(email, activeCode)) {
                return ResultMessage.Fail;
            } else {
                memberDao.save(member);
                return ResultMessage.Success;
            }
        }
    }

    @Override
    public String canteenRegister(String password) {
        Long id = canteenDao.count() + 1;
        int bit = 7 - 2;
        if (id > Math.pow(10, bit + 1))
            return null;
        String username = "CT";
        for (int i = 1; i < bit + 1; i++) {
            if (id < Math.pow(10, i) && id >= Math.pow(10, i - 1)) {
                for (int j = 0; j < bit - i; j++)
                    username += "0";
                username += String.valueOf(id);
                break;
            }
        }
        Canteen canteen = new Canteen();
        canteen.setId(username);
        canteen.setPassword(password);
        canteen.setPass(false);
        canteen.setSubmit(false);
        canteenDao.save(canteen);
        return username;
    }

    @Override
    public ResultMessage checkEmail(String email, String code) {
        Optional<Member> member_h = memberDao.findById(email);
        if (member_h.isPresent()) {
            Member member = member_h.get();
            if (member.getActiveCode().equals(code)) {
                member.setActive(true);
                member.setCancel(false);
                member.setConsumption(0);
                member.setGrade(MemberGrade.一级);
                memberDao.saveAndFlush(member);
                return ResultMessage.Success;
            }
        }
        return ResultMessage.Fail;
    }

    @Override
    public ResultMessage deleteUser(String id, String password) {
        Member member = memberDao.getOne(id);
        if (!member.getPassword().equals(password))
            return ResultMessage.Fail;
        else {
            member.setCancel(true);
            memberDao.saveAndFlush(member);
            return ResultMessage.Success;
        }
    }

    @Override
    public ResultMessage login(String username, String password) {
        Optional<Member> member_h = memberDao.findById(username);
        if (member_h.isPresent()) {
            Member member = member_h.get();
            if (member.isCancel())
                return ResultMessage.Cancel;
            if (member.getPassword().equals(password))
                if (member.isActive())
                    return ResultMessage.MemberLogin;
                else
                    return ResultMessage.NotActive;
            else
                return ResultMessage.Fail;
        }
        Optional<Canteen> canteen_h = canteenDao.findById(username);
        if (canteen_h.isPresent()) {
            Canteen canteen = canteen_h.get();
            if (canteen.getPassword().equals(password))
                return ResultMessage.CanteenLogin;
            else
                return ResultMessage.Fail;
        }
        if (username.equals("admin") && password.equals("admin"))
            return ResultMessage.AdminLogin;
        return ResultMessage.NotExised;
    }

    @Override
    public ResultMessage memberInfo(String id, String name) {
        Member member = memberDao.getOne(id);
        member.setName(name);
        memberDao.saveAndFlush(member);
        return ResultMessage.Success;
    }

    @Override
    public Member getMember(String userId) {
        Member member = memberDao.getOne(userId);
        return member;
    }

    @Override
    public ResultMessage canteenInfo(CanteenSaveVO canteenSaveVO) {
        Canteen canteen = canteenDao.getOne(canteenSaveVO.getId());
        canteen.setName(canteenSaveVO.getName());
        Address address = new Address();
        address.setContactName(canteenSaveVO.getContactName());
        address.setSex(canteenSaveVO.getSex());
        address.setLocation(canteenSaveVO.getAddress());
        try {
            Point point = LocateUtil.getLocation(canteenSaveVO.getAddress());
            address.setLongitude(point.getLng());
            address.setLatitude(point.getLat());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        addressDao.saveAndFlush(address);
        canteen.setLocation(address);
        canteen.setCanteenTypes(canteenSaveVO.getCanteenTypes());
        canteen.setSubmit(true);
        canteen.setPass(false);
        canteenDao.saveAndFlush(canteen);
        return ResultMessage.Success;
    }

    @Override
    public CanteenSaveVO getCanteenInfo(String userId) {
        Canteen canteen = canteenDao.getOne(userId);
        return CanteenTransfer.poTovo(canteen);
    }

    @Override
    public boolean ifSubmit(String id) {
        Canteen canteen = canteenDao.getOne(id);
        return canteen.isPass();
    }

    @Override
    public List<CanteenSaveVO> getCheckCanteenList() {
        List<Canteen> canteens = canteenDao.getCheckCanteens();
        List<CanteenSaveVO> canteenSaveVOS = new ArrayList<>();
        for (Canteen canteen : canteens) {
            canteenSaveVOS.add(CanteenTransfer.poTovo(canteen));
        }
        return canteenSaveVOS;
    }

    @Override
    public List<MemberVO> getMemberListByCanteen(String userId) {
        List<Member> members = memberDao.getMembersByCanteen(userId);
        List<MemberVO> memberVOS = new ArrayList<>();
        for (Member member : members) {
            memberVOS.add(new MemberVO(member.getId(), member.getName()));
        }
        return memberVOS;
    }

    @Override
    public List<CanteenSaveVO> getCanteenListByMember(String userId) {
        List<Canteen> canteens = canteenDao.getCanteensByMember(userId);
        List<CanteenSaveVO> canteenSaveVOS = new ArrayList<>();
        for (Canteen canteen : canteens) {
            canteenSaveVOS.add(CanteenTransfer.poTovo(canteen));
        }
        return canteenSaveVOS;
    }

    @Override
    public List<CanteenSaveVO> getCanteenListByType(CanteenType type) {
        List<Canteen> canteens = canteenDao.findCanteensByCanteenTypes(type);
        List<CanteenSaveVO> canteenSaveVOS = new ArrayList<>();
        for (Canteen canteen : canteens) {
            canteenSaveVOS.add(CanteenTransfer.poTovo(canteen));
        }
        return canteenSaveVOS;
    }

    /**
     * 根据用户地点和餐厅类型来获取餐厅列表
     *
     * @param type 类型
     * @param lng  用户的经度
     * @param lat  用户的纬度
     * @return
     */
    @Override
    public List<CanteenMarketVO> getCanteenListByTypeAndLocation(CanteenType type, double lng, double lat) {
        List<Canteen> canteens = canteenDao.findCanteensByCanteenTypes(type);
        List<CanteenMarketVO> canteenMarketVOS = new ArrayList<>();
        double standard = DISTANCE_SPAN;//4km范围的餐厅
        for (Canteen canteen : canteens) {
            Address address = canteen.getLocation();
            double distance = 0;
            String time = null;
            try {
                String str = DistanceUtil.computeTime(address.getLongitude(), address.getLatitude(), lng, lat);
                JSONObject jsonObject = JSONObject.fromObject(str);
                JSONObject result = (JSONObject) jsonObject.get("result");
                JSONArray locationObject = (JSONArray) result.get("routes");
                time = TimeUtil.getVisionTime((int) ((JSONObject) locationObject.get(0)).get("duration"));
                distance = ((double) ((int) ((JSONObject) locationObject.get(0)).get("distance"))) / 1000;
                System.out.println(distance);
                if (distance <= standard) {
                    int sales = billService.getMonthlyBillsNumber(canteen.getId());//月销量
                    List<Discount> discounts = discountDao.findDiscountsByUserIdOrderByFullPrice(canteen.getId());
                    CanteenMarketVO canteenMarketVO = new CanteenMarketVO(canteen.getId(), canteen.getName(), sales, time, distance, discounts);
                    canteenMarketVOS.add(canteenMarketVO);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
        }
        return canteenMarketVOS;
    }

    @Override
    public ResultMessage checkCanteens(CanteenIdsVO canteenIdsVO) {
        List<String> ids = canteenIdsVO.getIds();
        for (String id : ids) {
            Canteen canteen = canteenDao.getOne(id);
            canteen.setPass(true);
            canteenDao.saveAndFlush(canteen);
        }
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage checkMemberGrade(String userId, double consumption) {
        Member member = memberDao.getOne(userId);
        member.setConsumption(member.getConsumption() + consumption);
        double reduction = 0;
        if (member.getConsumption() < 100) {
            member.setGrade(MemberGrade.一级);
        }
        if (member.getConsumption() >= 100 && member.getConsumption() < 300) {
            member.setGrade(MemberGrade.二级);
            reduction = 30;
        } else if (member.getConsumption() >= 300 && member.getConsumption() < 500) {
            member.setGrade(MemberGrade.三级);
            reduction = 50;
        } else if (member.getConsumption() >= 500 && member.getConsumption() < 1000) {
            member.setGrade(MemberGrade.四级);
            reduction = 100;
        } else if (member.getConsumption() >= 1000) {
            member.setGrade(MemberGrade.五级);
            reduction = 300;
        }
        List<SystemDiscount> discounts = systemDiscountDao.findSystemDiscountsByUserIdAndAndGrade(userId, member.getGrade());
        if (discounts == null || discounts.size() == 0) {
            SystemDiscount discount = new SystemDiscount();
            discount.setUserId(userId);
            discount.setGrade(member.getGrade());
            discount.setReduction(reduction);
            systemDiscountDao.save(discount);
        }
        memberDao.saveAndFlush(member);
        return ResultMessage.Success;
    }

    @Override
    public UserNumberVO getUserNumber() {
        UserNumberVO userNumberVO = new UserNumberVO(canteenDao.getCanteensNumber(),memberDao.getMemberNumber());
        return userNumberVO;
    }


    private boolean sendEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("测试邮件");
        //发送邮件
        String sb = "点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！\n" + "http://localhost:8000/user/checkEmail?email=" +
                toEmail +
                "&code=" +
                code;
        message.setText(sb);
        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
