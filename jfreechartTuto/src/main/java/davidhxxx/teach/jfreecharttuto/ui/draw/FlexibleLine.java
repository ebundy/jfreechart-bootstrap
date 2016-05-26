package davidhxxx.teach.jfreecharttuto.ui.draw;

import java.awt.Color;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jfree.data.xy.XYDataItem;

import davidhxxx.teach.jfreecharttuto.ui.draw.state.FlexibleLineState;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.NONE)
public class FlexibleLine implements Serializable {

    @XmlElement
    private ChartPoint startCurrentLine;

    @XmlElement
    private ChartPoint endCurrentLine;

    // JAXB Compliance
    public FlexibleLine() {
    }

    public FlexibleLine(ChartPoint startCurrentLine, ChartPoint endCurrentLine, Color color) {
	this.startCurrentLine = startCurrentLine;
	this.endCurrentLine = endCurrentLine;
    }

    public FlexibleLine(FlexibleLineState flexibleLineState) {
	this.startCurrentLine = flexibleLineState.getStartCurrentLine();
	this.endCurrentLine = flexibleLineState.getEndCurrentLine();
    }

    public ChartPoint getStartCurrentLine() {
	return startCurrentLine;
    }

    public ChartPoint getEndCurrentLine() {
	return endCurrentLine;
    }

    @Override
    public String toString() {
	return "FlexibleLine [startCurrentLine=" + startCurrentLine + ", endCurrentLine=" + endCurrentLine +"]";
    }


    public XYDataItem getStart() {
	return new XYDataItem(startCurrentLine.getX(), startCurrentLine.getY());
    }

    public XYDataItem getEnd() {
	return new XYDataItem(endCurrentLine.getX(), endCurrentLine.getY());
    }

}
