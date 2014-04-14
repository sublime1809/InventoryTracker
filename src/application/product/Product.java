/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.product;

import application.reports.visitors.SupplyVisitor;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author chrystal
 * @author leckie
 */
public class Product implements Serializable, Comparable<Product> {
    //*********************************
    //*********************************
    // Attributes

    private Date creationDate;
    private Barcode barcode;
    private String productDescription;
    private int shelfLife;
    private Size threeMonthSupply;
    private Size size;
	private int ID;

//	public Product(Date _creationDate, Barcode _barcode, String _prodDesc,
//			int _shelfLife, Size _threeMonthSupply, Size _size)
//	{
//		creationDate = _creationDate;
//		barcode = _barcode;
//		productDescription = _prodDesc;
//		shelfLife = _shelfLife;
//		threeMonthSupply = _threeMonthSupply;
//		size = _size;
//	}
//	
    /**
     * This will create a new product object with the given date, barcode,
     * description, shelf life and supply
     *
     * @param _creationDate				the date the product was first added to the system
     * @param _barcode					the product's barcode
     * @param _productDescription		the product's description
     * @param _shelfLife				the product's shelf life
     * @param _threeMonthSupplyValue	the product's three month supply
	 * @param _size						the product's size
     */
    public Product(Date _creationDate, String _barcode, String _productDescription,
            int _shelfLife, float _threeMonthSupplyValue, Size _size) {
        this.creationDate = _creationDate;
        this.barcode = new Barcode(_barcode);
        this.productDescription = _productDescription;
        this.shelfLife = _shelfLife;
        this.threeMonthSupply = new Size(_threeMonthSupplyValue, SizeUnit.Count);
        this.size = _size;
    }
    
    public Product(Product _copy) {
        this.creationDate = new Date(_copy.getDate().getTime());
        this.barcode = new Barcode(_copy.getBarcode());
        this.productDescription = _copy.productDescription;
        this.shelfLife = _copy.shelfLife;
        this.threeMonthSupply = _copy.threeMonthSupply;
        this.size = _copy.size;
    }

    /**
	 * Constructor: creates a new product
	 */
	public Product() {
    }

    //******************************************
    //******************************************
    //                  GETTERS
    /**
     * Getter for Creation Date
     *
     * @return date of this object
     */
    public Date getDate() {
        return creationDate;
    }

    /**
     * Getter for Barcode
     *
     * @return barcode of this object
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Getter for Prduct Description
     *
     * @return productDescription of this object
     */
    public String getProdDesc() {
        return productDescription;
    }

    /**
     * Getter for Shelf Life
     *
     * @return shelfLife of this object
     */
    public int getShelfLife() {
        return shelfLife;
    }

    /**
	 * Gets the shelfLife of the product
	 * @return	the product's shelfLife
	 */
	public String getShelfLifeString() {
        if (shelfLife == 1) {
            return shelfLife + " month";
        }
        return shelfLife + " months";
    }

    /**
     * Getter for Three Month Supply
     *
     * @return threeMonthSupply of this object
     */
    public Size getMonthSupply() {
        return threeMonthSupply;
    }

    //******************************************
    //******************************************
    //                  SETTERS
    /**
     * Setter for Creation Date
     *
     * @param _date
     */
    public void setDate(Date _date) {
        creationDate = _date;
    }

    /**
     * Setter for Barcode
     *
     * @param _barcode
     */
    public void setBarcode(Barcode _barcode) {
//		assert (_barcode != null);
//		assert (Barcode.isValid(_barcode));
//                if(_barcode == null) throw new NullPointerException();
//                if(!Barcode.isValid(_barcode)) throw new InvalidBarcodeException();
        barcode = _barcode;
    }

    /**
     * Setter for Product Description
     *
     * @param _newProductDescription
     */
    public void setProdDesc(String _newProductDescription) {
        assert (_newProductDescription != null);
        assert (!_newProductDescription.equals(""));
        productDescription = _newProductDescription;
    }

    /**
     * Setter for Shelf Life
     *
     * @param _shelfLife
     */
    public void setShelfLife(int _shelfLife) {
        assert (_shelfLife >= 0);
        shelfLife = _shelfLife;
    }

    /**
     * Setter for Three Month Supply
     *
     * @param _threeMonthSupply
     */
    public void setMonthSupply(Size _threeMonthSupply) {
        assert (_threeMonthSupply.getSize() >= 0);
        threeMonthSupply = _threeMonthSupply;
    }

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

    //*********************************
    //*********************************
    // Methods	
    // +addToContainer(in Container: ProductContainer) : Product
    // +equals(in toCompare : Product): Boolean
    // +switchContainers(in old: ProdCont, in new: ProdCont)
    // +hasItem(in Item: Item, in Contained : ProdCont)
    /**
     * Compare two products to see if they are equal
     *
	 * @param toCompare		the other product you wish to compare this one with
	 * @return				-1 if this one is less than, 1 if the other is less than, 0 if equal
     */
    public boolean equals(Product toCompare) {

        if (this.barcode.compareTo(toCompare.barcode) == 0) {
            return true;
        }
        return false;
    }

    /**
     * Compares this product with another product to check if they are equal.
     * Products are equal if they have the same PRODUCT barcode.
     *
     * @param other The other product to be compared.
     *
     * @return int return -1 if less than other, 0 if equal, 1 if greater than
     * other
     */
    @Override
    public int compareTo(Product other) {
        return this.productDescription.compareTo(other.productDescription);
        //return this.barcode.compareTo(other.barcode);
    }

    /**
     * @return the size
     */
    public Size getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        return this.barcode.hashCode();
    }
    
    public Size getNSupply(int n) {
        Size current = this.threeMonthSupply;
        float nSupply = (current.getSize() / 3) * n;
        
        return new Size(nSupply, current.getUnit());
    }

    void accept(SupplyVisitor visitor) {
        //throw new UnsupportedOperationException("Not yet implemented");
        visitor.visitProduct(this);
        
    }
}
