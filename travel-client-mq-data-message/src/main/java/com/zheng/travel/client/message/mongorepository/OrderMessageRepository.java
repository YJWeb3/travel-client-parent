package com.zheng.travel.client.message.mongorepository;

import com.zheng.travel.client.message.mongo.OrderMessageMo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMessageRepository extends MongoRepository<OrderMessageMo,String> {
}
