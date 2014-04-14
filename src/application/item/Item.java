package application.item;

import application.product.Product;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import common.Barcode;
import common.exceptions.InvalidDateException;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents a single item in the inventory tracking system.
 *
 * @author chrystal
 */
public class Item implements Serializable, Comparable<Item> {

    private Barcode barcode;
    private Date entryDate;
    private Date exitTime;
    private ProductContainer productContainer;
    private Product product;
	private int ID;

    /**
     * Constructor: creates a new item.
     */
    public Item() {
    }

    public Item(Item other) {
        this.barcode = other.barcode;
        this.entryDate = other.entryDate;
        this.exitTime = other.exitTime;
        this.productContainer = other.productContainer;
        this.product = other.product;
    }

    /**
     * Constructor: creates a new Item
     *
     * @param barcode	the barcode of the new item
     * @param entryDate	the date the item was entered into the system
     * @param exitTime	the date the item was removed from the system
     * @param storageUnit	the storageUnit that contains the item
     * @param productGroup	the productGroup that contains the item
     * @param product	the product that this item is a member of.
     */
    public Item(Barcode barcode, Date entryDate, Date exitTime,
            StorageUnit storageUnit, ProductGroup productGroup, Product product) {
        this.barcode = barcode;
        this.entryDate = entryDate;
        this.exitTime = exitTime;

        if (productGroup != null) {
            this.productContainer = productGroup;
        } else if (storageUnit != null) {
            this.productContainer = storageUnit;
        }

        this.product = product;
    }
	
	public Item(Barcode barcode, Date entryDate, Date exitTime, ProductContainer container, 
			Product product) {
        this.barcode = barcode;
        this.entryDate = entryDate;
        this.exitTime = exitTime;

        productContainer = container;

        this.product = product;
    }

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

    /**
     * Compares this item with another item to check if they are equal. Items
     * are equal if they have the same id.
     *
     * @param other
     *
     * @return boolean True if these items are equal, false otherwise.
     */
    public boolean equals(Item other) {
        if (other != null && other.barcode.equals(this.barcode)) {
            return true;
        }

        return false;
    }

    /**
     * Compare this item with another item.
     *
     * @param other The other item to compare
     *
     * @return -1 if this item's barcode is less than the other item's, 0 if
     * these items are the exact same item (same item barcode), 1 if this item's
     * barcode is greater than the other item's
     *
     * @throws IllegalArgumentException
     */
    @Override
    public int compareTo(Item other) {
        assert (other != null);

        return this.entryDate.compareTo(other.entryDate);
    }

    /**
     * @return the barcode
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the entryDate
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @param entryDate the entryDate to set
     *
     */
    public void setEntryDate(Date entryDate) {

        this.entryDate = entryDate;
    }

    /**
     * @return the exitDate
     */
    public Date getExitTime() {
        return exitTime;
    }

    /**
     * @param exitTime the exit time to set
     *
     * @throws InvalidDateException if the exit time is before the entry date,
     * in the future or null
     */
    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public int getAge(Date endDate) {
        if (this.entryDate == null) {
            return -1;
        } else if (this.exitTime == null) {
            return (int) ((endDate.getTime() - 
					this.entryDate.getTime()) / (1000 * 60 * 60 * 24));
        } else {
            return (int) ((this.exitTime.getTime() - 
					this.entryDate.getTime()) / (1000 * 60 * 60 * 24));
        }
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        if (product.getShelfLife() > 0) {
            GregorianCalendar expirationDate = new GregorianCalendar();
            expirationDate.setTime(getEntryDate());

            expirationDate.add(GregorianCalendar.MONTH, product.getShelfLife());

            return expirationDate.getTime();
        }

        return null;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        return this.barcode.hashCode();
    }

    /**
     * @return the productGroup
     */
    public ProductGroup getProductGroup() {
        try {
            return (ProductGroup) this.productContainer;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * @return the storageUnit
     */
    public StorageUnit getStorageUnit() {
        try {
            return (StorageUnit) this.productContainer;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * @return the productContainer
     */
    public ProductContainer getProductContainer() {
        return productContainer;
    }

    /**
     * @param productContainer the productContainer to set
     */
    public void setProductContainer(ProductContainer productContainer) {
        this.productContainer = productContainer;
    }

    @Override
    public String toString() {
        return this.product.getProdDesc() + " : " + this.entryDate + " -> " + this.exitTime;
    }
}
