package davidhxxx.teach.jfreecharttuto.ui.stockselection;

import davidhxxx.teach.jfreecharttuto.model.Stock;

public interface StockSelectionChangedListener {

    void stockSelectionChanged(Stock selectedStock);

    void displayNextStock();

    void displayPreviousStock();

}
