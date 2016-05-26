package davidhxxx.teach.jfreecharttuto.ui.draw.state;

public interface DrawsChart {

    void addFlexibleLineInProgress(FlexibleLineState flexibleLineState);

    void endFlexibleLineInProgress(FlexibleLineState flexibleLineState);

    void clearWorkingState();


}
