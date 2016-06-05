package davidhxxx.teach.jfreecharttuto.model;

<<<<<<< HEAD
import java.util.List;

public class ListOfStock {

    private List<Stock> stocks;
=======
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListOfStock {

    private Map<String, Stock> stocksByIsin;
    private List<Stock> stocksSorted;

    public ListOfStock(Map<String, Stock> stocksByIsin) {
	this.stocksByIsin = stocksByIsin;
    }

    public List<Stock> getStocksOrderByNameAsc() {

	if (stocksSorted == null) {
	    stocksSorted = new ArrayList<Stock>(stocksByIsin.values());
	    Collections.sort(stocksSorted , new Stock.StockComparatorAlphaDesc());
	    stocksSorted = Collections.unmodifiableList(stocksSorted);
	}
	return stocksSorted;
    }
>>>>>>> refs/remotes/origin/uc3-branch

}
