package com.swust.generator.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * 数据库连接工具类
 */
public class DataBaseUtil {
    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://47.112.136.60:3306/course";
            String user = "xuetong";
            String password = "Xiaonie1996!";
            connection = DriverManager.getConnection(url, user, password);
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /***
     * 获取表注释
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static String getTableComment(String tableName) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = "select table_comment from information_schema.tables WHERE table_name = '" + tableName +"'";
        ResultSet resultSet = statement.executeQuery(sql);
        String tableNameCn = "";
        if (resultSet != null){
            while (resultSet.next()){
                tableNameCn = resultSet.getString("table_comment");
                break;
            }
        }
        resultSet.close();
        statement.close();
        connection.close();
        System.out.println("表名: " + tableNameCn);
        return tableNameCn;
    }

    /***
     * 下划线转小驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str){
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = linePattern.matcher(str);
        while (matcher.find()){
            matcher.appendReplacement(buffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    /**
     * 下划线转大驼峰
     */
    public static String lineToBigHump(String str){
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 数据库类型转java类型
     * @param sqlType
     * @return
     */
    public static String sqlTyoeToJavaType(String sqlType){
        if (sqlType.toUpperCase().contains("varchar".toUpperCase())
                || sqlType.toUpperCase().contains("char".toUpperCase())
                || sqlType.toUpperCase().contains("text".toUpperCase())) {
            return "String";
        } else if (sqlType.toUpperCase().contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.toUpperCase().contains("long".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("decimal".toUpperCase())) {
            return "BigDecimal";
        } else {
            return "String";
        }
    }
    /**
     * 获取所有列的信息
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static List<Field> getCloumnByTableName(String tableName) throws SQLException {
        List<Field> fieldList = new ArrayList<>();
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = "show full columns from `"+ tableName +"`";
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet != null){
            while (resultSet.next()){
                String colunmnName = resultSet.getString("Field");
                String type = resultSet.getString("Type");
                String comment = resultSet.getString("Comment");
                String nullAble = resultSet.getString("Null");
                Field field = new Field();
                field.setName(colunmnName);
                field.setNameHump(lineToHump(colunmnName));
                field.setNameBigHump(lineToBigHump(colunmnName));
                field.setType(type);
                field.setJavaType(DataBaseUtil.sqlTyoeToJavaType(resultSet.getString("Type")));
                field.setComment(comment);
                if (comment.contains("|")) {
                    field.setNameCn(comment.substring(0, comment.indexOf("|")));
                } else {
                    field.setNameCn(comment);
                }
                field.setNullAble("YES".equals(nullAble));
                //只有varchar类型需要判断长度
                if (type.toUpperCase().contains("varchar".toUpperCase())) {
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                } else {
                    field.setLength(0);
                }

               fieldList.add(field);
            }
        }
        resultSet.close();
        statement.close();
        connection.close();
        System.out.println("列信息: " + fieldList);
        return fieldList;
    }
}
