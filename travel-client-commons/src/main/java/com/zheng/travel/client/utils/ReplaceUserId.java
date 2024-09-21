package com.zheng.travel.client.utils;

import java.io.*;

public class ReplaceUserId {

    public boolean ReplaceUserId(String savepath, String tenUserid ) {

        try {
            FileInputStream fis = null;
            String fileStr = "";
            File sourcef = new File(savepath);
            fis = new FileInputStream(sourcef);
            byte[] b2 = new byte[(int) sourcef.length()];
            fis.read(b2);
            fileStr = new String(b2); //先获取模版中的代码

            tenUserid=tenUserid.replaceAll("\\$", "REPLACE_CHAR_DOLLAR");// 主要是replaceAll方法不支持$符号的替换，所以要先把$符替换成别的，之后再替换回来

            fileStr = fileStr.replaceAll("###tenUserid###", tenUserid);


            fileStr = fileStr.replaceAll("REPLACE_CHAR_DOLLAR", "\\$");// 最后将所有$符号还原

            File f = new File(savepath);
            BufferedWriter o = new BufferedWriter(new FileWriter(f));
            o.write(fileStr);//将编辑好的代码写入文件中
            o.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
