/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author owner
 */
public class SQLDatabase {
    private static final String driver = "org.sqlite.JDBC";
    private static boolean loadedDriver = false;
    private Connection connection = null;
    
    private static final void loadDriver() {
        try {
            Class.forName(driver);
            loadedDriver = true;
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public SQLDatabase(String db) {
        if(this.loadedDriver == false) {
            this.loadDriver();
        } else {
            this.connect(db);
        }
    }
    
    
    public Connection connect(String db) {
        String dbName = "db" + File.separator + db + ".sqlite";
        String connectionURL = "jdbc:sqlite:" + dbName;
        
//        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionURL);
            
            // to use transactions
            connection.setAutoCommit(false);
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public void query(String sql, String db) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            
            rs = stmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt(1);
                /**
                 * add other columns
                 */
            }
        } catch(SQLException e) {
            
        }
    }
    
    public void update(String db, int id, Map<String, String> update) {
        PreparedStatement stmt = null;
        try {
            String sql = "Update " + db + 
                        "set ";
            ArrayList<String> values = new ArrayList<String>();
            for(String key : update.keySet()) {
                sql += key + " = ?";
                values.add(update.get(key));
            }
            sql += "where id = ?";
            stmt = connection.prepareStatement(sql);
            for(int i = 0; i < values.size(); i++) {
                stmt.setString(i+1, values.get(i));
            }
            stmt.setInt(values.size()+1, id);
            stmt.close();
        } catch(SQLException e) {
            
        } finally {
                
        }
    }
    
    public void finishTransaction(String db, boolean commit) {
        try {
            if(commit) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch(SQLException e) {
            // ERROR
        }
    }
    
    public int insert(String db, List<String> keys, List<String> values) {
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try {
            String keysStr = "";
            String sql = "insert into " + db + " (title, author, genre) values (?,?,?)";
            for(String key : keys) {
                
            }
            for(String value : values) {
                
            }
            stmt = connection.prepareStatement(sql);
            for(int i = 0; i < values.size(); i++) {
                stmt.setString(i+1, values.get(i));
            }
            
        } catch(SQLException e) {
            
        }
        return 0;
    }
}
