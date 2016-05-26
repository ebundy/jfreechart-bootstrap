package davidhxxx.teach.jfreecharttuto.ui.draw.state;

import org.jfree.data.xy.XYDataItem;

import davidhxxx.teach.jfreecharttuto.ui.draw.ChartPoint;

public class FlexibleLineState {

    private ChartPoint startCurrentLine;
    private ChartPoint endCurrentLine;
    private int idDataSet = -1;

    public boolean isInProgress() {
	return startCurrentLine != null;
    }

    public void begin(ChartPoint chartPoint) {
	startCurrentLine = chartPoint;
    }

    public XYDataItem getStart() {
	return new XYDataItem(startCurrentLine.getX(), startCurrentLine.getY());
    }

    public XYDataItem getEnd() {
	return new XYDataItem(endCurrentLine.getX(), endCurrentLine.getY());
    }

    public ChartPoint getStartCurrentLine() {
	return startCurrentLine;
    }

    public ChartPoint getEndCurrentLine() {
	return endCurrentLine;
    }

    public void clear() {
	startCurrentLine = null;
	endCurrentLine = null;
	idDataSet = -1;
    }

    public int getIdDataSet() {
	return idDataSet;

    }

    public void setIdCurrentLine(Integer currentIdLine) {
	this.idDataSet = currentIdLine;
    }

    public void updateEnd(ChartPoint chartPoint) {
	this.endCurrentLine = chartPoint;
    }

    public void setDataSetIndex(int dataSetIndex) {
	this.idDataSet = dataSetIndex;

    }

}
