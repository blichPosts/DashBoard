package helperObjects;

import java.util.List;

public class AncestorsInfo {

	String ordinalMonth;
	String ordinalYear;
	String ordinalYearMonth;
	List<String> ancestorsNames;
	
	
	public String getOrdinalMonth() {
		return ordinalMonth;
	}
	public void setOrdinalMonth(String ordinalMonth) {
		this.ordinalMonth = ordinalMonth;
	}
	public String getOrdinalYear() {
		return ordinalYear;
	}
	public void setOrdinalYear(String ordinalYear) {
		this.ordinalYear = ordinalYear;
	}
	public String getOrdinalYearMonth() {
		return ordinalYearMonth;
	}
	public void setOrdinalYearMonth(String ordinalYearMonth) {
		this.ordinalYearMonth = ordinalYearMonth;
	}
	public List<String> getAncestorsNames() {
		return ancestorsNames;
	}
	public void setAncestorsNames(List<String> ancestorsNames) {
		this.ancestorsNames = ancestorsNames;
	} 
	
	
}
