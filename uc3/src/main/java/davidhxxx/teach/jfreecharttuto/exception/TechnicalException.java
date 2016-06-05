package davidhxxx.teach.jfreecharttuto.exception;

@SuppressWarnings("serial")
public class TechnicalException extends RuntimeException {

    public TechnicalException(String string, Throwable e) {
	super(string, e);
    }

    public TechnicalException(Exception e) {
	super(e);
    }

    public TechnicalException(String string) {
	super(string);
    }

}
