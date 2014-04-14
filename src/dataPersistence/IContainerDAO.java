/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public interface IContainerDAO {
	
	/**
	 * adds a container to the database
	 * @param container		container to be added
	 */
	public void createContainer(ContainerDTO container);
	
	/**
	 * gets all the containers
	 * @return	an iterator with a dto for all the containers
	 */
	public Iterator<ContainerDTO> getContainers();
	
	/**
	 * updates the container given
	 * @param container		the container with some new data
	 */
	public void updateContainer(ContainerDTO container);
	
	/**
	 * removes the container
	 * @param container		the container to be removed
	 */
	public void removeContainer(ContainerDTO container);
	
	/**
	 * adds a container to product mapping to the data base
	 * @param mapping	the new mapping
	 */
	public boolean createContainerProductMapping(MappingDTO mapping);
	
	public boolean deleteContainerProductMapping(MappingDTO mapping);
	
	/**
	 * gets all the mappings in the system
	 * @return	an iterator with a dto for all the mappings
	 */
	public Iterator<MappingDTO> getMappings();
}
