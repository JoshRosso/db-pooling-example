import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRetriever {

    private static final DataSource dataSource = setupDataSource("jdbc:mysql://127.0.0.1/university?user=root");
    private static final Logger logger = LogManager.getLogger(DataRetriever.class.getName());

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static List<Map<String, Object>> retrieveData(String sql) {

        try {
            Class.forName(MYSQL_DRIVER);
        } catch(ClassNotFoundException ex) {
            logger.error("Could not load driver. Exception: " + ex.getMessage());
        }

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
        //
        // First, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(connectURI, null);

        //
        // Next we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, null);

        //
        // Now we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);

        // Set the factory's pool property to the owning pool
        poolableConnectionFactory.setPool(connectionPool);

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource<PoolableConnection> dataSource =
                new PoolingDataSource<>(connectionPool);

        return dataSource;
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