package com.lemon.util;
;

import com.lemon.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2021-06-17 20:26
 */
public class JDBCUtils {
    public static Connection getConnection() {
        String url = "jdbc:mysql://" + Constants.DB_BASE_URI + "/" + Constants.DB_NAME + "?useUnicode=true&characterEncoding=utf-8";
        String user = Constants.DB_USERNAME;
        String password = Constants.DB_PWD;
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * sql更新
     *
     * @param sql
     */
    public static void upDate(String sql) {
        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(connection, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            //关闭数据库连接
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询所有结果集
     *
     * @param sql
     */
    public static List<Map<String, Object>> queryAll(String sql) {

        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> result = null;
        try {
            result = queryRunner.query(connection, sql, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 单个结果集
     *
     * @param sql
     * @return
     */
    public static Map<String, Object> queryOne(String sql) {

        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Map<String, Object> result = null;
        try {
            result = queryRunner.query(connection, sql, new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }

    /**
     * 查询单条数据
     *
     * @param sql
     * @return
     */
    public static Object querySingleData(String sql) {

        Connection connection = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        Object result = null;
        try {
            result = queryRunner.query(connection, sql, new ScalarHandler<Object>());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return result;
    }
}
