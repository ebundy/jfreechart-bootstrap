package davidhxxx.teach.jfreecharttuto.ui.draw.state;

import java.awt.Color;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import davidhxxx.teach.jfreecharttuto.ui.draw.FlexibleLine;
import davidhxxx.teach.jfreecharttuto.ui.draw.MyChartPanel;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
public class WorkingDrawsChart implements DrawsChart, Serializable {

    // de 10 à 100
    private static final int FIRST_ID_COUNTER = 10;

    private int nextDatasetIdCounter = FIRST_ID_COUNTER;

    // @XmlJavaTypeAdapter(FlexibleLineAdapter.class)
    @XmlElementWrapper(name = "flexibleLines")
    @XmlElement(name = "flexibleLine")
    private Map<Integer, FlexibleLine> flexibleLinesByIdDataSet = new HashMap<>();

    private MyChartPanel chartPanel;

    private DrawingStates drawingStates;

    // JAXB
    public WorkingDrawsChart() {

    }

    public WorkingDrawsChart(MyChartPanel panel) {
	this.chartPanel = panel;
	drawingStates = chartPanel.getDrawingStates();
    }

    @Override
    public void addFlexibleLineInProgress(FlexibleLineState flexibleLineState) {

//	ChartPoint endCurrentLine = flexibleLineState.getEndCurrentLine();

	int dataSetIndex = flexibleLineState.getIdDataSet();
	if (dataSetIndex == -1) {
	    dataSetIndex = getNextDataSetIndex();
	    flexibleLineState.setDataSetIndex(dataSetIndex);
	}

	addXyLineInGraph(new FlexibleLine(flexibleLineState), dataSetIndex, createXYLineRenderer(Color.BLACK));
    }
    
    private XYLineAndShapeRenderer createXYLineRenderer(Color color) {
	XYLineAndShapeRenderer lineRenderer;
	lineRenderer = new XYLineAndShapeRenderer(true, false);
	// lineRenderer.setBaseStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT,
	// BasicStroke.JOIN_BEVEL));
	lineRenderer.setSeriesPaint(0, color);
	return lineRenderer;
    }
    
    @Override
    public void endFlexibleLineInProgress(FlexibleLineState flexibleLineState) {
	FlexibleLine lineReference = new FlexibleLine(flexibleLineState.getStartCurrentLine(), flexibleLineState.getEndCurrentLine(), Color.BLUE);
	flexibleLinesByIdDataSet.put(flexibleLineState.getIdDataSet(), lineReference);
    }
    
    public void addXyLineInGraph(FlexibleLine flexibleLine, int dataSetIndex, XYLineAndShapeRenderer render) {

	XYSeries xySeriesLine = new XYSeries("flexible line " + dataSetIndex);
	xySeriesLine.add(flexibleLine.getStart());
	xySeriesLine.add(flexibleLine.getEnd());

	XYSeriesCollection xySeriesCollection = new XYSeriesCollection(xySeriesLine);

	chartPanel.getXYPlot().setDataset(dataSetIndex, xySeriesCollection);
	chartPanel.getXYPlot().setRenderer(dataSetIndex, render);

//	chartPanel.updateLegend();
    }

    
    private int getNextDataSetIndex() {
	nextDatasetIdCounter++;
	return nextDatasetIdCounter;
    }

    @Override
    public void clearWorkingState() {

	final int idDataSet = drawingStates.getFlexibleLineState().getIdDataSet();
	if (idDataSet != -1) {
	    drawingStates.getFlexibleLineState().clear();
	    chartPanel.getXYPlot().setDataset(idDataSet, null);
	}
    }
}
