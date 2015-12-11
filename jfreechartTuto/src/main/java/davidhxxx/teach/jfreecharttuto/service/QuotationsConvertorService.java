package davidhxxx.teach.jfreecharttuto.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
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

	TreeMap<Stock, QuotationsToImport> quotationsByIsin1 = new TreeMap<>();
	
	CsvBeanReader mapReader = null;
	
	CellProcessor[] processors = null;
	String[] header = null;
	CsvPreference csvPreference = null;
	
	// my format
	processors = getMyProviderStockProcessors();
	header = new String[] { "isin", "date", "open", "high", "low", "close", "volume" };
	csvPreference = ApplicationDirs.getCsvPreference(ApplicationDirs.MY_CVS_PREFERENCE);
	
	Stock stock = null;
	for (QuoteFileInformation quoteFileInformation : quoteFileInformations) {
	
	    File file = new File(quoteFileInformation.getFilePath());
	    try {
	
		mapReader = new CsvBeanReader(new FileReader(file), csvPreference);
	
		Quotation q;
		while ((q = mapReader.read(Quotation.class, header, processors)) != null) {
	
	
		    if (stock == null || (q.getIsin() != null && !q.getIsin().equals(stock.getIsin()))) {
			stock = LocalListService.getInstance().findStock(q.getIsin());
	
			if (stock == null) {
			    continue;
			}
		    }
		    QuotationsToImport quotations = quotationsByIsin1.get(stock);
		    if (quotations == null) {
			quotations = new QuotationsToImport();
			quotationsByIsin1.put(stock, quotations);
		    }
	
		    quotations.addSingleQuotationIfNotDateExit(q);
		}
	
	    }
	
	    catch (Exception e) {
		String msg = "Erreur lors de la conversion pour le fichier " + file;
		LOGGER.error(msg, e);
		throw new IllegalArgumentException(msg, e);
	    }
	    finally {
		if (mapReader != null) {
		    try {
			mapReader.close();
		    }
		    catch (IOException e) {
			LOGGER.warn("erreur lors de la fermeture du fichier ", e);
		    }
		}
	    }
	}
	TreeMap<Stock, QuotationsToImport> quotationsByIsin = quotationsByIsin1;

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
