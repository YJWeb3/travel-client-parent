package com.zheng.travel.client.mongoservice.message;

import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.mongo.MessageMo;
import com.zheng.travel.client.mongorepository.MessageRepository;
import com.zheng.travel.client.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public MessageMo saveMessage(MessageMo messageMo) {
        MessageMo messageMoDb = messageRepository.save(messageMo);
        return messageMoDb;
    }



    @Override
    public Page<MessageMo> findMessagePage(MessageVo messageVo) {
        if (messageVo.getPageNo() <= 0) {
            messageVo.setPageNo(0);
        }
        if (messageVo.getPageNo() > 0) {
            messageVo.setPageNo(messageVo.getPageNo()  - 1);
        }
        // 1: 设置分页和排序 这个是mongodb的属性名
        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(messageVo.getPageNo(),messageVo.getPageSize(), sort);
        //2:查询返条件设置
        MessageMo messageMo = new MessageMo();
        if(Vsserts.isNotNull(messageVo.getMsgtype()) && messageVo.getMsgtype()<3) {
            messageMo.setUserid(messageVo.getUserId());
        }
        messageMo.setMsgtype(messageVo.getMsgtype());
        // 4: 设置查询条件和规则
        Example example = Example.of(messageMo);
        // 5：查询分页返回
        Page<MessageMo> messageMoPage = messageRepository.findAll(example,pageable);
        return messageMoPage;
    }

//    @Override
//    public Page<MessageMo> findMessagePage(String userId,Integer pageNo,Integer pageSize) {
//        // 1: 设置分页和排序
//        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
//        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
//        // 5：查询分页返回
//        Page<MessageMo> messageMoPage = messageRepository.findAllByUseridEquals(userId,pageable);
//        return messageMoPage;
//    }


//    @Override
//    public Page<MessageMo> findMessagePage(String userId,Integer pageNo,Integer pageSize) {
//        // 1: 设置分页和排序
//        Sort sort = Sort.by(Sort.Direction.DESC, "ctime");
//        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
//
//        //2:查询返条件设置
//        MessageMo messageMo = new MessageMo();//userid keyword pageNo,pageSize
//        messageMo.setUserid(userId);
//        // 3: 条件的匹配器
//        ExampleMatcher matcher = ExampleMatcher.matching()      //创器建一个匹配
//                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
//                //.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
//                .withMatcher("keyword", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
//                .withIgnorePaths("pageNo","pageSize");  //忽略属性，不参与查询() ;  //忽略大小写
//        //Example example = Example.of(messageMo);// 把userid相等的查询出来
//        // 4: 设置查询条件和规则
//        Example example = Example.of(messageMo,matcher);// 根据userid进行包含匹配 where userid =1 and keyword like '%ccc%'
//        // 5：查询分页返回
//        Page<MessageMo> messageMoPage = messageRepository.findAll(example,pageable);
//        return messageMoPage;
//    }

}
