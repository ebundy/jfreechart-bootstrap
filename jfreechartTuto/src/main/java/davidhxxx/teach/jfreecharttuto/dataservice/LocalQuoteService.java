package davidhxxx.teach.jfreecharttuto.dataservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import davidhxxx.teach.jfreecharttuto.model.DateInterval;
import davidhxxx.teach.jfreecharttuto.model.Quotation;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.model.StockLoaded;
import davidhxxx.teach.jfreecharttuto.model.update.QuotationsToImport;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;
import davidhxxx.teach.jfreecharttuto.util.csv.FormatToStringLocalDate;
import davidhxxx.teach.jfreecharttuto.util.csv.ParseFloat;
import davidhxxx.teach.jfreecharttuto.util.csv.ParseLocalDate;

public class LocalQuoteService {

    public static final CsvPreference MY_CVS_PREFERENCE = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;

    private static Logger LOGGER = LoggerFactory.getLogger(LocalQuoteService.class);

    private LocalQuoteService() {
    }

    private static class LazyHolder {
	private static final LocalQuoteService INSTANCE = new LocalQuoteService();
    }

    // custom CSV processor
    private static CellProcessorAdaptor parseLocalDateddMMyyyyProcessor = new ParseLocalDate("dd/MM/yyyy");
    private static CellProcessorAdaptor formatToStringLocalDateddMMyyyyProcessor = new FormatToStringLocalDate("dd/MM/yyyy");

    public static LocalQuoteService getInstance() {
	return LazyHolder.INSTANCE;
    }

    public StockLoaded loadStockForChartPanel(String listName, String isin, DateInterval dateInterval) {
	Stock stock = LocalListService.getInstance().findStock(listName, isin);
	assertIntervalValid(dateInterval, stock.getIsin());
	StockLoaded stockLoaded = createDataSetAndStockDataLoaded(stock, dateInterval);

	return stockLoaded;
    }

    public void saveQuotationForAllStocks(Map<Stock, QuotationsToImport> quotationsByIsin) {
	for (Entry<Stock, QuotationsToImport> entry : quotationsByIsin.entrySet()) {
	    Stock stock = entry.getKey();
	    QuotationsToImport quotations = entry.getValue();
	    saveQuotations(stock.getIsin(), quotations);
	}
    }

    private void saveQuotations(String isin, QuotationsToImport allQuotations) {

	Stock stock = LocalListService.getInstance().findStock(isin);

	try {

	    for (Integer currentYear : allQuotations.getYears()) {

		for (Integer currentMonth : allQuotations.getMonths(currentYear)) {
		    writeQuotations(isin, allQuotations, stock, currentYear, currentMonth);
		}

	    }
	    LOGGER.info("Quotations saved for isin=" + isin);
	}

	catch (Exception e) {
	    String msg = "Error in saveQuotations with isin " + isin;
	    LOGGER.error(msg, e);
	    throw new IllegalArgumentException(msg, e);
	}

    }

    private ICsvMapWriter writeQuotations(String isin, QuotationsToImport allQuotations, Stock stock, Integer currentYear, Integer currentMonth) throws IOException {

	ICsvMapWriter mapWriter = null;
	try {
	    // the header columns are used as the keys to the Map
	    final String[] headers = { "isin", "date", "open", "high", "low", "close", "volume" };
	    final CellProcessor[] processors = getMyFormatQuotationProcessors(false);

	    mapWriter = getMapWriter(stock, currentYear, currentMonth, headers);
	    Map<String, Object> quotationMap = new HashMap<>();

	    Set<Quotation> quotationsForCurrentMonth = allQuotations.getQuotationsForYearAndMonth(currentYear, currentMonth);

	    for (Quotation currentQuotation : quotationsForCurrentMonth) {

		quotationMap.clear();

		final LocalDate currentQuotationDate = currentQuotation.getDate();

		// header : isin,date,open,high,low,close,volume
		quotationMap.put("isin", isin);
		quotationMap.put("date", currentQuotationDate);
		quotationMap.put("open", currentQuotation.getOpen());
		quotationMap.put("high", currentQuotation.getHigh());
		quotationMap.put("low", currentQuotation.getLow());
		quotationMap.put("close", currentQuotation.getClose());
		quotationMap.put("volume", currentQuotation.getVolume());

		mapWriter.write(quotationMap, headers, processors);
	    }

	    mapWriter.close();
	    return mapWriter;
	}
	finally {
	    if (mapWriter != null) {
		try {
		    mapWriter.close();
		}
		catch (IOException e) {
		    LOGGER.warn("erreur lors de la fermeture du fichier ", e);
		}
	    }
	}
    }

    private ICsvMapWriter getMapWriter(Stock stock, Integer year, int month, String[] headers) {

	createIsinYearMonthDirIfNotExist(stock, year);

	String fileName = computeStockQuotationsFile(stock, year, month);

	File destFile = new File(fileName);

	if (destFile.exists()) {
	    destFile.delete();
	}

	ICsvMapWriter mapWriter;
	try {
	    mapWriter = new CsvMapWriter(new FileWriter(fileName, false), ApplicationDirs.getCsvPreference(ApplicationDirs.MY_CVS_PREFERENCE));
	    mapWriter.writeHeader(headers);

	    return mapWriter;
	}
	catch (IOException e) {
	    e.printStackTrace();
	    throw new IllegalArgumentException(e);
	}

    }

    private String createIsinYearMonthDirIfNotExist(Stock stock, Integer year) {
	String path = computeStockQuotationsDir(stock, year);
	File isinDir = new File(path);
	if (!isinDir.exists())
	    isinDir.mkdirs();

	return path;

    }

    private String computeStockQuotationsDir(Stock stock, Integer year) {
	String isin = stock.getIsin();

	File quotationDirBase = ApplicationDirs.QUOTATIONS_DIR;

	return quotationDirBase + "/" + isin + "/" + year;
    }

    public CellProcessor[] getMyFormatQuotationProcessors(boolean isReading) {

	// header : isin;date;open;high;low;close;volume
	CellProcessorAdaptor dateProcessor = getMyDateProcessor(isReading);

	final CellProcessor[] processors = new CellProcessor[] {

		new NotNull(), dateProcessor, new ParseFloat(), new ParseFloat(), new ParseFloat(), new ParseFloat(), new ParseLong() };

	return processors;

    }

    public static CellProcessorAdaptor getMyDateProcessor(boolean isReading) {
	CellProcessorAdaptor dateProcessor = null;

	if (isReading) {
	    dateProcessor = parseLocalDateddMMyyyyProcessor;
	}

	else {
	    dateProcessor = formatToStringLocalDateddMMyyyyProcessor;
	}
	return dateProcessor;
    }

    private StockLoaded createDataSetAndStockDataLoaded(Stock stock, DateInterval dateInterval) {

	List<Quotation> quotationsLoaded = loadQuotations(stock, dateInterval);
	if (quotationsLoaded == null) {
	    return null;
	}

	LocalDate startDateOfList = quotationsLoaded.get(0).getDate();
	LocalDate endDateOfList = quotationsLoaded.get(quotationsLoaded.size() - 1).getDate();

	DateInterval dateIntervalEffective = new DateInterval(startDateOfList, endDateOfList);
	OHLCDataItem[] dataForChartPanel = null;

	dataForChartPanel = mapQuotationsToOHLCDataItem(stock.getIsin(), dateIntervalEffective, quotationsLoaded);

	if (dataForChartPanel == null || dataForChartPanel.length == 0)
	    return null;

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("Period displayable for stock  " + stock + "= " + dateInterval);
	}

	// Create a dataset, an Open, High, Low, Close dataset
	DefaultOHLCDataset dataset = new DefaultOHLCDataset(stock.getIsin(), dataForChartPanel);
	StockLoaded paramStockLoaded = new StockLoaded(stock, dataset, dateIntervalEffective);

	return paramStockLoaded;

    }

    private OHLCDataItem[] mapQuotationsToOHLCDataItem(String isin, DateInterval dateIntervalEffective, List<Quotation> quotationsLoaded) {

	OHLCDataItem[] dataItems = new OHLCDataItem[quotationsLoaded.size()];

	for (int i = 0; i < quotationsLoaded.size(); i++) {

	    Quotation quotation = quotationsLoaded.get(i);
	    OHLCDataItem item = new OHLCDataItem(quotation.getDate().toDate(), quotation.getOpen(), quotation.getHigh(), quotation.getLow(),
		    quotation.getClose(), quotation.getVolume());
	    dataItems[i] = item;
	}

	return dataItems;
    }

    private List<Quotation> loadQuotations(Stock stock, DateInterval period) {

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("entrée avec isin " + stock.getIsin() + ", periode=" + period);
	}

	List<Integer> yearsToLoad = computeYears(period);

	List<Quotation> allQuotations = new ArrayList<>();
	for (int i = 0; i < yearsToLoad.size(); i++) {
	    Integer yearToLoad = yearsToLoad.get(i);

	    List<Quotation> quotationForYear = loadQuotationsFromCsv(stock, yearToLoad);
	    allQuotations.addAll(quotationForYear);
	}

	if (allQuotations.size() == 0) {
	    return null;
	}

	return allQuotations;

    }

    private List<Integer> computeYears(DateInterval period) {
	int beginYear = period.getStartDate().getYear();
	int endYear = period.getEndDate().getYear();

	List<Integer> yearsToLoad = new ArrayList<>();
	for (int i = beginYear; i <= endYear; i++) {
	    yearsToLoad.add(i);
	}
	return yearsToLoad;
    }

    private List<Quotation> loadQuotationsFromCsv(Stock stock, Integer yearToLoad) {

	CsvMapReader csvMapReader = null;

	List<Quotation> quotationsListForYear = new ArrayList<>();

	try {
	    for (int month = 1; month <= 12; month++) {
		String fileName = computeStockQuotationsFile(stock, yearToLoad, month);

		if (!new File(fileName).exists()) {
		    LOGGER.debug("Le fichier " + fileName + " n'existe pas");
		    continue;
		}

		try {
		    csvMapReader = new CsvMapReader(new FileReader(fileName), ApplicationDirs.getCsvPreference(ApplicationDirs.MY_CVS_PREFERENCE));
		}
		catch (FileNotFoundException e) {
		    LOGGER.warn("Ereur lors du chargement du fichier " + fileName);
		    break;
		}

		// header : isin,date,open,high,low,close,volume
		final String[] headers = csvMapReader.getHeader(true);

		CellProcessor[] processors = createNewMyFormatQuotationProcessors(true);

		Map<String, Object> quotationMap = null;
		while ((quotationMap = csvMapReader.read(headers, processors)) != null) {
		    Quotation.Builder builder = new Quotation.Builder(quotationMap);
		    Quotation quotation = builder.build();
		    quotationsListForYear.add(quotation);
		}

	    }

	    return quotationsListForYear;
	}

	catch (Exception e) {
	    String msg = "Erreur pour charger les cotations , caused by ";
	    LOGGER.error(msg + e, e);
	    throw new IllegalArgumentException(msg, e);
	}
	finally {
	    try {
		if (csvMapReader != null)
		    csvMapReader.close();
	    }
	    catch (IOException e) {
		e.printStackTrace();
	    }

	}

    }

    private void assertIntervalValid(DateInterval dateInterval, String isin) {

	if (dateInterval == null)
	    return;

	if ((dateInterval.getEndDate() != null && dateInterval.getEndDate().isBefore(dateInterval.getStartDate()))
		|| dateInterval.getStartDate().getYear() < 2000)
	    throw new IllegalArgumentException("dateInterval " + dateInterval + " invalide pour charger les quotation de la valeur " + isin);

    }

    private String computeStockQuotationsFile(Stock stock, Integer year, Integer month) {
	String isin = stock.getIsin();
	String path = ApplicationDirs.QUOTATIONS_DIR + "/" + isin + "/" + year + "/" + month + ".csv";
	return path;
    }

    private CellProcessor[] createNewMyFormatQuotationProcessors(boolean isReading) {

	CellProcessorAdaptor dateProcessor = null;
	if (isReading) {
	    dateProcessor = new ParseLocalDate("dd/MM/yyyy");
	}

	else {
	    dateProcessor = new FormatToStringLocalDate("dd/MM/yyyy");
	}

	final CellProcessor[] processors = new CellProcessor[] { new NotNull(), dateProcessor, new ParseFloat(), new ParseFloat(), new ParseFloat(),
		new ParseFloat(), new ParseLong() };

	return processors;

    }

}
