package expenses;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class ExpenseTrendingMultipleValues extends BaseClass {

	
	private static List<HashMap<String, String>> expectedValues;
	private static HashMap<String, Boolean> hasData;
	
	private static List<String> vendorsSelectedCheckBox;
	private static List<WebElement> vendorsInChart;
	private static List<String> vendorsInChartNames;
	
	private static List<String> countriesSelectedCheckBox;
	private static List<WebElement> countriesInChart;
	private static List<String> countriesInChartNames;
	
	private static String chartId;
	
	
	// Verifies the content of the tooltips displayed on charts under Expense Trending charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyExpenseTrendingChartTooltip(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		chartId = UsageHelper.getChartId(barChartId);
		
		GeneralHelper.moveDown(chartId);

		FleetHelper.selectCategoryExpenses(barChartId, FleetHelper.getNameCategoryExpenses(categorySelector));
		
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		expectedValues = new ArrayList<HashMap<String, String>>();
		
		vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		vendorsSelectedCheckBox = FleetHelper.getVendorsSelected();
	
		ViewType view = ViewType.vendor;
		
		hasData = GeneralHelper.isThereDataForSelectedMonth(allValuesFromFile, view);		
		
				
		// Get the expected values of the vendors listed on the chart
		getExpectedValues(barChartId, allValuesFromFile, categorySelector, view);

		// Calculate the value for the 'Other' element on the chart
		calculateOtherExpectedValues(barChartId, allValuesFromFile, categorySelector, view);
		

		// Verify the info contained on each of the tooltips for the 13 months 		
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		int indexMonth = monthYearList.size()-1;
		int indexHighchart = 1;
		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(true, firstBar, barChartId, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name>
			// 3 <amount shown for the vendor>
			
			// ** May 17 - Ana 
			// Dash version 1.2.8 - tooltip's format has changed on DOM: the amount of items in the tooltip equals to the (amount of series * 2) + 1 
			// 0 MM-YYYY -- month and year appears once -- stays the same
			// 1 ? -- this is for the bullet -- stays the same
			// 2 : <vendor's name> : <amount shown for the vendor>  -- unified in the same line 
						
			
			List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
			int amount = highchartSeries.size();
			int factor = 2;  // Ana added - May 17
			
			int expectedAmountItemsTooltip = (amount * factor) + 2;  // Ana modif - May 17
			int amountItemsTooltipFound = tooltip.size();
			
			GeneralHelper.verifyExpectedAndActualValues(amountItemsTooltipFound, expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index = i * factor + 1;  // Ana modif - May 17 -- before modif --> int index =  i * 3 - 1;  
//				ShowText("index: " + index + ", factor: " + factor + ", i: " + i);
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();  // Ana modif - May 17 -- before modif --> tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim(); // Ana modif - May 17 -- before modif --> tooltip.get(index+1).getText().trim().replace(" ", "");
					
				// Get the value expected
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);
				
				System.out.println("Vendor: " + labelFound);
				System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);

				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			GeneralHelper.verifyExpectedAndActualLabels(monthYearFound, monthYearExpected);
			// System.out.println("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}
		
	}


	private static void getExpectedValues(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector, ViewType view) throws ParseException {
		
		
		for (int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++) {
			
			HashMap<String, String> map = new HashMap<>();
			
			for (int j = 0; j < allValuesFromFile.get(indexMonthValues).size(); j++) {
			
//				String keyVendor = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
				
				UsageOneMonth usageOneMonth = allValuesFromFile.get(indexMonthValues).get(j);
				
//				if (hasData.get(keyVendor)) {
				
				String itemName = "";
				
				if (view.equals(ViewType.country)) {
					
					itemName = allValuesFromFile.get(indexMonthValues).get(j).getCountry();
					
				} else if (view.equals(ViewType.vendor)) {
					
					itemName = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
					
				}
	
				if (barChartId == FleetHelper.expenseByVendorChart) {
				
					switch (categorySelector) {
					
						case FleetHelper.expenseCategoryAll:
							
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalCharge()), true));
							break;
							
						case FleetHelper.expenseCategoryVoice:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getVoiceCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryData:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDataCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryMessages:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getMessagesCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryRoaming:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryEquipment:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getEquipmentCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryTaxes:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTaxCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryOther:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getOtherCharges()), true));
							break;
							
						case FleetHelper.expenseCategoryAccount:
	
							map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()), true));
							break;
							
					}						
					
				} else if (barChartId == FleetHelper.costPerServiceNumberChart) {
					
					String costPerServiceNumber = "$0";
					int numberOfLines = (int) Double.parseDouble(usageOneMonth.getNumberOfLines());  // Integer.parseInt(usageOneMonth.getNumberOfLines());
					
					// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
					// This is to avoid the error generated by a division by zero.
					if (numberOfLines != 0) {   
						
						double costTmp = 0;
						
						switch (categorySelector) {
						
							case FleetHelper.expenseCategoryAll:
								
								costTmp = Double.parseDouble(usageOneMonth.getTotalCharge()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryVoice:
		
								costTmp = Double.parseDouble(usageOneMonth.getVoiceCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryData:
		
								costTmp = Double.parseDouble(usageOneMonth.getDataCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryMessages:
		
								costTmp = Double.parseDouble(usageOneMonth.getMessagesCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryRoaming:
		
								costTmp = Double.parseDouble(usageOneMonth.getRoamingCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryEquipment:
		
								costTmp = Double.parseDouble(usageOneMonth.getEquipmentCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryTaxes:
		
								costTmp = Double.parseDouble(usageOneMonth.getTaxCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryOther:
		
								costTmp = Double.parseDouble(usageOneMonth.getOtherCharges()) / numberOfLines;
								break;
								
							case FleetHelper.expenseCategoryAccount:
		
								costTmp = Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()) / numberOfLines;
								break;
							
						}	
						
						costPerServiceNumber = UsageCalculationHelper.roundNoDecimalDigits(costTmp, true);
						
					}
					
					map.put(itemName, costPerServiceNumber);
					
					
				} else if (barChartId == FleetHelper.countServiceNumbersChart) {
					
					map.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getNumberOfLines()), false));
											
				}
									
//				}
									
			}
			
			expectedValues.add(map);
			
		}	
		
	}
	
	
	private static void calculateOtherExpectedValues(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector, ViewType view) {
		
		
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
		
				
		// ** Calculate the value for "Other" **

		String otherLabel = "Other";
		
		// If more than 5 vendors/countries are selected on the PoV section, and there are 6 vendors/countries in chart,  
		// then the data for one or more vendors/countries is summarized in the "Other" item. The value for "Other" needs to be calculated.
		if (moreThanFiveItemsSelected && sixItemsInChart) {
			
			// System.out.println("More Than 5 Vendors Selected: " + moreThanFiveVendorsSelected);
			// System.out.println("6 Vendors in Chart: " + sixVendorsInChart);
						
			for (int indexMonthValues = 0; indexMonthValues < listUsageAllMonths.size(); indexMonthValues++) {

				double otherTmpSum = 0;
				double costServNumberTmpSum = 0;
				int numberLinesTmpSum = 0;
				String expensesOther = "";
				String countOther = "";
								
				for (int i = 0; i < amountItemsSelected; i++) {
					
					String item = itemsSelectedCheckbox.get(i);
					
					// ************ SEE NOTE NEXT TO NEXT LINE - CHANGE TO BE MADE **************************
					if (!itemsInChartNames.contains(item) && hasData.get(item)) { // <-- When Ed's fix is included on Dashboard, remove the "vendorHasData.get(v)" condition 
					
						UsageOneMonth usage = (UsageOneMonth) listUsageAllMonths.get(indexMonthValues).get(item);
							
						if (barChartId == FleetHelper.expenseByVendorChart) {
							
							switch (categorySelector) {
							
								case FleetHelper.expenseCategoryAll: 
									
									otherTmpSum += (Double.parseDouble(usage.getTotalCharge()));
									break;
									
								case FleetHelper.expenseCategoryVoice:
									
									otherTmpSum += (Double.parseDouble(usage.getVoiceCharges()));
									break;
									
								case FleetHelper.expenseCategoryData:
									
									otherTmpSum += (Double.parseDouble(usage.getDataCharges()));
									break;
									
								case FleetHelper.expenseCategoryMessages:
									
									otherTmpSum += (Double.parseDouble(usage.getMessagesCharges()));
									break;
									
								case FleetHelper.expenseCategoryRoaming:
									
									otherTmpSum += (Double.parseDouble(usage.getRoamingCharges()));
									break;
									
								case FleetHelper.expenseCategoryEquipment:
									
									otherTmpSum += (Double.parseDouble(usage.getEquipmentCharges()));
									break;
									
								case FleetHelper.expenseCategoryTaxes:
									
									otherTmpSum += (Double.parseDouble(usage.getTaxCharges()));
									break;
									
								case FleetHelper.expenseCategoryOther:
									
									otherTmpSum += (Double.parseDouble(usage.getOtherCharges()));
									break;
									
								case FleetHelper.expenseCategoryAccount:
									
									otherTmpSum += (Double.parseDouble(usage.getTotalAccountLevelCharges()));
									break;
										
							}
							
							expensesOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, true);
							expectedValues.get(indexMonthValues).put(otherLabel, expensesOther); 
							
							
						} else if (barChartId == FleetHelper.costPerServiceNumberChart) {
							
							int numberOfLines = (int) Double.parseDouble(usage.getNumberOfLines());
							
							// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
							// This is to avoid the error generated by a division by zero.
							if (numberOfLines != 0) { 
								
								switch (categorySelector) {
								
									case FleetHelper.expenseCategoryAll: 
										
										otherTmpSum += Double.parseDouble(usage.getTotalCharge());
										break;
										
									case FleetHelper.expenseCategoryVoice:
										
										otherTmpSum += Double.parseDouble(usage.getVoiceCharges());
										break;
										
									case FleetHelper.expenseCategoryData:
										
										otherTmpSum += Double.parseDouble(usage.getDataCharges());
										break;
										
									case FleetHelper.expenseCategoryMessages:
										
										otherTmpSum += Double.parseDouble(usage.getMessagesCharges());
										break;
										
									case FleetHelper.expenseCategoryRoaming:
										
										otherTmpSum += Double.parseDouble(usage.getRoamingCharges());
										break;
										
									case FleetHelper.expenseCategoryEquipment:
										
										otherTmpSum += Double.parseDouble(usage.getEquipmentCharges());
										break;
										
									case FleetHelper.expenseCategoryTaxes:
										
										otherTmpSum += Double.parseDouble(usage.getTaxCharges());
										break;
										
									case FleetHelper.expenseCategoryOther:
										
										otherTmpSum += Double.parseDouble(usage.getOtherCharges());
										break;
										
									case FleetHelper.expenseCategoryAccount:
										
										otherTmpSum += Double.parseDouble(usage.getTotalAccountLevelCharges());
										break;
											
								}
								
								numberLinesTmpSum += numberOfLines;
								costServNumberTmpSum = otherTmpSum / numberLinesTmpSum;
								expensesOther = UsageCalculationHelper.roundNoDecimalDigits(costServNumberTmpSum, true);
								expectedValues.get(indexMonthValues).put(otherLabel, expensesOther); 
								
							} else if (numberOfLines == 0 && numberLinesTmpSum == 0) {
								
								expectedValues.get(indexMonthValues).put(otherLabel, "$0"); 
								
							}
							
							
						} else if (barChartId == FleetHelper.countServiceNumbersChart) {
							
							otherTmpSum += Double.parseDouble(usage.getNumberOfLines());				
							countOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
							expectedValues.get(indexMonthValues).put(otherLabel, countOther);
							
						}	
														
					}
					
				}
				
			}
			
		}
		
	}


	
	public static void verifyExpenseTrendingChartTooltipByCountry(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws InterruptedException, ParseException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		chartId = UsageHelper.getChartId(barChartId);
		
		GeneralHelper.moveDown(chartId);
		
		FleetHelper.selectCategoryExpenses(barChartId, FleetHelper.getNameCategoryExpenses(categorySelector));
		
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		expectedValues = new ArrayList<HashMap<String, String>>();
		
		countriesInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		countriesInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < countriesInChart.size(); i++){
			
			countriesInChartNames.add(countriesInChart.get(i).getText());
		}	
		
		countriesSelectedCheckBox = FleetHelper.getCountriesSelected();
	
		ViewType view = ViewType.country;
		
		hasData = GeneralHelper.isThereDataForSelectedMonth(allValuesFromFile, view);		
		
				
		// Get the expected values of the countries listed on the chart
		getExpectedValues(barChartId, allValuesFromFile, categorySelector, view);

		// Calculate the value for the 'Other' element on the chart
		calculateOtherExpectedValues(barChartId, allValuesFromFile, categorySelector, view);
		

		// Verify the info contained on each of the tooltips for the 13 months 		
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		int indexMonth = monthYearList.size()-1;
		int indexHighchart = 1;
		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(true, firstBar, barChartId, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <country's name>
			// 3 <amount shown for the country>
			
			// ** May 17 - Ana 
			// Dash version 1.2.8 - tooltip's format has changed on DOM: the amount of items in the tooltip equals to the (amount of series * 2) + 1 
			// 0 MM-YYYY -- month and year appears once -- stays the same
			// 1 ? -- this is for the bullet -- stays the same
			// 2 : <country's name> : <amount shown for the country>  -- unified in the same line 
						
			
			List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
			int amount = highchartSeries.size();
			int factor = 2;
			int expectedAmountItemsTooltip = (amount * factor) + 2;
			GeneralHelper.verifyExpectedAndActualValues(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each country listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index = i * factor + 1;  
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim();
					
				// Get the value expected
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);
				
				System.out.println("Country: " + labelFound);
				System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);

				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			GeneralHelper.verifyExpectedAndActualLabels(monthYearFound, monthYearExpected);
			// System.out.println("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}
				
	}
	
}
