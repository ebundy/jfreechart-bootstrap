package davidhxxx.teach.jfreecharttuto.ui.draw.state;

public class DrawingStates {

    private FlexibleLineState flexibleLineState = new FlexibleLineState();

    public FlexibleLineState getFlexibleLineState() {
	return flexibleLineState;
    }

    public boolean isInProgress() {

	if (flexibleLineState.isInProgress())
	    return true;

	return false;
    }

}