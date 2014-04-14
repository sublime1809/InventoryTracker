/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.ContainerDTO;
import dataPersistence.IContainerDAO;
import dataPersistence.MappingDTO;
import dataPersistence.PersistenceManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephen Kitto
 * @author Leckie Gunter
 */
public class SQLContainerDAO implements IContainerDAO{

    private PersistenceManager persistenceManager = PersistenceManager.getInstance();

    /**
     * adds a container to the database
     *
     * @param container	container to be added
     */
    public void createContainer(ContainerDTO container){
        //System.out.println("Creating Container");
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try {
            String query = "INSERT INTO ProductContainer "
                    + "(Name, ParentID, MSUnit, MSValue) VALUES (?, ?, ?, ?);";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);
            stmt.setString(1, container.getName());
            stmt.setInt(2, container.getParentID());
            stmt.setString(3, container.getSupplyUnit());
            stmt.setFloat(4, container.getSupplyValue());

            if (stmt.executeUpdate() == 1) {
                keyStmt = this.persistenceManager.getConnection().createStatement();
                keyRS = keyStmt.executeQuery("select last_insert_rowid()");
                keyRS.next();
                int id = keyRS.getInt(1);   // ID of the new container
                container.setID(id);
            } else {
            }
        } catch (SQLException ex) {
            //System.out.println("SQL exception in create Container: " + ex.getMessage());
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
                //youre screwed
            }
        }
    }

    /**
     * gets all the containers
     *
     * @return	an iterator with a dto for all the containers
     */
    public Iterator<ContainerDTO> getContainers() {
		List<ContainerDTO> containers = new ArrayList();
        PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		
		try {
			String query = "select * from ProductContainer";
			stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
			
			keyRS = stmt.executeQuery();
			while(keyRS.next()){
				int id = keyRS.getInt("ID");
				String name = keyRS.getString("Name");
				int parentID = keyRS.getInt("ParentID");
				String sizeUnit = keyRS.getString("MSUnit");
				float unitValue = keyRS.getFloat("MSValue");
//				String sizeUnit;
				
//				if(unitID > 0 && unitID <= 10){
//					String otherQuery = "select Value from SizeUnit where ID = " + unitID;
//					PreparedStatement otherStmt = PersistenceManager.getInstance().getConnection().
//							prepareStatement(otherQuery);
//					ResultSet otherKeyRS = otherStmt.executeQuery();
//					otherKeyRS.next();
//					sizeUnit = otherKeyRS.getString(1);
//				}
//				else{
//					sizeUnit = null;
//				}
				containers.add(new ContainerDTO(id, name, parentID, sizeUnit, unitValue));
			}
			
		} catch (SQLException ex) {
		}

		return containers.iterator();	
    }

    /**
     * updates the container given
     *
     * @param container	the container with some new data
     */
    public void updateContainer(ContainerDTO container) {
        //System.out.println("Updating Container");
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try {
            String query = "UPDATE ProductContainer "
                    + "SET Name = ?, MSUnit = ?, MSValue = ?"
                    + "WHERE ID = ?;";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);
            stmt.setString(1, container.getName());
            stmt.setString(2, container.getSupplyUnit());
            stmt.setFloat(3, container.getSupplyValue());
            stmt.setInt(4, container.getID());
            
            if (stmt.executeUpdate() == 1) {
                //keyStmt = this.persistenceManager.getConnection().createStatement();
                //keyRS = keyStmt.executeQuery("select last_insert_rowid()");
                //keyRS.next();
                //int id = keyRS.getInt(1);   // ID of the new container
                //container.setID(id);
            } else {
            } 

        } catch (SQLException ex) {
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
    }

    /**
     * removes the container
     *
     * @param container	the container to be removed
     */
    public void removeContainer(ContainerDTO container) {
        //System.out.println("Removing Container");
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try {
            String query = "DELETE FROM ProductContainer "
                    + "WHERE ID = ?;";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);
            stmt.setInt(1, container.getID());
            
            if (stmt.executeUpdate() == 1) {
                //keyStmt = this.persistenceManager.getConnection().createStatement();
                //keyRS = keyStmt.executeQuery("select last_insert_rowid()");
                //keyRS.next();
                //int id = keyRS.getInt(1);   // ID of the new container
                //container.setID(id);
            } else {
            } 

        } catch (SQLException ex) {
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
    }

    /**
     * adds a container to product mapping to the data base
     *
     * @param mapping	the new mapping
     */
    public boolean createContainerProductMapping(MappingDTO mapping) {
        //System.out.println("Creating Product to Container Mapping");
        PreparedStatement stmt = null;
        Statement keyStmt = null;
        ResultSet keyRS = null;
        try {
            String query = "INSERT INTO Product_ProductContainer "
                    + "(ProductID, ProductContainerID) VALUES (?, ?);";
            stmt = this.persistenceManager.getConnection().prepareStatement(query);
            stmt.setInt(1, mapping.getProductID());
            stmt.setInt(2, mapping.getContainerID());

            if (stmt.executeUpdate() == 1) {
            } else {
                return false;
            }
        } catch (Exception ex) {
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
                return false;
                //youre screwed
            }
            return true;
        }
    }
	
	public boolean deleteContainerProductMapping(MappingDTO mapping) {
            //System.out.println("Removing Product to Container Mapping");
            PreparedStatement stmt = null;
            Statement keyStmt = null;
    
            try {    ResultSet keyRS = null;
                    String query = "DELETE FROM Product_ProductContainer "
						   + "WHERE ProductID = ? AND ProductContainerID = ?;";
                    stmt = this.persistenceManager.getConnection().prepareStatement(query);
                    stmt.setInt(1, mapping.getProductID());
                    stmt.setInt(2, mapping.getContainerID());

                if (stmt.executeUpdate() >= 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                    return false;
            }
	}

    /**
     * gets all the mappings in the system
     *
     * @return	an iterator with a dto for all the mappings
     */
    public Iterator<MappingDTO> getMappings() {
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		
		try {
			String query = "select * from Product_ProductContainer";
			stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
			List<MappingDTO> mappings = new ArrayList();
			
			keyRS = stmt.executeQuery();
			while(keyRS.next()){
				int id = keyRS.getInt("ID");
				int productID = keyRS.getInt("ProductID");
				int containerID = keyRS.getInt("ProductContainerID");
				
				mappings.add(new MappingDTO(productID, containerID));
			}

			return mappings.iterator();	
			
		} catch (SQLException ex) {
			return null;
		}	
    }
}
