/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SER;

import dataPersistence.ContainerDTO;
import dataPersistence.IContainerDAO;
import dataPersistence.MappingDTO;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public class SERContainerDAO implements IContainerDAO, Serializable {

	
	/**
	 * does nothing
	 */
	public void createContainer(ContainerDTO container){
		
	}
	
	/**
	 * does nothing
	 */
	public Iterator<ContainerDTO> getContainers(){
		return null;
	}
	
	/**
	 * does nothing
	 */
	public void updateContainer(ContainerDTO container){
		
	}
	
	/**
	 * does nothing
	 */
	public void removeContainer(ContainerDTO container){
		
	}
	
	/**
	 * does nothing
	 */
	public boolean createContainerProductMapping(MappingDTO mapping){
		return true;
	}
	
	/**
	 * does nothing
	 */
	public Iterator<MappingDTO> getMappings(){
		return null;
	}

	@Override
	public boolean deleteContainerProductMapping(MappingDTO mapping) {
		return true;
	}
}
