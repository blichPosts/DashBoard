package expenses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelperMultipleVendors;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class ExpenseTrendingMultipleValues extends BaseClass {

	
	// Verifies the content of the tooltips displayed on charts under Expense Trending charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyExpenseTrendingChartTooltip(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		
		String chartId = UsageHelper.getChartId(barChartId);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
				
		Thread.sleep(2000);
			
		List<WebElement> legendsElements = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<String> legends = new ArrayList<>();
		
		for (WebElement e: legendsElements) {
			legends.add(e.getText());
//			System.out.println("Legend: " + e.getText());
		}
		
		
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		//System.out.println("amount: " + amount);
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
				
		List<HashMap<String, String>> expectedValues = new ArrayList<>();
		List<String> expectedLabels = new ArrayList<>(); // it may be removed -- see if legends work as expected label
		
		HashMap<String, Boolean> vendorHasData = GeneralHelper.vendorHasDataForSelectedMonth(allValuesFromFile);
		
				
		for(int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++){
			
			HashMap<String, String> map = new HashMap<>();
			
			for (int j = 0; j < allValuesFromFile.get(indexMonthValues).size(); j++) {
			
				String keyVendor = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
				
				UsageOneMonth usageOneMonth = allValuesFromFile.get(indexMonthValues).get(j);
				
				if (vendorHasData.get(keyVendor)) {
	
					if (barChartId == ExpenseHelperMultipleVendors.expenseByVendorChart) {
					
						if (categorySelector == ExpenseHelperMultipleVendors.categoryAll){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalCharge()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryVoice){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getVoiceCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryData){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDataCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryMessages){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getMessagesCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryRoaming){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryEquipment){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getEquipmentCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryTaxes){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTaxCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryOther){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getOtherCharges()), true));
							
						} else if (categorySelector == ExpenseHelperMultipleVendors.categoryAccount){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()), true));
							
						}						
						
					} else if (barChartId == ExpenseHelperMultipleVendors.costPerServiceNumberChart) {
						
						String costPerServiceNumber = "$0";
						int numberOfLines = Integer.parseInt(usageOneMonth.getNumberOfLines());
						
						// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
						// This is to avoid the error generated by a division by zero.
						if (numberOfLines != 0) {   
							
							double costTmp = 0;
							
							if (categorySelector == ExpenseHelperMultipleVendors.categoryAll){
								
								costTmp = Double.parseDouble(usageOneMonth.getTotalCharge()) / numberOfLines;
																
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryVoice){

								costTmp = Double.parseDouble(usageOneMonth.getVoiceCharges()) / numberOfLines;
																
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryData){

								costTmp = Double.parseDouble(usageOneMonth.getDataCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryMessages){

								costTmp = Double.parseDouble(usageOneMonth.getMessagesCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryRoaming){

								costTmp = Double.parseDouble(usageOneMonth.getRoamingCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryEquipment){

								costTmp = Double.parseDouble(usageOneMonth.getEquipmentCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryTaxes){

								costTmp = Double.parseDouble(usageOneMonth.getTaxCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryOther){

								costTmp = Double.parseDouble(usageOneMonth.getOtherCharges()) / numberOfLines;
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryAccount){

								costTmp = Double.parseDouble(usageOneMonth.getTotalAccountLevelCharges()) / numberOfLines;
							}	
							
							costPerServiceNumber = UsageCalculationHelper.roundNoDecimalDigits(costTmp, true);
							
						}
						
						map.put(usageOneMonth.getVendorName(), costPerServiceNumber);
						
						
					} else if (barChartId == ExpenseHelperMultipleVendors.countServiceNumbersChart) {
						
						map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getNumberOfLines()), false));
												
					}
					
					// If the label to be added is not included on the expectedLabels list yet; and the label is included in the legends list 
					if(!expectedLabels.contains(keyVendor) && legends.contains(keyVendor))
						expectedLabels.add(keyVendor);
					
				}
									
			}
			
			expectedValues.add(map);
			
		}	
		
		
		
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
			
//			System.out.println("More Than 5 Vendors Selected: " + moreThanFiveVendorsSelected);
//			System.out.println("6 Vendors in Chart: " + sixVendorsInChart);
						
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
							
						if (barChartId == ExpenseHelperMultipleVendors.expenseByVendorChart) {
							
							if (categorySelector == ExpenseHelperMultipleVendors.categoryAll){
										
								otherTmpSum += (Double.parseDouble(usage.getTotalCharge()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryVoice){
								
								otherTmpSum += (Double.parseDouble(usage.getVoiceCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryData){
								
								otherTmpSum += (Double.parseDouble(usage.getDataCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryMessages){
								
								otherTmpSum += (Double.parseDouble(usage.getMessagesCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryRoaming){
								
								otherTmpSum += (Double.parseDouble(usage.getRoamingCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryEquipment){
								
								otherTmpSum += (Double.parseDouble(usage.getEquipmentCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryTaxes){
								
								otherTmpSum += (Double.parseDouble(usage.getTaxCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryOther){
								
								otherTmpSum += (Double.parseDouble(usage.getOtherCharges()));
								
							} else if (categorySelector == ExpenseHelperMultipleVendors.categoryAccount){
								
								otherTmpSum += (Double.parseDouble(usage.getTotalAccountLevelCharges()));
								
							}
							
							expensesOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, true);
							expectedValues.get(indexMonthValues).put(otherVendors, expensesOther); 
							
							
						} else if (barChartId == ExpenseHelperMultipleVendors.costPerServiceNumberChart) {
							
							int numberOfLines = Integer.parseInt(usage.getNumberOfLines());
							
							// If numberOfLines is not zero, then calculate the costPerServiceNumber, otherwise, costPerServiceNumber will be set to zero. 
							// This is to avoid the error generated by a division by zero.
							if (numberOfLines != 0) { 
								
								if (categorySelector == ExpenseHelperMultipleVendors.categoryAll){
									
									otherTmpSum += Double.parseDouble(usage.getTotalCharge());
																										
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryVoice){

									otherTmpSum += Double.parseDouble(usage.getVoiceCharges());
																	
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryData){

									otherTmpSum += Double.parseDouble(usage.getDataCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryMessages){

									otherTmpSum += Double.parseDouble(usage.getMessagesCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryRoaming){

									otherTmpSum += Double.parseDouble(usage.getRoamingCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryEquipment){

									otherTmpSum += Double.parseDouble(usage.getEquipmentCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryTaxes){

									otherTmpSum += Double.parseDouble(usage.getTaxCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryOther){

									otherTmpSum += Double.parseDouble(usage.getOtherCharges());
									
								} else if (categorySelector == ExpenseHelperMultipleVendors.categoryAccount){

									otherTmpSum += Double.parseDouble(usage.getTotalAccountLevelCharges());
									
								}
								
								numberLinesTmpSum += numberOfLines;
								costServNumberTmpSum = otherTmpSum / numberLinesTmpSum;
								expensesOther = UsageCalculationHelper.roundNoDecimalDigits(costServNumberTmpSum, true);
								expectedValues.get(indexMonthValues).put(otherVendors, expensesOther); 
								
							} else if (numberOfLines == 0 && numberLinesTmpSum == 0) {
								
								expectedValues.get(indexMonthValues).put(otherVendors, "$0"); 
								
							}
							
							
						} else if (barChartId == ExpenseHelperMultipleVendors.countServiceNumbersChart) {
							
							otherTmpSum += Double.parseDouble(usage.getNumberOfLines());				
							countOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
							expectedValues.get(indexMonthValues).put(otherVendors, countOther);
							
						}	
														
					}
					
				}
				
			}
			
			expectedLabels.add(otherVendors);
			
		}
		
	
		for (int i = 0; i < expectedLabels.size(); i++) {		
//			System.out.println(" Label " + i + ": " + expectedLabels.get(i));
		}
		

		// Verify the info contained on each of the tooltips for the 13 months 		
		
		while (indexHighchart <= monthYearList.size()) {
			
			String cssBar = "";
			
			if (barChartId == ExpenseHelperMultipleVendors.costPerServiceNumberChart) {
				
				cssBar = "#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + indexHighchart + ")";
	
				
			} else if (barChartId == ExpenseHelperMultipleVendors.expenseByVendorChart || barChartId == ExpenseHelperMultipleVendors.countServiceNumbersChart) {
				
				cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
				
			}
			
			
			String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(1)";
			
			// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));
			WebElement line = driver.findElement(By.cssSelector(cssLine));
			
			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// Get the location of the second line of the chart -> to get the "y" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			
			Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
			Point lineCoordinates = GeneralHelper.getAbsoluteLocation(line);   
			Robot robot = new Robot();
			
			int y_offset = 0;
			
			if (loginType.equals(LoginType.Command)) {
				
				if (barChartId == ExpenseHelperMultipleVendors.expenseByVendorChart) {
					
					y_offset = -50;  // these coordinates work on CMD :) - Dash v.1.1.12 - Feb 24
					
				}
				
				if (barChartId == ExpenseHelperMultipleVendors.costPerServiceNumberChart) {
					
					y_offset = -300;  // these coordinates work on CMD :) - Dash v.1.1.12 - Feb 24
					
				}
				
				if (barChartId == ExpenseHelperMultipleVendors.countServiceNumbersChart) {
				
					y_offset = -600;  // these coordinates work on CMD :) - Dash v.1.1.12 - Feb 24
					
				}
				
			} 
			
			int x = barCoordinates.getX();
			int y = lineCoordinates.getY() + y_offset;
			
			robot.mouseMove(x, y);
//			System.out.println("coordinates:  x: " + x + "  y: " + y);
			
			Thread.sleep(500);
			
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
				//System.out.println("Tooltip present");
			} catch (Exception e) {
				System.out.println("Tooltip NOT present");
				e.printStackTrace();
			}
					
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name>
			// 3 <amount shown for the vendor>
			
			
			int expectedAmountItemsTooltip = (amount * 3) + 1;
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index =  i * 3 - 1;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
					
				// Get the value expected
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);
				
				System.out.println("Vendor: " + labelFound);
				System.out.println("valueFound: " + valueFound + ", valueExpected: " + valueExpected);

				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth).replace("/", "-");
				
			Assert.assertEquals(monthYearFound, monthYearExpected);
			System.out.println("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}
		
	}

}
