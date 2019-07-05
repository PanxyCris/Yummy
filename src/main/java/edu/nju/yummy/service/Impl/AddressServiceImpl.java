package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.AddressDao;
import edu.nju.yummy.dao.CanteenDao;
import edu.nju.yummy.dao.MemberDao;
import edu.nju.yummy.entity.Address;
import edu.nju.yummy.entity.Canteen;
import edu.nju.yummy.entity.Member;
import edu.nju.yummy.entity.Point;
import edu.nju.yummy.enums.Label;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.Sex;
import edu.nju.yummy.service.AddressService;
import edu.nju.yummy.util.LocateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Component
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CanteenDao canteenDao;

    @Override
    public Address getAddress(long id) {
        return addressDao.getOne(id);
    }

    @Override
    public Address getDefaultAddress(String userId) {
        Optional<Canteen> canteen_h = canteenDao.findById(userId);
        if (canteen_h.isPresent()) {
            return canteen_h.get().getLocation();
        } else {
            Member member = memberDao.getOne(userId);
            if (member.getAddresses() != null && member.getAddresses().size() > 0)
                return member.getAddresses().get(0);
            else
                return null;
        }
    }

    @Override
    public List<Address> getAddressList(String userId) {
        Member member = memberDao.getOne(userId);
        return member.getAddresses();
    }

    @Override
    public ResultMessage saveAddress(String userId, long aid, String contactName, Sex sex, String phoneNumber,
                                     String location, double longitude, double latitude, String houseNumber, Label label) {
        Address address = new Address();
        if (aid != -1) {
            address = addressDao.getOne(aid);
        }
        address.setContactName(contactName);
        address.setSex(sex);
        address.setPhoneNumber(phoneNumber);
        address.setLocation(location);
        try {
            Point point = LocateUtil.getLocation(location);
            address.setLongitude(point.getLng());
            address.setLatitude(point.getLat());
        } catch (UnsupportedEncodingException e) {
            address.setLongitude(longitude);
            address.setLatitude(latitude);
            e.printStackTrace();
        }
        address.setHouseNumber(houseNumber);
        address.setLabel(label);
        Member member = memberDao.getOne(userId);
        List<Address> addresses = member.getAddresses();
        if (aid == -1)
            addresses.add(address);
        else {
            for (Address tmpAddress : addresses) {
                if (tmpAddress.getAid() == aid) {
                    addresses.remove(tmpAddress);
                    addresses.add(address);
                    break;
                }
            }
        }
        member.setAddresses(addresses);
        memberDao.saveAndFlush(member);
//        addressDao.save(address);
        return ResultMessage.Success;
    }
}
