package com.zheng.travel.client.message.mongorepository;

import com.zheng.travel.client.message.mongo.SysMessageMo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMessageRepository extends MongoRepository<SysMessageMo,String> {
}
