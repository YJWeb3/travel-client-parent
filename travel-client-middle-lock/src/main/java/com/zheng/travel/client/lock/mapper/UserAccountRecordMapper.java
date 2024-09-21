package com.zheng.travel.client.lock.mapper;


import com.zheng.travel.client.lock.model.UserAccountRecord;

public interface UserAccountRecordMapper {
    //插入记录
    int insert(UserAccountRecord record);
    //根据主键id查询
    UserAccountRecord selectByPrimaryKey(Integer id);
}