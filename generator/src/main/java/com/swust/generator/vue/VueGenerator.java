package com.swust.generator.vue;

import com.swust.generator.util.DataBaseUtil;
import com.swust.generator.util.Field;
import com.swust.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * 服务端sevice dto 均由该方法生成
 */
public class VueGenerator {

    public static String MODULE = "system";
    public static String toVuePath = "admins\\src\\views\\admin\\";

    // 代码优化 通过读取mybatis-generator.xml文件获取Domain字段
    public static String generatorConfigPath = "server/src/main/resources/generator/generatorConfig.xml";

    public static void main(String[] args) throws IOException, TemplateException, SQLException, DocumentException {

        //只生成配置文件的第一个table节点
        File file = new File(generatorConfigPath);
        SAXReader reader = new SAXReader();
        //读取xml文件到Document中
        Document document = reader.read(file);
        //获取xml文件的根节点
        Element rootElement =  document.getRootElement();
        //读取contextElement节点
        Element contextElement = rootElement.element("context");
        //定义Element用于遍历
        Element tableElement;
        //取第一个table节点
        tableElement = contextElement.elementIterator("table").next();

        //   <table tableName="section" domainObjectName="Section"/>
        String Domain = tableElement.attributeValue("domainObjectName");
        String tableName = tableElement.attributeValue("tableName");
        String tableNameCn = DataBaseUtil.getTableComment(tableName);
        String domain = Domain.substring(0,1).toLowerCase() + Domain.substring(1);

        String module = MODULE;
        List<Field> fieldList = DataBaseUtil.getCloumnByTableName(tableName);
        Set<String> typeSet = getJavaTypes(fieldList);

        Map<String, Object> map = new HashMap<>();

        map.put("Domain", Domain);
        map.put("domain", domain);
        map.put("tableNameCn", tableNameCn);
        map.put("module", module);
        map.put("fieldList", fieldList);
        map.put("typeSet", typeSet);

        FreemarkerUtil.initConfig("vue.ftl");
        FreemarkerUtil.generator(map, toVuePath + domain +".vue");
    }

    /**
     * 获取所有的java类型并使用set去重
     * @param fields
     * @return
     */
    private static Set<String> getJavaTypes(List<Field> fields){
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fields.size(); i++){
            Field field = fields.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
