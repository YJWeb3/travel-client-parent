package com.zheng.travel.client.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class ContentSafeFilter {
    private final static Whitelist user_content_filter = Whitelist.relaxed();
    static {
        //增加可信标签到白名单
        user_content_filter.addTags("embed","object","param","span","div","img");
        user_content_filter.removeTags("a","script","noscript","table","tr","td","th","tbody","div","video","canvas",
                "link","style","iframe","input","textarea","object","embed","font","html","body");
        //增加可信属性
        user_content_filter.addAttributes("src", "alt");
        user_content_filter.addAttributes(":all", "style", "class", "id", "name");
        user_content_filter.addAttributes("object", "width", "height","classid","codebase");
        user_content_filter.addAttributes("param", "name", "value");
        user_content_filter.addAttributes("embed", "quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
    }

    /**
     * 对用户输入内容进行过滤
     * @param html
     * @return
     */
    public static String filter(String html) {
        if(StringUtils.isBlank(html)) return "";
        return Jsoup.clean(html, user_content_filter);
    }

    /**
     * 比较宽松的过滤，但是会过滤掉object，script， span,div等标签，适用于富文本编辑器内容或其他html内容
     * @param html
     * @return
     */
    public static String relaxed(String html) {
        return Jsoup.clean(html, Whitelist.relaxed());
    }

    /**
     * 去掉所有标签，返回纯文字.适用于textarea，input
     * @param html
     * @return
     */
    public static String pureText(String html) {
        return Jsoup.clean(html, Whitelist.none());
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String unsafe = "<div>是打发<script>alert(1)</script>斯蒂芬斯蒂芬</div>";
        System.out.println("安理会投票弃权后，阿联酋首次就乌克兰局势发布官方声明安理会投票弃权后，阿联酋首次就乌克兰局势发布官方声明安理会".length());

        
        String safe = ContentSafeFilter.pureText(unsafe);
        System.out.println("safe: " + safe);
    }

}