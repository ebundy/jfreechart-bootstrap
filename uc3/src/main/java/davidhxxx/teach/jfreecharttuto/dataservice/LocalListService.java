package davidhxxx.teach.jfreecharttuto.dataservice;

import java.io.File;
import java.io.FilenameFilter;

import java.util.List;

import davidhxxx.teach.jfreecharttuto.model.ListOfStockList;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;

public class LocalListService {

    private static LocalListService instance;

    private File listsPath;

    private ListOfStockList listOfStockList;

    private LocalListService() {
	setListsPath(new File(ApplicationDirs.BASE_DIR + "/list"));
	loadListOfStockListNames();
    }

    public static LocalListService getInstance() {
	if (instance == null)
	    instance = new LocalListService();

	return instance;
    }

    public List<String> getNamesOfLists() {
	return listOfStockList.getNames();
    }

    private void loadListOfStockListNames() {
	listOfStockList = new ListOfStockList();

	File[] files = listsPath.listFiles(new FilenameFilter() {

	    @Override
	    public boolean accept(File dir, String name) {
		if (name.contains(".csv"))
		    return true;

		return false;
	    }
	});
	listOfStockList.addListNames(files);

    }

    private void setListsPath(File path) {
	this.listsPath = path;
	listsPath.mkdirs();
    }

}
