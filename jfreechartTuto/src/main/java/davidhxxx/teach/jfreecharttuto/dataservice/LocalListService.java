package davidhxxx.teach.jfreecharttuto.dataservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import davidhxxx.teach.jfreecharttuto.model.ListOfStockList;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;

public class LocalListService {

    public enum ListPredefined {
	SBF120_STOCKS("CAC40", "FR0003999481", Locale.FRENCH);

	private final String fileName;
	private final String isinIndice;
	private final Locale locale;

	ListPredefined(String fileName, String isinIndice, Locale locale) {
	    this.fileName = fileName;
	    this.isinIndice = isinIndice;
	    this.locale = locale;
	}

	public String getFileName() {
	    return fileName;
	}

	public String getIsinIndice() {
	    return isinIndice;
	}

	public static ListPredefined findByListName(String listFileName) {
	    for (ListPredefined currentLP : values()) {
		if (currentLP.fileName.equalsIgnoreCase(listFileName)) {
		    return currentLP;
		}
	    }
	    return null;
	}

	public boolean isFrench() {
	    return locale == Locale.FRENCH;
	}

	public boolean isUs() {
	    return locale == Locale.US;
	}

	public boolean isUk() {
	    return locale == Locale.UK;
	}

    }

    public static final String NOT_UPDATABLE_LIST = "not-updatable";

    private static final CsvPreference MY_CVS_PREFERENCE = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;

    private static Logger LOGGER = LoggerFactory.getLogger(LocalListService.class);

    private static LocalListService instance;

    private File listsPath;

    private ListOfStockList listOfStockList;

    private String[] headersAvecTicker = new String[] { "isin", "name", "ticker" };


    private LocalListService() {
	setListsPath(new File(ApplicationDirs.BASE_DIR + "/list"));
	loadListOfStockListNames();
    }

    private void setListsPath(File path) {
	this.listsPath = path;
	listsPath.mkdirs();
    }
  
    public static LocalListService getInstance() {
	if (instance == null)
	    instance = new LocalListService();

	return instance;
    }

    public List<String> getNamesOfLists() {
	return listOfStockList.getNames();
    }

    public Stock findStock(String listName, String isin) {
  	if (!listOfStockList.isLoaded(listName))
  	    loadStockList(listName);

  	return listOfStockList.getStock(listName, isin);
      }

      public Stock findStock(String isin) {
  	
  	for (String name : listOfStockList.getNames()) {
  	    Stock stock = findStock(name, isin);

  	    if (stock != null) {
  		return stock;
  	    }
  	}
  	return null;
      }
      
    public List<Stock> loadStockList(String listName) {
	return loadStockListCommon(listName, false);
    }
    
    

    private List<Stock> loadStockListCommon(String listName, boolean isNotListSearch) {

	if (listOfStockList.isLoaded(listName)) {
	    return listOfStockList.getStocks(listName);
	}

	synchronized (listOfStockList) {
	    Map<String, Stock> stocksByIsin = new HashMap<>();

	    ICsvMapReader mapReader = null;
	    try {
		File pathToUse = listsPath;
		if (isNotListSearch) {
		    pathToUse = new File(ApplicationDirs.BASE_DIR);
		}

		mapReader = new CsvMapReader(new FileReader(pathToUse + "/" + listName + ".csv"), MY_CVS_PREFERENCE);
		mapReader.getHeader(true);
	    }
	    catch (FileNotFoundException e) {
		LOGGER.error("Exception lors de loadClosedDayDuringYear()", e);
		throw new IllegalArgumentException(e);
	    }
	    catch (IOException e) {
		LOGGER.error("Exception lors de loadClosedDayDuringYear()", e);
		throw new IllegalArgumentException(e);
	    }

	    // the header columns are used as the keys to the Map
	    // final String[] headers = { "isin", "name", "ticker" };

	    String[] headersList = null;
	    CellProcessor[] processors = null;

	    ListPredefined listPredefined = ListPredefined.findByListName(listName);
	    if (listPredefined == null) {
		// throw new IllegalArgumentException("cas non prévu");
		listPredefined = ListPredefined.SBF120_STOCKS;
	    }

	    headersList = headersAvecTicker;
	    processors = getStockListProcessorsWithTicker();

	    try {

		// header : isin,date,open,high,low,close,volume
		Map<String, Object> stockMap;
		while ((stockMap = mapReader.read(headersList, processors)) != null) {

		    String isin = ((String) stockMap.get("isin"));
		    String name = ((String) stockMap.get("name"));

		    String ticker = null;
		    String locale = null;

		    if (mapReader.length() == 3) {
			ticker = ((String) stockMap.get("ticker"));
		    }

		    else if (mapReader.length() == 4) {
			ticker = ((String) stockMap.get("ticker"));
			locale = ((String) stockMap.get("locale"));
		    }

		    Stock stock = new Stock(isin, name, ticker);

		    stocksByIsin.put(isin, stock);
		}

		LOGGER.info("stocksByIsin=" + stocksByIsin);

	    }
	    catch (IOException e) {
		LOGGER.error("Exception lors de loadLastUpdateStocks()", e);
		throw new IllegalArgumentException(e);

	    }

	    listOfStockList.createList(listName, stocksByIsin);
	}

	return listOfStockList.getStocks(listName);
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

  

}
