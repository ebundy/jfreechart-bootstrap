package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalListService;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.ui.stockselection.StockSelectionChangedListener;
import davidhxxx.teach.jfreecharttuto.ui.stockselection.StockSelectorDialog;
import davidhxxx.teach.jfreecharttuto.util.WidgetUtil;

@SuppressWarnings("serial")
public class Uc3DisplayStocksFromSelectedListMain extends JFrame implements StockSelectionChangedListener {

    // buttons
    private JComboBox<String> comboListOfStockList;

    private JButton selectValeursBtn;

    // containers
    private JSplitPane hSplitPaneGeneral;

    private StockSelectorDialog selectStockDialog;

    private JToolBar toolBar;

    private JScrollPane scrollPaneForReferenceChart;

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		new Uc3DisplayStocksFromSelectedListMain("trading bootstrap app");
	    }

	});
    }

    public Uc3DisplayStocksFromSelectedListMain(String name) {

	super(name);
	Locale.setDefault(new Locale("fr"));
	UIManager.put("ToolTip.font", new Font("Serif", Font.BOLD, 18));

	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	hSplitPaneGeneral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	hSplitPaneGeneral.setDividerLocation(150);

	add(hSplitPaneGeneral, BorderLayout.CENTER);

	initToolBar();
	selectStockDialog = new StockSelectorDialog(this,  comboListOfStockList.getSelectedItem().toString());
	hSplitPaneGeneral.setLeftComponent(toolBar);
	hSplitPaneGeneral.setRightComponent(scrollPaneForReferenceChart);
	setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    @Override
    public void displayNextStock() {
	selectStockDialog.selectNextStock();
    }

    @Override
    public void displayPreviousStock() {
	selectStockDialog.selectPreviousStock();
    }

    private void initToolBar() {
	toolBar = new JToolBar("Still draggable");
	toolBar.setOrientation(SwingConstants.VERTICAL);

	comboListOfStockList = createComboListOfListOfStock();
	toolBar.add(comboListOfStockList);
	createSperationMoyenne(toolBar);

	selectValeursBtn = new JButton("Valeurs");
	WidgetUtil.setUniqueSizeAndCenter(selectValeursBtn, 120, 30);
	selectValeursBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		selectStockDialog.setVisible(true);
	    }
	});
	toolBar.add(selectValeursBtn);
    }

    private void createSperationMoyenne(JToolBar toolBar) {
	toolBar.add(new Separator(new Dimension(1, 30)));
    }

    private JComboBox<String> createComboListOfListOfStock() {
	final JComboBox<String> comboListOfStockList = new JComboBox<String>() {
	    @Override
	    public Dimension getMaximumSize() {
		return getPreferredSize();
	    }
	};

	WidgetUtil.setUniqueSizeAndCenter(comboListOfStockList, 120, 30);
	List<String> listOfListOfStockName = LocalListService.getInstance().getNamesOfLists();
	for (String listOfStockName : listOfListOfStockName) {
	    comboListOfStockList.addItem(listOfStockName);
	}

	((JLabel) comboListOfStockList.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

	comboListOfStockList.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		selectStockDialog.onStockListChanged(comboListOfStockList.getSelectedItem().toString());
	    }
	});
	return comboListOfStockList;
    }

    @Override
    public void stockSelectionChanged(Stock selectedStock) {
    }

}
