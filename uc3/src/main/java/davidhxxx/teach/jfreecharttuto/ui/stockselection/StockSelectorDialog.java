package davidhxxx.teach.jfreecharttuto.ui.stockselection;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalListService;
import davidhxxx.teach.jfreecharttuto.model.Stock;

@SuppressWarnings("serial")
public class StockSelectorDialog extends JDialog implements StockListChangedListener {

    private JPanel selectStockPopupPanel;
    private StockSelectorForOneList stockSelector;
    private LocalListService localListService = LocalListService.getInstance();
    private Action validerAction;
    private String listNameSelected;

    public StockSelectorDialog(StockSelectionChangedListener stockSelectionChangedListener, String listNameSelected) {
	this.listNameSelected = listNameSelected;
	buildObject(stockSelectionChangedListener);
    }

    public Stock getFirst() {
	return stockSelector.getSelection();

    }

    @Override
    public void onStockListChanged(String listName) {
	setVisible(false);
	this.listNameSelected = listName;

	buildObject(stockSelector.getStockSelectionChangedListener());
    }

    public void selectNextStock() {
	stockSelector.selectNextStock();
    }

    public void selectPreviousStock() {
	stockSelector.selectPreviousStock();
    }

    public void selectStock(String isin) {
	if (stockSelector.getSelection().getIsin().equals(isin))
	    return;

	stockSelector.select(isin);
    }

    private void buildObject(final StockSelectionChangedListener stockSelectionChangedListener) {

	getContentPane().removeAll();
	setSize(200, 400);

	if (selectStockPopupPanel == null) {
	    selectStockPopupPanel = new JPanel();
	}
	else {
	    selectStockPopupPanel.removeAll();
	}

	selectStockPopupPanel.setLayout(new BorderLayout());
	getContentPane().add(selectStockPopupPanel);

	List<Stock> stocks = localListService.loadStocks(listNameSelected);
	stockSelector = new StockSelectorForOneList(stocks, stockSelectionChangedListener);
	JScrollPane listPane = new JScrollPane(stockSelector.getJList());
	selectStockPopupPanel.add(listPane, BorderLayout.CENTER);

	validerAction = new AbstractAction() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Stock stockSelected = stockSelector.getSelection();
		stockSelectionChangedListener.stockSelectionChanged(stockSelected);
	    }
	};

	KeyStroke keyEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyEnter, "valider");
	getRootPane().getActionMap().put("valider", validerAction);

    }

}
