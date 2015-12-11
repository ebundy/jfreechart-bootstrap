package davidhxxx.teach.jfreecharttuto.model;

public class Stock {

    private String isin;

    private String name;

    private String ticker;

    public Stock(String isin, String name, String ticker) {
	this.isin = isin;
	this.name = name;
	this.ticker = ticker;
    }

    public String getStockNameAndIsin() {
	return name + "(" + isin + ")";
    }


    public String getIsin() {
	return isin;
    }

    public String getName() {
	return name;
    }

    public String getTicker() {
	return ticker;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((isin == null) ? 0 : isin.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	if (name == null) {
	    if (other.name != null)
		return false;
	}
	else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name;
    }

}
