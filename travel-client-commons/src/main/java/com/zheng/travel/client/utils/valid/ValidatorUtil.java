package com.zheng.travel.client.utils.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 校验工具类
 * @author: xuke
 * @time: 2020/9/26 1:19
 */
public class ValidatorUtil {

    public static void main(String[] args) {
        //System.out.println(MD5Util.md5(  "123456"+"kuangstudy.!@3"));
        System.out.println(isValidatorPhone("15074816437"));
        System.out.println(isValidatorPhone("11074816437"));
        System.out.println(isValidatorPhone("xccx"));
        System.out.println(isURL("xccx"));
        System.out.println(isURL("http://www.baidu.com"));
        System.out.println("ok" + isURL("https://uncss-online.com/"));
        System.out.println("ok11" + isURL("http://weathernew.pae.baidu.com/weathernew/pc?query=%E6%A2%85%E5%B7%9E%E5%A4%A9%E6%B0%94&srcid=4982&city_name=%E6%A2%85%E5%B7%9E&province_name=%E5%B9%BF%E4%B8%9C"));
        System.out.println(isEmail("http://www.baidu.com"));
        System.out.println(isEmail("xuchengfeifei@163.com"));
        System.out.println(isEmail("xuchengfei_fei@163.com"));
    }

    /**
     * @return boolean
     * @Author xuke
     * @Description 验证手机号码
     * @Date 0:33 2020/9/26
     * @Param [phone]
     **/
    public static boolean isValidatorPhone(String phone) {
        String regex = "(^0?1[1|2|3|4|5|7|6|8|9][0-9]\\d{8}$)";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * @author xuke
     * @description 校验邮箱
     * @params [email]
     * @date 2021/2/27 16:16
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }

        String regEx1 = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author xuke
     * @description 校验url
     * @params [str]
     * @date 2021/2/18 16:36
     */
    public static boolean isURL(String url) {
        //String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }

    /**
     * 顶级域名判断；如果要忽略大小写，可以直接在传入参数的时候toLowerCase()再做判断
     *
     * @param str
     * @return
     */
    public static boolean isTopURL(String str) {
        //转换为小写
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "(" + domainRules + "))" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }


    /**
     * 判断字符串是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }


}
