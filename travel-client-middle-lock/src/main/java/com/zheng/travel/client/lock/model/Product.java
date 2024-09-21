package com.zheng.travel.client.lock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

//商品抢购记录实体
@Data
@ToString
@TableName("kss_product")
public class Product implements java.io.Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;    //主键id
    private Integer status;//商品的发布状态
    private String productNo; //商品编号
    private String productTitle; //商品标题
    private String productImg; //商品封面
    private String productDesc; //商品编描述
    private Date createTime;  //抢购时间
}

