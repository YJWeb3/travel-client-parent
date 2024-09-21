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
@TableName("kss_product_records")
public class ProductRecords implements java.io.Serializable{

    @TableId(type = IdType.AUTO)
    private Integer id;    //主键id
    private Integer userId;//用户id
    private String productNo; //商品编号
    private Date createTime;  //抢购时间
}

