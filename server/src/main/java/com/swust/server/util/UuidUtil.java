package com.swust.server.util;

import java.util.UUID;

/***
 * 获取短的Uuid函数
 */
public class UuidUtil {
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public static String getShortUuid(){
        StringBuffer buffer = new StringBuffer();
        String uuid = UuidUtil.getUuid();
        for (int i = 0; i < 8; i++){
            String s =  uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(s, 16);
            //对62取余
            buffer.append(chars[x % 0x3E]);
        }

        return buffer.toString();
    }

    public static String getUuid(){
        String uuid = UUID.randomUUID().toString();
        // 去掉"-"符号
        return uuid.replaceAll("-","");
    }
}
