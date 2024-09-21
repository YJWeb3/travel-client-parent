package com.zheng.travel.client.sharding.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *
 * @description sharding jdbc 精准`分表`策略
 **/
@Slf4j
@Component
public class PreciseShardingTableAlgorithm implements StandardShardingAlgorithm<Integer> {
 
    // 分表数量
    private static int tableSize;
 
    @Value("${tableSize}")
    public void setTableSize(int size) {
        tableSize = size;
    }

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Integer> preciseShardingValue) {
        log.info("Table PreciseShardingAlgorithm tableNames:{} ,preciseShardingValue: {}.",
                JSON.toJSONString(tableNames), JSON.toJSONString(preciseShardingValue));
        // 按表数量取模
        // 截取用户编号倒数二三位数字，（如1234的倒数二三位为23）
        int mod = preciseShardingValue.getValue() % tableSize;
        for (String tableName : tableNames) {
            // 分表的规则
            if (tableName.endsWith(String.valueOf(mod))) {
                return tableName;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Integer> rangeShardingValue) {
        return null;
    }



    @Override
    public void init() {
 
    }
 
    @Override
    public String getType() {
        return null;
    }
}