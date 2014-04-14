/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import application.reports.visitors.Visitor;
import common.exceptions.InvalidContainerException;
import java.util.Iterator;

/**
 *
 * @author skitto
 */
public class HomeStorage extends ProductContainer
{
    /**
	 * The name that is used to identify a ProductContainer as the Root of the container tree
	 */
	public static final String ROOT_NAME = "rootUnit";

	/**
	 * constructor 
	 * 
	 * @throws InvalidContainerException 
	 */
	public HomeStorage() throws InvalidContainerException
	{
		super(ROOT_NAME, null);
		super.setID(0);
	}

	/**
	 * The name of the root of the tree should never be changed
	 */
	@Override
	public void setName(String name)
	{
		assert (false);
	}

	/**
	 * The parent of the root of the tree should never be changed
	 */
	@Override
	public void setParent(ProductContainer parent)
	{
		assert (false);
	}

	/**
	 * Add a child container to this container.
	 *
	 * @param container ProductContainer to add to children of this container
	 *
	 * @throw InvalidContainerException if the container is invalid
	 */
	@Override
	protected void addSubContainer(ProductContainer container)
	{
		assert (container != null);
		assert (container instanceof StorageUnit);
		super.addSubContainer(container);
	}

	@Override
	public void acceptPre(Visitor visitor) {
		visitor.visitHomeStorage(this);
		super.acceptPre(visitor);
	}

	@Override
	public void acceptPost(Visitor visitor) {
		super.acceptPost(visitor);
		visitor.visitHomeStorage(this);
	}
}
