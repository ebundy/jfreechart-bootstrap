package davidhxxx.teach.jfreecharttuto.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

    private static DateTimeFormatter displayDateFormatWithSlash = DateTimeFormat.forPattern("dd/MM/yyyy");

    public static LocalDate newWithNoTime() {
	return new LocalDate();
    }

    public static DateTime parseDateFrWithSlash(String stringDate) {

	stringDate = stringDate.trim();
	try {
	    return newWithNoTime(new DateTime(displayDateFormatWithSlash.parseDateTime(stringDate)));
	}
	catch (Exception e) {	   
	    throw new IllegalArgumentException(e);
	}
    }

    public static DateTime newWithNoTime(DateTime dateTime) {
	return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0, 0, 0);
    }

}
