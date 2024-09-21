package com.zheng.travel.client.lock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

//商品库存实体
@Data
@ToString
@TableName("kss_product_stock")
public class ProductStock implements java.io.Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;   //主键Id
    private String productNo;//商品编号
    private Integer stock;//存库
    private Integer isActive;//是否上架 0 未上架 1 上架
}