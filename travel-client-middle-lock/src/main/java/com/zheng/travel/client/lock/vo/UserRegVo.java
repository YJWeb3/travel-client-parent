package com.zheng.travel.client.lock.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegVo implements java.io.Serializable {

    // uuid
    private String uuid;
    // 用户名
    private String userName;
    // 密码
    private String password;

}
