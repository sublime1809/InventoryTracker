/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.history;

import application.item.Item;
import application.storage.ProductContainer;
import common.exceptions.CommandFailedException;

/**
 *
 * @author Trevor
 */
public class TransferItemsCommand extends Command
{
	Item item;
	ProductContainer oldContainer;
	ProductContainer newContainer;
	boolean firstOfProduct;
	boolean productWasInContainer;
	
	public TransferItemsCommand(Item i, ProductContainer oldCont, ProductContainer newCont)
	{
		super();
		this.item = i;
		this.oldContainer = oldCont;
		this.newContainer = newCont;
		this.firstOfProduct = false;
		this.productWasInContainer = false;
	}
	
	@Override
	public void execute() throws CommandFailedException
	{
		try	
		{
			itemsFacade.moveItemToStorageUnit(item, newContainer);
		}
		catch (Exception ex)
		{
			throw new CommandFailedException();
		}
	}

	@Override
	public void undo() throws CommandFailedException
	{
		try	
		{
			itemsFacade.moveItemToStorageUnit(item, oldContainer);
			
			if(!this.productWasInContainer)
			{
				//remove product from container
				this.productsFacade.deleteProduct(item.getProduct(), newContainer);
			}
		}
		catch (Exception ex)
		{
			throw new CommandFailedException();
		}
	}

	public void setFirstOfProduct(boolean b)
	{
		firstOfProduct = b;
	}
	
	public boolean wasFirstOfProduct()
	{
		return firstOfProduct;
	}
	
	public void productWasInContainer(boolean b)
	{
		productWasInContainer = b;
	}
	
	public boolean productWasInContainer()
	{
		return productWasInContainer;
	}
	
	public Item getItem()
	{
		return item;
	}
}
