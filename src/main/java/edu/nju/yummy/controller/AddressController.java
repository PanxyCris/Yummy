package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Address;
import edu.nju.yummy.enums.Label;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.enums.Sex;
import edu.nju.yummy.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("/getAddress")
    public @ResponseBody
    Address getAddress(long addressId) {
        return addressService.getAddress(addressId);
    }

    @RequestMapping("/getDefaultAddress")
    public @ResponseBody
    Address getDefaultAddress(String userId) {
        return addressService.getDefaultAddress(userId);
    }

    @RequestMapping("/getAddressList")
    public @ResponseBody
    List<Address> getAddressList(String userId) {
        return addressService.getAddressList(userId);
    }

    @RequestMapping("/save")
    public @ResponseBody
    ResultMessage saveAddress(String userId, long aid, String contactName, Sex sex, String phoneNumber,
                              String location, double longitude, double latitude, String houseNumber, Label label) {
        return addressService.saveAddress(userId, aid, contactName, sex, phoneNumber, location, longitude, latitude, houseNumber, label);
    }
}
