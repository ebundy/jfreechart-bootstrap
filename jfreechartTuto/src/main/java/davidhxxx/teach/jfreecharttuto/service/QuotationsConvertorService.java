package davidhxxx.teach.jfreecharttuto.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalListService;
import davidhxxx.teach.jfreecharttuto.model.Quotation;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.model.update.QuotationsToImport;
import davidhxxx.teach.jfreecharttuto.model.update.QuoteFileInformation;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;
import davidhxxx.teach.jfreecharttuto.util.csv.ParseLocalDate;
import davidhxxx.teach.jfreecharttuto.util.csv.ParseFloat;

public class QuotationsConvertorService {

    private static class LazyHolder {
	private static final QuotationsConvertorService INSTANCE = new QuotationsConvertorService();
    }

    private static Logger LOGGER = LoggerFactory.getLogger(QuotationsConvertorService.class);

    public static QuotationsConvertorService getInstance() {
	return LazyHolder.INSTANCE;
    }

    public TreeMap<Stock, QuotationsToImport> convertFromMyProviderToQuotations(List<QuoteFileInformation> quoteFileInformations) {

	TreeMap<Stock, QuotationsToImport> quotationsByIsin = new TreeMap<>(new StockAlphaNameAscComparator());

	CsvMapReader csvMapReader = null;

	CellProcessor[] processors = null;
	String[] header = null;
	CsvPreference csvPreference = null;

	// my format
	processors = getMyProviderStockProcessors();
	// header = new String[] { "isin", "date", "open", "high", "low", "close", "volume" };
	csvPreference = ApplicationDirs.getCsvPreference(ApplicationDirs.MY_CVS_PREFERENCE);

	Stock stock = null;
	for (QuoteFileInformation quoteFileInformation : quoteFileInformations) {

	    File file = new File(quoteFileInformation.getFilePath());

	    try {
		// csvMapReader = new CsvBeanReader(new FileReader(file), csvPreference);
		// header = mapReader.getHeader(true);

		csvMapReader = new CsvMapReader(new FileReader(file), ApplicationDirs.getCsvPreference(ApplicationDirs.MY_CVS_PREFERENCE));
		header = csvMapReader.getHeader(true);

		Map<String, Object> quotationMap = null;

		while ((quotationMap = csvMapReader.read(header, processors)) != null) {

		    Quotation.Builder builder = new Quotation.Builder(quotationMap);
		    Quotation quotation = builder.build();

		    if (stock == null || (quotation.getIsin() != null && !quotation.getIsin().equals(stock.getIsin()))) {
			stock = LocalListService.getInstance().findStock(quotation.getIsin());

			if (stock == null) {
			    continue;
			}
		    }

		    QuotationsToImport quotations = quotationsByIsin.get(stock);
		    if (quotations == null) {
			quotations = new QuotationsToImport();
			quotationsByIsin.put(stock, quotations);
		    }

		    quotations.addSingleQuotationIfNotDateExit(quotation);
		}
	    }
	    catch (Exception e) {
		String msg = "Erreur lors de la conversion pour le fichier " + file;
		LOGGER.error(msg, e);
		throw new IllegalArgumentException(msg, e);
	    }

	    finally {
		if (csvMapReader != null) {
		    try {
			csvMapReader.close();
		    }
		    catch (IOException e) {
			LOGGER.warn("erreur lors de la fermeture du fichier ", e);
		    }
		}
	    }
	}


	LOGGER.debug("convertFileInformationsToQuotationsForSingleValue ends. Nb Isin =" + quotationsByIsin.size());
	return quotationsByIsin;

    }

    public CellProcessor[] getMyProviderStockProcessors() {
	// String[] BeanAttribute = { "isin", "date", "open", "high", "low", "close", "volume"};
	final CellProcessor[] processors = new CellProcessor[] { new NotNull(), new ParseLocalDate("dd/MM/yy"), new ParseFloat(), new ParseFloat(),
		new ParseFloat(), new ParseFloat(), new ParseLong() };

	return processors;
    }

}
