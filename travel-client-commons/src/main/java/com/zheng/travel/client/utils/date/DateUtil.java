package com.zheng.travel.client.utils.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtil {
    /**
     * 获取某天的时间
     *
     * @param index 为正表示当前时间加天数，为负表示当前时间减天数
     * @return String
     */
    public static String getTimeDay(int index) {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH, index);
        String date = fmt.format(calendar.getTime());
        return date;
    }

    public static void main(String[] args){
        System.out.println(getTimeDay(0));
        System.out.println(getTimeDay(-1));
    }

}
