package com.jiuhong.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 角色信息
 * Created by za-wuxiaoyang on 2018/9/10.
 */
@Entity
@Data
public class RoleInfo implements Serializable{


    private static final long serialVersionUID = -1591413626579375359L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roleName; //角色名称

    @ManyToOne(fetch = FetchType.EAGER)
    private UserInfo userInfo; //用户信息

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<PermissionInfo> permissions; //权限集合
}
