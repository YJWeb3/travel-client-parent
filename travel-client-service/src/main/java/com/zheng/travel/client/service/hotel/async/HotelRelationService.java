package com.zheng.travel.client.service.hotel.async;

import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.service.hotel.IHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotelRelationService {

    @Autowired
    private TravelRedisOperator redisOperator;
    @Autowired
    private IHotelService hotelService;
    @Value("${travel.message.count}")
    private Integer messageCount;

    @Async
    public void countHotelComment(Long hotelId) {
        redisOperator.increment("hotel:comment:count:"+hotelId,1L);
        String countComments = redisOperator.get("hotel:comment:count:" + hotelId);
        if (Vsserts.isNotEmpty(countComments)) {
            Integer cc = Integer.parseInt(countComments);
            if (cc >= messageCount) {
                Hotel hotel = new Hotel();
                hotel.setId(hotelId);
                hotel.setComments(cc);
                hotelService.updateById(hotel);
            }
        }
    }


    @Async
    public void delHotelComment(Long hotelId){
        redisOperator.decrement("hotel:comment:count:"+hotelId,1L);
        String countComments = redisOperator.get("hotel:comment:count:" + hotelId);
        if (Vsserts.isNotEmpty(countComments)) {
            Integer cc = Integer.parseInt(countComments);
            if (cc >= messageCount) {
                Hotel hotel = new Hotel();
                hotel.setId(hotelId);
                hotel.setComments(cc);
                hotelService.updateById(hotel);
            }
        }
    }

    @Async
    public void sendRegMessage() {
        log.info("sendRegMessage - 执行同步");
    }
}
