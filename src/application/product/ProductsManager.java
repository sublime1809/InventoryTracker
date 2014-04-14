package application.product;

import application.AssistantToTheRegionalManager;
import application.item.Item;
import application.reports.visitors.SupplyVisitor;
import application.storage.ProductContainer;
import common.Barcode;
import common.DTOConverter;
import common.Mapping;
import common.Size;
import common.SizeUnit;
import common.exceptions.CannotBeRemovedException;
import dataPersistence.IContainerDAO;
import dataPersistence.IItemDAO;
import dataPersistence.IProductDAO;
import dataPersistence.ItemDTO;
import dataPersistence.MappingDTO;
import dataPersistence.PersistenceManager;
import dataPersistence.ProductDTO;
import java.io.Serializable;
import java.util.*;

//barcode to product
//product to items
//product to containers
/**
 *
 * @author Trevor
 * @author Leckie
 */
public class ProductsManager implements Serializable {

    private static ProductsManager INSTANCE;
    private PersistenceManager persistenceManager = PersistenceManager.getInstance();

    /**
     * Gets the instance of this singleton class
     *
     * @return	the one and only instance of this class
     */
    public static ProductsManager getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = AssistantToTheRegionalManager.getInstance().getPM();
//        }
//
//        return INSTANCE;
		return AssistantToTheRegionalManager.getInstance().getPM();
    }
    private HashSet<Product> products;
    private HashMap<Barcode, Product> barcodeToProduct;
    private HashMap<Product, List<ProductContainer>> productToContainers;
    private HashMap<Product, List<Item>> productToItems;

    public ProductsManager() {
        products = new HashSet<Product>();
        barcodeToProduct = new HashMap<Barcode, Product>();
        productToContainers = new HashMap<Product, List<ProductContainer>>();
        productToItems = new HashMap<Product, List<Item>>();
    }

    /**
     * used to reset the manager mostly just for testing purposes
     */
    public void reset() {
        //no pre-condition for this method
        INSTANCE = new ProductsManager();
    }

    /**
     * Add a new product with the associated maps for barcode, item, container
     *
     * @param product
     * @param barcode
     * @param container
     */
    public boolean addNewProduct(Product product, Barcode barcode, ProductContainer container) {
        assert canAddBarcodeProductPair(barcode, product);
        assert canAddProductContainerPair(product, container);

        ProductDTO productDTO = DTOConverter.productToProductDTO(product);
        IProductDAO productDAO = PersistenceManager.getInstance().getProductDAO();
        if (productDAO.createProduct(productDTO)) {
			product.setID(productDTO.getID());
            products.add(product);
            addBarcodeProductPair(barcode, product);
            addProductContainerPair(product, container);
            productToItems.put(product, new ArrayList());
            return true;
        } else {
            return false;
        }
    }

    /**
     * This will add a product to the system but not to a container
     *
     * @param product
     * @param barcode
     */
    public boolean addNewProduct(Product product, Barcode barcode) {
        assert canAddBarcodeProductPair(barcode, product);

        ProductDTO productDTO = DTOConverter.productToProductDTO(product);
        IProductDAO productDAO = PersistenceManager.getInstance().getProductDAO();
        if (productDAO.createProduct(productDTO)) {
            product.setID(productDTO.getID());
            products.add(product);
            addBarcodeProductPair(barcode, product);
            productToItems.put(product, new ArrayList());
            productToContainers.put(product, new ArrayList());
            return true;
        } else {
            return false;
        }
    }

    public void loadProduct(Product product, Barcode barcode) {
        assert canAddBarcodeProductPair(barcode, product);

        products.add(product);
        addBarcodeProductPair(barcode, product);
        productToItems.put(product, new ArrayList());
        productToContainers.put(product, new ArrayList());
    }

    /**
     * Checks the constraints put on new products as given in the Data
     * Dictionary
     *
     * Pre condition: the product is not null
     *
     * Post condition the product checked is a valid product.
     *
     * @param product
     *
     * @return
     */
    public boolean canAddProduct(Product product) {
        //creation date equal to earliest
        //barcode is non empty
        //description is non empty
        //shelf life is positive val >=0
        //product containers, at most 1 PG in SU
        assert !products.contains(product);
        assert product != null;

        if (products.contains(product)) {
            return false;
        }

        Size supply = product.getMonthSupply();
        // TODO check this error!
        if (supply.getUnit() == SizeUnit.Count) {
            if (supply.getSize() != 1) {
//				return false;
            }
        } else if (product.getShelfLife() < 0) {
            return false;
        } else if (product.getMonthSupply().getSize() < 0) {
            return false;
        }

        return validDescription(product) && validDate(product);
    }

    private boolean validDate(Product product) {
        ArrayList<Date> dates = new ArrayList();
        for (Product p : products) {
            if (p.equals(product)) {
                dates.add(p.getDate());
            }
        }
        Collections.sort(dates);
        Date earliestDate;
        if (dates.isEmpty()) {
            earliestDate = product.getDate();
        } else {
            earliestDate = dates.get(0);
        }

        if (product.getDate() != earliestDate) {
            return false;
        }
        return true;
    }

    private boolean validDescription(Product product) {
        if (product.getProdDesc() == null || product.getProdDesc().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns an iterator of the Items associated to an product.
     *
     * Pre condition: the product us not null the product is contained in the
     * map as a key
     *
     * @param product
     *
     * @return Iterator<Item>
     */
    public Iterator<Item> getItems(Product product) {
        assert product != null;
        assert productToItems.containsKey(product);

        List<Item> result = productToItems.get(product);

        return result.iterator();
    }

    /**
     * Returns the Product associated to a product barcode.
     *
     * Pre condition: the barcode is not null.
     *
     * @param barcode
     *
     * @return Product
     */
    public Product getProduct(Barcode barcode) {
        assert barcode != null;

        Product result = barcodeToProduct.get(barcode);

        return result;
    }

    /**
     * Returns an iterator over the product containers the product is in
     *
     * pre condition: the product is not null the map contains the product as a
     * key
     *
     * @param product
     *
     * @return Iterator<ProductContianer>
     */
    public Iterator<ProductContainer> getContainers(Product product) {
        assert product != null;
        assert productToContainers.containsKey(product);

        List<ProductContainer> result = productToContainers.get(product);
        if (result != null) {
            return result.iterator();
        } else {
            return null;
        }
    }

    /**
     * Add the item to the given product. If the product already exists, then
     * the item is added to the existing items list Else, the product and item
     * are added as a new key in the map.
     *
     * Pre condition: the product and item are not null
     *
     * Post condition: the product is mapped to the item.
     *
     * @param product
     * @param item
     */
    public void addProductItemPair(Product product, Item item) {
        assert product != null;
        assert item != null;

        if (productToItems.containsKey(product)) {
            List<Item> oldItems = productToItems.get(product);
            if (!oldItems.contains(item)) {
                oldItems.add(item);
                productToItems.remove(product);
                productToItems.put(product, oldItems);
            }
        } else {
            List<Item> items = new ArrayList();
            items.add(item);
            productToItems.put(product, items);
        }
    }

    /**
     * Remove the given item from the product.
     *
     * Pre condition: the product and item are not null the product exists
     *
     * Post Condition The Item is removed from the product
     *
     * @param product
     * @param item
     */
    public boolean removeProductToItem(Product product, Item item) {
        assert product != null;
        assert item != null;
        assert canRemoveProductToItem(product, item) == true;
        assert productToItems.containsKey(product);

//        ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
//        IItemDAO itemDAO = PersistenceManager.getInstance().getItemDAO();
//        itemDTO.setProductID(product.getID());
//        if(itemDAO.updateItem(itemDTO)) {
            List<Item> currItems = productToItems.get(product);
            Iterator<Item> items = currItems.iterator();
            while (items.hasNext()) {
                Item cur = items.next();
                if (cur.equals(item)) {
                    item = cur;
                }
            }
            productToItems.get(product).remove(item);
            return true;
        
    }

    /**
     * Check to see if the Item can be removed This method would fail on one of
     * these conditions 1.) The Item or product is null 2.) The product does not
     * exist in the map.
     *
     * @param product
     * @param item
     *
     * @return true if the item can be removed, false otherwise
     */
    public boolean canRemoveProductToItem(Product product, Item item) {
        assert product != null;
        assert productToItems.containsKey(product) == true;

        if (product == null) {
            return false;
        }
        if (!productToItems.containsKey(product)) {
            return false;
        }
        if (productToItems.get(product).isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Add a Barcode Product Pair
     *
     * @param barcode
     * @param barcode
     */
    private void addBarcodeProductPair(Barcode barcode, Product product) {
        assert barcode != null;
        assert product != null;
        assert canAddBarcodeProductPair(barcode, product) == true;

        barcodeToProduct.put(barcode, product);
    }

    /**
     * Updates a product to have new values
     *
     * @param oldProduct	the product that is to be changed
     * @param newProduct	a product that holds the values that will be copies
     * into the actual one
     */
    public boolean updateProduct(Product oldProduct, Product newProduct) {
        newProduct.setID(oldProduct.getID());

        ProductDTO productDTO = DTOConverter.productToProductDTO(newProduct);
        IProductDAO productDAO = PersistenceManager.getInstance().getProductDAO();
        if (productDAO.updateProduct(productDTO)) {
            Product product = this.barcodeToProduct.remove(oldProduct.getBarcode());

            product.setSize(newProduct.getSize());
            product.setMonthSupply(newProduct.getMonthSupply());
            product.setProdDesc(newProduct.getProdDesc());
            product.setShelfLife(newProduct.getShelfLife());

            this.barcodeToProduct.put(product.getBarcode(), product);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Check if the Barcode Product pair can be added.
     *
     * @param barcode
     * @param product
     *
     * @return true if the barcode can be added to the product
     */
    public boolean canAddBarcodeProductPair(Barcode barcode, Product product) {
        assert barcode != null;
        assert product != null;
        assert barcodeToProduct.containsKey(barcode) == false;

        if (barcode == null) {
            return false;
        }
        if (product == null) {
            return false;
        }
        if (barcodeToProduct.containsKey(barcode)) {
            return false;
        }

        return true;
    }

    /**
     * Add the Product to ProductContainer pair
     *
     * @param product
     * @param productContainer
     */
    public boolean addProductContainerPair(Product product, ProductContainer productContainer) {
        assert canAddProductContainerPair(product, productContainer);

        List<ProductContainer> containers = new ArrayList();
        containers.add(productContainer);

        if (productToContainers.containsKey(product)) {
            List<ProductContainer> oldContainers = productToContainers.get(product);
//            MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(new Mapping(productContainer, product));
//            IContainerDAO containerDAO = PersistenceManager.getInstance().getContainerDAO();
//            if (containerDAO.createContainerProductMapping(mappingDTO)) {
                // the following lines are to make sure that there are new duplicates
                oldContainers.removeAll(containers);
                oldContainers.addAll(containers);
                productToContainers.remove(product);
                productToContainers.put(product, containers);
                return true;
//            } else {
//                return false;
//            }
        } else {
//            MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(new Mapping(productContainer, product));
//            IContainerDAO containerDAO = PersistenceManager.getInstance().getContainerDAO();
//            if (containerDAO.createContainerProductMapping(mappingDTO)) {
                productToContainers.put(product, containers);
                return true;
//            } else {
//                return false;
//            }
        }
    }

    /**
     * Check to see if the Product-ProductContainer pair can be added
     *
     * @param product
     * @param productContainer
     *
     * @return true if the Product can be added to the Container
     */
    public boolean canAddProductContainerPair(Product product, ProductContainer productContainer) {
        assert product != null;
        assert productContainer != null;

        if (product == null) {
            return false;
        }
        if (productContainer == null) {
            return false;
        }

        return true;
    }

    /**
     * Remove the given product from the Map
     *
     * @param product
     * @param container
     */
    public boolean removeProductToContainerPair(Product product, ProductContainer container) {
        //change this to accept ProductContainer and remove just the one container.
        assert product != null;
        assert container != null;
        assert canRemoveProductToContainerPair(product) == true;

        boolean result = false;


        if (productToContainers.containsKey(product)) {
            List<ProductContainer> oldContainers = productToContainers.get(product);
            MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(new Mapping(container, product));
            IContainerDAO containerDAO = PersistenceManager.getInstance().getContainerDAO();
            if (containerDAO.deleteContainerProductMapping(mappingDTO)) {
                oldContainers.remove(container);
                productToContainers.remove(product);
                productToContainers.put(product, oldContainers);
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * Check to see if the given product can be removed from the Container
     *
     * @param product
     *
     * @return true if the Product can removed from the container
     */
    public boolean canRemoveProductToContainerPair(Product product) {
        assert product != null;
        assert productToContainers.containsKey(product);

        if (product == null) {
            return false;
        }
        if (!productToContainers.containsKey(product)) {
            return false;
        }

        return true;
    }

    /**
     * checks to see if there are any items of the given product in the system
     *
     * @param product	the product we are looking at
     * @return	true if there is at least one item of this product in the system
     */
    public boolean hasItems(Product product) {
        assert product != null;
        assert productToItems.containsKey(product);
        assert productToItems.get(product) != null;

        if (productToItems.get(product).isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param product
     * @throws CannotBeRemovedException
     */
    public boolean removeEntirely(Product product) throws CannotBeRemovedException {
        assert (canRemove(product, null));
        if (!canRemove(product, null)) {
            throw new CannotBeRemovedException();
        }

        ProductDTO productDTO = DTOConverter.productToProductDTO(product);
        IProductDAO productDAO = PersistenceManager.getInstance().getProductDAO();
        if (productDAO.removeProduct(productDTO)) {
            products.remove(product);
            barcodeToProduct.remove(product.getBarcode());
            productToContainers.remove(product);
            productToItems.remove(product);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if the given product can be removed from the given
     * container
     *
     * @param product	the product that we may wish to remove
     * @param container	the container that we will remove it from
     * @return	true if the product can be removed from that container
     */
    public boolean canRemove(Product product, ProductContainer container) {
        assert product != null;
        List<Item> items = this.productToItems.get(product);
        if (container != null) {
            for (Item item : items) {
                if (item.getProductContainer() == container) {
                    return false;
                }
            }
        } else {
            return !hasItems(product);
        }
        return true;
    }

    /**
     * gets all the products in the system
     *
     * @return	an iterator with all the products in the system
     */
    public Iterator<Product> getProducts() {
        return products.iterator();
    }

    /**
     * gets the number of items of the product in the system
     *
     * @param product	the product of interest
     * @return	the numbers of items of this product in the system
     */
    public int getItemCount(Product product) {
        return productToItems.get(product).size();
    }

    public void accept(SupplyVisitor visitor) {
        //throw new UnsupportedOperationException("Not yet implemented");

        ArrayList<Product> sortedProducts = new ArrayList<Product>();

        for (Product p : products) {
            sortedProducts.add(p);
        }

        Collections.sort(sortedProducts);

        for (Product p : sortedProducts) {
            p.accept(visitor);
        }

    }

    public Product findProduct(int id) {
        Iterator<Product> allProducts = products.iterator();
        while (allProducts.hasNext()) {
            Product cur = allProducts.next();
            if (cur.getID() == id) {
                return cur;
            }
        }

        return null;
    }
}
