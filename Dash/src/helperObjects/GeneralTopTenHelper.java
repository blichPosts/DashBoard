package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;

import Dash.BaseClass;

public class GeneralTopTenHelper extends BaseClass {

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


	// Format service number from the AJAX call 
	public static String formatPhoneNumber(String serviceNumber) {
		
		// ShowText("serviceNumber: " + serviceNumber);
		
		String numberFormatted = serviceNumber;
		
		// Remove letters, keep only numbers
		numberFormatted = numberFormatted.replaceAll("[^0-9]", "");

		// Remove leading and trailing spaces
		numberFormatted = numberFormatted.trim();
		
		// ShowText("Service Number Formatted: " + numberFormatted);
		
		return numberFormatted;
		
	}
	
	
	// Format phone number from UI (label on the vertical axis) 
	public static String formatPhoneNumberUI(String label) {
		
		// ShowText("**UI ** serviceNumber: " + label);
		
		String servNumberPart = "";
		String employeeNamePart = "";
		
		try {
			servNumberPart = label.split("[A-Z]")[0];  // label.split(" " + "[A-Z]")[0];
		} catch (ArrayIndexOutOfBoundsException e1) {
			
		}
		
		String[] tmpEmpNamePart = label.split("[0-9]");
		
		try {
			employeeNamePart = tmpEmpNamePart[tmpEmpNamePart.length-1];  // label.split("[0-9]" + " ")[1];
		} catch (ArrayIndexOutOfBoundsException e2) {
			
		}
		
		String numberFormatted = servNumberPart;
		
		// Remove blank spaces
		numberFormatted = numberFormatted.replace(" ", "");
		employeeNamePart = employeeNamePart.replace(" ", "");
		
		// Remove dashes
		numberFormatted = numberFormatted.replace("-", "");
		
		// Remove brackets ( )
		numberFormatted = numberFormatted.replace("(", "");
		numberFormatted = numberFormatted.replace(")", "");
		
		// Remove + sign
		numberFormatted = numberFormatted.replace("+", "");
		
		// Remove leading and trailing spaces
		numberFormatted = numberFormatted.trim();
		
		String labelFormatted = numberFormatted + employeeNamePart; 
		
		// ShowText("**UI ** Service Number Formatted: " + labelFormatted);
		
		return labelFormatted.trim();
		
	}
	
	
	public static String getOnlyPhoneNumberFormatted(String label) {
		
		// ShowText("**UI ** serviceNumber: " + label);
		
		String servNumberPart = "";
		
		try {
			servNumberPart = label.split(" " + "[A-Z]")[0];
		} catch (ArrayIndexOutOfBoundsException e1) {
			
		}
				
		String numberFormatted = servNumberPart;
		
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
				
		// ShowText("**UI ** Service Number Formatted: " + labelFormatted);
		
		return numberFormatted.trim();
		
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
	
	
}
