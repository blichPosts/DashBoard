package expenses;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;

public class TotalExpensesValues extends BaseClass {

	
	private static HashMap<String, UsageOneMonth> expensesMap;
	private static HashMap<String, String> voiceExpensesValues;
	private static HashMap<String, String> dataExpensesValues;
	private static HashMap<String, String> messagesExpensesValues;
	private static HashMap<String, String> roamingExpensesValues;
	private static HashMap<String, String> equipmentExpensesValues;
	private static HashMap<String, String> taxesExpensesValues;
	private static HashMap<String, String> otherExpensesValues;
	private static HashMap<String, String> accountExpensesValues;
	
	private static List<String> vendorsSelectedCheckBox;
	private static List<String> countriesSelectedCheckBox;
	
	private static List<WebElement> vendorsInChart;
	private static List<WebElement> countriesInChart;
	
	private static List<String> vendorsInChartNames;
	private static List<String> countriesInChartNames;
	
	private static boolean noSlicesVerifiedYet;
	
	
	// Verifies the content of the tooltips displayed on charts under Total Expenses BAR chart
	public static void verifyTotalExpensesBarChartTooltipByVendor(int barChartId, List<UsageOneMonth> listOneMonthData, ViewType view) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		vendorsSelectedCheckBox = FleetHelper.getVendorsSelected(); //driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		
		expensesMap = new HashMap<String, UsageOneMonth>();
		
		vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++) {
		
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		// Get the expected values of the vendors listed on the chart
		getExpectedValues(listOneMonthData, view);
		
		// Get the expected values of the vendors grouped under the 'Other' element on the chart
		calculateOtherExpectedValues(chartId, view);
		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		int indexHighchart = 1;
		int expectedAmountItemsTooltip = 17; // 8 categories, 2 elements per category --> 16 + 1 vendor name = 17 items
		

		// Verify the info contained on each of the tooltips for all the vendors listed in chart
		while (indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()) {
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next vendor
			if (vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected)) || vendorsInChartNames.get(indexVendorInChart).equals("Other")) {
				
				// Move the mouse pointer to the desired bar
				GeneralHelper.moveMouseToBar(chartId, indexHighchart, barChartId, 100);
				
				
				// Get the tooltip's text
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
				 
				
				// BAR CHART
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 <*spend category* (Voice, Data, Messages, Roaming, Equipment, Taxes, Other, Account)> : <Amount for *spend category*>
				// ...
				// 17
				
				// Verify that the amount of items in the tooltip equals to the (amount of series * 2) + 1:
				GeneralHelper.verifyExpectedAndActualValues(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country/vendor shown on the tooltip
				String vendorNameFound = tooltip.get(0).getText();
				String vendorNameExpected = vendorsInChartNames.get(indexVendorInChart);
					
				GeneralHelper.verifyExpectedAndActualLabels(vendorNameFound, vendorNameExpected);
				System.out.println("Vendor name found: " + vendorNameFound + ", Vendor name expected: " + vendorNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for (int i = 1; i <= legends.size(); i++) {
				
					int index = i * 2; // Ana modif - May 18 -- before modif --> i * 3 - 1;
					
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().split(":")[1].trim();  // Ana modif - May 18 -- before modif --> tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index).getText().split(":")[2].trim();  // Ana modif - May 18 -- before modif --> tooltip.get(index+1).getText().trim().replace(" ", "");
					
					verifyValuesFound(labelFound, valueFound, vendorNameExpected, index);
					
				}
				
				indexHighchart++;
				indexVendorInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((vendorsSelectedCheckBox.size() - indexVendorSelected) > 1)
				indexVendorSelected++;
			
		}	
		
	}
	
	
	
	private static void getExpectedValues(List<UsageOneMonth> listOneMonthData, ViewType view) {

		expensesMap = new HashMap<String, UsageOneMonth>();
		
		for (UsageOneMonth u: listOneMonthData) {
			
			if (view.equals(ViewType.country)) {
				
				expensesMap.put(u.getCountry(), u);
				
			} else if (view.equals(ViewType.vendor)) {
				
				expensesMap.put(u.getVendorName(), u);
				
			}
			
		}
		
		
		// The HashMaps<vendorName/countryName, value>  will contain the expected values
		voiceExpensesValues = new HashMap<String, String>();
		dataExpensesValues = new HashMap<String, String>();
		messagesExpensesValues = new HashMap<String, String>();
		roamingExpensesValues = new HashMap<String, String>();
		equipmentExpensesValues = new HashMap<String, String>();
		taxesExpensesValues = new HashMap<String, String>();
		otherExpensesValues = new HashMap<String, String>();
		accountExpensesValues = new HashMap<String, String>();
		
		
		for (int i = 0; i < listOneMonthData.size(); i++) {
			
			String itemName = "";
			
			if (view.equals(ViewType.country)) {
				
				itemName = listOneMonthData.get(i).getCountry();
				
			} else if (view.equals(ViewType.vendor)) {
				
				itemName = listOneMonthData.get(i).getVendorName();
				
			}
			
			
			voiceExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getVoiceCharges()), true));
			dataExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDataCharges()), true));
			messagesExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getMessagesCharges()), true));
			roamingExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingCharges()), true));
			equipmentExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getEquipmentCharges()), true));
			taxesExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTaxCharges()), true));
			otherExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getOtherCharges()), true));
			accountExpensesValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTotalAccountLevelCharges()), true));
			
		}
		
		
	}


	
	private static void calculateOtherExpectedValues(String chartId, ViewType view) {
		
		int amountItemsSelected = 0;
		int amountItemsInChart = 0;
		
		List<String> itemsSelectedCheckbox = new ArrayList<>();
		List<String> itemsInChartNames = new ArrayList<>();
		
		if (view.equals(ViewType.country)) {
			
			amountItemsSelected = countriesSelectedCheckBox.size();
			amountItemsInChart = countriesInChartNames.size();
			
			itemsSelectedCheckbox.addAll(countriesSelectedCheckBox);
			itemsInChartNames.addAll(countriesInChartNames);
			
		} else if (view.equals(ViewType.vendor)) {
			
			amountItemsSelected = vendorsSelectedCheckBox.size();
			amountItemsInChart = vendorsInChartNames.size();
			
			itemsSelectedCheckbox.addAll(vendorsSelectedCheckBox);
			itemsInChartNames.addAll(vendorsInChartNames);
		}
		
		
		boolean moreThanFiveItemsSelected = (amountItemsSelected > 5);
		boolean sixItemsInChart = (amountItemsInChart == 6);
		
		String voiceValueOther = "";
		String dataValueOther = "";
		String messagesValueOther = "";
		String roamingValueOther = "";
		String equipmentValueOther = "";
		String taxesValueOther = "";
		String otherValueOther = "";
		String accountValueOther = "";
		String otherLabel = "Other";
	
		// If more than 5 vendors/countries are selected on the PoV section and there are 6 vendors/countries in chart,  
		// then the data for one or more vendors/countries is summarized in the "Other" item.
		if (moreThanFiveItemsSelected && sixItemsInChart) {
			
			double voiceTmpSum = 0;
			double dataTmpSum = 0;
			double messagesTmpSum = 0;
			double roamingTmpSum = 0;
			double equipmentTmpSum = 0;
			double taxesTmpSum = 0;
			double otherTmpSum = 0;
			double accountTmpSum = 0;
			
			
			for (int i = 0; i < amountItemsSelected; i++){
				
				String item = itemsSelectedCheckbox.get(i);
				
				if (!itemsInChartNames.contains(item)){
					
					// ShowText("Vendor " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) expensesMap.get(item);

					// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getTotalCharge();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						// ShowText("there's data for the vendor");
						
						voiceTmpSum += Double.parseDouble(usage.getVoiceCharges());
						voiceValueOther = UsageCalculationHelper.roundNoDecimalDigits(voiceTmpSum, true);
						voiceExpensesValues.put(otherLabel, voiceValueOther);
						
						dataTmpSum += Double.parseDouble(usage.getDataCharges());
						dataValueOther = UsageCalculationHelper.roundNoDecimalDigits(dataTmpSum, true);
						dataExpensesValues.put(otherLabel, dataValueOther);
						
						messagesTmpSum += Double.parseDouble(usage.getMessagesCharges());
						messagesValueOther = UsageCalculationHelper.roundNoDecimalDigits(messagesTmpSum, true);
						messagesExpensesValues.put(otherLabel, messagesValueOther);
						
						roamingTmpSum += Double.parseDouble(usage.getRoamingCharges());
						roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, true);
						roamingExpensesValues.put(otherLabel, roamingValueOther);
						
						equipmentTmpSum += Double.parseDouble(usage.getEquipmentCharges());
						equipmentValueOther = UsageCalculationHelper.roundNoDecimalDigits(equipmentTmpSum, true);
						equipmentExpensesValues.put(otherLabel, equipmentValueOther);
						
						taxesTmpSum += Double.parseDouble(usage.getTaxCharges());
						taxesValueOther = UsageCalculationHelper.roundNoDecimalDigits(taxesTmpSum, true);
						taxesExpensesValues.put(otherLabel, taxesValueOther);
						
						otherTmpSum += Double.parseDouble(usage.getOtherCharges());
						otherValueOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, true);
						otherExpensesValues.put(otherLabel, otherValueOther);
						
						accountTmpSum += Double.parseDouble(usage.getTotalAccountLevelCharges());
						accountValueOther = UsageCalculationHelper.roundNoDecimalDigits(accountTmpSum, true);
						accountExpensesValues.put(otherLabel, accountValueOther);
					
					} else {
						
						System.out.println("there's NO data for the vendor");
						
					}
						
				}

			}
			
		}
		
		
	}


	
//	private static void moveMouseToBar(String chartId, int indexHighchart) throws AWTException, InterruptedException {
//		
//		String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
//		
//		// The 'bar' WebElement will be used to set the position of the mouse on the chart
//		WebElement bar = driver.findElement(By.cssSelector(cssSelector));
//
//		// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
//		Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
//		
//		int x = coordinates.getX();
//		int y = coordinates.getY();
//		
//		Dimension d = bar.getSize();
//		int height = d.getHeight();
//		int width = d.getWidth();
//	
//		int x_BarOffset = (int) (width * 0.5);
//		int y_BarOffset = (int) (height * 0.5);
//		
//		int y_offset = (int) GeneralHelper.getScrollPosition();		
//
//		x = x + x_BarOffset;
//		y = y + y_BarOffset + y_offset;
//		
//		Robot robot = new Robot();
//		robot.mouseMove(x, y); 
//		
//		if (width > 10.0) {
//			bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
//		}
//		
//		if (width < 10.0) {
//			robot.mousePress(InputEvent.BUTTON1_MASK);
//			robot.mouseRelease(InputEvent.BUTTON1_MASK);
//		}
//		
//		
//	}



	private static void verifyValuesFound(String labelFound, String valueFound, String vendorNameExpected, int index) {
		
		String valueExpected = "";
		String labelExpected = "";
		
		// Verify the labels' text and amounts shown on the tooltip 					
	
		switch (index) {
		
			case 2:
				valueExpected = voiceExpensesValues.get(vendorNameExpected);
				labelExpected = "Voice";
				break;
				
			case 4:   // Ana modif - May 18 -- before modif --> case 5:
				valueExpected = dataExpensesValues.get(vendorNameExpected);
				labelExpected = "Data";
				break;
				
			case 6:   // Ana modif - May 18 -- before modif --> case 8:
				valueExpected = messagesExpensesValues.get(vendorNameExpected);
				labelExpected = "Messages";
				break;
				
			case 8:   // Ana modif - May 18 -- before modif --> case 11:
				valueExpected = roamingExpensesValues.get(vendorNameExpected);
				labelExpected = "Roaming";
				break;
				
			case 10:    // Ana modif - May 18 -- before modif --> case 14:
				valueExpected = equipmentExpensesValues.get(vendorNameExpected);
				labelExpected = "Equipment";
				break;
				
			case 12:   // Ana modif - May 18 -- before modif --> case 17:
				valueExpected = taxesExpensesValues.get(vendorNameExpected);
				labelExpected = "Taxes";
				break;
				
			case 14:   // Ana modif - May 18 -- before modif --> case 20:
				valueExpected = otherExpensesValues.get(vendorNameExpected);
				labelExpected = "Other";
				break;
				
			case 16:   // Ana modif - May 18 -- before modif --> case 23:
				valueExpected = accountExpensesValues.get(vendorNameExpected);
				labelExpected = "Account";
				break;
			
		}
		
		ShowText("Label Found: " + labelFound + ", Label Expected: " + labelExpected);
		ShowText("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
		
		GeneralHelper.verifyExpectedAndActualLabels(labelFound, labelExpected);
		GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
		
	}



	// Verifies the content of the tooltips displayed on Total Expenses PIE chart
	
	public static void verifyTotalExpensesPieChartTooltipByVendor(int barChartId, List<UsageOneMonth> listOneMonthData) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> vendorsSelectedCheckBox = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
				
		HashMap<String, UsageOneMonth> vendorExpensesMap = new HashMap<String, UsageOneMonth>();
		
		for (UsageOneMonth u: listOneMonthData) {
			vendorExpensesMap.put(u.getVendorName(), u);
		}
		
		Thread.sleep(2000);
		
		// The HashMap<vendorName, value>  will contain the expected values
		HashMap<String, String> totalExpensesValues = new HashMap<String, String>();
		
		for (int i = 0; i < listOneMonthData.size(); i++) {
			totalExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTotalCharge()), true));
		}
		
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>g>g>g>g>text"));
		List<String> vendorsInChartNames = new ArrayList<String>();
		
		int expectedAmountItemsTooltip = 2;  // Ana modif - May 17 -- before modif --> 4    // 1 category, 3 elements per category --> 3 + 1 vendor name = 4 items
		
		for (int i = 0; i < vendorsInChart.size(); i++) {
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String totalValueOther = "";
		String otherVendors = "Other";
	
		// If more than 5 vendors are selected and there are 6 vendors in chart,  
		// then the vendors that have data for the selected month are summarized in the "Other" item.
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
			
			double totalTmpSum = 0;
			
			for (int i = 0; i < vendorsSelectedCheckBox.size(); i++){
				
				String v = vendorsSelectedCheckBox.get(i).getText();
				
				if (!vendorsInChartNames.contains(v)){
					
					// ShowText("Vendor " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) vendorExpensesMap.get(v);

					// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getDomesticVoice();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						totalTmpSum += Double.parseDouble(usage.getTotalCharge());
						totalValueOther = UsageCalculationHelper.roundNoDecimalDigits(totalTmpSum, true);
						totalExpensesValues.put(otherVendors, totalValueOther);
						// ShowText("there's data for the vendor");
						
					} else {
						
						// ShowText("there's NO data for the vendor");
						
					}
						
				}

			}
			
		}
		
		
		
//		System.out.println("vendorsInChartNames: " + vendorsInChartNames.size()); 
		
//		System.out.println(" * Vendors In Chart * "); 
//		for(String s: vendorsInChartNames){
//			System.out.println("*** " + s);
//		}
//		
//		System.out.println("vendorsSelectedCheckBox: " + vendorsSelectedCheckBox.size()); 
//		for(WebElement w: vendorsSelectedCheckBox){
//			System.out.println("*** " + w.getText());
//		}

		
		
		// **************************************************************************************
		// Verify the info contained on each of the tooltips for all the vendors listed in chart
		// **************************************************************************************
		
		// Gets all the sections of the pie chart and puts it into a list
		List<WebElement> listChartParts = driver.findElements(By.cssSelector("#" + chartId + ">svg>g>g>path.highcharts-point"));
		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		noSlicesVerifiedYet = true;
				
		List<String> tmpListVendorFound = new ArrayList<>();
		
		
		while (indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()) {
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next vendor
			if (vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()) || vendorsInChartNames.get(indexVendorInChart).equals("Other")) {
				
				// The 'bar' WebElement will be used to set the position of the mouse on the chart
				WebElement slice = listChartParts.get(indexVendorInChart);
				
				int amountSlices = listChartParts.size();
				
				moveMouseToElement(slice, amountSlices, chartId);
				
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
				
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 				
				// PIE CHART
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Expense
				// 3 <Total Expense Amount for vendor>
				
				GeneralHelper.verifyExpectedAndActualValues(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify the label and the amount shown on the tooltip
				int index = 1;
		
				// Verify country/vendor shown on the tooltip
				String vendorNameFound = tooltip.get(index).getText().split(":")[1].trim();
				tmpListVendorFound.add(vendorNameFound);
				
				ShowText("Vendor Name Found: " + vendorNameFound);
				
				
				// Get the value on tooltip and remove all blank spaces
				String valueFound = tooltip.get(index).getText().split(":")[2].trim(); // Ana modif - May 17 -- before modif --> ttooltip.get(index+1).getText().trim().replace(" ", "");
				
				// Verify the label's text and amount shown on the tooltip
				// The vendor name found by the mouse hover will be used to get the expected value. 
				// Since in some cases the pie chart has very narrow sections it's not possible to ensure that the mouse hover will obtain the value for those tiny parts 
				// In the cases where the mouse hover won't get the value for the intended vendor, the test would fail. 
				String valueExpected = totalExpensesValues.get(vendorNameFound);

				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
				ShowText("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
				
				indexVendorInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((vendorsSelectedCheckBox.size() - indexVendorSelected) > 1)
				indexVendorSelected++;
			
		}	
		
		
	}

	
	
	public static void moveMouseToElement(WebElement slice, int amountSlices, String chartId) throws AWTException, InterruptedException {
		
		
		// Get the location of the slice of the pie chart
		Point coordinates = GeneralHelper.getAbsoluteLocation(slice);
		
		int x = coordinates.getX();
		int y = coordinates.getY();
		
		Dimension d = slice.getSize();
		int height = d.getHeight();
		int width = d.getWidth();

		Robot robot = new Robot();
		
		if (amountSlices == 1 || noSlicesVerifiedYet) {
		
			robot.mouseMove((x - 20), (y - 20));
			noSlicesVerifiedYet = false;
			
		}
		
		Thread.sleep(1000);
		
		int x_sliceOffset = (int) (width * 0.5);
		int y_sliceOffset = (int) (height * 0.5);
		
		int y_offset = (int) GeneralHelper.getScrollPosition();
					
		x = x + x_sliceOffset - 3;
		y = y + y_sliceOffset + y_offset;
		
		robot.mouseMove(x, y); 
				
		
		try {
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
			//System.out.println("Tooltip present");
		} catch (Exception e) {
			System.out.println("Tooltip NOT present");
			e.printStackTrace();
		}
		
	}



	public static void verifyTotalExpensesBarChartTooltipByCountry(int barChartId, List<UsageOneMonth> listOneMonthData) throws InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		countriesSelectedCheckBox = FleetHelper.getCountriesSelected();
		
		countriesInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		countriesInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < countriesInChart.size(); i++) {
		
			countriesInChartNames.add(countriesInChart.get(i).getText());
		}
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		ViewType view = ViewType.country;
		
		// Get the expected values of the countries listed on the chart
		getExpectedValues(listOneMonthData, view);
		
		// Get the expected values of the countries grouped under the 'Other' element on the chart
		calculateOtherExpectedValues(chartId, view);
		
		int indexCountrySelected = 0;
		int indexCountryInChart = 0;
		int indexHighchart = 1;
		int expectedAmountItemsTooltip = 17; // 8 categories, 2 elements per category --> 16 + 1 country name = 17 items
		

		// Verify the info contained on each of the tooltips for all the countries listed in chart
		while (indexCountrySelected < countriesSelectedCheckBox.size() && indexCountryInChart < countriesInChartNames.size()) {
			
			// If the country in countriesSelectedCheckBox list is present in the countriesInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next country
			if (countriesInChartNames.contains(countriesSelectedCheckBox.get(indexCountrySelected)) || countriesInChartNames.get(indexCountryInChart).equals("Other")) {
				
				// Move the mouse pointer to the desired bar
				GeneralHelper.moveMouseToBar(chartId, indexHighchart, barChartId, 100);
				
				
				// Get the tooltip's text
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
				 
				
				// BAR CHART
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 <*spend category* (Voice, Data, Messages, Roaming, Equipment, Taxes, Other, Account)> : <Amount for *spend category*>
				// ...
				// 17
				
				// Verify that the amount of items in the tooltip equals to the (amount of series * 2) + 1:
				GeneralHelper.verifyExpectedAndActualValues(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country/vendor shown on the tooltip
				String countryNameFound = tooltip.get(0).getText();
				String countryNameExpected = countriesInChartNames.get(indexCountryInChart);
					
				GeneralHelper.verifyExpectedAndActualLabels(countryNameFound, countryNameExpected);
				System.out.println("Country name found: " + countryNameFound + ", Country name expected: " + countryNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for (int i = 1; i <= legends.size(); i++) {
				
					int index = i * 2;
					
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().split(":")[1].trim();
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index).getText().split(":")[2].trim();
					
					verifyValuesFound(labelFound, valueFound, countryNameExpected, index);
					
				}
				
				indexHighchart++;
				indexCountryInChart++;
				
			}
			
			// Only add 1 to indexCountrySelected if the amount of checkboxes checked has not been reached  
			if ((countriesSelectedCheckBox.size() - indexCountrySelected) > 1)
				indexCountrySelected++;
			
		}	
		
	}



	public static void verifyTotalExpensesPieChartTooltipByCountry(int pieChartId, List<UsageOneMonth> listOneMonthData) throws ParseException, InterruptedException, AWTException {
			
		String chartId = UsageHelper.getChartId(pieChartId);
		
		List<String> countriesSelectedCheckBox = FleetHelper.getCountriesSelected();
				
		HashMap<String, UsageOneMonth> countriesExpensesMap = new HashMap<String, UsageOneMonth>();
		
		for (UsageOneMonth u: listOneMonthData) {
			
			countriesExpensesMap.put(u.getCountry(), u);
		}
		
		Thread.sleep(2000);
		
		// The HashMap<countryName, value>  will contain the expected values
		HashMap<String, String> totalExpensesValues = new HashMap<String, String>();
		
		for (int i = 0; i < listOneMonthData.size(); i++) {
			
			totalExpensesValues.put(listOneMonthData.get(i).getCountry(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTotalCharge()), true));
			
		}
		
		
		List<WebElement> countriesInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>g>g>g>g>text"));
		List<String> countriesInChartNames = new ArrayList<String>();
		
		int expectedAmountItemsTooltip = 2;
		
		for (int i = 0; i < countriesInChart.size(); i++) {
			
			countriesInChartNames.add(countriesInChart.get(i).getText());
		}	
		
		
		boolean moreThanFiveCountriesSelected = countriesSelectedCheckBox.size() > 5;
		boolean sixCountriesInChart = countriesInChartNames.size() == 6;
		
		String totalValueOther = "";
		String labelOther = "Other";
	
		
		// If more than 5 countries are selected on the PoV section and there are 6 countries in chart,  
		// then the data for one or more countries is summarized in the "Other" item.
		if (moreThanFiveCountriesSelected && sixCountriesInChart) {
			
			double totalTmpSum = 0;
			
			for (int i = 0; i < countriesSelectedCheckBox.size(); i++){
				
				String v = countriesSelectedCheckBox.get(i);
				
				if (!countriesInChartNames.contains(v)){
					
					// ShowText("country " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) countriesExpensesMap.get(v);

					// If there's data for the selected month/country add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getDomesticVoice();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						totalTmpSum += Double.parseDouble(usage.getTotalCharge());
						totalValueOther = UsageCalculationHelper.roundNoDecimalDigits(totalTmpSum, true);
						totalExpensesValues.put(labelOther, totalValueOther);
						// ShowText("there's data for the country");
						
					} else {
						
						// ShowText("there's NO data for the country");
						
					}
						
				}

			}
		
		}

		
		// **************************************************************************************
		// Verify the info contained on each of the tooltips for all the countries listed in chart
		// **************************************************************************************
		
		// Gets all the sections of the pie chart and puts it into a list
		List<WebElement> listChartParts = driver.findElements(By.cssSelector("#" + chartId + ">svg>g>g>path.highcharts-point"));
		
		int indexCountrySelected = 0;
		int indexCountryInChart = 0;
		noSlicesVerifiedYet = true;
				
		List<String> tmpListCountryFound = new ArrayList<>();
		
		
		while (indexCountrySelected < countriesSelectedCheckBox.size() && indexCountryInChart < countriesInChartNames.size()) {
			
			// If the country in countriesSelectedCheckBox list is present in the countriesInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next country
			if (countriesInChartNames.contains(countriesSelectedCheckBox.get(indexCountrySelected)) || countriesInChartNames.get(indexCountryInChart).equals("Other")) {
				
				// The 'bar' WebElement will be used to set the position of the mouse on the chart
				WebElement slice = listChartParts.get(indexCountryInChart);
				
				int amountSlices = listChartParts.size();
				
				moveMouseToElement(slice, amountSlices, chartId);
								
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
				
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 				
				// PIE CHART
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Expense
				// 3 <Total Expense Amount for country>
				
				GeneralHelper.verifyExpectedAndActualValues(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify the label and the amount shown on the tooltip
				int index = 1;
		
				// Verify country/vendor shown on the tooltip
				String countryNameFound = tooltip.get(index).getText().split(":")[1].trim();
				tmpListCountryFound.add(countryNameFound);
				
				ShowText("Country name found: " + countryNameFound);
				
				
				// Get the value on tooltip and remove all blank spaces
				String valueFound = tooltip.get(index).getText().split(":")[2].trim();
				
				// Verify the label's text and amount shown on the tooltip
				// The country name found by the mouse hover will be used to get the expected value. 
				// Since in some cases the pie chart has very narrow sections it's not possible to ensure that the mouse hover will obtain the value for those tiny parts 
				// In the cases where the mouse hover won't get the value for the intended country, the test would fail. 
				String valueExpected = totalExpensesValues.get(countryNameFound);

				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
				ShowText("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
				indexCountryInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((countriesSelectedCheckBox.size() - indexCountrySelected) > 1)
				indexCountrySelected++;
			
		}
	
	}
	
}
