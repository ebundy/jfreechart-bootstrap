package davidhxxx.teach.jfreecharttuto.model;

import static org.junit.Assert.*;

import java.io.File;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import davidhxxx.teach.jfreecharttuto.fixture.test.FixtureCreation;
import junit.framework.Assert;

public class ListOfStockListTest {

    /*
     * tested :
     * - addListNames()
     * - getNames()
     * - isStocksLoaded() -> false case
     */
    @Test
    public void addListNames_add_files_less_extension_without_adding_stocks_AND_getNames_return_them() throws Exception {
	ListOfStockList listOfStockList = new ListOfStockList();
	File[] fileNamesWithExtension = new File[2];
	fileNamesWithExtension[0] = new File("listFake1.csv");
	fileNamesWithExtension[1] = new File("listFake2.csv");
	// action
	listOfStockList.addListNames(fileNamesWithExtension);
	// assertion
	Assert.assertFalse(listOfStockList.isStocksLoaded("listFake1"));
	Assert.assertFalse(listOfStockList.isStocksLoaded("listFake2"));
	List<String> actualNames = listOfStockList.getNames();
	List<String> expectedNames = Arrays.asList("listFake1", "listFake2");
	ReflectionAssert.assertReflectionEquals(expectedNames, actualNames, ReflectionComparatorMode.LENIENT_ORDER);
    }

    /*
     * * tested :
     * - addStocks()
     * - getStocksOrderedByNameAsc()
     * - isStocksLoaded() -> true case
     */
    @Test
    public void addStocks_with_listname_and_stocks_adds_stocks_AND_getStocksOrderedByNameAsc_return_them() throws Exception {
	ListOfStockList listOfStockList = new ListOfStockList();

	Map<String, Stock> stocksByIsin = FixtureCreation.createStockByIsinForListOne();
	final String listName = "liste1";
	// action
	listOfStockList.addStocks(listName, stocksByIsin);
	// assertion
	List<Stock> expectedStockOrdered = FixtureCreation.createStockInListOneOrderedByNameAsc();
	Assert.assertEquals(expectedStockOrdered, listOfStockList.getStocksOrderedByNameAsc(listName));
	Assert.assertTrue(listOfStockList.isStocksLoaded(listName));
    }

}
