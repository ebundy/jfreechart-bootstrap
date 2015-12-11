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

    // Option
    private JMenuItem optionTelechargerCoursItem = new JMenuItem("Mettre à jour les cours");

    public MainFrameMenu() {

	// added menu items in Fichier
	optionsMenu.add(optionTelechargerCoursItem);
	optionsMenu.add(optionTelechargerCoursItem);

	// add menu bar
	add(optionsMenu);
	add(stockMenu);

	addListenerInFichierMenu();
    }

    private void addListenerInFichierMenu() {
	optionTelechargerCoursItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// File myFile = ApplicationDirs.BASE_DIR_FILE;
		// if (Desktop.isDesktopSupported()) {
		// try {
		// Desktop.getDesktop().open(myFile);
		// }
		// catch (IOException e1) {
		// e1.printStackTrace();
		// throw new IllegalArgumentException();
		// }
		// }
	    }
	});

    }

}
