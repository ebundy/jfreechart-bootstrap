package davidhxxx.teach.jfreecharttuto.model;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfStockList {

    private Map<String, ListOfStock> listOfStockByListName = new HashMap<String, ListOfStock>();

    public void addListNames(File[] filenamesWithExtention) {

	for (File filenameWithExtention : filenamesWithExtention) {
	    String fileNameWithoutExtension = filenameWithExtention.getName().replace(".csv", "");
	    listOfStockByListName.put(fileNameWithoutExtension, null);
	}
    }

    public boolean isLoaded(String listName) {
	ListOfStock listOfStock = listOfStockByListName.get(listName);
	if (listOfStock == null)
	    return false;
	return listOfStock.isLoaded();
    }

    public List<Stock> getStocks(String listName) {
	ListOfStock listOfStock = listOfStockByListName.get(listName);
	return listOfStock.getStocksOrderByNameDesc();
    }

    public void createList(String listName, Map<String, Stock> stocksByIsin) {
	ListOfStock listOfStock = new ListOfStock(stocksByIsin);
	listOfStockByListName.put(listName, listOfStock);
    }

    public Stock getStock(String listName, String isin) {
	return listOfStockByListName.get(listName).getStock(isin);
    }

    public List<String> getNames() {
	return new ArrayList<>(listOfStockByListName.keySet());
    }

}
