package edu.nju.yummy.dao;

import edu.nju.yummy.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberDao extends JpaRepository<Member, String> {
    @Query(value = "select * from member m where m.id in (select b.user_id from bill b where b.bill_state>=2 and b.canteen_id=?1)", nativeQuery = true)
    List<Member> getMembersByCanteen(String userId);

    @Query(value = "select count(*) from member m", nativeQuery = true)
    int getMemberNumber();
}
