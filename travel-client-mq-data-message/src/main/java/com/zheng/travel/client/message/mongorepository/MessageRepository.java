package com.zheng.travel.client.message.mongorepository;

import com.zheng.travel.client.message.mongo.MessageMo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<MessageMo,String> {

}
