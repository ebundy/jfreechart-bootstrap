package davidhxxx.teach.jfreecharttuto.model.update;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import davidhxxx.teach.jfreecharttuto.model.Quotation;
import davidhxxx.teach.jfreecharttuto.model.comparator.QuotationDateComparatorAsc;

public class QuotationsToImport {

    private Map<Integer, Map<Integer, TreeSet<Quotation>>> quotationsByYearAndMonthSorted = new HashMap<>();

    public void addSingleQuotationIfNotDateExit(Quotation q) {

	int year = q.getDate().getYear();
	int month = q.getDate().getMonthOfYear();

	Map<Integer, TreeSet<Quotation>> quotationsByMonth = quotationsByYearAndMonthSorted.get(year);
	if (quotationsByMonth == null) {
	    quotationsByMonth = new HashMap<>();
	    quotationsByYearAndMonthSorted.put(year, quotationsByMonth);
	}

	TreeSet<Quotation> quotationsForMonth = quotationsByMonth.get(month);
	if (quotationsForMonth == null) {
	    quotationsForMonth = new TreeSet<>(new QuotationDateComparatorAsc());
	    quotationsByMonth.put(month, quotationsForMonth);
	}

	if (!CollectionUtils.isEmpty(quotationsForMonth)) {
	    if (quotationsForMonth.contains(q)) {
		return;
	    }
	}

	quotationsForMonth.add(q);

    }

    public Set<Integer> getMonths(Integer year) {
	return quotationsByYearAndMonthSorted.get(year).keySet();
    }

    public Set<Integer> getYears() {
	return quotationsByYearAndMonthSorted.keySet();
    }

    public Set<Quotation> getQuotationsForYearAndMonth(Integer year, Integer currentMonth) {
	return quotationsByYearAndMonthSorted.get(year).get(currentMonth);
    }

    @Override
    public String toString() {
	return "Quotations [quotationsByYearSorted=" + quotationsByYearAndMonthSorted + "]";
    }

}
