package davidhxxx.teach.jfreecharttuto.ui.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.HorizontalAlignment;

import davidhxxx.teach.jfreecharttuto.model.StockLoaded;
import davidhxxx.teach.jfreecharttuto.ui.draw.StockDrawn.TypeCours;

@SuppressWarnings("serial")
public class MyChartPanel extends ChartPanel {

    private static final double DOMAIN_LOWER_AND_UPPER_MARGIN = 0.015;
    public static final int DEFAULT_CHART_HEIGHT = 300;
    public static final int DEFAULT_CHART_WIDTH = 800;

    private JFreeChart chart;

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

    }

    public void redraw(StockLoaded stockDataLoaded) {
	StockDrawn stockDrawn = new StockDrawn(TypeCours.CANDLESTICK);
	stockDrawn.draw(stockDataLoaded, this);
	chart.setTitle(stockDataLoaded.getStock().getStockNameAndIsin());
    }

    public XYPlot getXYPlot() {
	return (XYPlot) getChart().getPlot();
    }

}
