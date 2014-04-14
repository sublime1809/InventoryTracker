/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import application.ContainersFacade;
import application.ItemsFacade;
import application.ProductFacade;
import application.item.Item;
import application.product.Product;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import dataPersistence.ContainerDTO;
import dataPersistence.ItemDTO;
import dataPersistence.ProductDTO;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Stephen Kitto
 */
public class DTOConverterTest {
	
	private static Item item;
	private static Product product;
	private static StorageUnit storageUnit;
	private static ProductGroup productGroup;
	private static ItemDTO itemDTO;
	private static ProductDTO productDTO;
	private static ContainerDTO containerDTO;
	private static ContainersFacade CF = ContainersFacade.getInstance();
	private static ItemsFacade IF = ItemsFacade.getInstance();
	private static ProductFacade PF = ProductFacade.getInstance();
	
	public DTOConverterTest() {
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception{
		storageUnit = new StorageUnit("storageUnit", null);
		storageUnit.setID(1);
		CF.addProductContainer(storageUnit, null);
		productGroup = new ProductGroup("productGroup", storageUnit);
		productGroup.setID(2);
		productGroup.setThreeMonthSupply(new Size(1, SizeUnit.Count));
		CF.addProductContainer(productGroup, storageUnit);
		product = new Product();
		product.setBarcode(new Barcode());
		product.setDate(new Date());
		product.setID(1);
		product.setMonthSupply(new Size(1, SizeUnit.Count));
		product.setProdDesc("product");
		product.setShelfLife(1);
		product.setSize(new Size((float)1.5, SizeUnit.Pounds));
		PF.addProduct(product, storageUnit);
		item = new Item();
		item.setBarcode(new Barcode());
		item.setEntryDate(new Date());
		item.setExitTime(null);
		item.setID(1);
		item.setProduct(product);
		item.setProductContainer(productGroup);
		IF.addItem(item);
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() throws Exception{
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of itemToItemDTO method, of class DTOConverter.
	 */
	@Test
	public void testItemToItemDTO() {
		System.out.println("itemToItemDTO");
		
		ItemDTO testDTO = DTOConverter.itemToItemDTO(item);
		assertEquals(item.getID(), testDTO.getID());
		assertEquals(item.getEntryDate(), testDTO.getEntryDate());
		assertTrue(testDTO.getExitDate() == null);
		assertEquals(item.getProductContainer().getID(), testDTO.getContainerID());
		assertEquals(item.getProduct().getID(), testDTO.getProductID());
		assertEquals(item.getBarcode().toString(), testDTO.getBarcode());
	}

	/**
	 * Test of itemDTOToItem method, of class DTOConverter.
	 */
	@Test
	public void testItemDTOToItem() {
		System.out.println("itemDTOToItem");

		itemDTO = DTOConverter.itemToItemDTO(item);
		Item testItem = DTOConverter.itemDTOToItem(itemDTO);
		assertTrue(item.equals(testItem));
		assertEquals(item.getBarcode(), testItem.getBarcode());
		assertEquals(item.getEntryDate(), testItem.getEntryDate());
		assertEquals(product, testItem.getProduct());
	}

	/**
	 * Test of productToProductDTO method, of class DTOConverter.
	 */
	@Test
	public void testProductToProductDTO() {
		System.out.println("productToProductDTO");

		ProductDTO testDTO = DTOConverter.productToProductDTO(product);
		assertEquals(product.getBarcode().toString(), testDTO.getBarcode());
		assertEquals(product.getProdDesc(), testDTO.getDescription());
		assertEquals(product.getShelfLife(), testDTO.getShelflife());
		assertTrue(product.getSize().getSize() == testDTO.getSizeValue());
		assertEquals(product.getSize().getUnitString(), testDTO.getSizeUnit());
		assertEquals(product.getID(), testDTO.getID());
	}

	/**
	 * Test of productDTOToProduct method, of class DTOConverter.
	 */
	@Test
	public void testProductDTOToProduct() {
		System.out.println("productDTOToProduct");

		productDTO = DTOConverter.productToProductDTO(product);
		Product testProduct = DTOConverter.productDTOToProduct(productDTO);
		assertTrue(testProduct.equals(product));
		assertEquals(testProduct.getProdDesc(), product.getProdDesc());
		assertEquals(testProduct.getID(), product.getID());
		assertEquals(testProduct.getBarcode(), product.getBarcode());
	}

	/**
	 * Test of containerToContainerDTO method, of class DTOConverter.
	 */
	@Test
	public void testContainerToContainerDTO() {
		System.out.println("containerToContainerDTO");

		ContainerDTO testDTO = DTOConverter.containerToContainerDTO(storageUnit);
		assertEquals(storageUnit.getName(), testDTO.getName());
		assertEquals(storageUnit.getID(), testDTO.getID());
		assertTrue(testDTO.getParentID() == 0);
		assertTrue(testDTO.getSupplyUnit() == null);
		assertTrue(testDTO.getSupplyValue() == 0.0);
		
		testDTO = DTOConverter.containerToContainerDTO(productGroup);
		assertEquals(productGroup.getName(), testDTO.getName());
		assertEquals(productGroup.getID(), testDTO.getID());
		assertEquals(productGroup.getParent().getID(), testDTO.getParentID());
		assertTrue(productGroup.getThreeMonthSupply().getSize() == testDTO.getSupplyValue());
		assertEquals(productGroup.getThreeMonthSupply().getUnitString(), testDTO.getSupplyUnit());
	}

	/**
	 * Test of containerDTOToContainer method, of class DTOConverter.
	 */
	@Test
	public void testContainerDTOToContainer() {
		System.out.println("containerDTOToContainer");

		containerDTO = DTOConverter.containerToContainerDTO(storageUnit);
		ProductContainer testContainer = DTOConverter.containerDTOToContainer(containerDTO);
		assertEquals(testContainer.getID(), storageUnit.getID());
		assertEquals(testContainer.getName(), "storageUnit");
		assertTrue(testContainer instanceof StorageUnit);
		assertTrue(testContainer.getParent() == null);
		containerDTO = DTOConverter.containerToContainerDTO(productGroup);
		testContainer = DTOConverter.containerDTOToContainer(containerDTO);
		assertEquals(testContainer.getID(), productGroup.getID());
		assertEquals(testContainer.getName(), "productGroup");
		assertTrue(testContainer instanceof ProductGroup);
		assertTrue(testContainer.getParent() != null);
		assertEquals(testContainer.getParent().getName(), productGroup.getParent().getName());
	}
}
