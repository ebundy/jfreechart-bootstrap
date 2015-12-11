package davidhxxx.teach.jfreecharttuto.model.comparator;

import java.util.Comparator;

import davidhxxx.teach.jfreecharttuto.model.Quotation;


public class QuotationDateComparatorAsc implements Comparator<Quotation> {

    @Override
    public int compare(Quotation q1, Quotation q2) {

	if (q1.getDate().isBefore(q2.getDate()))
	    return -1;

	if (q1.getDate().isAfter(q2.getDate()))
	    return 1;

	return 0;
    }

}
