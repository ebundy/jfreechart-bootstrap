package davidhxxx.teach.jfreecharttuto.model;

import java.io.File;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public class ListOfStockListTest {

    /*
     * tested:
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
	List<String> actualNames = listOfStockList.getNames();
	List<String> expectedNames = Arrays.asList("listFake1", "listFake2");
	ReflectionAssert.assertReflectionEquals(expectedNames, actualNames, ReflectionComparatorMode.LENIENT_ORDER);
    }

}
