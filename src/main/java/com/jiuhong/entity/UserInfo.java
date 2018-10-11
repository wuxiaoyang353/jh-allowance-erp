package com.jiuhong.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 * Created by za-wuxiaoyang on 2018/9/10.
 */
@Data
@Entity
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 6934799514580930891L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //自增id

    private String userId;

    @Column(unique = true)
    private String userName; //用户名

    private String password; //用户密码

    private String salt; //加密密码的盐

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<RoleInfo> roles; //角色

    private String statue; //状态

    /**
     * 密码盐.
     *
     * @return
     */
    public String getCredentialsSalt() {
        return this.userName + this.salt;
    }

}
