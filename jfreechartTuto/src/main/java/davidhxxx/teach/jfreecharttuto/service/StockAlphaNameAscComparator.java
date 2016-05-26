package davidhxxx.teach.jfreecharttuto.service;

import java.util.Comparator;

import davidhxxx.teach.jfreecharttuto.model.Stock;

public class StockAlphaNameAscComparator implements Comparator<Stock> {
    

    @Override
    public int compare(Stock o1, Stock o2) {
	return o1.getName().compareTo(o2.getName());
    }

}
