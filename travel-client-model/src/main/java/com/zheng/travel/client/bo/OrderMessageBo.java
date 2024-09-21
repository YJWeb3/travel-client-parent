package com.zheng.travel.client.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



// lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderMessageBo implements java.io.Serializable {
    private String id; //消息注解
    private String title;//消息的标题
    private String userid;//消息来自的用户id
    private String nickname;//消息来自的用户昵称
    private String avatar;//消息来自的用户头像
    //消息的类似， 1：我的消息  2：订单消息  3：系统消息
    private Integer msgtype;
    //业务的id opid可能是用户，也可能是订单
    private Long opid;
    private String icon;//消息图标
    private String toUserid;//消息的接收者
    private String msgcontent;//消息来自的用户id
    private String createTime;//消息来自的用户id
    private Integer checked;// 0未读 1已读
}