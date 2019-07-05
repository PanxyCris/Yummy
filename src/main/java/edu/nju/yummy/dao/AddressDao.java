package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDao extends JpaRepository<Address,Long> {
}
