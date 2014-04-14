/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.history;

import application.item.Item;
import application.product.Product;
import application.storage.ProductContainer;
import application.storage.StorageUnit;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.CommandFailedException;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidItemException;
import common.exceptions.InvalidProductException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Trevor
 */
public class AddItemsCommand extends Command
{
	private Product _product;
	private StorageUnit _storageUnit;
	private Collection<Item> _items;
	private boolean _productWasInStorageUnit;
	private boolean _firstOfProduct;
	private boolean _firstOfProductInSystem;

	public AddItemsCommand(Collection<Item> items, Product product, StorageUnit storageUnit, 
			boolean productWasInStorageUnit, boolean firstOfProduct, boolean firstInSystem)
	{
		super();
		this._items = items;
		this._product = product;
		this._storageUnit = storageUnit;
		this._productWasInStorageUnit = productWasInStorageUnit;
		this._firstOfProduct = firstOfProduct;
		this._firstOfProductInSystem = firstInSystem;
	}
	
	@Override
	public void execute() throws CommandFailedException
	{
		try
		{
			if (!productsFacade.productExists(_product))
			{
				//add product
				productsFacade.addProduct(_product, _storageUnit);
			}
			
			//add product to container
			if(!_productWasInStorageUnit)
			{
				productsFacade.addProductToContainer(_product, _storageUnit);
			}
			
			//Add the items
			itemsFacade.addItems(_items);
			
//			itemsFacade.newAddItems(_items);
		}
		catch (Exception ex)
		{
			throw new CommandFailedException();
		}
	}

	@Override
	public void undo() throws CommandFailedException
	{
		itemsFacade.undoAddItems(_items);
		if(!_productWasInStorageUnit)
		{
			try
			{
				//remove product from container
				ProductContainer cont = 
						containersFacade.getContainerThatHasProduct(_storageUnit, _product);
				productsFacade.deleteProduct(_product, cont);
			}
			catch (CannotBeRemovedException ex)
			{
				throw new CommandFailedException();
			}
		}
		
		if(_firstOfProductInSystem){
			productsFacade.undoAddProduct(_product);
		}
		
		for(Item item : _items)
		{
			item.setID(0);
		}
		
//		if(productsFacade.getItemCount(_product) == 0)
//		{
//			productsFacade.undoAddProduct(_product);
//		}
	}
	
	public Collection<Item> getItems()
	{
		return _items;
	}
	
	public Product getProduct()
	{
		return _product;
	}
	
	public boolean productWasInContainer()
	{
		return _productWasInStorageUnit;
	}
	
	public boolean firstOfProduct()
	{
		return _firstOfProduct;
	}
}
