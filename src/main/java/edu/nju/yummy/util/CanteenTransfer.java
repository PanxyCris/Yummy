package edu.nju.yummy.util;

import edu.nju.yummy.entity.Address;
import edu.nju.yummy.entity.Canteen;
import edu.nju.yummy.vo.CanteenMarketVO;
import edu.nju.yummy.vo.CanteenSaveVO;

public class CanteenTransfer {

    public static CanteenSaveVO poTovo(Canteen canteen) {
        Address address = canteen.getLocation();
        if(address==null){
            address = new Address();
        }
        CanteenSaveVO canteenSaveVO = new CanteenSaveVO(canteen.getId(), canteen.getName(), address.getContactName(),
                address.getSex(), address.getLocation(), address.getLongitude(), address.getLatitude(), canteen.getCanteenTypes(),canteen.isPass());
        return canteenSaveVO;
    }
}
