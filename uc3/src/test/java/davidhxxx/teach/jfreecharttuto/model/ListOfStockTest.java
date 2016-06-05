package davidhxxx.teach.jfreecharttuto.model;

import java.util.List;

import org.junit.Test;

import davidhxxx.teach.jfreecharttuto.fixture.test.FixtureCreation;
import junit.framework.Assert;

public class ListOfStockTest {

    @Test
    public void getStocksOrderByNameAsc_when_first_call_creates_and_returns_unmodifiable_stock_list() throws Exception {

	ListOfStock listOfStock = new ListOfStock(FixtureCreation.createStockByIsinForListOne());
	// action
	List<Stock> actualStocks = listOfStock.getStocksOrderByNameAsc();
	// assertion
	List<Stock> expectedStocks = FixtureCreation.createStockInListOneOrderedByNameAsc();
	Assert.assertEquals(expectedStocks, actualStocks);

	try {
	    actualStocks.remove(0);	    
	}
	catch (java.lang.UnsupportedOperationException e) {
	    return;
	}
	Assert.fail("UnsupportedOperationException expected");
    }

    @Test
    public void getStocksOrderByNameAsc_after_first_call_returns_existing_unmodifiable_stock_list() throws Exception {
	ListOfStock listOfStock = new ListOfStock(FixtureCreation.createStockByIsinForListOne());
	// action
	List<Stock> actualStocks = listOfStock.getStocksOrderByNameAsc();
	List<Stock> actualStocksSecondCall = listOfStock.getStocksOrderByNameAsc();
	// assertion
	Assert.assertSame(actualStocks, actualStocksSecondCall);
    }

}
