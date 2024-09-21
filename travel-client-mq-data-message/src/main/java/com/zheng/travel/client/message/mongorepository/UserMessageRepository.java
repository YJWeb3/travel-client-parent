package com.zheng.travel.client.message.mongorepository;

import com.zheng.travel.client.message.mongo.UserMessageMo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageRepository extends MongoRepository<UserMessageMo,String> {

}
