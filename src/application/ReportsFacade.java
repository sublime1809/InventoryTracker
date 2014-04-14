/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.Observable;

/**
 * A singleton class that is used to generate reports.
 * 
 * @author Stephen Kitto
 */
public class ReportsFacade extends Observable{
	
	private static ReportsFacade instance;
	
	/**
	 * Gets the one and only instance of this singleton class
	 * 
	 * @return		the instance of this class
	 */
	public static ReportsFacade getInstance() {
		if (instance == null){
			instance = new ReportsFacade();
		}
		return instance;
	}
	
	/**
	 * Constructor
	 */
	private ReportsFacade(){
		
	}
}
