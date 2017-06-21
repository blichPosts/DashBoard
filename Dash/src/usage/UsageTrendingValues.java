package usage;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class UsageTrendingValues extends BaseClass {
	
	private static List<HashMap<String, String>> expectedValues;  // The HashMap "expectedValues" will have one entry per vendor/country listed in chart. The key is the vendor's/country's name. 		
	private static HashMap<String, Boolean> hasData;
	
	private static List<String> vendorsSelectedCheckBox;
	private static List<WebElement> vendorsInChart;
	private static List<String> vendorsInChartNames;
	
	private static List<String> countriesSelectedCheckBox;
	private static List<WebElement> countriesInChart;
	private static List<String> countriesInChartNames;
	private static int category;
	
	
	// Verifies the content of the tooltips displayed on charts under Usage Trending Domestic and Roaming charts
	// * It works for any number of selected vendors *
	public static void verifyUsageTrendingChartTooltipByVendor(int chartNum, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		String chartId = UsageHelper.getChartId(chartNum);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
		
		Thread.sleep(2000);
		
		category = categorySelector;
			
		expectedValues = new ArrayList<>();
		
		vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		vendorsSelectedCheckBox = FleetHelper.getVendorsSelected();
	
			
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		ViewType view = ViewType.vendor;
		
		hasData = GeneralHelper.isThereDataForSelectedMonth(allValuesFromFile, view);
				
		getExpectedValues(allValuesFromFile, chartNum, view);
		
		calculateOtherExpectedValues(allValuesFromFile, chartNum, view);
		

		// *******************************************************************
		// Verify the info contained on each of the tooltips for the 13 months 		
		// *******************************************************************
		
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		int indexHighchart = 1;
		int indexMonth = monthYearList.size()-1;
		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(false, firstBar, chartNum, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name> : <amount shown for the vendor>
			
			int amountOfSeries = highchartSeries.size();
			int factor = 2;  // Ana added - May 17
			int expectedAmountItemsTooltip = (amountOfSeries * factor) + 2;  // Ana modif - May 18
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the amount shown
			for(int i = 1; i <= legends.size(); i++) {
			
				int index =  i * factor + 1;  // Ana modif - May 18 -- before modif --> int index =  i * 3 - 1;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();  // Ana modif - May 18 -- before modif --> tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: 15 256 985. Value needed is: 15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim(); // Ana modif - May 18 -- before modif --> tooltip.get(index+1).getText().trim().replace(" ", "");
							
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);
				
				System.out.println("Vendor: " + labelFound);
				System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
				
				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			String chartName = "";
			
			if (chartNum == UsageHelper.usageTrendingDomesticChart) {
				chartName = "Domestic";
			} else if (chartNum == UsageHelper.usageTrendingRoamingChart) {
				chartName = "Roaming";
			}
			
			Assert.assertEquals(monthYearFound, monthYearExpected + ": " + chartName); 
			// System.out.println("Month/Year Found: " + monthYearFound + ", Month/Year Expected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}

	}

		
	
	private static void getExpectedValues(List<List<UsageOneMonth>> allValuesFromFile, int chartNum, ViewType view){
		
		for(int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++){
			
			HashMap<String, String> map = new HashMap<>();
			
			for (int j = 0; j < allValuesFromFile.get(indexMonthValues).size(); j++) {
			
				String key = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
				
				UsageOneMonth usageOneMonth = allValuesFromFile.get(indexMonthValues).get(j);
				
//				if (vendorHasData.get(key)) {
					
					String itemName = "";
					
					if (view.equals(ViewType.country)) {
						
						itemName = allValuesFromFile.get(indexMonthValues).get(j).getCountry();
						
					} else if (view.equals(ViewType.vendor)) {
						
						itemName = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
						
					}
					
	
					if (chartNum == UsageHelper.usageTrendingDomesticChart) {
					
						if(category == UsageHelper.categoryVoice){

							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDomesticVoice()) 
								+ Double.parseDouble(usageOneMonth.getDomesticOverageVoice()), false));
							
						} else if (category == UsageHelper.categoryData){
							
							map.put(itemName, UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usageOneMonth.getDomesticDataUsageKb())));
															
						} else if (category == UsageHelper.categoryMessages){
							
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDomesticMessages()), false));
							
						}
						
					} else if (chartNum == UsageHelper.usageTrendingRoamingChart) {
						
						if(category == UsageHelper.categoryVoice){

							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingVoice()), false));
							
						} else if (category == UsageHelper.categoryData){
							
							map.put(itemName, UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usageOneMonth.getRoamingDataUsageKb())));
															
						} else if (category == UsageHelper.categoryMessages){
							
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingMessages()), false));
							
						}
						
					}
					
//				}
									
			}
			
			expectedValues.add(map);
			
		}	
		
	}
	
	
	
	private static void calculateOtherExpectedValues(List<List<UsageOneMonth>> allValuesFromFile, int chartNum, ViewType view) {
		
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
		
				
		// The list contains one HashMap per month. Each HashMap contains the data for all the vendors.
		List<HashMap<String, UsageOneMonth>> listUsageAllMonths = new ArrayList<>();
		
		for (int i = 0; i < allValuesFromFile.size(); i++) {
			
			List<UsageOneMonth> listForMonth = allValuesFromFile.get(i);
			
			HashMap<String, UsageOneMonth> mapTmp = new HashMap<>();
			
			for (int j = 0; j < listForMonth.size(); j++) {
			
				UsageOneMonth usage = listForMonth.get(j);
				String itemName = ""; 
						
				if (view.equals(ViewType.country)) {
					
					itemName = usage.getCountry();
					
				} else if (view.equals(ViewType.vendor)) {

					itemName = usage.getVendorName();
					
				}
				
				mapTmp.put(itemName, usage);
				
			}
			
			listUsageAllMonths.add(mapTmp);
			
		}
		
				
		String otherLabel = "Other";
		
		
		// If more than 5 vendors/countries are selected on the PoV section, and there are 6 vendors/countries in chart,  
		// then the data for one or more vendors/countries is summarized in the "Other" item. The value for "Other" needs to be calculated.
		if (moreThanFiveItemsSelected && sixItemsInChart) {
						
			for (int indexMonthValues = 0; indexMonthValues < listUsageAllMonths.size(); indexMonthValues++) {

				double otherTmpSum = 0;
				String valueForOther = "";
								
				for (int i = 0; i < amountItemsSelected; i++){
					
					String item = itemsSelectedCheckbox.get(i);
					
					// ************ SEE NOTE NEXT TO NEXT LINE - CHANGE TO BE MADE **************************
					if (!itemsInChartNames.contains(item) && hasData.get(item)){ // <-- When Ed's fix is included on Dashboard, remove the "vendorHasData.get(v)" condition 
					
						UsageOneMonth usage = (UsageOneMonth) listUsageAllMonths.get(indexMonthValues).get(item);
							
						if (chartNum == UsageHelper.usageTrendingDomesticChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								otherTmpSum += (Double.parseDouble(usage.getDomesticVoice()) + Double.parseDouble(usage.getDomesticOverageVoice()));
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							} else if (category == UsageHelper.categoryData){
								
								otherTmpSum += Double.parseDouble(usage.getDomesticDataUsageKb());
								valueForOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(otherTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								otherTmpSum += Double.parseDouble(usage.getDomesticMessages());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							}
							
							
						} else if (chartNum == UsageHelper.usageTrendingRoamingChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingVoice());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							} else if (category == UsageHelper.categoryData){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingDataUsageKb());
								valueForOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(otherTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingMessages());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							}

						}
						
						expectedValues.get(indexMonthValues).put(otherLabel, valueForOther); 
														
					}
					
				}
				
			}
			
		}
				
		
	}
	
	

	public static void verifyUsageTrendingChartTooltipByCountry(int chartNum, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		String chartId = UsageHelper.getChartId(chartNum);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
		
		Thread.sleep(2000);
		
		category = categorySelector;
			
		expectedValues = new ArrayList<>();
		
		countriesInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		countriesInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < countriesInChart.size(); i++){
			
			countriesInChartNames.add(countriesInChart.get(i).getText());
		}	
		
		countriesSelectedCheckBox = FleetHelper.getCountriesSelected();
	
			
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));

		ViewType view = ViewType.country;
		
		hasData = GeneralHelper.isThereDataForSelectedMonth(allValuesFromFile, view);
		
		getExpectedValues(allValuesFromFile, chartNum, view);
		
		calculateOtherExpectedValues(allValuesFromFile, chartNum, view);
		

		// *******************************************************************
		// Verify the info contained on each of the tooltips for the 13 months 		
		// *******************************************************************
		
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		int indexHighchart = 1;
		int indexMonth = monthYearList.size()-1;
		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(false, firstBar, chartNum, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <country name> : <amount shown for the country>
			
			int amountOfSeries = highchartSeries.size();
			int factor = 2;
			int expectedAmountItemsTooltip = (amountOfSeries * factor) + 2;
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each country listed in the tooltip verify the amount shown
			for(int i = 1; i <= legends.size(); i++) {
			
				int index =  i * factor + 1;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: 15 256 985. Value needed is: 15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim();
							
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);
				
				System.out.println("Country: " + labelFound);
				System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
				
				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			String chartName = "";
			
			if (chartNum == UsageHelper.usageTrendingDomesticChart) {
				chartName = "Domestic";
			} else if (chartNum == UsageHelper.usageTrendingRoamingChart) {
				chartName = "Roaming";
			}
			
			Assert.assertEquals(monthYearFound, monthYearExpected + ": " + chartName); 
			ShowText("Month/Year Found: " + monthYearFound + ", Month/Year Expected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}

		
	}
	
	
	

	// NOT USED - REPLACED BY THE METHOD ADDED IN GeneralHelper CLASS
//	private static void moveMouseToBar(String chartId, int indexHighchart) throws InterruptedException, AWTException{
//		
//		String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
//		
//		// 'bar' WebElement will be used to set the position of the mouse on the chart
//		WebElement bar = driver.findElement(By.cssSelector(cssBar));
//				
//		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
//		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
//		Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
//		
//		int y_offset = (int) GeneralHelper.getScrollPosition();
//		
//		int x = barCoordinates.getX();
//		int y = GeneralHelper.getYCoordinate(chartId) + y_offset;
//		
//		Robot robot = new Robot(); 
//		robot.mouseMove(x, y);
//		
//		// ShowText("coordinates:  x: " + x + "  y: " + y);
//		
//		Thread.sleep(500);
//		
//		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//		
//		
//		try {
//			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
//			//System.out.println("Tooltip present");
//		} catch (Exception e) {
//			System.out.println("Tooltip NOT present");
//			e.printStackTrace();
//		}
//		
//		
//	}

	
	
	// *** TO BE REMOVED --- replaced by 2 methods in new class FleetHelper- 
	
//	public static List<List<UsageOneMonth>> getListWithData(List<List<UsageOneMonth>> data) throws ParseException {
//		
//		
//		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
//		
//		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
//		
//		// Some vendors might not have information for all last 13 months; in that case the value displayed on the Usage Trending chart is zero
//		// So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
//		for (List<UsageOneMonth> list: data) {
//			
//			List<UsageOneMonth> valuesFromFileOneVendor = UsageHelper.addMissingMonthsForVendor(list);
//			listVendorsSelectedData.add(valuesFromFileOneVendor);
//		
//		}
//		
//
//		int indexMonth = 0;
//	
//		List<List<UsageOneMonth>> dataForUsageTrending = new ArrayList<>();
//		
//		do {
//		
//			// listOneMonthData will have the data for a specific month, for all the vendors previously selected
//			List<UsageOneMonth> listOneMonthData = new ArrayList<>();
//			
//			// values has the 13 months for one vendor
//			for (List<UsageOneMonth> values: listVendorsSelectedData){
//				
//				int indexMonthForVendorSelected = 0;
//				boolean dataFoundForMonth = false;
//				
//				while (indexMonthForVendorSelected < values.size() && !dataFoundForMonth){
//					
//					String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
//					
//					if (monthsToSelect.get(indexMonth).equals(monthYear)) {
// 
//						listOneMonthData.add(values.get(indexMonthForVendorSelected));
//						dataFoundForMonth = true;
//						
//					}
//						
//					indexMonthForVendorSelected++;
//				}
//					
//			}
//
//			dataForUsageTrending.add(listOneMonthData);
//			
//			indexMonth++;
//			
//		} while (indexMonth < monthsToSelect.size());
//		
//		
//		return dataForUsageTrending;
//		
//	}
//		
	
}
