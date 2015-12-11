package davidhxxx.teach.jfreecharttuto.util.csv;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.util.CsvContext;

public class ParseLocalDate extends CellProcessorAdaptor {

    private DateTimeFormatter dateFormat;

    public ParseLocalDate(String formatDate) {
	dateFormat = DateTimeFormat.forPattern(formatDate);
    }

    @Override
    public Object execute(Object value, CsvContext context) {

	if (!(value instanceof String)) {
	    throw new SuperCsvException("input should be a String!");
	}

	try {
	    LocalDate date = dateFormat.parseDateTime((String) value).toLocalDate();
	    return next.execute(date, context);
	}
	catch (Exception e) {
	    throw new IllegalArgumentException("Erreur dans la date fournie, String=" + value, e);
	}

    }

}