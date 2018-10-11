package com.jiuhong.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 权限信息
 * Created by za-wuxiaoyang on 2018/9/10.
 */
@Entity
@Data
public class PermissionInfo implements Serializable {

    private static final long serialVersionUID = -3995638673790303463L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String permission;

    @ManyToOne(fetch = FetchType.EAGER)
    private RoleInfo roleInfo;
}
