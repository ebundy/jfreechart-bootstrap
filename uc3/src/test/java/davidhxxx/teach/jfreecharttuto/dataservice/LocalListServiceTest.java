package davidhxxx.teach.jfreecharttuto.dataservice;

import java.util.Arrays;
import java.util.List;

<<<<<<< HEAD
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public class LocalListServiceTest {

    @Test
    public void getNamesOfLists_returns_list_ordered_by_name() throws Exception {
	List<String> namesOfLists = LocalListService.getInstance().getNamesOfLists();
	List<String> expectedNamesOfLists = Arrays.asList("liste1", "liste2");
	ReflectionAssert.assertReflectionEquals(expectedNamesOfLists, namesOfLists, ReflectionComparatorMode.LENIENT_ORDER);
    }
=======
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import davidhxxx.teach.jfreecharttuto.exception.TechnicalException;
import davidhxxx.teach.jfreecharttuto.fixture.test.FixtureCreation;
import davidhxxx.teach.jfreecharttuto.model.ListOfStockList;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import junit.framework.Assert;

/**
 * Must be considered as a integration test since the under-test class  relies on intermediary layers but also not mocked data (csv files in src/test/resources)
 * @author davidou
 *
 */
public class LocalListServiceTest {

//    @Mock
//    private ListOfStockList listOfStockListMock;

    @Before
    public void before() {
//	MockitoAnnotations.initMocks(this);
	ReflectionTestUtils.setField(LocalListService.getInstance(), "instance", null);
    }

    @Test
    public void getNamesOfLists_returns_name_of_lists() throws Exception {
	List<String> namesOfLists = LocalListService.getInstance().getNamesOfLists();
	List<String> expectedNamesOfLists = Arrays.asList("liste1", "liste2");
	ReflectionAssert.assertReflectionEquals(expectedNamesOfLists, namesOfLists, ReflectionComparatorMode.LENIENT_ORDER);
    }

    @Test
    public void loadStocks_save_in_cache_and_returns_stocks_ordered_by_stock_name() throws Exception {
	// fixture
	final String listName = "liste1";
	// action
	List<Stock> actualStocks = LocalListService.getInstance().loadStocks(listName);
	// assertion returned stocks
	List<Stock> expectedStocks = FixtureCreation.createStockInListOneOrderedByNameAsc();
	Assert.assertEquals(5, actualStocks.size());
	Assert.assertEquals(expectedStocks, actualStocks);
	// assertion cached stocks
	ListOfStockList actualListOfStockList = (ListOfStockList) ReflectionTestUtils.getField(LocalListService.getInstance(), "listOfStockList");
	Assert.assertTrue(actualListOfStockList.isStocksLoaded(listName));
	Assert.assertEquals(expectedStocks, actualListOfStockList.getStocksOrderedByNameAsc(listName));
    }

    @Test
    public void loadStocks_already_in_cache_return_stocks_from_cache() throws Exception {
	final String listName = "liste1";

	// ReflectionTestUtils.setField(LocalListService.getInstance(), "listOfStockList", listOfStockListMock);
	// Mockito.when(listOfStockListMock.isLoaded(listName)).thenReturn(true);
//	List<Stock> stocksByMock = new ArrayList<>();
	// Mockito.when(listOfStockListMock.getStocksOrderedByStockName(listName)).thenReturn(stocksByMock);
	// Mockito.verify(listOfStockListMock, Mockito.never()).feedStockList(listName, Mockito.eq(Mockito.anyMap()));
	// Mockito.verifyNoMoreInteractions(listOfStockListMock);
	List<Stock> actualStocks = LocalListService.getInstance().loadStocks(listName);
	// action
	List<Stock> actualStocksSecondCall = LocalListService.getInstance().loadStocks(listName);
	// assertion
	Assert.assertSame(actualStocks, actualStocksSecondCall);
    }

    @Test(expected = TechnicalException.class)
    public void loadStocks_with_listname_not_existing_throws_exception() throws Exception {
	LocalListService.getInstance().loadStocks("liste100");
    }

 
>>>>>>> refs/remotes/origin/uc3-branch

}
