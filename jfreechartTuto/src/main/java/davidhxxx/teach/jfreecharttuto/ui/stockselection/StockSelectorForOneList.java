package davidhxxx.teach.jfreecharttuto.ui.stockselection;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;

import davidhxxx.teach.jfreecharttuto.model.Stock;

public class StockSelectorForOneList {

    public static class StockComparatorAlphaDesc implements Comparator<Stock> {

	@Override
	public int compare(Stock s1, Stock s2) {
	    return s1.toString().compareTo(s2.toString());
	}

    }

//    public enum Sort {
//	ALPHA {
//	    @Override
//	    void sort(List<Stock> stocks) {
//		Collections.sort(stocks, new StockComparatorAlphaDesc());
//	    }
//	};
//
//	abstract void sort(List<Stock> stocks);
//
//    }

    private JList<Stock> isinJList;
    private final StockSelectionChangedListener stockSelectionChangedListener;

    public StockSelectorForOneList(List<Stock> stocks, StockSelectionChangedListener stockSelectionChangedListener, boolean withBtnValider) {

	this.stockSelectionChangedListener = stockSelectionChangedListener;
	isinJList = new JList<Stock>();
	if (!withBtnValider) {
	    initJListListener(isinJList);
	}

	if (CollectionUtils.isEmpty(stocks))
	    return;

	UIManager.put("List.timeFactor", 500L);

	isinJList.setCellRenderer(new StockListCellRenderer());
	isinJList.setListData(stocks.toArray(new Stock[stocks.size()]));
	isinJList.setSelectedIndex(0);

    }

    private void initJListListener(final JList<Stock> isinJList) {

	isinJList.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(MouseEvent evt) {

		Stock selectedStock = isinJList.getSelectedValue();
		if (selectedStock == null)
		    return;
		stockSelectionChangedListener.stockSelectionChanged(selectedStock);
	    }
	});
    }

    public JList<Stock> getJList() {
	return isinJList;
    }

    public Stock getSelection() {
	return isinJList.getSelectedValue();
    }

    public Stock getStock(String isin) {
	final ListModel<Stock> model = isinJList.getModel();
	for (int i = 0; i < model.getSize(); i++) {
	    Stock stock = model.getElementAt(i);
	    if (stock.getIsin().trim().equalsIgnoreCase(isin))
		return stock;
	}
	return null;
    }

    public Stock selectNextStock() {

	int nextStockIndex = isinJList.getSelectedIndex() + 1;

	if (nextStockIndex > isinJList.getModel().getSize() - 1)
	    return null;

	isinJList.setSelectedIndex(nextStockIndex);

	Stock stock = isinJList.getModel().getElementAt(nextStockIndex);
	stockSelectionChangedListener.stockSelectionChanged(stock);
	return stock;
    }

    public Stock selectPreviousStock() {

	int nextStockIndex = isinJList.getSelectedIndex() - 1;

	if (nextStockIndex < 0)
	    return null;

	isinJList.setSelectedIndex(nextStockIndex);

	Stock stock = isinJList.getModel().getElementAt(nextStockIndex);
	stockSelectionChangedListener.stockSelectionChanged(stock);
	return stock;
    }

    public Stock selectLastStock() {

	int lastElementIndex = isinJList.getModel().getSize() - 1;

	if (isinJList.getSelectedIndex() == isinJList.getModel().getSize() - 1) {
	    isinJList.setSelectedIndex(0);
	}
	isinJList.setSelectedIndex(lastElementIndex);

	return isinJList.getModel().getElementAt(lastElementIndex);
    }

    public void selectFirstStock() {
	isinJList.setSelectedIndex(0);
    }

    public void select(String isin) {
	Stock stock = getStock(isin);
	isinJList.setSelectedValue(stock, true);
    }

    public void updateStocks(List<Stock> stocks) {

	isinJList.setListData(stocks.toArray(new Stock[stocks.size()]));

    }

    public void setAutoValidSelection(boolean bool) {
	if (bool) {
	    isinJList.addListSelectionListener(new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
		    Stock selectedStock = isinJList.getSelectedValue();
		    if (selectedStock == null)
			return;
		    stockSelectionChangedListener.stockSelectionChanged(selectedStock);
		}
	    });
	}

    }

    public StockSelectionChangedListener getStockSelectionChangedListener() {
	return stockSelectionChangedListener;
    }
}
