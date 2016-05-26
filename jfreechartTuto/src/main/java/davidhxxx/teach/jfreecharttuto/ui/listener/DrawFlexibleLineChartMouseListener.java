package davidhxxx.teach.jfreecharttuto.ui.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import davidhxxx.teach.jfreecharttuto.ui.draw.ChartPoint;
import davidhxxx.teach.jfreecharttuto.ui.draw.MyChartPanel;
import davidhxxx.teach.jfreecharttuto.ui.draw.state.DrawsChart;
import davidhxxx.teach.jfreecharttuto.ui.draw.state.FlexibleLineState;
import davidhxxx.teach.jfreecharttuto.util.ChartPointUtil;

public final class DrawFlexibleLineChartMouseListener implements ChartMouseListener {

    private static Logger LOGGER = LoggerFactory.getLogger(DrawFlexibleLineChartMouseListener.class);

    private final MyChartPanel myChartPanel;

    private DrawsChart drawChart;

    public DrawFlexibleLineChartMouseListener(MyChartPanel myChartPanel, DrawsChart drawChart) {
	this.myChartPanel = myChartPanel;
	this.drawChart = drawChart;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {

	MouseEvent mouseEvent = event.getTrigger();
	if (!SwingUtilities.isLeftMouseButton(mouseEvent))
	    return;

	Point location = mouseEvent.getPoint();

	final FlexibleLineState flexibleLineState = myChartPanel.getDrawingStates().getFlexibleLineState();
	boolean isInProgress = flexibleLineState.isInProgress();

	final ChartPoint chartPoint = ChartPointUtil.convertJavaPointToChartPoint(location, myChartPanel);

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("point2D=" + location.toString());
	    LOGGER.debug("chartPoint=" + chartPoint);
	}

	if (!isInProgress) {
	    flexibleLineState.begin(chartPoint);
	}

	else if (isInProgress) {
	    flexibleLineState.updateEnd(chartPoint);
	    drawChart.endFlexibleLineInProgress(flexibleLineState);
	    flexibleLineState.clear();
	}

    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {

	Point location = event.getTrigger().getPoint();

	final FlexibleLineState flexibleLineState = myChartPanel.getDrawingStates().getFlexibleLineState();
	if (!flexibleLineState.isInProgress())
	    return;

	ChartPoint chartPoint = ChartPointUtil.convertJava2DToChartPoint(location, myChartPanel);

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug("point2D=" + location.toString());
	    LOGGER.debug("chartPoint=" + chartPoint);
	}

	flexibleLineState.updateEnd(chartPoint);
	drawChart.addFlexibleLineInProgress(flexibleLineState);
    }

}