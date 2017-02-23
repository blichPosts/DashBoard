package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;

public class GeneralTopTenHelper {

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


	// Format number NEW 
	public static String formatPhoneNumber(String serviceNumber) {
		
		System.out.println("serviceNumber: " + serviceNumber);
		
		String numberFormatted = serviceNumber;
		
		// Remove blank spaces
		numberFormatted = numberFormatted.replace(" ", "");
		
		// Remove dashes
		numberFormatted = numberFormatted.replace("-", "");
		
		// Remove brackets ( )
		numberFormatted = numberFormatted.replace("(", "");
		numberFormatted = numberFormatted.replace(")", "");
		
		// Remove + sign
		numberFormatted = numberFormatted.replace("+", "");
		
		// Remove leading and trailing spaces
		numberFormatted = numberFormatted.trim();
		
		System.out.println("Service Number Formatted: " + numberFormatted);
		
		return numberFormatted;
		
	}
	
	
	// Temporal solution - Will need to review it with real data 
	public static boolean allValuesAreZero(List<FleetTopTenData> topTenValues) {
		
		boolean zeroValues = false;
		
		for (FleetTopTenData data: topTenValues) {
			
			if (data.getValue() == 0)
				zeroValues = true;
		}
		
		return zeroValues;
		
	}
	
	public static boolean allValuesAreZeroHierarchy(List<HierarchyTopTenData> topTenValues) {
		
		boolean zeroValues = false;
		
		for (HierarchyTopTenData data: topTenValues) {
			
			if (data.getValue() == 0)
				zeroValues = true;
		}
		
		return zeroValues;
		
	}
	
	
	// Format Number OLD - Converts a service number with format 203-555-1119 to (203) 555-1119
//	public static String formatPhoneNumber(String serviceNumber) {
//		
//		System.out.println("serviceNumber: " + serviceNumber);
//		
//		String[] serviceNumberParts = serviceNumber.split("-"); 
//		String numberFormatted  ="";
//		
//		if (serviceNumberParts.length == 3) {
//		
//			String prefix = serviceNumberParts[0];
//			String number = serviceNumberParts[1] + "-" + serviceNumberParts[2];
//			
//			numberFormatted = "(" + prefix + ") " + number;
//			
//		} 
//		
//		if (serviceNumberParts.length == 2 && serviceNumberParts[0].contains("(") && serviceNumberParts[0].contains(")")) {
//			
//			numberFormatted = serviceNumberParts[0].replace(" ", "") + "-" + serviceNumberParts[1].replace(" ", "");
//			
//		}
//		
//		if (serviceNumberParts.length == 1) {
//		
//			numberFormatted = serviceNumberParts[0].replace("+", "").replace(" ", "");
//			
//		}
//		
//		System.out.println("serviceNumberFormatted: " + numberFormatted.trim());
//		return numberFormatted.trim();
//		
//	}
	
	
}
