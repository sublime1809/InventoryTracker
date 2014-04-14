/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import application.reports.visitors.NoticesVisitor;
import application.reports.visitors.Visitor;
import common.Size;

/**
 *
 * @author chrystal
 */
public class ProductGroup extends ProductContainer
{

	protected Size threeMonthSupply;

	/**
	 * constructor 
	 * @param name
	 * @param parentContainer
	 */
	public ProductGroup(String name, ProductContainer parentContainer)
	{
		super(name, parentContainer);
	}

	/**
	 * constructor
	 * @param name
	 * @param parentContainer
	 * @param size
	 */
	public ProductGroup(String name, ProductContainer parentContainer, Size size)
	{
		super(name, parentContainer);
		assert (parentContainer != null);
		assert (size.getSize() >= 0);

		threeMonthSupply = size;
	}

	/**
	 * gets the three month supply of this product group
	 *
	 * @return	the three month supply of the product group
	 */
	public Size getThreeMonthSupply()
	{
		//there are no pre-conditions for this method
		return threeMonthSupply;
	}

	/**
	 * sets the three month supply
	 *
	 * @param threeMonthSupply	the new three month supply
	 */
	public void setThreeMonthSupply(Size threeMonthSupply)
	{
		assert (threeMonthSupply != null);
		assert (threeMonthSupply.getSize() >= 0);
		if (threeMonthSupply.getSize() < 0)
		{
			throw new IllegalArgumentException();
		}
		this.threeMonthSupply = threeMonthSupply;
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
		assert (container instanceof ProductGroup);
		super.addSubContainer(container);
	}

	@Override
	public void acceptPre(Visitor visitor) {
		visitor.visitProductGroup(this);
		super.acceptPre(visitor);
	}

	@Override
	public void acceptPost(Visitor visitor) {
		if(!(visitor instanceof NoticesVisitor)){
			super.acceptPost(visitor);
		}
		visitor.visitProductGroup(this);
	}
        
        public Size getNSupply(int n) {
            Size current = this.threeMonthSupply;
            float nSupply = (current.getSize() / 3) * n;

        return new Size(nSupply, current.getUnit());
    }
}
