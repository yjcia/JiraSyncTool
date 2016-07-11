package com.kewill.jira.util;

import com.kewill.jira.common.JiraAttribute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by YanJun on 2016/7/1.
 */
public class DbConnectionUtil {

    private final int INIT_COUNT = 5;
    private final int MAX_COUNT = 30;
    private int count = 0;
    private LinkedList<Connection> connectionPoolList;
    private static final DbConnectionUtil connectionPool = new DbConnectionUtil();
    private final Object wait = new Object();
    private DbConnectionUtil(){
        connectionPoolList = new LinkedList<Connection>();
        try{
            Class.forName(PropertyUtil.getPropValue(JiraAttribute.DRIVER));
            for(int i =0;i<INIT_COUNT;i++){
                Connection conn = createConnection();
                if(conn != null){
                    connectionPoolList.add(conn);
                    count ++;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        try {
            String url = PropertyUtil.getPropValue(JiraAttribute.URL);
            String user = PropertyUtil.getPropValue(JiraAttribute.USER);
            String password = PropertyUtil.getPropValue(JiraAttribute.PASSWORD);
            return DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static DbConnectionUtil getInstance() {
        return connectionPool;
    }
    public Connection getConnection(){
        synchronized (connectionPoolList){
            while (connectionPoolList.size() > 0){
                Connection conn =  connectionPoolList.removeLast();
                try {
                    if(conn.isValid(2000)){
                        return conn;
                    }else{
                        count --;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(count < MAX_COUNT){
                count ++;
                return createConnection();
            }
            synchronized (wait){
                try {
                    wait.wait(3000);
                    if(connectionPoolList.size() > 0){
                        return connectionPoolList.removeLast();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public void releaseConnection(Connection connection) {
        connectionPoolList.add(connection);
        synchronized (wait) {
            wait.notify();
        }
    }
}
