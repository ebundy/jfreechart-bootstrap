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
<<<<<<< HEAD
	    listOfStockByListName.put(fileNameWithoutExtension, new ListOfStock());
	}
    }

    public List<String> getNames() {
	return new ArrayList<>(listOfStockByListName.keySet());
=======
	    listOfStockByListName.put(fileNameWithoutExtension, null);
	}
    }

    public List<String> getNames() {
	return new ArrayList<>(listOfStockByListName.keySet());
    }

    public boolean isStocksLoaded(String listName) {
	ListOfStock listOfStock = listOfStockByListName.get(listName);
	if (listOfStock == null)
	    return false;
	
	return true;
    }

    public List<Stock> getStocksOrderedByNameAsc(String listName) {
	ListOfStock listOfStock = listOfStockByListName.get(listName);
	return listOfStock.getStocksOrderByNameAsc();
    }
    
    
    public void addStocks(String listName, Map<String, Stock> stocksByIsin) {
	ListOfStock listOfStock = new ListOfStock(stocksByIsin);
	listOfStockByListName.put(listName, listOfStock);
>>>>>>> refs/remotes/origin/uc3-branch
    }

}
