package com.zheng.travel.client.mongorepository;
import com.zheng.travel.client.mongo.MessageMo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends MongoRepository<MessageMo,String> {
//
//
//    Page<MessageMo>  findAllByUseridEquals (String userid,Pageable pageable);
//


    /**
     * 通过实现MongoRepository 自定义查询条件
     * @param userId
     * @param pageable
     * @return
     */
    Page<MessageMo> findAllByUseridEqualsOrderByCreateTimeDesc(String userId, Pageable pageable);
}
