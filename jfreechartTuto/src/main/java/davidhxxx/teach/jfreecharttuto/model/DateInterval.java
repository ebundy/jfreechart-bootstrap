package davidhxxx.teach.jfreecharttuto.model;

import org.joda.time.LocalDate;

public class DateInterval {

    private LocalDate startDate;

    private LocalDate endDate;

    public DateInterval(LocalDate start, LocalDate end) {

	if (start != null && end != null && start.isAfter(end))
	    throw new IllegalArgumentException("le param start doit être située avant dans temps que le param end");

	startDate = start;
	endDate = end;
    }

    public DateInterval(DateInterval dateIntervalInput) {
	this(dateIntervalInput.getStartDate(), dateIntervalInput.getEndDate());
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
	result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	DateInterval other = (DateInterval) obj;
	if (endDate == null) {
	    if (other.endDate != null)
		return false;
	}
	else if (!endDate.equals(other.endDate))
	    return false;
	if (startDate == null) {
	    if (other.startDate != null)
		return false;
	}
	else if (!startDate.equals(other.startDate))
	    return false;
	return true;
    }

}
