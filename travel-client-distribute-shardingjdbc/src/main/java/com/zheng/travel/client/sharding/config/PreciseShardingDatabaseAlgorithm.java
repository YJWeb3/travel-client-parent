package com.zheng.travel.client.sharding.config;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;


@Slf4j
@Component(value = "preciseShardingDatabaseAlgorithm")
public class PreciseShardingDatabaseAlgorithm implements StandardShardingAlgorithm<String> {
    // 主库别名
    private static final String DBM = "master";

    private static int dataBaseSize;

    @Value("${dataBaseSize}")
    public void setDataBaseSize(int size) {
        dataBaseSize = size;
    }

    /**
     * @description: 分库策略，按用户编号最后一位数字对数据库数量取模
     *
     * @param dbNames 所有库名
     * @param preciseShardingValue 精确分片值，包括（columnName，logicTableName，value）
     * @return 表名
     * @author luffylv
     * @date 2022/4/15
     */
    @Override
    public String doSharding(Collection<String> dbNames, PreciseShardingValue<String> preciseShardingValue) {
        log.info("Database PreciseShardingAlgorithm dbNames:{} ,preciseShardingValue: {}.", JSON.toJSONString(dbNames),
                JSON.toJSONString(preciseShardingValue));

        // 若走主库，直接返回主库
        if (dbNames.size() == 1) {
            Iterator<String> iterator = dbNames.iterator();
            String dbName = iterator.next();
            if (DBM.equals(dbName)) {
                return DBM;
            }
        }

        // 按数据库数量取模
        String num = StringUtils.substring(preciseShardingValue.getValue(), -1);
        int mod = Integer.parseInt(num) % dataBaseSize;
        for (String dbName : dbNames) {
            // 分库的规则
            if (dbName.endsWith(String.valueOf(mod))) {
                return dbName;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
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
