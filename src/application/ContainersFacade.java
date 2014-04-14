/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.item.Item;
import application.item.ItemsManager;
import application.product.Product;
import application.product.ProductsManager;
import application.storage.ProductContainer;
import application.storage.RemovedItemsManager;
import application.storage.StorageUnit;
import application.storage.StorageUnitsManager;
import common.DTOConverter;
import common.Notification;
import common.NotificationType;
import common.Size;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidContainerException;
import dataPersistence.ContainerDTO;
import dataPersistence.IContainerDAO;
import dataPersistence.PersistenceManager;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author owner
 */
public class ContainersFacade extends Observable {

    private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();
    private PersistenceManager persistenceManager = PersistenceManager.getInstance();
    private static ContainersFacade instance = null;

    /**
     * Gets the instance of this singleton class
     *
     * @return	the one and only instance of this class.
     */
    public static ContainersFacade getInstance() {
        if (instance == null) {
            instance = new ContainersFacade();
        }
        return instance;
    }

    private ContainersFacade() {
    }

    /**
     * checks to see if a productContainer can be added
     *
     * @param newContainer
     * @param parentContainer
     * @return
     */
    public boolean canAddProductContainer(ProductContainer newContainer, 
			ProductContainer parentContainer) {
        return storageUnits.canBeAdded(newContainer, parentContainer);
    }

    /**
     * checks to if a container can be removed
     *
     * @param container
     * @return
     */
    public boolean canRemoveProductContainer(ProductContainer container) {
        return storageUnits.canBeRemoved(container);
    }

    /**
     * Checks to see if the given container can be edited
     *
     * @param container	the container we wish to edit
     * @return	true if it can be edited, false if not.
     */
    public boolean canEditProductContainer(ProductContainer container, String newName) {
        return storageUnits.canEditProductContainer(container, newName);
    }

    /**
     * adds a product container
     *
     * @param container
     * @param parent
     * @throws InvalidAddException
     * @throws InvalidContainerException
     */
    public void addProductContainer(ProductContainer container, ProductContainer parent)
            throws InvalidAddException, InvalidContainerException {
        assert (container != null);
        boolean result = true;
        this.persistenceManager.startTransaction();
        ContainerDTO containerDTO = DTOConverter.containerToContainerDTO(container);
        IContainerDAO contDAO = this.persistenceManager.getContainerDAO();

        contDAO.createContainer(containerDTO);
        container.setID(containerDTO.getID());

        storageUnits.addProductContainer(container, parent);
        Notification notification = new Notification(NotificationType.ContainerAdded);
        notification.setContainerInvolved(container);
        setChanged();
        notifyObservers(notification);
        
        this.persistenceManager.finishTransaction(result);
    }

    /**
     * removes a productContainer
     *
     * @param container
     */
    public void removeProductContainer(ProductContainer container) {
        boolean result = true;
        this.persistenceManager.startTransaction();
        ContainerDTO containerDTO = DTOConverter.containerToContainerDTO(container);
        IContainerDAO contDAO = this.persistenceManager.getContainerDAO();

        try {
            contDAO.removeContainer(containerDTO);
            storageUnits.removeContainer(container);
        } catch (CannotBeRemovedException ex) {
            result = false;
        }
        Notification notification = new Notification(NotificationType.ContainerRemoved);
        notification.setContainerInvolved(container);
        setChanged();
        notifyObservers(notification);
        
        this.persistenceManager.finishTransaction(result);
    }

    /**
     * Changes the name of the given container to that of the given newName
     *
     * @param container	the container that is being edited
     * @param newName	the new name of the container
     */
    public void editProductContainer(ProductContainer container, String newName) {

        boolean result = true;
        this.persistenceManager.startTransaction();
        ContainerDTO containerDTO = DTOConverter.containerToContainerDTO(container);
        containerDTO.setName(newName);
        IContainerDAO contDAO = this.persistenceManager.getContainerDAO();

        contDAO.updateContainer(containerDTO);

        container.setName(newName);

        Notification notification = new Notification(NotificationType.ContainerChanged);
        notification.setContainerInvolved(container);
        setChanged();
        notifyObservers(notification);

        this.persistenceManager.finishTransaction(result);
    }

    public boolean storageUnitHasProduct(StorageUnit su, Product product) {
        return storageUnits.getContainerThatHasProduct(su, product) != null;
    }

    public ProductContainer getContainerThatHasProduct(StorageUnit su, Product product) {
        return storageUnits.getContainerThatHasProduct(su, product);
    }

    /**
     * returns all the children of the given container
     *
     * @param container
     * @return
     */
    public Iterator<ProductContainer> getSubContainers(ProductContainer container) {
        return storageUnits.getSubContainers(container);
    }

    /**
     * Gets the set of products that are in the given container
     *
     * @param productContainer
     * @return
     */
    public Iterator<Product> getProducts(ProductContainer productContainer) {
        return storageUnits.getProducts(productContainer);
    }

    /**
     * gets the items of the given product within a container
     *
     * @param container	the container holding the items
     * @param product	the product that you want all the items of
     * @return	an iterator of all the items
     */
    public Iterator<Item> getItemsOfProduct(ProductContainer container, Product product) {
        return storageUnits.getItemsOfProduct(container, product);
    }

    /**
     * Gets the storage unit that contains the given product container
     *
     * @param container	the container that we want its storage unit
     * @return	the storage unit that contains the given container
     */
    public ProductContainer getStorageUnit(ProductContainer container) {
        return storageUnits.getStorageUnit(container);
    }

    /**
     * checks to see if the given container is a storageUnit
     *
     * @param container	the container to be checked
     * @return	true if it is a storageunit, false if not
     */
    public boolean isStorageUnit(ProductContainer container) {
        return container.getParent().getName().equals("rootUnit");
    }

    /**
     * gets the three month supply of a container
     *
     * @param container	the container that we want the three month supply of
     * @return	the three month supply of the given container
     */
    public Size getThreeMonthSupply(ProductContainer container) {
        return storageUnits.getThreeMonthSupply(container);
    }

    /**
     * gets the root of all the containers
     *
     * @return	the root of all the containers
     */
    public ProductContainer getRoot() {
        return storageUnits.getRoot();
    }

    /**
     * counts up the items of the given product in the given container
     *
     * @param container	the container to look in
     * @param product	the product of items we want
     * @return	the number of items in the container
     */
    public int getItemCountInContainer(ProductContainer container, Product product) {
        return storageUnits.getItemCountInContainer(container, product);
    }

    public void reset() {
        storageUnits = StorageUnitsManager.getInstance();
        persistenceManager = PersistenceManager.getInstance();
    }
    /**
     * changes the three month supply of the given container
     *
     * @param container	the container that needs a new supply
     * @param size	the new supply
     */
//	public void setThreeMonthSupply(ProductContainer container, Size size)
//	{
//		storageUnits.setThreeMonthSupply(container, size);
//	}
//    private void addItemToContainer(Item item, ProductContainer container) 
//			throws InvalidAddException, InvalidItemException {
////        items.updateItemProductContainer(item, container);
//        storageUnits.addItem(container, item);
//    }
//    private void removeItemFromContainer(Item item, ProductContainer container) 
//			throws InvalidItemException {
//        items.remove(item);
//        storageUnits.removeItem(container, item);
//    }
//    private void addProductToContainer(Product product, ProductContainer container) 
//			throws InvalidProductException {
//        Iterator<ProductContainer> currentContainers = products.getContainers(product);
//        boolean canAddProdToContainer = true;
//        while (currentContainers.hasNext()) {
//            ProductContainer p = currentContainers.next();
//            if (p.getParent() != null) {
//                if (storageUnits.getStorageUnit(p) == storageUnits.getStorageUnit(container)) {
//                    canAddProdToContainer = false;
//                }
//
//            }
//        }
//        //if(!p.isStorageUnit()){
//        //  if(p.getRootStorageUnit() == container.getRootStorageUnit()) throw error or something
//        //  else proceed normally
//        if (canAddProdToContainer) {
//            products.addProductContainerPair(product, container);
//            storageUnits.addProduct(container, product);
//        }
//    }
//    private void removeProductFromContainer(Product product, ProductContainer container) 
//			throws InvalidProductException, CannotBeRemovedException {
//        products.removeProductToContainerPair(product, container);
//        storageUnits.removeProduct(container, product);
//    }
}
