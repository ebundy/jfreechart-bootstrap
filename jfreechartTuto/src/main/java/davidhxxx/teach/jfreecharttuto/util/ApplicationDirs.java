package davidhxxx.teach.jfreecharttuto.util;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.prefs.CsvPreference;

public class ApplicationDirs {


    static {

	Configuration config = null;
	try {
	    config = new PropertiesConfiguration("config.properties");
	}
	catch (ConfigurationException e) {
	    throw new IllegalArgumentException("Error during configuration file loading");
	}

	String appBaseDir = config.getString("app.base.dir");
	if (StringUtils.isEmpty(appBaseDir)) {
	    throw new IllegalArgumentException("app.base.dir property must be valued in config.properties");
	}

	File repertoireBase = new File(appBaseDir);
	if (!repertoireBase.exists()) {
	    throw new IllegalArgumentException("directory " + repertoireBase + " doesn't exist");
	}

	if (!repertoireBase.isDirectory()) {
	    throw new IllegalArgumentException("the path " + repertoireBase + " is not a directory");
	}

	BASE_DIR = appBaseDir;
    }

    public static final String BASE_DIR;

    public static final File QUOTATIONS_DIR = new File(ApplicationDirs.BASE_DIR + "/quotes/");
    
    public static final File QUOTATIONS_TO_IMPORT_DIR = new File(ApplicationDirs.BASE_DIR + "/quotes-to-import/");

    public static CsvPreference getCsvPreference(CsvPreference csvPreference) {
	return new CsvPreference.Builder(csvPreference).skipComments(new CommentStartsWith("#")).surroundingSpacesNeedQuotes(true).build();
    }

    public static final CsvPreference MY_CVS_PREFERENCE = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
}
