package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;

public class FleetTopTenHelper {

	public static int categoryExpenseAll = 1;
	public static int categoryExpenseVoice = 2;
	public static int categoryExpenseData = 3;
	public static int categoryExpenseMessages = 4;
	public static int categoryExpenseRoaming = 5;
	
	public static int categoryDomesticUsageVoice = 1;
	public static int categoryDomesticUsageVoiceOverage = 2;
	public static int categoryDomesticUsageData = 3;
	public static int categoryDomesticUsageMessages = 4;
	
	public static int categoryRoamingUsageVoice = 1;
	public static int categoryRoamingUsageData = 2;
	public static int categoryRoamingUsageMessages = 3;
	
	public static int expenseChart = 0;
	public static int domesticUsageChart = 1;
	public static int roamingUsageChart = 2;
	

	
	public static void verifyValuesSortedDescendantOrder(List<String> valuesFromChart) {
		
		List<Double> valuesSorted = new ArrayList<>();
		List<Double> originalValues = new ArrayList<>();
		
		for (String value: valuesFromChart) {
			
			valuesSorted.add(Double.parseDouble(value));
			originalValues.add(Double.parseDouble(value));
			
		}
		
		Collections.sort(valuesSorted);
		Collections.reverse(valuesSorted);
		
		Assert.assertEquals(originalValues, valuesSorted);
				
	}

	
	// Converts a service number with format 203-555-1119 to (203) 555-1119
	public static String formatPhoneNumber(String serviceNumber) {
		
		String[] serviceNumberParts = serviceNumber.split("-"); 
		String prefix = serviceNumberParts[0];
		String number = serviceNumberParts[1] + "-" + serviceNumberParts[2];
		String numberFormatted = "(" + prefix + ") " + number; 
		return numberFormatted;
		
	}
	
	
}
