package zAnaNoCommit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelperMultipleVendors;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class TestFunctionsAnaNoCommit extends BaseClass {

	public static void main(String[] args) throws ParseException {

		System.out.println();
		
		//System.out.println("1024 ^2 = " + Math.pow(1024, 2));
		
		/*
		convertVoiceUnits(75);
		convertVoiceUnits(786.3);
		convertVoiceUnits(42563.369);
		convertVoiceUnits(2000000.0);
		convertVoiceUnits(2323256446.1);
		convertVoiceUnits(23232564465555.0);
		convertVoiceUnits(2102658);
		getNumericValue("36K min");
		getNumericValueWithPrefixes("36K min");
		
		String s1 = "Tangoe, Inc. Canada";
		System.out.println(s1);
		System.out.println(removePunctuationCharacters(s1));
		
		s1 = "Tangoe, Inc.";
		System.out.println(s1);
		System.out.println(removePunctuationCharacters(s1));
		
		s1 = "AT&T Mobility ";
		System.out.println(s1);
		System.out.println(removePunctuationCharacters(s1));
		
		s1 = "SingTel Singapore";
		System.out.println(s1);
		System.out.println(removePunctuationCharacters(s1));
		
		long test = Math.round(Double.parseDouble("2.3"));
		System.out.println(Long.toString(test));
		
		convertDataUnitToGbNoDecimalPoint(123456789);
		
		List<String> names = new ArrayList<>();
		names.add("Telstra Australia");
		names.add("Vivo Brazil");
		//names.add("Telcel Mexico");
		names.add("SingTel Singapore");
		//names.add("Etisalat");
		//names.add("AT&T Mobility");
//		names.add("O2 UK");
		sortVendorsAlphabetically(names);
		*/
		
		
		String monthYearSelectedString = "January 2017";
	//	String[] monthYearSelectedInt = CommonTestStepActions.getMonthYearInteger(monthYearSelectedString);
//		System.out.println("monthYearSelectedInt: " + monthYearSelectedInt[0] + "-" + monthYearSelectedInt[1]);
		
		String label = "(603) 560-6572 G.Lomas";
		formatPhoneNumberUI(label);
		
	}
	
	
	public static void sortVendorsAlphabetically(List<String> names) {
		
//		UsageOneMonth[] vendorsSortedTmp = new UsageOneMonth[listVendorsSelectedData.size()];
		String[] vendorNames = new String[names.size()];
		
		System.out.println("Unsorted list:");
		
		for (int i = 0; i < names.size(); i++){
			vendorNames[i] = names.get(i);
//			vendorsSortedTmp[i] = listVendorsSelectedData.get(i);
			System.out.println(vendorNames[i]);
		}
		
		int limit = vendorNames.length;
		
		
		for (int j = 1; j < limit; j++){
			
			for (int i = 0; i < limit; i++){
				
				//System.out.println("VendorsName size: " + vendorNames.length);
							
				if (vendorNames[i].compareTo(vendorNames[j]) > 1){
					
//					System.out.println("i: " + i + ", j:" + j);
//					System.out.println("Vendors not sorted : " + vendorNames[i] + ", " + vendorNames[j]);
					String vendorTmp = vendorNames[i];
					vendorNames[i] = vendorNames[j];
					vendorNames[j] = vendorTmp;
//					System.out.println("Vendors sorted ?: " + vendorNames[i] + ", " + vendorNames[j]);
//					UsageOneMonth usageTmp = vendorsSortedTmp[i];
//					vendorsSortedTmp[i] = vendorsSortedTmp[j];
//					vendorsSortedTmp[j] = usageTmp;
					
				}
				
			}
		
		}
			
		System.out.println("\n" + "** Sorted list:");
		
		List<UsageOneMonth> listUsageVendorsSorted = new ArrayList<>();
		
		for (int i = 0; i < vendorNames.length; i++){
			System.out.println(vendorNames[i]);
//			listUsageVendorsSorted.add(vendorsSortedTmp[i]);
		}
		
//		return listUsageVendorsSorted;
		
	}
	
	
	
	public static String convertDataUnitToGbNoDecimalPoint(double amount) {
		
		String amountConverted = "";
		double tmpAmount = 0.0;
		
		System.out.println("Original amount: " + amount + " KB");
		tmpAmount = amount / Math.pow(1024, 2);
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		long roundedValue = Math.round(tmpAmount);
		amountConverted = Long.toString(roundedValue);
		System.out.println("Value rounded: " + amountConverted  + " GB");
		
		return amountConverted;
		
	}
	
	public static String removePunctuationCharacters(String string){
				
		return string.replace(".", "").replace("\"", "").replace(",",""); 
		
	}
	
	
	public static String convertVoiceUnits(double amount){
		
		String amountConverted = "";
		String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		System.out.println("Original amount: " + amount);
			
		if (amount < 1000.0){
			tmpAmount = amount;
		}
		else if (amount > 1000.0 && amount < 1000000.0){
			tmpAmount = amount / 1000;
			unit = "K";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / 1000000;
			unit = "M";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / 1000000000;
			unit = "G";
		}
		else if (amount > 1000000000000.0){
			tmpAmount = amount / 1000000000000.0;
			unit = "T";
		}
		
		
		System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue()) + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue()) + unit;
				
			}
			
		}else{
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue) + unit;
						
		}
		
		
		System.out.println("Value rounded for KPI: " + amountConverted);
		
		return amountConverted;
		
	}
	
	
	public static String getNumericValue(String stringValue) {
		
		String unitK = "K";
		String unitM = "M";
		String unitG = "G";
		String unitT = "T";
		
		String valueNoUnits = "";
		
		System.out.println("Original Value: " + stringValue);
		
		if (stringValue.contains(unitK)){
			
			String[] kpiValueParts = stringValue.split(unitK);
			valueNoUnits = kpiValueParts[0];
							
		}else if (stringValue.contains(unitM)){
			
			String[] kpiValueParts = stringValue.split(unitM);
			valueNoUnits = kpiValueParts[0];
			
		}else if (stringValue.contains(unitG)){
			
			String[] kpiValueParts = stringValue.split(unitG);
			valueNoUnits = kpiValueParts[0];
		
		}else if (stringValue.contains(unitT)){
			
			String[] kpiValueParts = stringValue.split(unitT);
			valueNoUnits = kpiValueParts[0];
		
		}else{
			valueNoUnits = stringValue;
			
		}
		
		if(valueNoUnits.endsWith(".0")){
			String[] tmpValue = valueNoUnits.split(".0"); 
			valueNoUnits = tmpValue[0];
		}
		
		System.out.println("Value with no units: " + valueNoUnits);
		
		return valueNoUnits;
		
	}
	

	public static String getNumericValueWithPrefixes(String stringValue) {
		
		String minutes = "min";
		String messages = "msg";
		
		String valueNoUnits = "";
		
		System.out.println("Original Value: " + stringValue);
		
		if (stringValue.contains(minutes)){
			
			String[] valueParts = stringValue.split(minutes);
			valueNoUnits = valueParts[0];
							
		}else if (stringValue.contains(messages)){
			
			String[] valueParts = stringValue.split(messages);
			valueNoUnits = valueParts[0];
			
		}else{
			valueNoUnits = stringValue;
			
		}
		
		if(valueNoUnits.endsWith(".0")){
			String[] tmpValue = valueNoUnits.split(".0"); 
			valueNoUnits = tmpValue[0];
		}
		
		System.out.println("Value with no units: " + valueNoUnits);
		
		return valueNoUnits;
		
	}
	
	
	
	public static void testJavascriptExecutor(){
		
		   JavascriptExecutor js = (JavascriptExecutor)driver;
		   String sText =  js.executeScript("window.hello = function(){return 'Hello';}; return hello();").toString();
		   
		   ShowText(sText);
		   if(sText != null)
		   {
		    ShowText("good");
		   }
		   else
		   {
		    ShowText("is null");
		   }
		
	}
	
	
	// Format phone number from UI (label on the vertical axis) 
	public static String formatPhoneNumberUI(String label) {
		
		System.out.println("**UI ** serviceNumber: " + label);
		
		String servNumberPart = label.split(" " + "[A-Z]")[0];
		String employeeNamePart = label.split("[0-9]" + " ")[1];
		
		ShowText("servNumberPart: " + servNumberPart);
		ShowText("employeeNamePart: " + employeeNamePart);
		
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
		
		String labelFormatted = numberFormatted + employeeNamePart; 
		
		
		System.out.println("**UI ** Service Number Formatted: " + labelFormatted);
		
		return labelFormatted;
		
	}
	
	
	
	
	
	
	//***************************************************************************************************
	// This is the code we used to use when we read values from FILES INSTEAD OF READING FROM AJAX CALL:
	//***************************************************************************************************
	/*
	 * // Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);
		
		// Wait for countries and vendors to be loaded on PoV section
		WaitForElementPresent(By.cssSelector(".tdb-povGroup>.tdb-povGroup"), ExtremeTimeout);
				
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}

		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		String path = ExpenseHelperMultipleVendors.path;
		int amountOfVendors = 100;

		if (amountOfVendors > vendorNames.size())
			amountOfVendors = vendorNames.size();
		
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		// #1 Select Vendor View and Unselect all vendors  
		CommonTestStepActions.SelectVendorView();
		//CommonTestStepActions.UnSelectAllVendors();
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = new ArrayList<>();
		
		// Run the test for each vendor 
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			String vendorSelected = vendorNames.get(i);
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileTmp = ReadFilesHelper.getJsonDataExpenseUsage(vendor);  // ReadFilesHelper.getDataFromSpreadsheet(completePath);			
			listSelectedDataForMonthListUnified.add(valuesFromFileTmp);
				
			// #3 Select one vendor
			//CommonTestStepActions.selectOneVendor(vendor);
			
		}
	 * 
	 * 
	 */
		
	
	
	// Assert.assertTrue(eleList.get(y).getAttribute("class").contains("option--selected"),errMessage);

}
