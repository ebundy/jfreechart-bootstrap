package davidhxxx.teach.jfreecharttuto.ui.stockselection;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import davidhxxx.teach.jfreecharttuto.model.Stock;

@SuppressWarnings("serial")
public class StockListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> stockList, Object stockObj, int index, boolean isSelected, boolean cellHasFocus) {

	super.getListCellRendererComponent(stockList, stockObj, index, isSelected, cellHasFocus);
	Stock stock = (Stock) stockObj;
	setText(stock.toString());
	return this;
    }
}
