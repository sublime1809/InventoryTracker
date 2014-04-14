/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import application.item.Item;
import application.item.ItemsManager;
import application.product.Product;
import application.product.ProductsManager;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import application.storage.StorageUnitsManager;
import dataPersistence.ContainerDTO;
import dataPersistence.ItemDTO;
import dataPersistence.MappingDTO;
import dataPersistence.ProductDTO;
import java.util.Date;

/**
 *
 * @author Stephen Kitto
 */
public class DTOConverter {

    private static StorageUnitsManager SUM = StorageUnitsManager.getInstance();
    private static ItemsManager IM = ItemsManager.getInstance();
    private static ProductsManager PM = ProductsManager.getInstance();

    /**
     * creates an ItemDTO based on an item
     *
     * @param item	the item the new DTO should represent
     * @return	the new DTO
     */
    public static ItemDTO itemToItemDTO(Item item) {
        return new ItemDTO(item.getID(), item.getBarcode().toString(),
                item.getEntryDate(), item.getExitTime(), item.getProduct().getID(),
                item.getProductContainer().getID());
    }

    /**
     * creates an Item based on the DTO
     *
     * @param dto	the DTO that represents the item to be made
     * @return	the new item
     */
    public static Item itemDTOToItem(ItemDTO dto) {
        Product product = PM.findProduct(dto.getProductID());
        ProductContainer container = SUM.findContainer(dto.getContainerID());
        Item result = new Item(new Barcode(dto.getBarcode()), dto.getEntryDate(),
                dto.getExitDate(), container, product);
        result.setID(dto.getID());

        return result;
    }

    /**
     * creates a ProductDTO based on a product
     *
     * @param product	the product the new dto should represent
     * @return	the new DTO
     */
    public static ProductDTO productToProductDTO(Product product) {
        return new ProductDTO(product.getID(), product.getProdDesc(),
                product.getBarcode().toString(), product.getShelfLife(),
                product.getSize().getSize(), product.getSize().getUnitString(),
                product.getMonthSupply().getSize());
    }

    /**
     * creates a product based on the DTO
     *
     * @param dto	the DTO that represents the product to be created
     * @return	the new product
     */
    public static Product productDTOToProduct(ProductDTO dto) {
        SizeUnit unit = SizeUnit.parse(dto.getSizeUnit());
        Product result = new Product(new Date(), dto.getBarcode(), dto.getDescription(),
                dto.getShelflife(), dto.getThreeMonthSupply(), new Size(dto.getSizeValue(), unit));
        result.setID(dto.getID());
        return result;
    }

    /**
     * creates a containerDTO based on the given container
     *
     * @param container	the container that the new DTO should represent
     * @return	the new dto
     */
    public static ContainerDTO containerToContainerDTO(ProductContainer container) {
        float supplyValue;
        String supplyUnit;
        int parentID;
        if (container instanceof ProductGroup) {
            supplyValue = ((ProductGroup) container).getThreeMonthSupply().getSize();
            supplyUnit = ((ProductGroup) container).getThreeMonthSupply().getUnitString();
            parentID = container.getParent().getID();
        } else {
            supplyValue = 0;
            supplyUnit = null;
            parentID = 0;
        }
        return new ContainerDTO(container.getID(), container.getName(),
                parentID, supplyUnit, supplyValue);
    }

    /**
     * creates a productContainer based on the given dto
     *
     * @param dto	the dto that represents the container that should be created
     * @return	the new container
     */
    public static ProductContainer containerDTOToContainer(ContainerDTO dto) {
        ProductContainer container;
        if (dto.getSupplyUnit() == null) {
            container = new StorageUnit(dto.getName(), null);
        } else {
            ProductContainer parent = SUM.findContainer(dto.getParentID());
            ProductGroup tempContainer = new ProductGroup(dto.getName(), parent);
            tempContainer.setThreeMonthSupply(new Size(dto.getSupplyValue(),
                    SizeUnit.parse(dto.getSupplyUnit())));
            container = tempContainer;
        }
        container.setID(dto.getID());
        return container;
    }

    /**
     * creates a mappingDTO based on the given mapping
     *
     * @param mapping	the mapping that should be made into dto
     * @return	a new mappingDTO
     */
    public static MappingDTO mappingToMappingDTO(Mapping mapping) {
        return new MappingDTO(mapping.getProduct().getID(), mapping.getContainer().getID());
    }

    /**
     * creates a mapping based on the given dto
     *
     * @param dto	the dto that represents the mapping that should be created
     * @return	the new mapping
     */
    public static Mapping mappingDTOToMapping(MappingDTO dto) {
        ProductContainer container = SUM.findContainer(dto.getContainerID());
        Product product = PM.findProduct(dto.getProductID());
        return new Mapping(container, product);
    }
}
