package edu.nju.yummy.controller;

import edu.nju.yummy.entity.Account;
import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/getAccountList")
    public @ResponseBody
    List<Account> getAccountList(String userId) {
        return accountService.getAccountList(userId);
    }

    @RequestMapping("/getAccount")
    public @ResponseBody
    Account getAccount(String accountId) {
        return accountService.getAccount(accountId);
    }

    @RequestMapping("/saveAccount")
    public @ResponseBody
    ResultMessage saveAccount(String userId, String accountId, String bankName, String password, double balance) {
        return accountService.saveAccount(userId, accountId, bankName, password, balance);
    }
}
