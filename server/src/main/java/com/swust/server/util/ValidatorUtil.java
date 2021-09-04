package com.swust.server.util;

import com.swust.server.exceptions.ValidatorException;
import org.springframework.util.StringUtils;

public class ValidatorUtil {
    /**
     * 非空校验
     * @param str
     * @param fileName
     */
    public static void require (Object str, String fileName){
        if (StringUtils.isEmpty(str)){
            throw new ValidatorException(fileName + "不能为空");
        }
    }

    /***
     * 长度校验
     * @param str
     * @param filedName
     * @param min
     * @param max
     */
    public static void length(String str, String filedName, int min, int max){
        if (StringUtils.isEmpty(str)){
            return;
        }
        int len = 0;
        if (!StringUtils.isEmpty(str)){
            len = str.length();
        }

        if (len < min || len > max){
            throw new ValidatorException(filedName + "长度" + min + "~" + max + "位");
        }
    }
}
