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
	    listOfStockByListName.put(fileNameWithoutExtension, new ListOfStock());
	}
    }

    public List<String> getNames() {
	return new ArrayList<>(listOfStockByListName.keySet());
    }

}
