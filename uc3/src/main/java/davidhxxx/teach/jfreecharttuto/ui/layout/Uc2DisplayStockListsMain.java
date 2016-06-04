package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalListService;
import davidhxxx.teach.jfreecharttuto.util.WidgetUtil;

@SuppressWarnings("serial")
public class Uc2DisplayStockListsMain extends JFrame {

    // buttons
    private JComboBox<String> comboListOfStockList;

    // containers
    private JSplitPane hSplitPaneGeneral;

    private JToolBar toolBar;

    private JScrollPane scrollPaneForReferenceChart;

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		new Uc2DisplayStockListsMain("trading bootstrap app");
	    }

	});
    }

    public Uc2DisplayStockListsMain(String name) {

	super(name);
	Locale.setDefault(new Locale("fr"));
	UIManager.put("ToolTip.font", new Font("Serif", Font.BOLD, 18));

	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	hSplitPaneGeneral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	hSplitPaneGeneral.setDividerLocation(150);

	add(hSplitPaneGeneral, BorderLayout.CENTER);

	initToolBar();
	hSplitPaneGeneral.setLeftComponent(toolBar);
	hSplitPaneGeneral.setRightComponent(scrollPaneForReferenceChart);
	setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initToolBar() {
	toolBar = new JToolBar("Still draggable");
	toolBar.setOrientation(SwingConstants.VERTICAL);

	comboListOfStockList = createComboListOfListOfStock();
	toolBar.add(comboListOfStockList);
	createSperationMoyenne(toolBar);
    }

    private void createSperationMoyenne(JToolBar toolBar) {
	toolBar.add(new Separator(new Dimension(1, 30)));
    }

    private JComboBox<String> createComboListOfListOfStock() {
	final JComboBox<String> comboListOfStockList = new JComboBox<String>() {
	    @Override
	    public Dimension getMaximumSize() {
		return getPreferredSize();
	    }
	};

	WidgetUtil.setUniqueSizeAndCenter(comboListOfStockList, 120, 30);
	List<String> listOfListOfStockName = LocalListService.getInstance().getNamesOfLists();
	for (String listOfStockName : listOfListOfStockName) {
	    comboListOfStockList.addItem(listOfStockName);
	}

	((JLabel) comboListOfStockList.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

	return comboListOfStockList;
    }

}
