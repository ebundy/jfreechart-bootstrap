package davidhxxx.teach.jfreecharttuto.model;

import org.jfree.data.xy.DefaultOHLCDataset;

public class StockLoaded {

    private DefaultOHLCDataset dataset;

    private DateInterval intervalIncluded;

    private Stock stock;

    public StockLoaded(Stock stock, DefaultOHLCDataset dataset, DateInterval dateIntervalIncluded) {
	this.stock = stock;
	this.dataset = dataset;
	intervalIncluded = new DateInterval(dateIntervalIncluded);
    }

    public DefaultOHLCDataset getDataset() {
	return dataset;
    }

    public DateInterval getIntervalIncluded() {
	return intervalIncluded;
    }

    public Stock getStock() {
	return stock;
    }

}
