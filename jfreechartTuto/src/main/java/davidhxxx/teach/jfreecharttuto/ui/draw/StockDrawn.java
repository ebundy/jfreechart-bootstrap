package davidhxxx.teach.jfreecharttuto.ui.draw;

import java.awt.Color;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;

import davidhxxx.teach.jfreecharttuto.model.StockLoaded;

public class StockDrawn implements IndicatorDrawn {

    public enum TypeCours {
	CANDLESTICK, LINE;
    }

    private TypeCours typeCours;

    public StockDrawn(TypeCours typeCours) {
	if (typeCours == null) {
	    throw new IllegalArgumentException("le param typeCours doit être non null");
	}
	this.typeCours = typeCours;
    }

    @Override
    public void draw(StockLoaded stockLoaded, MyChartPanel chartPanel) {

	XYPlot xyPlot = chartPanel.getXYPlot();

	if (typeCours == TypeCours.CANDLESTICK) {
	    CandlestickRenderer renderer = new CandlestickRenderer();
	    renderer.setSeriesPaint(0, Color.BLACK);
	    renderer.setDrawVolume(true);
	    renderer.setVolumePaint(Color.WHITE);
	    renderer.setUpPaint(Color.GREEN);
	    renderer.setDownPaint(Color.RED);
	    xyPlot.setRenderer(0, renderer);
	}

	else if (typeCours == TypeCours.LINE) {
	    StandardXYItemRenderer rendererMa = new StandardXYItemRenderer();
	    rendererMa.setSeriesPaint(0, Color.BLACK);
	    xyPlot.setRenderer(0, rendererMa);
	}

	xyPlot.setDataset(0, stockLoaded.getDataset());

    }

}
