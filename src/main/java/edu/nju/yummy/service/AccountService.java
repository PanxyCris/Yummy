package edu.nju.yummy.service;

import edu.nju.yummy.entity.Account;
import edu.nju.yummy.enums.ResultMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<Account> getAccountList(String userId);

    Account getAccount(String accountId);

    ResultMessage saveAccount(String userId, String accountId, String bankName, String password, double balance);

    ResultMessage moneyMovement(String payerAccountId,String payeeAccountId, double money);
}
