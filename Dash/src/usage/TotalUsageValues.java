package usage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class TotalUsageValues extends BaseClass {

	
	private static HashMap<String, UsageOneMonth> vendorUsageMap;
	private static HashMap<String, String> domesticValues;
	private static HashMap<String, String> overageValues;
	private static HashMap<String, String> roamingValues;
	private static List<WebElement> vendorsSelectedCheckBox;
	private static List<WebElement> vendorsInChart;
	private static List<String> vendorsInChartNames;
	private static int expectedAmountItemsTooltip = 4;
	private static int chartNum;
	private static int category;
	
	
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyTotalUsageChartTooltip(int barChartId, List<UsageOneMonth> listOneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		chartNum = barChartId; 
		
		category = categorySelector;
		
		vendorsSelectedCheckBox = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		
		vendorUsageMap = new HashMap<String, UsageOneMonth>();
		
		vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
	
		getExpectedValues(listOneMonthData);
	
		calculateOtherExpectedValues();
		
		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		int indexHighchart = 1;
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while (indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()){
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next vendor
			if (vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()) || vendorsInChartNames.get(indexVendorInChart).equals("Other")){
				
				moveMouseToBar(chartId, indexHighchart);
				
				try {
					WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
					//System.out.println("Tooltip present");
				} catch (Exception e) {
					System.out.println("Tooltip NOT present");
					e.printStackTrace();
				}
				
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
								
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
				
				// For Domestic chart: 
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Domestic:
				// 3 <Amount for Domestic>
				// 4 ? --> bullet
				// 5 Domestic Overage:
				// 6 <Amount for Domestic Overage>
				
				// For Roaming chart: 
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Roaming:
				// 3 <Amount for Roaming>
				
				// Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country/vendor shown on the tooltip
				String vendorNameFound = tooltip.get(0).getText();
				String vendorNameExpected = vendorsInChartNames.get(indexVendorInChart);
					
				Assert.assertEquals(vendorNameFound, vendorNameExpected);
				ShowText("vendorNameFound: " + vendorNameFound + ", vendorNameExpected: " + vendorNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for(int i = 1; i <= legends.size(); i++){
				
					int index =  i * 3 - 1;
									
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");

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


	
	private static void getExpectedValues(List<UsageOneMonth> listOneMonthData) {
		
		for (UsageOneMonth u: listOneMonthData) {
			vendorUsageMap.put(u.getVendorName(), u);
		}
		
		
		// The HashMap<vendorName, value>  will contain the expected values
		domesticValues = new HashMap<String, String>();
		overageValues = new HashMap<String, String>();
		roamingValues = new HashMap<String, String>();
		
		
		for(int i = 0; i < listOneMonthData.size(); i++){
			
			if (chartNum == UsageHelper.totalUsageDomesticChart) {
				
				if(category == UsageHelper.categoryVoice){
					
					domesticValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticVoice()), false));
					overageValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticOverageVoice()), false));
					expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
					
				} else if (category == UsageHelper.categoryData){
					
					domesticValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getDomesticDataUsageKb())));
					
				} else if (category == UsageHelper.categoryMessages){
					
					domesticValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticMessages()), false));
					
				}
				
			} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
				
				if(category == UsageHelper.categoryVoice){
					
					roamingValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingVoice()), false));
					
				} else if (category == UsageHelper.categoryData){
					
					roamingValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getRoamingDataUsageKb())));
					
				} else if (category == UsageHelper.categoryMessages){
					
					roamingValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingMessages()), false));
					
				}
				
			}
			
		}
		
	}
	
	
	
	private static void calculateOtherExpectedValues() {
		
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String domesticValueOther = "";
		String overageValueOther = "";
		String roamingValueOther = "";
		String otherVendors = "Other";
	
		// If more than 5 vendors are selected and there are 6 vendors in chart,  
		// then the vendors that have data for the selected month are summarized in the "Other" item.
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
			
			double domesticTmpSum = 0;
			double overageTmpSum = 0;
			double roamingTmpSum = 0;
			
			
			for (int i = 0; i < vendorsSelectedCheckBox.size(); i++){
				
				String v = vendorsSelectedCheckBox.get(i).getText();
				
				if (!vendorsInChartNames.contains(v)){
					
					ShowText("Vendor " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) vendorUsageMap.get(v);

					// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getDomesticVoice();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						if (chartNum == UsageHelper.totalUsageDomesticChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticVoice());
								domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum, false);
								
								overageTmpSum += Double.parseDouble(usage.getDomesticOverageVoice());
								overageValueOther = UsageCalculationHelper.roundNoDecimalDigits(overageTmpSum, false);
								overageValues.put(otherVendors, overageValueOther);
								
							} else if (category == UsageHelper.categoryData){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticDataUsageKb());
								domesticValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(domesticTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticMessages());
								domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum, false);
								
							}
							
							domesticValues.put(otherVendors, domesticValueOther);
							
							
						} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingVoice());
								roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, false);
								
							} else if (category == UsageHelper.categoryData){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingDataUsageKb());
								roamingValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(roamingTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingMessages());
								roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, false);
								
							}
							
							roamingValues.put(otherVendors, roamingValueOther);
							
						}
						
					} 
						
				}

			}
			
		}
		
	}

	
	
	private static void moveMouseToBar(String chartId, int indexHighchart) throws AWTException, InterruptedException {
		
		String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
		
		// The 'bar' WebElement will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssSelector));

		// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
		Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x = coordinates.getX();
		int y = coordinates.getY();
		
		Dimension d = bar.getSize();
		int height = d.getHeight();
		int width = d.getWidth();
		
		int x_offset = (int) (width * 0.5);
		int y_offset = (int) (height * 0.5);
		
		// If the bar's width is zero (it means the value represented is zero) then the coordinates passed to robot.mouseMove will not be useful to get the tooltip visible.
		// We add 20 to "x" so the mouse is hovered over the chart and the tooltip is displayed. 
		if (width == 0)
			x_offset = 20;
		
		y_offset += (int) GeneralHelper.getScrollPosition();
		
//		if (loginType.equals(LoginType.Command)) {
//			
//			y_offset = y_offset -50;  //  these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
//			
//		}						

		x = x + x_offset;
		y = y + y_offset;
		
		Robot robot = new Robot();
		
		robot.mouseMove(x, y); 
		Thread.sleep(500);
		robot.mouseMove(x + 5, y);
		
		if (width > 10.0) {
			bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
		}
		
		if (width < 10.0) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		
		
	}

	
	
	private static void verifyValuesFound(String labelFound, String valueFound, String vendorNameExpected, int index) {
		
		String valueExpected = "";
		String labelExpected = "";
		
		// Verify the labels' text and amounts shown on the tooltip 					
		if (chartNum == UsageHelper.totalUsageDomesticChart) {
			
			if (index == 2) {
				
				valueExpected = domesticValues.get(vendorNameExpected);
				labelExpected = "Domestic";
				
			}
			
			if (category == UsageHelper.categoryVoice && index == 5) {
				
				valueExpected = overageValues.get(vendorNameExpected);
				labelExpected = "Domestic Overage";
				
			}
			
		} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
			
			valueExpected = roamingValues.get(vendorNameExpected);
			labelExpected = "Roaming";
			
		}
		
		Assert.assertEquals(labelFound, labelExpected);
		
		ShowText("  labelFound: " + labelFound + ", labelExpected: " + labelExpected);
		ShowText("  valueFound: " + valueFound + ", valueExpected: " + valueExpected);

		GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
		
		
	}


	
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	// **** ONLY FOR ONE VENDOR SELECTED ****
//	public static void verifyTotalUsageChartTooltip(int chartNum, UsageOneMonth oneMonthData, int category) throws ParseException, InterruptedException, AWTException {
//		
//		String chartId = UsageHelper.getChartId(chartNum);
//		
//		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
//		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
//		
//		int indexHighchart = 1;
//		
//		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
//		List<String> vendorsInChartList = new ArrayList<String>();
//		
//		for(int i = 0; i < vendorsInChart.size(); i++){
//			vendorsInChartList.add(vendorsInChart.get(i).getText());
//		}		
//				
////			Thread.sleep(2000);
//		
//		String domesticValue = "";
//		String overageValue = "";
//		String roamingValue = "";
//		int expectedAmountItemsTooltip = 4;
//		
//		// Get the data and set up the expected values to be compared to the values found on the tooltips
//		if (chartNum == 0) {
//		
//			if(category == UsageHelper.categoryVoice){
//				
//				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticVoice()), false);
//				overageValue = oneMonthData.getDomesticOverageVoice();
//				expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
//				
//			} else if (category == UsageHelper.categoryData){
//				
//				domesticValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getDomesticDataUsageKb()));
//				
//			} else if (category == UsageHelper.categoryMessages){
//				
//				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticMessages()), false);
//				
//			}
//			
//		} else if (chartNum == 1) {
//			
//			if(category == UsageHelper.categoryVoice){
//				
//				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingVoice()), false);
//				
//			} else if (category == UsageHelper.categoryData){
//				
//				roamingValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getRoamingDataUsageKb()));
//				
//			} else if (category == UsageHelper.categoryMessages){
//				
//				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingMessages()), false);
//				
//			}
//			
//		}
//		
//		
//		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
//		while(indexHighchart <= vendorsInChartList.size()){
//			
//			String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
//			//ShowText("cssSelector: " + cssSelector);
//			
//			// The 'bar' WebElement will be used to set the position of the mouse on the chart
//			WebElement bar = driver.findElement(By.cssSelector(cssSelector));
//
//			// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
//			Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
//			
//			int x = coordinates.getX();
//			int y = coordinates.getY();
//			
//			Dimension d = bar.getSize();
//			int height = d.getHeight();
//			int width = d.getWidth();
//
//						
//			Robot robot = new Robot();
//			
//			int x_offset = (int) (width * 0.5);
//			int y_offset = (int) (height * 0.5);
//			
//			// If the bar's width is zero (it means the value represented is zero) then the coordinates passed to robot.mouseMove will not be useful to get the tooltip visible.
//			// We add 20 to "x" so the mouse is hovered over the chart and the tooltip is displayed. 
//			if (width == 0)
//				x_offset = 50;
//			
//			if (loginType.equals(LoginType.Command)) {
//				
//				y_offset = y_offset -50;  // these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
//				
//			}						
//
//			x = x + x_offset;
//			y = y + y_offset;
//			
//			robot.mouseMove(x + 20, y); 
//			Thread.sleep(500);
//			
//			robot.mouseMove(x, y); 
//			
//			robot.mousePress(InputEvent.BUTTON1_MASK);
//			robot.mouseRelease(InputEvent.BUTTON1_MASK);
//			
//				
//						
//			try {
//				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
//				//ShowText("Tooltip present");
//			} catch (Exception e) {
//				ShowText("Tooltip NOT present");
//				e.printStackTrace();
//			}
//			
//			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
//			
//			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
//			
//			// For Domestic chart: 
//			// 0 <vendor/country name>
//			// 1 ? -- this is for the bullet
//			// 2 Domestic:
//			// 3 <Amount for Domestic>
//			// 4 ? --> bullet
//			// 5 Domestic Overage:
//			// 6 <Amount for Domestic Overage>
//			
//			// For Roaming chart: 
//			// 0 <vendor/country name>
//			// 1 ? -- this is for the bullet
//			// 2 Roaming:
//			// 3 <Amount for Roaming>
//			
//			Thread.sleep(2000);
//			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
//			
//						
//			// Verify country/vendor shown on the tooltip
//			//ShowText("Tooltip text: " + tooltip.get(0).getText());
//						
//			String vendorNameFound = tooltip.get(0).getText();
//			String vendorNameExpected = oneMonthData.getVendorName();
//			
//			ShowText("vendorNameFound: " + vendorNameFound + ", vendorNameExpected: " + vendorNameExpected);
//			Assert.assertEquals(vendorNameFound, vendorNameExpected);
//									
//			
//			// Verify the label and the amount shown on the tooltip
//			for(int i = 1; i <= legends.size(); i++){
//			
//				int index =  i * 3 - 1;
//								
//				// Get the label and remove colon at the end of its text 
//				String labelFound = tooltip.get(index).getText().replace(":", "");   //substring(0, tooltip.get(index).getText().length()-1);
//
//				// Get the value on tooltip and remove all blank spaces
//				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
//				
//				ShowText("label: " + labelFound); 
//				
//				String valueExpected = "";
//				
//				// Verify the labels' text and amounts shown on the tooltip 					
//				if (chartNum == UsageHelper.totalUsageDomesticChart) {
//					
//					if (index == 2) {
//						Assert.assertEquals(labelFound, "Domestic");
//						valueExpected = domesticValue;
//					}
//					
//					if (category == UsageHelper.categoryVoice && index == 5) {
//						
//						Assert.assertEquals(labelFound, "Domestic Overage"); 
//						valueExpected = overageValue;
//					}
//					
//				} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
//					
//					Assert.assertEquals(labelFound, "Roaming"); 
//					valueExpected = roamingValue;
//					
//				}
//
//				ShowText("valueFound: " + valueFound + ", valueExpected: " + valueExpected); 
//				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
//				
//			}
//						
//			indexHighchart++;
//			
//		}
//		
//	}


	
}
