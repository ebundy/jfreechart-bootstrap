package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MainFrameMenu extends JMenuBar {

    private JMenu optionsMenu = new JMenu("Options");
    private JMenu stockMenu = new JMenu("Stock");
    private JMenu interrogationPointMenu = new JMenu("?");

    // Option
    private JMenuItem optionTelechargerCoursItem = new JMenuItem("Mettre à jour les cours");

    // Stock
    private JMenuItem stockOpenQuotationsDirItem = new JMenuItem("Ouvrir le répertoire des quotations de la valeur");
    private JMenuItem stockCopyIsinInClipboardItem = new JMenuItem("Copier le code isin dans le presse-papier");
    private JMenuItem stockCopyStockNameInClipboardItem = new JMenuItem("Copier le libelle du stock dans le presse-papier");

    public MainFrameMenu() {

	// add menu items in Fichier
	optionsMenu.add(optionTelechargerCoursItem);
	optionsMenu.add(optionTelechargerCoursItem);

	// add menu items in Fichier
	stockMenu.add(stockCopyStockNameInClipboardItem);
	stockMenu.add(stockCopyIsinInClipboardItem);
	stockMenu.add(stockOpenQuotationsDirItem);

	// add menu bar
	add(optionsMenu);
	add(stockMenu);
	add(interrogationPointMenu);

	addListenerInFichierMenu();
    }

    private void addListenerInFichierMenu() {
	optionTelechargerCoursItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
	    }
	});

    }

}
