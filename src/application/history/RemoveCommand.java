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
 * @author Stephen Kitto
 */
public class RemoveCommand extends Command{
	
	private Item itemRemoved;
	private ProductContainer oldContainer;
	
	public RemoveCommand(Item item, ProductContainer container){
		itemRemoved = item;
		oldContainer = container;
	}

	public Item getItemRemoved() {
		return itemRemoved;
	}

	public ProductContainer getOldContainer() {
		return oldContainer;
	}

	@Override
	public void execute() throws CommandFailedException {
		itemsFacade.removeItem(itemRemoved);
	}

	@Override
	public void undo() throws CommandFailedException {
		try {
			itemRemoved.setExitTime(null);
			itemsFacade.addItem(itemRemoved);
		} catch (Exception ex) {
			throw new CommandFailedException();
		}
	}
	
}
