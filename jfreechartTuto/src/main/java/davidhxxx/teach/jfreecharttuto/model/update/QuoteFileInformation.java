package davidhxxx.teach.jfreecharttuto.model.update;

import davidhxxx.teach.jfreecharttuto.model.DateInterval;

public class QuoteFileInformation {

    private DateInterval dateInterval;
    private String filePath;

    public QuoteFileInformation(DateInterval period, String filePath) {
	this.dateInterval = period;
	this.filePath = filePath;
    }

    public DateInterval getDateInterval() {
	return dateInterval;
    }

    public String getFilePath() {
	return filePath;
    }

}
