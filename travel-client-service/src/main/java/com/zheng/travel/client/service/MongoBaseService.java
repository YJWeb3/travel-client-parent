package com.zheng.travel.client.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface MongoBaseService extends BaseService {

    /**
     * @param clz
     * @param <T>
     * @param <R>
     * @return
     */
    default <T, R> Page<R> tranferPageMongoBo(Page<T> poPage, Class<R> clz) {
        List<R> hotelBos = tranferListBo(poPage.getContent(), clz);
        Page<R> boPage = new PageImpl<R>(hotelBos, poPage.getPageable(), poPage.getTotalElements());
        return boPage;
    }
}
