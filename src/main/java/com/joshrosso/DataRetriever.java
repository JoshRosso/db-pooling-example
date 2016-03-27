package com.joshrosso;

import org.apache.commons.dbcp2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRetriever {

    private static final DataSource dataSource = setupDataSource("jdbc:mysql://52.24.207.152/university?user=tester&password=tester");
    private static final Logger logger = LogManager.getLogger(DataRetriever.class.getName());

    public static List<Map<String, Object>> retrieveData(String sql) {

        List<Map<String,Object>> resultList = null;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.prepareStatement(sql)) {

             try (ResultSet rset = stmt.executeQuery(sql)) {

                 resultList = parseResultSet(rset);

             } catch(SQLException ex) {
                 logger.error("Query failed to execute. Exception: " + ex.getMessage());
             }

        } catch(SQLException ex) {
            logger.error("Could not prepare statement: " + ex.getMessage());
        }

        return resultList;
    }

    public static DataSource setupDataSource(String connectURI) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(connectURI);
        ds.setMaxIdle(60);
        ds.setMaxTotal(80);
        ds.setMinIdle(20);
        ds.setMaxWaitMillis(30000);
        return ds;
    }

    private static List<Map<String,Object>> parseResultSet(ResultSet rset) throws SQLException {
        List<Map<String,Object>> resultList = new ArrayList<>();
        ResultSetMetaData rsMeta = rset.getMetaData();
        int numOfCol = rsMeta.getColumnCount();

        while(rset.next()) {
            Map<String,Object> row = new HashMap<>();
            for(int i = 1; i <= numOfCol; i++) {
                row.put(rsMeta.getColumnName(i), rset.getObject(i));
            }
            resultList.add(row);
        }

        return resultList;
    }

}