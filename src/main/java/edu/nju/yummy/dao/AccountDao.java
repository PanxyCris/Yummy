package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountDao extends JpaRepository<Account,String> {
    List<Account> findAccountsByUserId(String userId);

    Account findFirstByUserId(String userId);
}
