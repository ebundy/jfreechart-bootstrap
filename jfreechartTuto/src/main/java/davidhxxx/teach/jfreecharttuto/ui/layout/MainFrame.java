package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalListService;
import davidhxxx.teach.jfreecharttuto.dataservice.LocalQuoteService;
import davidhxxx.teach.jfreecharttuto.model.DateInterval;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.model.StockLoaded;
import davidhxxx.teach.jfreecharttuto.ui.draw.MyChartPanel;
import davidhxxx.teach.jfreecharttuto.ui.listener.DrawFlexibleLineChartMouseListener;
import davidhxxx.teach.jfreecharttuto.ui.stockselection.StockSelectionChangedListener;
import davidhxxx.teach.jfreecharttuto.ui.stockselection.StockSelectorDialog;
import davidhxxx.teach.jfreecharttuto.util.DateUtil;
import davidhxxx.teach.jfreecharttuto.util.WidgetUtil;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements StockSelectionChangedListener {

    static Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

    // buttons
    private JComboBox<String> comboListOfStockList;

    private JButton selectValeursBtn;

    private JXDatePicker dateDebut;

    private JXDatePicker dateFin;

    // containers
    private JSplitPane hSplitPaneGeneral;

    private StockSelectorDialog selectStockDialog;

    private JToolBar toolBar;

    private JScrollPane scrollPaneForReferenceChart;

    private ActionListener addFlexibleLineAction;

    private MyChartPanel myChartPanel;

    // buttons actions
    private JButton addFlexibleLineBtn;

    private Stock stockSelected;

    public static void main(String[] args) {
	try {
	    SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
		    new MainFrame("mon application de trading");
		}

	    });
	}
	catch (Exception e) {
	    new ErrorDialog("ERREUR GLOBALE lors du chargement de l'application", e);
	}
    }

    public MainFrame(String name) {

	super(name);
	Locale.setDefault(new Locale("fr"));
	UIManager.put("ToolTip.font", new Font("Serif", Font.BOLD, 18));

	setVisible(true);
	setJMenuBar(new MainFrameMenu());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	hSplitPaneGeneral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	hSplitPaneGeneral.setDividerLocation(150);

	add(hSplitPaneGeneral, BorderLayout.CENTER);

	initToolBar();
	initGlobalListener();
	selectStockDialog = StockSelectorDialog.createWithValidationAuto(this, false, comboListOfStockList.getSelectedItem().toString());
	initDateListener();
	createScrollPaneForChart();
	hSplitPaneGeneral.setLeftComponent(toolBar);
	hSplitPaneGeneral.setRightComponent(scrollPaneForReferenceChart);
	setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initGlobalListener() {
	// no action
	// select indicateur
	KeyStroke keyNoAction = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyNoAction, "no_action");

	getRootPane().getActionMap().put("no_action", new AbstractAction() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		myChartPanel.getWorkingDraws().clearWorkingState();

	    }
	});

    }

    @Override
    public void displayNextStock() {
	selectStockDialog.selectNextStock();
    }

    @Override
    public void displayPreviousStock() {
	selectStockDialog.selectPreviousStock();
    }

    @Override
    public void stockSelectionChanged(Stock stockSelected) {

	try {
	    loadMyChartPanel(stockSelected);
	    this.stockSelected = stockSelected;
	}
	catch (Throwable e) {
	    new ErrorDialog("ERREUR (globale) lors de la sélection d'une nouvelle valeur.", "StockSelected=" + stockSelected, e);
	}

    }

    private void handledChangedDates() {
	LocalDate beginDate = null;
	LocalDate endDate = null;

	if (dateDebut.getDate() != null) {
	    beginDate = new LocalDate(dateDebut.getDate());
	}

	if (dateFin.getDate() != null) {
	    endDate = new LocalDate(dateFin.getDate());
	}

	if (dateDebut.getDate() == null && dateFin.getDate() != null) {
	    dateDebut.setDate(endDate.minusMonths(3).toDate());
	}

	if (beginDate == null) {
	    beginDate = new LocalDate(dateDebut.getDate());
	}

	if (endDate != null && endDate.isBefore(beginDate)) {
	    dateDebut.setDate(endDate.minusMonths(3).toDate());
	}

	redrawChartPanel(beginDate, endDate);
    }

    public void redrawChartPanel(LocalDate beginDate, LocalDate endDate) {

	DateInterval periodLoaded = new DateInterval(beginDate, endDate);

	StockLoaded stockLoaded = LocalQuoteService.getInstance().loadStockForChartPanel(comboListOfStockList.getSelectedItem().toString(), stockSelected.getIsin(), periodLoaded);

	myChartPanel.redraw(stockLoaded);

	if (stockLoaded.getIntervalIncluded().getStartDate() != null) {
	    dateDebut.setDate(stockLoaded.getIntervalIncluded().getStartDate().toDate());
	}

	if (stockLoaded.getIntervalIncluded().getEndDate() != null) {
	    dateFin.setDate(stockLoaded.getIntervalIncluded().getEndDate().toDate());
	}

    }

    private void createScrollPaneForChart() {
	scrollPaneForReferenceChart = new JScrollPane(null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollPaneForReferenceChart.setMinimumSize(new Dimension(800, 300));
    }

    private void initToolBar() {
	toolBar = new JToolBar("Still draggable");
	toolBar.setOrientation(SwingConstants.VERTICAL);

	comboListOfStockList = createComboListOfListOfStock();
	toolBar.add(comboListOfStockList);

	createSperationMoyenne(toolBar);

	selectValeursBtn = createCenterBtn("Valeurs");
	selectValeursBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		selectStockDialog.setVisible(true);
	    }
	});
	toolBar.add(selectValeursBtn);

	// DATE
	LocalDate actualDate = DateUtil.newWithNoTime();

	createSperationMoyenne(toolBar);
	createSperationMoyenne(toolBar);
	JLabel lblDateDebut = new JLabel("date de début");
	toolBar.add(lblDateDebut);
	lblDateDebut.setAlignmentX(Component.CENTER_ALIGNMENT);
	dateDebut = new JXDatePicker(actualDate.minusYears(1).toDate());
	dateDebut.setAlignmentX(Component.CENTER_ALIGNMENT);
	toolBar.add(dateDebut);

	createSperationMoyenne(toolBar);

	JLabel lblDateFin = new JLabel("date de fin");
	toolBar.add(lblDateFin);
	lblDateFin.setAlignmentX(Component.CENTER_ALIGNMENT);
	dateFin = new JXDatePicker(actualDate.toDate());
	dateFin.setAlignmentX(Component.CENTER_ALIGNMENT);
	toolBar.add(dateFin);

	createSperationMoyenne(toolBar);

	// BLOC TWO OPTIONS : FLEXIBLE AND HORIZONTAL LINE ADDING
	URL resource = MainFrame.class.getResource("/img/toolbar/flexible-line.png");
	addFlexibleLineBtn = new JButton(new ImageIcon(resource));
	// setUniqueSizeWorkingDrawBtn(addFlexibleLineBtn);
	WidgetUtil.setUniqueSize(addFlexibleLineBtn, 35, 35);
	addFlexibleLineBtn.setAlignmentX((Component.CENTER_ALIGNMENT));

	// JPanel panelBtnsGrouppedOne = new JPanel();
	// panelBtnsGrouppedOne.add(addFlexibleLineBtn, BorderLayout.WEST);
	// panelBtnsGrouppedOne.add(addHorizontalLineBtn, BorderLayout.EAST);

	toolBar.add(addFlexibleLineBtn);

    }

    private void createSperationMoyenne(JToolBar toolBar) {
	toolBar.add(new Separator(new Dimension(1, 30)));
    }

    private JButton createCenterBtn(String libelleBtn) {
	JButton btn = new JButton(libelleBtn);
	setUniqueSizeAndCenter(btn, 120, 30);
	btn.setAlignmentX((Component.CENTER_ALIGNMENT));
	return btn;
    }

    private void setUniqueSizeAndCenter(JComponent component, int width, int height) {
	Dimension preferredSize = new Dimension(width, height);
	component.setPreferredSize(preferredSize);
	component.setSize(preferredSize);
	component.setMinimumSize(preferredSize);
	component.setMaximumSize(preferredSize);
	component.setAlignmentX((Component.CENTER_ALIGNMENT));
    }

    private JComboBox<String> createComboListOfListOfStock() {
	final JComboBox<String> comboListOfStockList = new JComboBox<String>() {
	    @Override
	    public Dimension getMaximumSize() {
		return getPreferredSize();
	    }
	};

	setUniqueSizeAndCenter(comboListOfStockList, 120, 30);
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

    private void loadMyChartPanel(davidhxxx.teach.jfreecharttuto.model.Stock stockSelected) {
	setTitle(stockSelected.getStockNameAndIsin());

	DateInterval periodDisplayed = findPeriodDisplayedInRefPanel();

	String isin = stockSelected.getIsin();
	StockLoaded stockLoaded = LocalQuoteService.getInstance().loadStockForChartPanel(comboListOfStockList.getSelectedItem().toString(), isin, periodDisplayed);

	if (stockLoaded == null) {
	    return;
	}

	myChartPanel = new MyChartPanel();
	myChartPanel.redraw(stockLoaded);
	dateDebut.setDate(stockLoaded.getIntervalIncluded().getStartDate().toDate());

	scrollPaneForReferenceChart.getViewport().add(myChartPanel);

	if (stockLoaded.getIntervalIncluded().getEndDate() != null) {
	    dateFin.setDate(stockLoaded.getIntervalIncluded().getEndDate().toDate());
	}

    }

    private void initDateListener() {

	final ActionListener actionListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		handledChangedDates();
	    }

	};
	dateDebut.addActionListener(actionListener);
	dateFin.addActionListener(actionListener);
    }

    private DateInterval findPeriodDisplayedInRefPanel() {

	LocalDate startDate = null;
	LocalDate endDate = null;

	if (dateDebut.getDate() != null) {
	    startDate = new LocalDate(dateDebut.getDate());
	}

	if (dateFin.getDate() != null) {
	    endDate = new LocalDate(dateFin.getDate());
	}

	DateInterval dateInterval = new DateInterval(startDate, endDate);
	return dateInterval;
    }

    private void addFlexibleLineAction(JButton addFlexibleLineBtn) {
	addFlexibleLineAction = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		removeAllListenersOnChartPanel();
		myChartPanel.addChartMouseListener(new DrawFlexibleLineChartMouseListener(myChartPanel, myChartPanel.getWorkingDraws()));
	    }

	};
	addFlexibleLineBtn.addActionListener(addFlexibleLineAction);
    }

    private void removeAllListenersOnChartPanel() {
	myChartPanel.removeAllDrawingListenerOnChartPanel();
	myChartPanel.clearWorkingDrawInProgress();
    }
}
