package edu.nju.yummy.service;

import edu.nju.yummy.entity.Address;
import edu.nju.yummy.enums.Label;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.Sex;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    Address getAddress(long id);

    Address getDefaultAddress(String userId);

    List<Address> getAddressList(String userId);

    ResultMessage saveAddress(String userId, long aid, String contactName, Sex sex, String phoneNumber,
                              String location, double longitude, double latitude, String houseNumber, Label label);
}
