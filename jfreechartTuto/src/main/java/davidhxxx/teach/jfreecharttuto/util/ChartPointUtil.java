package davidhxxx.teach.jfreecharttuto.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.plot.XYPlot;

import davidhxxx.teach.jfreecharttuto.model.Quotation;
import davidhxxx.teach.jfreecharttuto.ui.draw.ChartPoint;
import davidhxxx.teach.jfreecharttuto.ui.draw.MyChartPanel;

public class ChartPointUtil {

    public static ChartPoint convertJavaPointToChartPoint(Point location, MyChartPanel chartPanel) {
	return convertJava2DToChartPoint(new Point2D.Double(location.getX(), location.getY()), chartPanel);
    }

    public static ChartPoint convertJava2DToChartPoint(Point2D source, MyChartPanel chartPanel) {
	Rectangle2D plotArea = chartPanel.getScreenDataArea();
	XYPlot xyPlot = chartPanel.getXYPlot();
	double chartX = xyPlot.getDomainAxis().java2DToValue(source.getX(), plotArea, xyPlot.getDomainAxisEdge());
	double chartY = xyPlot.getRangeAxis().java2DToValue(source.getY(), plotArea, xyPlot.getRangeAxisEdge());

	return new ChartPoint(chartX, chartY);

    }

    public static ChartPoint convertXJava2DToChartPoint(double x2d, MyChartPanel myChartPanel) {
	return convertJava2DToChartPoint(new Point2D.Double(x2d, 0), myChartPanel);

    }

    public static ChartPoint createChartPointFromBusinessValue(Quotation quotation, MyChartPanel chartPanel) {
	double x = (long) quotation.getDate().toDate().getTime();
	double y = quotation.getClose();

	ChartPoint chartPoint = new ChartPoint(x, y);

	return chartPoint;
    }
}
