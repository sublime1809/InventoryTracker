/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.DTO.DTOBase;
import dataPersistence.PersistenceManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author owner
 */
public abstract class SqlDao {
    private String tableName = "";
    private String[] columns = null;
    private String[] createColumns = null;
    private PersistenceManager persistenceManager = PersistenceManager.getInstance();
    
    protected ResultSet read() {
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet rs = null;
        try
        {
            String query = "SELECT * FROM " + this.tableName;
            stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
            rs = stmt.executeQuery();
            return rs;
        } catch(SQLException e) {
            return null;
        }
        
    }
    
    protected String getSizeUnit(int id) {
        try {
            String sizeUnitQuery = "SELECT Value FROM SizeUnit WHERE ID = " + id;
            PreparedStatement SUstmt = 
				PersistenceManager.getInstance().getConnection().prepareStatement(sizeUnitQuery);
            ResultSet SUresults = SUstmt.executeQuery();
            if(SUresults.first() == true) {
                return SUresults.getString("Value");
            } else {
                return "";
            }
        } catch(SQLException e) {
            return null;
        }
    }
    
    protected boolean create(DTOBase object, SQLTypes[] types, Object[] values) {
        assert(types.length == values.length);
		
		boolean result = false;
        
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try
        {
            String query = "INSERT INTO " + this.tableName + " (";
            for(int i = 0; i < getCreateColumns().length; i++) {
                query += getCreateColumns()[i];
                if(i+1 < getCreateColumns().length) {
                    query += ",";
                } else {
                    query += ")";
                }
            }
            query += " VALUES (";
            for(int i = 0; i < values.length; i++) {
                query += "?";
                if(i+1 < values.length) {
                    query += ",";
                } else {
                    query += ")";
                }
            }
            query += ";";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);	
            for(int i = 0; i < types.length; i++) {
                SQLTypes type = types[i];
                switch(type) {
                    case Integer:
                        stmt.setInt(i+1, (int) values[i]);
                        break;
                    case String:
                        stmt.setString(i+1, (String) values[i]);
                        break;
                    case Date:
                        stmt.setDate(i+1, new java.sql.Date(((Date)values[i]).getTime()));
                        break;
                    case Boolean:
                        stmt.setBoolean(i+1, (boolean)values[i]);
                        break;
                    case Float:
                        stmt.setFloat(i+1, (float)values[i]); 
                    default:
                        break;
                }
            }
            //System.out.println(query);
            if(stmt.executeUpdate() == 1)
            {
                    keyStmt = this.persistenceManager.getConnection().createStatement();
                    keyRS = keyStmt.executeQuery("select last_insert_rowid();");
                    keyRS.next();
                    int id = keyRS.getInt(1);   // ID of the new book
                    object.setID(id);
                    result = true;
            }
            else
            {

            }
        }
        catch (SQLException ex)
        {

        }
        finally
        {
                try
                {
                        if (stmt != null) stmt.close();
                        if (keyRS != null) keyRS.close();
                        if (keyStmt != null) keyStmt.close();
                }
                catch (SQLException ex)
                {
                        //youre screwed
                }
        }
		
		return result;
    }
    protected boolean edit(DTOBase object, SQLTypes[] types, Object[] values) {
        assert(types.length == values.length);
        assert(createColumns.length == values.length);
        
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try
        {
            String query = "UPDATE " + this.tableName + " SET ";
            for(int i = 0; i < createColumns.length; i++) {
                query += createColumns[i] + " = ?";
                if(i+1 < createColumns.length) {
                    query += ", ";
                }
            }
            query += " WHERE ID = ?";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);	
            
            for(int i = 0; i < types.length; i++) {
                SQLTypes type = types[i];
                switch(type) {
                    case Integer:
                        stmt.setInt(i+1, (int) values[i]);
                        break;
                    case String:
                        stmt.setString(i+1, (String) values[i]);
                        break;
                    case Date:
                        stmt.setDate(i+1, new java.sql.Date(((Date)values[i]).getTime()));
                        break;
                    case Boolean:
                        stmt.setBoolean(i+1, (boolean)values[i]);
                        break;
                    case Float:
                        stmt.setFloat(i+1, (float)values[i]); 
                    default:
                        break;
                }
            }
            stmt.setInt(types.length+1, object.getID());

            if(stmt.executeUpdate() == 1)
            {
                return true;
            }
            else
            {

            }
        }
        catch (SQLException ex)
        {

        }
        finally
        {
                try
                {
                    if (stmt != null) stmt.close();
                    if (keyRS != null) keyRS.close();
                    if (keyStmt != null) keyStmt.close();
                    return true;
                }
                catch (SQLException ex)
                {
                    return false;
                }
        }
    }
    protected boolean delete(DTOBase dto){
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        
        boolean result = false;
        try {
            String query = "DELETE FROM " + this.tableName + " WHERE ID = ?;";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);
            stmt.setInt(1, dto.getID());
            
            if (stmt.executeUpdate() == 1) {
                result = true;
            } else {
                result = false;
            } 

        } catch (SQLException ex) {
            result = false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (keyRS != null) {
                    keyRS.close();
                }
                if (keyStmt != null) {
                    keyStmt.close();
                }
            } catch (SQLException ex) {
                //you're screwed
            }
        }
        return result;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the columns
     */
    public String[] getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    /**
     * @return the createColumns
     */
    public String[] getCreateColumns() {
        return createColumns;
    }

    /**
     * @param createColumns the createColumns to set
     */
    public void setCreateColumns(String[] createColumns) {
        this.createColumns = createColumns;
    }
    
    
}
