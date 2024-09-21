package com.zheng.travel.client.message.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


// lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("travel_user_message")
public class UserMessageMo implements java.io.Serializable {

    @Id
    private String id; //消息注解
    @Field("title")
    private String title;//消息的标题
    @Field("userid")
    private String userid;//消息来自的用户id
    @Field("nickname")
    private String nickname;//消息来自的用户昵称
    @Field("avatar")
    private String avatar;//消息来自的用户头像
    //消息的类似， 1：我的消息  2：订单消息  3：系统消息
    @Field("msgtype")
    private Integer msgtype;
    //业务的id opid可能是用户，也可能是订单
    @Field("opid")
    private Long opid;
    @Field("icon")
    private String icon;//消息图标
    @Field("toUserid")
    private String toUserid;//消息的接收者
    @Field("msgcontent")
    private String msgcontent;//消息来自的用户id
    @Field("ctime")
    private String createTime;//消息来自的用户id
}
