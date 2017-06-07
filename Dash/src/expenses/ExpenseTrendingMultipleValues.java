package expenses;

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


public class ExpenseTrendingMultipleValues extends BaseClass {

	
	private static List<HashMap<String, String>> expectedValues;
	private static HashMap<String, Boolean> vendorHasData;
	private static String chartId;
	
	
	// Verifies the content of the tooltips displayed on charts under Expense Trending charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyExpenseTrendingChartTooltip(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		chartId = UsageHelper.getChartId(barChartId);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
				
		Thread.sleep(2000);
			
		List<WebElement> legendsElements = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<String> legends = new ArrayList<>();
		
		for (WebElement e: legendsElements) {
			legends.add(e.getText());
			// System.out.println("Legend: " + e.getText());
		}
		
		vendorHasData = GeneralHelper.vendorHasDataForSelectedMonth(allValuesFromFile);		
		
		expectedValues = new ArrayList<HashMap<String, String>>();
		
		// Get the expected values of the vendors listed on the chart
		getExpectedValues(barChartId, allValuesFromFile, categorySelector);

		// Calculate the value for the 'Other' element on the chart
		calculateOtherExpectedValues(barChartId, allValuesFromFile, categorySelector);
		

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
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index = i * factor + 1;  // Ana modif - May 17 -- before modif --> int index =  i * 3 - 1;  
				
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
			String monthYearExpected = monthYearList.get(indexMonth).replace("/", "-");
				
			Assert.assertEquals(monthYearFound, monthYearExpected);
			// System.out.println("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}
		
	}


	// NOT USED - REPLACED BY THE METHOD ADDED IN GeneralHelper CLASS
//	private static void moveMouseToBar(int barChartId, int indexHighchart) throws InterruptedException, AWTException {
//		
//		String cssBar = "";
//		
//		if (barChartId == FleetHelper.costPerServiceNumberChart) {
//			
//			cssBar = "#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + indexHighchart + ")";
//
//			
//		} else if (barChartId == FleetHelper.expenseByVendorChart || barChartId == FleetHelper.countServiceNumbersChart) {
//			
//			cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
//			
//		}
//		
//		
//		// 'bar' WebElement will be used to set the position of the mouse on the chart
//		WebElement bar = driver.findElement(By.cssSelector(cssBar));
//					
//		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
//		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
//		
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



	private static void getExpectedValues(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException {
		
		
		for(int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++){
			
			HashMap<String, String> map = new HashMap<>();
			
			for (int j = 0; j < allValuesFromFile.get(indexMonthValues).size(); j++) {
			
				String keyVendor = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
				
				UsageOneMonth usageOneMonth = allValuesFromFile.get(indexMonthValues).get(j);
				
				if (vendorHasData.get(keyVendor)) {
	
					if (barChartId == FleetHelper.expenseByVendorChart) {
					
						if (categorySelector == FleetHelper.expenseCategoryAll){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalCharge()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryVoice){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getVoiceCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryData){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDataCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryMessages){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getMessagesCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryRoaming){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryEquipment){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getEquipmentCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryTaxes){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTaxCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryOther){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getOtherCharges()), true));
							
						} else if (categorySelector == FleetHelper.expenseCategoryAccount){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()), true));
							
						}						
						
					} else if (barChartId == FleetHelper.costPerServiceNumberChart) {
						
						String costPerServiceNumber = "$0";
						int numberOfLines = Integer.parseInt(usageOneMonth.getNumberOfLines());
						
						// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
						// This is to avoid the error generated by a division by zero.
						if (numberOfLines != 0) {   
							
							double costTmp = 0;
							
							if (categorySelector == FleetHelper.expenseCategoryAll){
								
								costTmp = Double.parseDouble(usageOneMonth.getTotalCharge()) / numberOfLines;
																
							} else if (categorySelector == FleetHelper.expenseCategoryVoice){

								costTmp = Double.parseDouble(usageOneMonth.getVoiceCharges()) / numberOfLines;
																
							} else if (categorySelector == FleetHelper.expenseCategoryData){

								costTmp = Double.parseDouble(usageOneMonth.getDataCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryMessages){

								costTmp = Double.parseDouble(usageOneMonth.getMessagesCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryRoaming){

								costTmp = Double.parseDouble(usageOneMonth.getRoamingCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryEquipment){

								costTmp = Double.parseDouble(usageOneMonth.getEquipmentCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryTaxes){

								costTmp = Double.parseDouble(usageOneMonth.getTaxCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryOther){

								costTmp = Double.parseDouble(usageOneMonth.getOtherCharges()) / numberOfLines;
								
							} else if (categorySelector == FleetHelper.expenseCategoryAccount){

								costTmp = Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()) / numberOfLines;
							}	
							
							costPerServiceNumber = UsageCalculationHelper.roundNoDecimalDigits(costTmp, true);
							
						}
						
						map.put(usageOneMonth.getVendorName(), costPerServiceNumber);
						
						
					} else if (barChartId == FleetHelper.countServiceNumbersChart) {
						
						map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getNumberOfLines()), false));
												
					}
										
				}
									
			}
			
			expectedValues.add(map);
			
		}	
		
	}
	
	
	
	private static void calculateOtherExpectedValues(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) {
		
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<String> vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		List<WebElement> vendorsSelectedCheckBox = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		
		// The list contains one HashMap per month. Each HashMap contains the data for all the vendors.
		List<HashMap<String, UsageOneMonth>> listUsageAllMonths = new ArrayList<>();
		
		for (int i = 0; i < allValuesFromFile.size(); i++) {
			
			List<UsageOneMonth> listForMonth = allValuesFromFile.get(i);
			
			HashMap<String, UsageOneMonth> mapTmp = new HashMap<>();
			
			for (int j = 0; j < listForMonth.size(); j++) {
			
				UsageOneMonth u = listForMonth.get(j);
				mapTmp.put(u.getVendorName(), u);
				
			}
			
			listUsageAllMonths.add(mapTmp);
			
		}
		
				
		//  Calculate the value for "Other"
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String otherVendors = "Other";
		
		// If more than 5 vendors are selected, and there are 6 vendors in chart
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
			
			// System.out.println("More Than 5 Vendors Selected: " + moreThanFiveVendorsSelected);
			// System.out.println("6 Vendors in Chart: " + sixVendorsInChart);
						
			for (int indexMonthValues = 0; indexMonthValues < listUsageAllMonths.size(); indexMonthValues++) {

				double otherTmpSum = 0;
				double costServNumberTmpSum = 0;
				int numberLinesTmpSum = 0;
				String expensesOther = "";
				String countOther = "";
								
				for (int i = 0; i < vendorsSelectedCheckBox.size(); i++) {
					
					String v = vendorsSelectedCheckBox.get(i).getText();
					
					// ************ SEE NOTE NEXT TO NEXT LINE - CHANGE TO BE MADE **************************
					if (!vendorsInChartNames.contains(v) && vendorHasData.get(v)) { // <-- When Ed's fix is included on Dashboard, remove the "vendorHasData.get(v)" condition 
						
//						System.out.println("Vendor " + v + ", is not listed in chart");
					
						UsageOneMonth usage = (UsageOneMonth) listUsageAllMonths.get(indexMonthValues).get(v);
							
						if (barChartId == FleetHelper.expenseByVendorChart) {
							
							if (categorySelector == FleetHelper.expenseCategoryAll){
										
								otherTmpSum += (Double.parseDouble(usage.getTotalCharge()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryVoice){
								
								otherTmpSum += (Double.parseDouble(usage.getVoiceCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryData){
								
								otherTmpSum += (Double.parseDouble(usage.getDataCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryMessages){
								
								otherTmpSum += (Double.parseDouble(usage.getMessagesCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryRoaming){
								
								otherTmpSum += (Double.parseDouble(usage.getRoamingCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryEquipment){
								
								otherTmpSum += (Double.parseDouble(usage.getEquipmentCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryTaxes){
								
								otherTmpSum += (Double.parseDouble(usage.getTaxCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryOther){
								
								otherTmpSum += (Double.parseDouble(usage.getOtherCharges()));
								
							} else if (categorySelector == FleetHelper.expenseCategoryAccount){
								
								otherTmpSum += (Double.parseDouble(usage.getTotalAccountLevelCharges()));
								
							}
							
							expensesOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, true);
							expectedValues.get(indexMonthValues).put(otherVendors, expensesOther); 
							
							
						} else if (barChartId == FleetHelper.costPerServiceNumberChart) {
							
							int numberOfLines = Integer.parseInt(usage.getNumberOfLines());
							
							// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
							// This is to avoid the error generated by a division by zero.
							if (numberOfLines != 0) { 
								
								if (categorySelector == FleetHelper.expenseCategoryAll){
									
									otherTmpSum += Double.parseDouble(usage.getTotalCharge());
																										
								} else if (categorySelector == FleetHelper.expenseCategoryVoice){

									otherTmpSum += Double.parseDouble(usage.getVoiceCharges());
																	
								} else if (categorySelector == FleetHelper.expenseCategoryData){

									otherTmpSum += Double.parseDouble(usage.getDataCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryMessages){

									otherTmpSum += Double.parseDouble(usage.getMessagesCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryRoaming){

									otherTmpSum += Double.parseDouble(usage.getRoamingCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryEquipment){

									otherTmpSum += Double.parseDouble(usage.getEquipmentCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryTaxes){

									otherTmpSum += Double.parseDouble(usage.getTaxCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryOther){

									otherTmpSum += Double.parseDouble(usage.getOtherCharges());
									
								} else if (categorySelector == FleetHelper.expenseCategoryAccount){

									otherTmpSum += Double.parseDouble(usage.getTotalAccountLevelCharges());
									
								}
								
								numberLinesTmpSum += numberOfLines;
								costServNumberTmpSum = otherTmpSum / numberLinesTmpSum;
								expensesOther = UsageCalculationHelper.roundNoDecimalDigits(costServNumberTmpSum, true);
								expectedValues.get(indexMonthValues).put(otherVendors, expensesOther); 
								
							} else if (numberOfLines == 0 && numberLinesTmpSum == 0) {
								
								expectedValues.get(indexMonthValues).put(otherVendors, "$0"); 
								
							}
							
							
						} else if (barChartId == FleetHelper.countServiceNumbersChart) {
							
							otherTmpSum += Double.parseDouble(usage.getNumberOfLines());				
							countOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
							expectedValues.get(indexMonthValues).put(otherVendors, countOther);
							
						}	
														
					}
					
				}
				
			}
			
		}
		
	}
	
	

}
