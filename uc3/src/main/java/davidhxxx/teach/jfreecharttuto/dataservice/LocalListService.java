package davidhxxx.teach.jfreecharttuto.dataservice;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import davidhxxx.teach.jfreecharttuto.exception.TechnicalException;
import davidhxxx.teach.jfreecharttuto.model.ListOfStockList;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;

public class LocalListService {

    private static final CsvPreference MY_CVS_PREFERENCE = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalListService.class);

    private static final String[] csvListHeaders = new String[] { "isin", "name", "ticker" };

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

    public List<Stock> loadStocks(String listName) {

	if (listOfStockList.isStocksLoaded(listName)) {
	    return listOfStockList.getStocksOrderedByNameAsc(listName);
	}

	Map<String, Stock> stocksByIsin = new HashMap<>();

	ICsvMapReader mapReader = null;

	try {
	    File pathToUse = listsPath;

	    mapReader = new CsvMapReader(new FileReader(pathToUse + "/" + listName + ".csv"), MY_CVS_PREFERENCE);
	    mapReader.getHeader(true);

	    String[] headersList = null;
	    CellProcessor[] processors = null;

	    headersList = csvListHeaders;
	    processors = getStockListProcessorsWithTicker();

	    Map<String, Object> stockMap;
	    while ((stockMap = mapReader.read(headersList, processors)) != null) {

		String isin = ((String) stockMap.get("isin"));
		String name = ((String) stockMap.get("name"));
		String ticker = ((String) stockMap.get("ticker"));

		Stock stock = new Stock(isin, name, ticker);

		stocksByIsin.put(isin, stock);
	    }

	    LOGGER.info("stocksByIsin=" + stocksByIsin);
	    listOfStockList.addStocks(listName, stocksByIsin);

	    return listOfStockList.getStocksOrderedByNameAsc(listName);

	}
	catch (IOException e) {
	    LOGGER.error("Exception lors de loadLastUpdateStocks()", e);
	    throw new TechnicalException(e);

	}
	finally {
	    try {
		if (mapReader != null)
		    mapReader.close();
	    }
	    catch (IOException e) {
		throw new TechnicalException(e);
	    }

	}

    }

    private CellProcessor[] getStockListProcessorsWithTicker() {
	final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), new NotNull(), new Optional() };
	return processors;
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
