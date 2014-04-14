/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import application.reports.visitors.Visitor;
import common.exceptions.InvalidContainerException;
import java.util.UUID;

/**
 *
 * @author chrystal
 */
public class StorageUnit extends ProductContainer
{

	/**
	 * constructor
	 * @param name
	 * @param parentContainer
	 */
	public StorageUnit(String name, ProductContainer parentContainer)
	{
		//the pre-conditions are asserted in ProductContainer
		super(name, parentContainer);
	}

	/**
	 * Add a child container to this container.
	 *
	 * @param container ProductContainer to add to children of this container
	 */
	@Override
	protected void addSubContainer(ProductContainer container)
	{
		assert (container != null);
		assert (container instanceof ProductGroup);
		super.addSubContainer(container);
	}

	@Override
	public void acceptPre(Visitor visitor) {
		visitor.visitStorageUnit(this);
		super.acceptPre(visitor);
	}

	@Override
	public void acceptPost(Visitor visitor) {
		super.acceptPost(visitor);
		visitor.visitStorageUnit(this);
	}
}
