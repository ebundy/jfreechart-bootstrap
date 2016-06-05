package davidhxxx.teach.jfreecharttuto.model;

import java.util.Comparator;

public class Stock {

    public static class StockComparatorAlphaDesc implements Comparator<Stock> {

	@Override
	public int compare(Stock s1, Stock s2) {
	    return s1.toString().compareTo(s2.toString());
	}

    }
    
    private final String isin;

    private final String name;

    private final String ticker;

    public Stock(String isin, String name, String ticker) {
	this.isin = isin;
	this.name = name;
	this.ticker = ticker;
    }

    public String getIsin() {
	return isin;
    }

    @Override
    public String toString() {
	return name;
    }
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((isin == null) ? 0 : isin.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Stock other = (Stock) obj;
	if (isin == null) {
	    if (other.isin != null)
		return false;
	}
	else if (!isin.equals(other.isin))
	    return false;
	
	return true;
    }
}
