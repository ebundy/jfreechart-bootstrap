package davidhxxx.teach.jfreecharttuto.fixture.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import davidhxxx.teach.jfreecharttuto.model.Stock;

public class FixtureCreation {

    public static Map<String, Stock> createStockByIsinForListOne() {
	Map<String, Stock> stocksByIsin = new HashMap<>();
	stocksByIsin.put("US88579Y1010", new Stock("US88579Y1010", "3M Co.", "MMM"));
	stocksByIsin.put("US0258161092", new Stock("US0258161092", "American Express Co.", "AXP"));
	stocksByIsin.put("US0378331005", new Stock("US0378331005", "Apple Inc.", "AAPL"));
	stocksByIsin.put("US0970231058", new Stock("US0970231058", "Boeing Co.", "BA"));
	stocksByIsin.put("US1491231015", new Stock("US1491231015", "Caterpillar Inc.", "CAT"));
	return stocksByIsin;
    }

    public static List<Stock> createStockInListOneOrderedByNameAsc() {
	List<Stock> stocks = new ArrayList<>();
	stocks.add(new Stock("US88579Y1010", "3M Co.", "MMM"));
	stocks.add(new Stock("US0258161092", "American Express Co.", "AXP"));
	stocks.add(new Stock("US0378331005", "Apple Inc.", "AAPL"));
	stocks.add(new Stock("US0970231058", "Boeing Co.", "BA"));
	stocks.add(new Stock("US1491231015", "Caterpillar Inc.", "CAT"));
	return stocks;
    }
}
