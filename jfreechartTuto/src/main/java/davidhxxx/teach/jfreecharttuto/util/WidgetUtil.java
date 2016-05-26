package davidhxxx.teach.jfreecharttuto.util;

import java.awt.Dimension;

import javax.swing.JComponent;

public class WidgetUtil {

    public static void setUniqueSize(final JComponent component, int width, int height) {
	component.setPreferredSize(new Dimension(width, height));
	component.setSize(new Dimension(width, height));
	component.setMinimumSize(new Dimension(width, height));
	component.setMaximumSize(new Dimension(width, height));
    }
}
