package davidhxxx.teach.jfreecharttuto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import davidhxxx.teach.jfreecharttuto.ui.stockselection.StockSelectorForOneList.StockComparatorAlphaDesc;

public class ListOfStock {

    private Map<String, Stock> stocksByIsin;

    private boolean isLoaded;

    private List<Stock> stocksSorted;

    public ListOfStock(Map<String, Stock> stocksByIsin) {
	this.stocksByIsin = stocksByIsin;
	isLoaded = true;
    }

    public boolean isLoaded() {
	return isLoaded;
    }

    public List<Stock> getStocksOrderByNameDesc() {

	if (stocksSorted == null) {
	    stocksSorted = new ArrayList<Stock>(stocksByIsin.values());
	    Collections.sort(stocksSorted, new StockComparatorAlphaDesc());
	    stocksSorted = Collections.unmodifiableList(stocksSorted);
	}
	return stocksSorted;
    }

    public Stock getStock(String isin) {
	return stocksByIsin.get(isin);
    }

}
