package helperObjects;

import Dash.BaseClass;

public class LocalizationHelper extends BaseClass {

	
	public static String getLocalizedMonthYear(String month, String year, String languageTag) {

		String monthYear = "";
		
		if (languageTag.equals("[ja]")) {
			
			monthYear = year + " " + languageTag + month;
			
		} else {
			
			monthYear = languageTag + month + " " + year;
			
		}
				
		return monthYear;
	}

	
	
}
