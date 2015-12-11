package davidhxxx.teach.jfreecharttuto.util.csv;

import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class FormatToStringLocalDate extends CellProcessorAdaptor implements DateCellProcessor {

    private SimpleDateFormat formatter;

    /**
     * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
     * SimpleDateFormat.
     * 
     * @param dateFormat
     *            the date format String (see {@link SimpleDateFormat})
     * @throws NullPointerException
     *             if dateFormat is null
     */
    public FormatToStringLocalDate(final String dateFormat) {
	super();
	checkPreconditionsAndCreateFormatter(dateFormat);

    }

    /**
     * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
     * SimpleDateFormat, then calls the next processor in the chain.
     * 
     * @param dateFormat
     *            the date format String (see {@link SimpleDateFormat})
     * @param next
     *            the next processor in the chain
     * @throws NullPointerException
     *             if dateFormat or next is null
     */
    public FormatToStringLocalDate(final String dateFormat, final StringCellProcessor next) {
	super(next);
	checkPreconditionsAndCreateFormatter(dateFormat);

    }

    /**
     * Checks the preconditions for creating a new FmtDate processor.
     * 
     * @param dateFormat
     *            the date format String
     * @throws NullPointerException
     *             if dateFormat is null
     */
    private void checkPreconditionsAndCreateFormatter(final String dateFormat) {
	if (dateFormat == null) {
	    throw new NullPointerException("dateFormat should not be null");
	}

	try {
	    formatter = new SimpleDateFormat(dateFormat);
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(String.format("'%s' is not a valid date format", dateFormat));

	}

    }

    /**
     * {@inheritDoc}
     * 
     * @throws SuperCsvCellProcessorException
     *             if value is null or is not a Date, or if dateFormat is not a valid date format
     */
    @Override
    public Object execute(final Object value, final CsvContext context) {
	validateInputNotNull(value, context);

	if (!(value instanceof LocalDate)) {
	    throw new SuperCsvCellProcessorException(LocalDate.class, value, context, this);
	}

	LocalDate localDate = (LocalDate) value;
	String result = formatter.format(localDate.toDate());
	return next.execute(result, context);
    }


}