package davidhxxx.teach.jfreecharttuto.model;

import java.util.Map;

import org.joda.time.LocalDate;

public class Quotation {

    public static class Builder {

	private String isin;

	private LocalDate date;

	private Float open;

	private Float high;

	private Float low;

	private Float close;

	private Long volume;

	private Map<String, Object> quotationMap;

	public Builder() {
	}

	public Builder(Map<String, Object> quotationMap) {
	    this.quotationMap = quotationMap;
	    	withIsin((String) getFromMap("isin"))
		    .withDate((LocalDate) getFromMap("date"))
		    .withOpen((float) getFromMap("open"))
		    .withHigh((float) getFromMap("high"))
		    .withLow((float) getFromMap("low"))
		    .withClose((float) getFromMap("close"))
		    .withVolume((long) getFromMap("volume"));
	    
	}

	@SuppressWarnings("unchecked")
	private <T> T getFromMap(String keyName) {
	    return (T) (quotationMap.get(keyName));
	}

	public Builder withIsin(String isin) {
	    this.isin = isin;
	    return this;
	}

	public Builder withDate(LocalDate date) {
	    this.date = date;
	    return this;
	}

	public Builder withOpen(float open) {
	    this.open = open;
	    return this;
	}

	public Builder withHigh(float high) {
	    this.high = high;
	    return this;
	}

	public Builder withLow(float low) {
	    this.low = low;
	    return this;
	}

	public Builder withClose(float close) {
	    this.close = close;
	    return this;
	}

	public Builder withVolume(long volume) {
	    this.volume = volume;
	    return this;
	}

	public Quotation build() {
	    assertNotNull("isin", isin);
	    assertNotNull("date", date);
	    assertNotNull("open", open);
	    assertNotNull("high", high);
	    assertNotNull("low", low);
	    assertNotNull("close", close);
	    assertNotNull("volume", volume);

	    return new Quotation(this);
	}

	private void assertNotNull(String fieldName, Object fieldValue) {

	    if (fieldValue == null) {
		throw new IllegalArgumentException("field " + fieldName + " must not be null");
	    }
	}
    }

    private String isin;

    private LocalDate date;

    private float open;

    private float high;

    private float low;

    private float close;

    private long volume;

    private Quotation(Builder builder) {
	close = builder.close;
	date = builder.date;
	high = builder.high;
	isin = builder.isin;
	low = builder.low;
	open = builder.open;
	volume = builder.volume;
    }

    public String getIsin() {
	return isin;
    }

    public LocalDate getDate() {
	return date;
    }

    public float getOpen() {
	return open;
    }

    public float getHigh() {
	return high;
    }

    public float getLow() {
	return low;
    }

    public float getClose() {
	return close;
    }

    public long getVolume() {
	return volume;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;

	if (obj == null)
	    return false;

	Quotation other = (Quotation) obj;

	if (date == null) {
	    if (other.date != null)
		return false;
	}

	else if (!date.equals(other.date))
	    return false;

	return true;
    }

}
