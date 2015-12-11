package davidhxxx.teach.jfreecharttuto.ui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ErrorDialog extends JDialog {

    private static Logger LOGGER = LoggerFactory.getLogger(ErrorDialog.class);

    public ErrorDialog(String title, Throwable throwable) {
	consturctorWithThrowableCommon(throwable, title);
    }

    public ErrorDialog(String title, String params, Throwable throwable) {

	final String titleAndParam = title + " \n\nparams=\n" + params;
	LOGGER.error(titleAndParam, throwable);
	consturctorWithThrowableCommon(throwable, titleAndParam);
    }

    private void consturctorWithThrowableCommon(Throwable throwable, String title) {

	setModal(true);
	setSize(800, 600);

	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());

	// NORTH
	final JTextArea titleTextArea = new JTextArea(title);
	JScrollPane titleScrollPane = new JScrollPane(titleTextArea);
	titleTextArea.setLineWrap(true);
	titleScrollPane.setMinimumSize(new Dimension(600, 100));
	titleScrollPane.setMaximumSize(new Dimension(600, 100));
	titleTextArea.setMinimumSize(new Dimension(600, 100));
	titleTextArea.setMaximumSize(new Dimension(600, 100));
	panel.add(titleScrollPane, BorderLayout.NORTH);

	if (throwable != null) {
	    // CENTER
	    final JLabel labelExceptionType = new JLabel("Exception de type : " + throwable.getClass());
	    labelExceptionType.setMinimumSize(new Dimension(600, 100));
	    panel.add(labelExceptionType, BorderLayout.CENTER);

	    // SOUTH
	    String fullStackTrace = ExceptionUtils.getFullStackTrace(throwable);
	    JScrollPane listPane = new JScrollPane(new JTextArea("StackTrace : " + fullStackTrace));
	    listPane.setMaximumSize(new Dimension(600, 200));
	    listPane.setPreferredSize(new Dimension(600, 200));
	    panel.add(listPane, BorderLayout.SOUTH);
	}

	add(panel);
	setVisible(true);
    }

    // public static void main(String[] args) {
    // new ErrorDialog("caca", new IllegalArgumentException());
    // }
}
