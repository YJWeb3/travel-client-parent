package com.zheng.travel.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "travel_user_order")
public class UserOrder implements java.io.Serializable {

    @Id
    private Long id;    //主健
    private String orderno;    //订单编号
    private Long userId;    //下单用户id
    private String nickname;    //	下单用户的昵称
    private String avatar;    //	下单用户的头像
    private Integer status;    //	订单状态 -1 失效的订单 0 未支付的订单 1 已支付 2已完成
    @CreatedDate
    private Date createTime;    //	创建时间
    @LastModifiedDate
    private Date updateTime;    //	更新时间
    private String paymoney;    //	下单金额
}
