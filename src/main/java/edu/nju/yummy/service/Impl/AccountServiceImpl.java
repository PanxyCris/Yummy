package edu.nju.yummy.service.Impl;

import edu.nju.yummy.dao.AccountDao;
import edu.nju.yummy.dao.CanteenDao;
import edu.nju.yummy.dao.MemberDao;
import edu.nju.yummy.entity.Account;
import edu.nju.yummy.entity.Canteen;
import edu.nju.yummy.entity.Member;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> getAccountList(String userId) {
        return accountDao.findAccountsByUserId(userId);
    }

    @Override
    public Account getAccount(String accountId) {
        return accountDao.getOne(accountId);
    }

    @Override
    public ResultMessage saveAccount(String userId, String accountId, String bankName, String password, double balance) {
        Optional<Account> account_h = accountDao.findById(accountId);
        Account account;
        if (account_h.isPresent()) {
            Account hasAccount = account_h.get();
            if (!hasAccount.getPassword().equals(password))
                return ResultMessage.Fail;
            else {
                account = hasAccount;
                account.setBalance(balance);
            }
        } else
            account = new Account(accountId, userId, bankName, password, balance);
        accountDao.saveAndFlush(account);
        return ResultMessage.Success;
    }

    @Override
    public ResultMessage moneyMovement(String payerAccountId, String payeeAccountId, double money) {
        Account payerAccount = accountDao.getOne(payerAccountId);
        Account payeeAccount = accountDao.getOne(payeeAccountId);
        payerAccount.setBalance(payerAccount.getBalance() - money);
        payeeAccount.setBalance(payeeAccount.getBalance() + money);
        accountDao.saveAndFlush(payerAccount);
        accountDao.saveAndFlush(payeeAccount);
        return ResultMessage.Success;
    }


}
