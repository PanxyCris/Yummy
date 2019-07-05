package edu.nju.yummy.entity;

import edu.nju.yummy.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    String id;
    String name;//名称
    String password;//密码
    String image;//用户的头像
}
