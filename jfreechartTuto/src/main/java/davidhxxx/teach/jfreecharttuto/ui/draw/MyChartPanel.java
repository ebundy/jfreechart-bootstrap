package davidhxxx.teach.jfreecharttuto.ui.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.HorizontalAlignment;
import org.joda.time.LocalDate;

import davidhxxx.teach.jfreecharttuto.model.Quotation;
import davidhxxx.teach.jfreecharttuto.model.StockLoaded;
import davidhxxx.teach.jfreecharttuto.ui.draw.StockDrawn.TypeCours;
import davidhxxx.teach.jfreecharttuto.ui.draw.state.DrawingStates;
import davidhxxx.teach.jfreecharttuto.ui.draw.state.DrawsChart;
import davidhxxx.teach.jfreecharttuto.ui.draw.state.WorkingDrawsChart;
import davidhxxx.teach.jfreecharttuto.ui.listener.DrawFlexibleLineChartMouseListener;

@SuppressWarnings("serial")
public class MyChartPanel extends ChartPanel {

    private static final double DOMAIN_LOWER_AND_UPPER_MARGIN = 0.015;
    public static final int DEFAULT_CHART_HEIGHT = 300;
    public static final int DEFAULT_CHART_WIDTH = 800;

    private JFreeChart chart;
    private StockLoaded stockDataLoaded;
    private DrawingStates drawingStates = new DrawingStates();;
    private WorkingDrawsChart workingDraws;
    private List<ChartMouseListener> chartMouseListeners = new ArrayList<>();
    private JMenuItem menuDetecterFiguresChandeliers = null;

    /**
     * For EMPTY CHART
     * 
     * @param stockLoaded
     * @param isReferenceChart
     * @param pointPositionChangedInChartListener
     * @param mainFrameCallBack
     */
    public MyChartPanel() {

	super(null, true, true, true, false, true);
	configureChartOnce();

	// setVisibleDomainRange(domainVisibleRange);
	// autoAdjustRangeAxisAccordingVisibleValues();

	chart.getTitle().setHorizontalAlignment(HorizontalAlignment.CENTER);
	chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);

    }

    private void configureChartOnce() {

	// param commun
	DateAxis domainAxis = new DateAxis();
	domainAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
	domainAxis.setLowerMargin(DOMAIN_LOWER_AND_UPPER_MARGIN);
	domainAxis.setUpperMargin(DOMAIN_LOWER_AND_UPPER_MARGIN);

	setMaximumDrawWidth(1800);
	setMaximumDrawHeight(1400);
	domainAxis.setMinorTickMarksVisible(true);
	domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);

	NumberAxis rangeAxis = new NumberAxis("Price");
	rangeAxis.setLowerMargin(0.01);
	rangeAxis.setUpperMargin(0.01);
	rangeAxis.setAutoRange(true);
	rangeAxis.setFixedDimension(40);
	rangeAxis.setAutoRangeIncludesZero(false);

	// fin param commun
	XYPlot xyPlot = new XYPlot(null, domainAxis, rangeAxis, null);
	xyPlot.setBackgroundPaint(Color.LIGHT_GRAY);
	// conf both sides price
	xyPlot.setRangeAxis(0, rangeAxis);
	xyPlot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
	xyPlot.mapDatasetToRangeAxis(0, 0);

	chart = new JFreeChart(null, null, xyPlot, true);
	chart.setTitle(new org.jfree.chart.title.TextTitle("The title", new java.awt.Font("Serif", java.awt.Font.BOLD, 16)));

	setChart(chart);
	setPreferredSize(new Dimension(DEFAULT_CHART_HEIGHT, DEFAULT_CHART_HEIGHT));
	setZoomAroundAnchor(true);
	setDomainZoomable(true);
	setRangeZoomable(false);
	chart.setNotify(true);
	setMouseWheelEnabled(true);
	addMouseWheelListener(new MouseWheelListener() {

	    @Override
	    public void mouseWheelMoved(MouseWheelEvent e) {
		handleMaxAndMinDateVisible();
	    }
	});
	workingDraws = new WorkingDrawsChart(this);
	addChartMouseListener(new DrawFlexibleLineChartMouseListener(this, workingDraws));

    }

    private void handleMaxAndMinDateVisible() {
	Date dateMin = stockDataLoaded.getIntervalIncluded().getStartDate().minusDays(2).toDate();
	Date dateMax = stockDataLoaded.getIntervalIncluded().getEndDate().plusDays(20).toDate();

	DateAxis domainAxis = getDateAxis();
	Range range = domainAxis.getRange();
	Date minDateActual = new Date((long) range.getLowerBound());
	Date maxDateActual = new Date((long) range.getUpperBound());

	if (minDateActual.before(dateMin)) {
	    domainAxis.setMinimumDate(dateMin);
	}

	if (maxDateActual.after(dateMax)) {
	    domainAxis.setMaximumDate(dateMax);
	}
    }

    public void redraw(StockLoaded stockDataLoaded) {
	this.stockDataLoaded = stockDataLoaded;
	StockDrawn stockDrawn = new StockDrawn(TypeCours.CANDLESTICK);
	stockDrawn.draw(stockDataLoaded, this);
	chart.setTitle(stockDataLoaded.getStock().getStockNameAndIsin());
	DateAxis domainAxis = getDateAxis();
	domainAxis.setMinimumDate(stockDataLoaded.getIntervalIncluded().getStartDate().minusDays(2).toDate());
	domainAxis.setMaximumDate(stockDataLoaded.getIntervalIncluded().getEndDate().plusDays(20).toDate());

	// domainAxis.setRange(stockDataLoaded.getIntervalIncluded().getStartDate().toDate(), stockDataLoaded.getIntervalIncluded().getEndDate().toDate());
    }

    private DateAxis getDateAxis() {
	return (DateAxis) getXYPlot().getDomainAxis();
    }

    public XYPlot getXYPlot() {
	return (XYPlot) getChart().getPlot();
    }

    public DrawingStates getDrawingStates() {
	return drawingStates;
    }

    public DrawsChart getWorkingDraws() {
	return workingDraws;
    }

    @Override
    public void addChartMouseListener(ChartMouseListener listener) {
	super.addChartMouseListener(listener);
	chartMouseListeners.add(listener);
    }

    public void removeAllDrawingListenerOnChartPanel() {

	for (Iterator<ChartMouseListener> it = chartMouseListeners.iterator(); it.hasNext();) {
	    ChartMouseListener chartMouseListener = it.next();
	    removeChartMouseListener(chartMouseListener);
	    it.remove();
	}
    }

    public void clearWorkingDrawInProgress() {
	workingDraws.clearWorkingState();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
	if (!SwingUtilities.isRightMouseButton(event))
	    return;

	if (menuDetecterFiguresChandeliers != null) {
	    getPopupMenu().remove(menuDetecterFiguresChandeliers);
	}

	final List<ChartEntity> entitiesForPoint = getEntities(event);

	JPopupMenu popupMenu = new JPopupMenu();

	if (isContainedInstanceOf(entitiesForPoint, XYItemEntity.class)) {

	    final XYItemEntity xyItemEntity = findEntity(entitiesForPoint, XYItemEntity.class);
	    if (!(xyItemEntity.getDataset() instanceof OHLCDataset))
		return;

	    // Figure Chandeliers
	    menuDetecterFiguresChandeliers = new JMenuItem("detection figures chandeliers");
	    menuDetecterFiguresChandeliers.setForeground(Color.BLUE);
	    menuDetecterFiguresChandeliers.setFont(new Font("Serif", Font.BOLD, 15));
	    popupMenu.add(menuDetecterFiguresChandeliers);
	    getPopupMenu().add(menuDetecterFiguresChandeliers);
	    menuDetecterFiguresChandeliers.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    OHLCDataset dataset = (OHLCDataset) xyItemEntity.getDataset();
		    Number dateNumber = dataset.getX(xyItemEntity.getSeriesIndex(), xyItemEntity.getItem());

		    displayFiguresChandeliers(new LocalDate(dateNumber));
		}

	    });

	}

	if (popupMenu.getSubElements().length > 0) {
	    // popupMenu.show(this, java2Dx, java2Dy);
	}
	super.mouseReleased(event);
    }

    private void displayFiguresChandeliers(LocalDate localDate) {

	DefaultOHLCDataset allQuotations = stockDataLoaded.getDataset();

	// final int indexSelectedQuotation = allQuotations.getindexOf(localDate);
	//
	// int nbPrevQuotationToTake = 0;
	// if (periodUnit == PeriodUnit.DAILY) {
	// nbPrevQuotationToTake = NB_PREV_QUOTATION_FOR_DAILY_BASE;
	// }
	//
	// else if (periodUnit == PeriodUnit.WEEKLY) {
	// nbPrevQuotationToTake = NB_PREV_QUOTATION_FOR_WEEKLY_BASE;
	// }
	//
	// else {
	// throw new IllegalArgumentException();
	// }
	//
	// QuotationsList prevQuotations = new QuotationsList(allQuotations.subList(indexSelectedQuotation - nbPrevQuotationToTake, indexSelectedQuotation));
	//
	// List<JapaneseFigure> figures = JapaneseStickService
	// .rechercherToutesLesFiguresPossibles(allQuotations.get(indexSelectedQuotation), prevQuotations, true);
	//
	// String figuresMsg = "";
	// if (!figures.isEmpty()) {
	// figuresMsg = "UP FIGURE=\n" + renderStringFigures(figures);
	//
	// }
	//
	// figures = JapaneseStickService.rechercherToutesLesFiguresPossibles(allQuotations.get(indexSelectedQuotation), prevQuotations, false);
	//
	// if (!figures.isEmpty()) {
	// figuresMsg += "\n-----------------------------\nDOWN FIGURE=\n" + renderStringFigures(figures);
	//
	// }
	//
	// if (!figuresMsg.isEmpty()) {
	// JOptionPane.showMessageDialog(null, figuresMsg);
	// }
    }

    private <T> T findEntity(List<ChartEntity> chartEntities, Class class1) {

	for (ChartEntity chartEntity : chartEntities) {
	    if (chartEntity.getClass().equals(class1)) {
		return (T) chartEntity;
	    }

	}
	return null;
    }

    private boolean isContainedInstanceOf(List<ChartEntity> chartEntities, Class class1) {

	for (ChartEntity chartEntity : chartEntities) {
	    if (chartEntity.getClass().isAssignableFrom(class1)) {
		return true;
	    }

	}
	return false;
    }

    private List<ChartEntity> getEntities(MouseEvent event) {
	int java2Dx = event.getX();
	int java2Dy = event.getY();
	List<ChartEntity> entitiesForPoint = getEntitiesForPoint(java2Dx, java2Dy);
	return entitiesForPoint;
    }

    public List<ChartEntity> getEntitiesForPoint(final int viewX, final int viewY) {
	final ChartRenderingInfo info = getChartRenderingInfo();

	final List<ChartEntity> vecEntities = new ArrayList<ChartEntity>();
	if (info != null) {

	    final Insets insets = getInsets();
	    final double x = (viewX - insets.left) / getScaleX();
	    final double y = (viewY - insets.top) / getScaleY();
	    final EntityCollection entities = info.getEntityCollection();
	    final int numEntities = entities.getEntityCount();

	    for (int i = 0; i < numEntities; i++) {
		final ChartEntity entity = entities.getEntity(i);

		if (entity.getArea().contains(x, y)) {
		    vecEntities.add(entity);
		}
	    }

	}
	return vecEntities;

    }

}
