package com.zheng.travel.client.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.travel.client.utils.Tool;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface BaseService {

    /**
     * @param clz
     * @param <T>
     * @param <R>
     * @return
     */
    default <T, R> R tranferBo(T pojo, Class<R> clz) {
        try {
            R r = clz.newInstance();
            BeanUtils.copyProperties(pojo, r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param clz
     * @param <T>
     * @param <R>
     * @return
     */
    default <T, R> List<R> tranferListBo(List<T> list, Class<R> clz) {
        try {
            List<R> collect = list.stream()
                    .map(pojo -> this.tranferBo(pojo, clz))
                    .collect(Collectors.toList());
            return collect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param clz
     * @param <T>
     * @param <R>
     * @return
     */
    default <T, R> Page<R> tranferPageBo(Page<T> poPage, Class<R> clz) {
        List<R> hotelBos = tranferListBo(poPage.getRecords(), clz);
        Page<R> boPage = new Page<>();
        boPage.setTotal(poPage.getTotal());
        boPage.setPages(poPage.getPages());
        boPage.setSize(poPage.getSize());
        boPage.setCurrent(poPage.getCurrent());
        boPage.setRecords(hotelBos);
        return boPage;
    }



    default <T> List<String> getFilterFields(Class<T> clz, List<String> filterField) {
        List<String> list = new ArrayList<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            list.add(Tool.humpToLine(field.getName()));
        }
        List<String> collect = list.stream().filter(field -> filterField.stream()
                .filter(f -> f.equals(field)).count() == 0).collect(Collectors.toList());

        return collect;
    }
}
