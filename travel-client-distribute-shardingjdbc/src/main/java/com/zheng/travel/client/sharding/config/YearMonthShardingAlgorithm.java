package com.zheng.travel.client.sharding.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.ShardingAutoTableAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


@Component
@Slf4j
public class YearMonthShardingAlgorithm implements StandardShardingAlgorithm<Date>, ShardingAutoTableAlgorithm {

    @Override
    public final void init() {

    }

    @Override
    public int getAutoTablesAmount() {
        return 0;
    }

    @Override
    public Collection<String> getAllPropertyKeys() {
        return Arrays.asList("range-lower", "range-upper", "sharding-volume");
    }

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {
        String logicTableName = preciseShardingValue.getLogicTableName();
        Date localDateTime = preciseShardingValue.getValue();
        String columnName = preciseShardingValue.getColumnName();
        Calendar ca = Calendar.getInstance();
        ca.setTime(localDateTime);
        int year = ca.get(Calendar.YEAR);//年份数值 2021
        int month = ca.get(Calendar.MONTH) + 1;//第几个月 8
        //int week = ca.get(Calendar.WEEK_OF_YEAR);//一年中的第几周
        //int day = ca.get(Calendar.DAY_OF_YEAR);//一年中的第几天
        String tabName = logicTableName + "_" + year + (month < 10 ? "0" + month : month);
        return tabName;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        return collection;
    }

    @Override
    public String getType() {
        return null;
    }
}
