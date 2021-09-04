package com.swust.generator.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * freemarker代码生成工具类
 */
public class FreemarkerUtil {

    public static final String ftlPath = "generator/src/main/java/com/swust/generator/ftl/";

    public static Template template;
    /**
     * 初始化方法
     * @param ftlName
     * @throws IOException
     */
    public static void initConfig(String ftlName) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setDirectoryForTemplateLoading(new File(ftlPath));
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
        template = configuration.getTemplate(ftlName);
    }

    public static void generator(Map<String, Object> map, String fileName) throws IOException, TemplateException {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        template.process(map, bufferedWriter);
        bufferedWriter.flush();
        fileWriter.close();
    }
}
