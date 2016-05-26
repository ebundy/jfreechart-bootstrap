package davidhxxx.teach.jfreecharttuto.ui.draw;

import java.awt.geom.Point2D;
import java.io.Serializable;

import davidhxxx.teach.jfreecharttuto.model.Quotation;

public class ChartPoint implements Serializable {

    private double x;
    private double y;

    // JAXB Compliance
    public ChartPoint() {
    }

    public ChartPoint(double x, double y) {
	super();
	this.x = x;
	this.y = y;
    }

    public double getX() {
	return x;
    }

    public void setX(double x) {
	this.x = x;
    }

    public double getY() {
	return y;
    }

    public void setY(double y) {
	this.y = y;
    }

    public Point2D toPoint2D() {
	return new Point2D.Double(x, y);
    }

    @Override
    public String toString() {
	return "ChartPoint [x=" + x + ", y=" + y + "]";
    }


}
