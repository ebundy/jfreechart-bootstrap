package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import davidhxxx.teach.jfreecharttuto.dataservice.LocalQuoteService;
import davidhxxx.teach.jfreecharttuto.model.Stock;
import davidhxxx.teach.jfreecharttuto.model.update.QuotationsToImport;
import davidhxxx.teach.jfreecharttuto.model.update.QuoteFileInformation;
import davidhxxx.teach.jfreecharttuto.service.QuotationsConvertorService;
import davidhxxx.teach.jfreecharttuto.util.ApplicationDirs;
import davidhxxx.teach.jfreecharttuto.util.DateUtil;

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
		File dirBaseProposed = ApplicationDirs.QUOTATIONS_TO_IMPORT_DIR;

		JFileChooser fc = new JFileChooser(dirBaseProposed);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int res = fc.showOpenDialog(null);
		File csvSelected = null;

		if (res == JFileChooser.APPROVE_OPTION) {
		    csvSelected = fc.getSelectedFile();
		}

		if (csvSelected == null) {
		    JOptionPane.showMessageDialog(null, "Pas de fichier CSV choisi. ", "Aborting...", JOptionPane.WARNING_MESSAGE);
		    return;
		}

		List<QuoteFileInformation> quoteFileInformations = new ArrayList<>();

		if (!csvSelected.exists()) {
		    JOptionPane.showMessageDialog(null, "Lee fichier CSV indiqué n'existe pas ", "Aborting...", JOptionPane.WARNING_MESSAGE);
		    return;
		}
		
		if (csvSelected.isDirectory()) {
		    File[] files = csvSelected.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
			    return file.getPath().toLowerCase().endsWith(".csv");
			}
		    });

		    if (files.length >= 1) {
			for (File file : files) {
			    QuoteFileInformation quoteFileInformation = new QuoteFileInformation(file.getPath());
			    quoteFileInformations.add(quoteFileInformation);
			}
		    }
		}

		else {
		    if (csvSelected.getPath().toLowerCase().endsWith(".csv")) {
			QuoteFileInformation quoteFileInformation = new QuoteFileInformation(csvSelected.getPath());
			quoteFileInformations.add(quoteFileInformation);
		    }
		}

		if (quoteFileInformations.size()==0){
		    JOptionPane.showMessageDialog(null, "Aucun fichier CSV trouvé à l'emplacement indiqué." , "Aborting...", JOptionPane.WARNING_MESSAGE);
		    return;
		}
		
//		// date minimum de prise en compte
//		String dateOpenPosition = JOptionPane.showInputDialog(null, "Entrez la date minimale de prise en compte (dd/MM/YYYY)\\nou rien si pas de contrainte à appliquer");
//		DateTime dateEnter = null;

//		if (!StringUtils.isEmpty(dateOpenPosition)) {
//		    try {
//			dateEnter = DateUtil.parseDateFrWithSlash(dateOpenPosition);
//		    }
//		    catch (IllegalArgumentException errorDateInput) {
//			JOptionPane.showMessageDialog(null, "la date entrée " + dateOpenPosition + " n'est pas dans le format attendu");
//			return;
//		    }
//		}

		TreeMap<Stock, QuotationsToImport> quotationsByStock = QuotationsConvertorService.getInstance().convertFromMyProviderToQuotations(quoteFileInformations);
		LocalQuoteService.getInstance().saveQuotationForAllStocks(quotationsByStock);
		
		String stockLabel="stock";
		if (quotationsByStock.size()>1){
		    stockLabel="stocks";
		}
		JOptionPane.showMessageDialog(null, quotationsByStock.size() +  " "  + stockLabel + " mis à jour");
		

	    }
	});

    }

}
