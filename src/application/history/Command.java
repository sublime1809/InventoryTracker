/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.history;

import application.ContainersFacade;
import application.ItemsFacade;
import application.ProductFacade;
import common.exceptions.CommandFailedException;

/**
 *
 * @author Trevor
 */
public abstract class Command
{
	protected ItemsFacade itemsFacade;
	protected ProductFacade productsFacade;
	protected ContainersFacade containersFacade;
	
	public Command()
	{
		this.itemsFacade = ItemsFacade.getInstance();
		this.productsFacade = ProductFacade.getInstance();
		this.containersFacade = ContainersFacade.getInstance();
	}
	
	public abstract void execute() throws CommandFailedException;
	public abstract void undo() throws CommandFailedException;
	
}
