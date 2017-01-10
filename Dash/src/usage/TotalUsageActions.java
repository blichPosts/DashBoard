package usage;

import org.testng.Assert;

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

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class TotalUsageActions extends BaseClass{

	
	// Verifies that if a vendor is selected on the PoV section, and the value to be shown is greater than zero, the vendor name is listed on the x axis on the chart,
	// unless there are already 5 vendors listed. In this case the value for the selected vendor may be included under "Other"  
	public static void vendorsAddedToCharts(int chartNum, int kpiNum) throws InterruptedException {
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames(); 
		List<WebElement> kpiTiles = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
			
		String currentKPIvalue = "";
		String previousKPIvalue = "";
		
		String chartId = UsageHelper.getChartId(chartNum);
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));
		
		categorySelectors.get(kpiNum-1).click();
		
		int count = 1;
		
		for (WebElement v : vendors){
		
			String vendorName = v.getText();
			
			// Select one vendor
			CommonTestStepActions.selectOneVendor(vendorName);
			//System.out.println("Vendor selected: " + vendorName);
			
			// Wait 2 seconds to the values to get updated on Dashboard, after the vendor selection
			Thread.sleep(2000);
			
			currentKPIvalue = kpiTiles.get(kpiNum-1).getText(); 
			//System.out.println("Current KPI tile value: " + currentKPIvalue);
			
			List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
			
			
			// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
			if(count == 1){
				
				//System.out.println("First Vendor..");
				
				// Verify that vendor is listed on chart if value is NOT zero 
				if(!currentKPIvalue.equals("0")){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						//System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
				}
				
				
			} else if (count > 1 && count < 6) { // From second vendor. If KPI value doesn't change, it means value for the last selected vendor is zero and it won't be listed on the chart.
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						//System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
					
				}
				
			} else if (count >= 6) {
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						//System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText()) || label.getText().equals("Other")){
							vendorInChart = true;
						}
						
					}
					
					Assert.assertTrue(vendorInChart);
					
				}
				
			}
  
			previousKPIvalue = currentKPIvalue;
			count++;
			
		}
		
		
	}

	
	
	// Verifies that if a country is selected on the PoV section, and the value to be shown is greater than zero, the country name is listed on the x axis on the chart,
	// unless there are already 5 countries listed. In this case the value for the selected country may be included under "Other"  
	public static void countriesAddedToCharts(int chartNum, int kpiNum) throws InterruptedException {
		
		List<WebElement> kpiTiles = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		List<List<WebElement>> countriesAndVendors = UsageHelper.getCountriesAndVendors();
		
		String currentKPIvalue = "";
		String previousKPIvalue = "";
		
		String chartId = UsageHelper.getChartId(chartNum);
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));
		
		categorySelectors.get(kpiNum-1).click();
		
		int vendorCount = 1;
		int countryCount = 1;
				
		
		for (List<WebElement> c : countriesAndVendors){
			
			String countryName = c.get(0).getText();
			
			//System.out.println("Country Name: " + countryName);
			
			for (int i = 1; i < c.size(); i++){
			
				String vendorName = c.get(i).getText();
				
				// Select one vendor
				CommonTestStepActions.selectOneVendor(vendorName);
				//System.out.println("Vendor selected: " + vendorName);
				
				// Wait 2 seconds to give time for the values to get updated on Dashboard, after the vendor selection
				Thread.sleep(2000);
				
				currentKPIvalue = kpiTiles.get(kpiNum-1).getText(); 
				//System.out.println("Current KPI tile value: " + currentKPIvalue);
				
				List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
				
				// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
				if(vendorCount == 1 && countryCount == 1){
					
					// Verify that vendor is listed on chart if value is NOT zero 
					if(!currentKPIvalue.equals("0")){
						
						boolean countryInChart = false; 
						
						for (WebElement label: xAxisLabels){
							//System.out.println("Label in x axis: " + label.getText());
							
							if(label.getText().equals(countryName)){
								countryInChart = true;
							}
						}
						
						Assert.assertTrue(countryInChart);
					}
					
					
				} else if (countryCount > 1 && countryCount < 6) { // From second vendor. If KPI value doesn't change, it means value for the last selected vendor is zero and it won't be listed on the chart.
					
					// Verify that vendor is listed on chart if value is NOT zero
					if(!currentKPIvalue.equals(previousKPIvalue)){
						
						boolean countryInChart = false; 
						
						for (WebElement label: xAxisLabels){
							//System.out.println("Label in x axis: " + label.getText());
							
							if(label.getText().equals(countryName)){
								countryInChart = true;
							}
						}
						
						Assert.assertTrue(countryInChart);
						
					}
					
				} else if (countryCount >= 6) {
					
					// Verify that vendor is listed on chart if value is NOT zero
					if(!currentKPIvalue.equals(previousKPIvalue)){
						
						boolean countryInChart = false; 
						
						for (WebElement label: xAxisLabels){
							//System.out.println("Label in x axis: " + label.getText());
							
							if(label.getText().equals(countryInChart) || label.getText().equals("Other")){
								countryInChart = true;
							}
							
						}
						
						Assert.assertTrue(countryInChart);
						
					}
					
				}
				
				vendorCount++;
				previousKPIvalue = currentKPIvalue;
								
			}
			
			countryCount++;
			
		}

	}




	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	// It does not verify the amounts -- SEE following method
	public static void verifyTotalUsageChartTooltip(int barChartId) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		// It gets the legends for "Domestic" and "Domestic Overage" or Roaming
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		int indexHighchart = 1;
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartList = new ArrayList<String>();
		
		for(int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartList.add(vendorsInChart.get(i).getText());
		}	
				
		boolean firstBar = true;
				
		Thread.sleep(2000);
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while(indexHighchart <= vendorsInChartList.size()){
			
			String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			//System.out.println("cssSelector: " + cssSelector);
			
			// The 'bar' WebElement will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssSelector));

			// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
			Point coordinates = bar.getLocation();
			Robot robot = new Robot(); 
			robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 70); // these coordinates work :) 
			
			if(firstBar && !(bar.getAttribute("height").toString().equals("0"))){
				bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
				firstBar = false;
			}
				
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
				//System.out.println("Tooltip present");
			} catch (Exception e) {
				System.out.println("Tooltip NOT present");
				e.printStackTrace();
			}
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			int expectedAmountItemsTooltip = 0;
			
			if(barChartId == 0)
				expectedAmountItemsTooltip = 7;
			else if (barChartId == 1)
				expectedAmountItemsTooltip = 4;
			
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			
			// For Domestic chart: 
			// <vendor/country name>
			// ? -- this is for the bullet
			// Domestic
			// <Amount for Domestic>
			// Domestic Overage
			// <Amount for Domestic Overage>
			
			// For Roaming chart: 
			// <vendor/country name>
			// ? -- this is for the bullet
			// Roaming
			// <Amount for Roaming>
			
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			// Verify country/vendor shown on the tooltip
			//System.out.println("Tooltip text: " + tooltip.get(0).getText());
			Assert.assertEquals(tooltip.get(0).getText(), vendorsInChartList.get(indexHighchart-1));
						
			
			// Verify the vendor's name and the amount shown on the tooltip
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
								
				// Verify the vendor's name on tooltip
				// Remove colon at the end of legend's name 
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
				
				Assert.assertEquals(labelFound, legends.get(i - 1).getText()); 
				//System.out.println("Tooltip text: " + labelFound);

			}
			
			indexHighchart++;
			
		}
		
	}
	
	
	
	
/*		
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	public static void verifyTotalUsageChartTooltip(int barChartId, UsageOneMonth oneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		int indexHighchart = 1;
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartList = new ArrayList<String>();
		
		for(int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartList.add(vendorsInChart.get(i).getText());
		}	
				
		//boolean firstBar = true;
				
		Thread.sleep(2000);
		
		String domesticValue = "";
		String overageValue = "";
		String roamingValue = "";
		int expectedAmountItemsTooltip = 4;
		
		if (barChartId == 0) {
		
			if(categorySelector == UsageHelper.categoryVoice){
				
				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticVoice()));
				overageValue = oneMonthData.getDomesticOverageVoice();
				expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
				
			} else if (categorySelector == UsageHelper.categoryData){
				
				domesticValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getDomesticDataUsageKb()));
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticMessages()));
				
			}
			
		} else if (barChartId == 1) {
			
			if(categorySelector == UsageHelper.categoryVoice){
				
				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingVoice()));
				
			} else if (categorySelector == UsageHelper.categoryData){
				
				roamingValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getRoamingDataUsageKb()));
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingMessages()));
				
			}
			
		}
		
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while(indexHighchart <= vendorsInChartList.size()){
			
			String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			//System.out.println("cssSelector: " + cssSelector);
			
			// The 'bar' WebElement will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssSelector));

			// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
			Point coordinates = bar.getLocation();
			Robot robot = new Robot(); 
			robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 70); // these coordinates work :) 
			
			if (!(bar.getAttribute("height").toString().equals("0"))){
				bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
				//firstBar = false;
			}
			
			if (bar.getAttribute("height").toString().equals("0")){
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
				
			
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
			
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			// Verify country/vendor shown on the tooltip
			//System.out.println("Tooltip text: " + tooltip.get(0).getText());
//			Assert.assertEquals(tooltip.get(0).getText(), oneMonthData.getVendorName());  //vendorsInChartList.get(indexHighchart-1));
						
			
			// Verify the label and the amount shown on the tooltip
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
								
				// Get the label and remove colon at the end of its text 
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
				
				System.out.println("label: " + labelFound + ", value: " + valueFound); 
							
				// Verify the labels' text and amounts shown on the tooltip 					
				if (barChartId == UsageHelper.totalUsageDomesticChart) {
					
					if (index == 2) {
						Assert.assertEquals(labelFound, "Domestic");
						Assert.assertEquals(valueFound, domesticValue);
					}
					
					if (categorySelector == UsageHelper.categoryVoice && index == 5) {
						
						Assert.assertEquals(labelFound, "Domestic Overage"); 
						Assert.assertEquals(valueFound, overageValue);
					}
					
				} else if (barChartId == UsageHelper.totalUsageRoamingChart) {
					
					Assert.assertEquals(labelFound, "Roaming"); 
					Assert.assertEquals(valueFound, roamingValue);
					
				}

			}
			
			indexHighchart++;
			
		}
		
	}

*/
	
	// ********************** HERE **********************

	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	public static void verifyTotalUsageChartTooltip(int barChartId, List<UsageOneMonth> listOneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> vendorsSelectedCheckBox = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		
//		System.out.println("HashMap test!!!");
		HashMap<String, UsageOneMonth> vendorUsageMap = new HashMap<String, UsageOneMonth>();
		for (UsageOneMonth u: listOneMonthData) {
			vendorUsageMap.put(u.getVendorName(), u);
//			System.out.println(vendorUsageMap.get(u.getVendorName()).getVendorName()); // + " - Domestic voice: " + u.getDomesticVoice());
		}
		
		
		Thread.sleep(2000);
		
		// Lists that will contain the expected values
		// replace the lists with values by hashmaps <vendorName, value> 
		HashMap<String, String> domesticValue = new HashMap<String, String>();
		HashMap<String, String> overageValue = new HashMap<String, String>();
		HashMap<String, String> roamingValue = new HashMap<String, String>();
		
		int expectedAmountItemsTooltip = 4;
		
		for(int i = 0; i < listOneMonthData.size(); i++){
			
			if (barChartId == 0) {
				
				if(categorySelector == UsageHelper.categoryVoice){
					
					domesticValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticVoice())));
					overageValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticOverageVoice())));
					expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
					
				} else if (categorySelector == UsageHelper.categoryData){
					
					domesticValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getDomesticDataUsageKb())));
					
				} else if (categorySelector == UsageHelper.categoryMessages){
					
					domesticValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticMessages())));
					
				}
				
			} else if (barChartId == 1) {
				
				if(categorySelector == UsageHelper.categoryVoice){
					
					roamingValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingVoice())));
					
				} else if (categorySelector == UsageHelper.categoryData){
					
					roamingValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getRoamingDataUsageKb())));
					
				} else if (categorySelector == UsageHelper.categoryMessages){
					
					roamingValue.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingMessages())));
					
				}
				
			}
			
		}
		
		
		
		
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String domesticValueOther = "";
		String overageValueOther = "";
		String roamingValueOther = "";
		String otherVendors = "Other";
	
		// If more than 5 vendors are selected and there are 6 vendors in chart. This will be 5 vendors and "Other", summarizing the values for one or more vendors
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
			
			System.out.println("More Than 5 Vendors Selected: " + moreThanFiveVendorsSelected);
			System.out.println("6 Vendors in Chart: " + sixVendorsInChart);
			
			double domesticTmpSum = 0;
			double overageTmpSum = 0;
			double roamingTmpSum = 0;
			
			
			for (int i = 0; i < vendorsSelectedCheckBox.size(); i++){
				
				String v = vendorsSelectedCheckBox.get(i).getText();
				
				if (!vendorsInChartNames.contains(v)){
					
					System.out.println("Vendor " + v + ", is not listed in chart");
					
				//	for (int j = 0; j <listOneMonthData.size(); j++){
						
						//if (listOneMonthData.get(j).getVendorName().equals(v)){
							
							System.out.println("vendor name to use as a key: " + v);
							UsageOneMonth usage = (UsageOneMonth) vendorUsageMap.get(v);
//							System.out.println(vendorUsageMap.get(usage.getVendorName()).getVendorName() + " - Domestic voice: " + usage.getDomesticVoice());
							// ******
							// listOneMonthData.get(j) is replaced by usage. See if it works!!!
							
							// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
							boolean usageNull;
							
							try {
								
								usage.getDomesticVoice();
								usageNull = false;
								
							} catch (NullPointerException e) {
								
								usageNull = true;
								
							}
							
							
							if (!usageNull) {
								
								System.out.println("there's data for the vendor");
								
								if (barChartId == 0) {
									
									if(categorySelector == UsageHelper.categoryVoice){
										// ****************
										// ** Trying first with only one vendor summed up in "Other", then try for more vendors , add the value to the ...Other variables
										// **************
										domesticTmpSum += Double.parseDouble(usage.getDomesticVoice());
										domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum);  //Double.parseDouble(usage.getDomesticVoice()));
										
										overageTmpSum += Double.parseDouble(usage.getDomesticOverageVoice());
										overageValueOther = UsageCalculationHelper.roundNoDecimalDigits(overageTmpSum);
										overageValue.put(otherVendors, overageValueOther);
										
									} else if (categorySelector == UsageHelper.categoryData){
										
										domesticTmpSum += Double.parseDouble(usage.getDomesticDataUsageKb());
										domesticValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(domesticTmpSum);
										
									} else if (categorySelector == UsageHelper.categoryMessages){
										
										domesticTmpSum += Double.parseDouble(usage.getDomesticMessages());
										domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum);
										
									}
									
									domesticValue.put(otherVendors, domesticValueOther);
									
									
								} else if (barChartId == 1) {
									
									if(categorySelector == UsageHelper.categoryVoice){
										
										roamingTmpSum += Double.parseDouble(usage.getRoamingVoice());
										roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum);
										
									} else if (categorySelector == UsageHelper.categoryData){
										
										roamingTmpSum += Double.parseDouble(usage.getRoamingDataUsageKb());
										roamingValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(roamingTmpSum);
										
									} else if (categorySelector == UsageHelper.categoryMessages){
										
										roamingTmpSum += Double.parseDouble(usage.getRoamingMessages());
										roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum);
										
									}
									
									roamingValue.put(otherVendors, roamingValueOther);
									
								}
								
							} else {
								System.out.println("there's NO data for the vendor");
							}
							
					}
					
			//	}
				
			}
			
		}
		
		
		
		
		
		
		//System.out.println("domestic list size: " + domesticValue.size());
//		System.out.println("vendorsInChartNames: " + vendorsInChartNames.size()); 
		for(String s: vendorsInChartNames){
//			System.out.println("*** " + s);
		}
		
		//System.out.println("vendorsSelectedCheckBox: " + vendorsSelectedCheckBox.size()); 
		for(WebElement w: vendorsSelectedCheckBox){
//			System.out.println("*** " + w.getText());
		}

		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		int indexHighchart = 1;
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while(indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()){
			
//			System.out.println("indexVendorSelected: " + indexVendorSelected + ", vendorsSelectedCheckBox.size(): " + vendorsSelectedCheckBox.size());
//		    System.out.println("indexVendorInChart: " + indexVendorInChart + ", vendorsInChartNames.size(): " + vendorsInChartNames.size()); 
//			System.out.println("Vendor name chart: " +  vendorsInChartNames.get(indexVendorInChart));
//			System.out.println("Vendor name checkbox: " +  vendorsSelectedCheckBox.get(indexVendorSelected).getText());
//			System.out.println("Condition is: " +  vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()));
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", run the test. Else, move to the next vendor
			if(vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()) || vendorsInChartNames.get(indexVendorInChart).equals("Other")){
			
			//	System.out.println("Inside if");
				
				String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
				//System.out.println("cssSelector: " + cssSelector);
				
				// The 'bar' WebElement will be used to set the position of the mouse on the chart
				WebElement bar = driver.findElement(By.cssSelector(cssSelector));
	
				// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
				Point coordinates = bar.getLocation();
				Robot robot = new Robot(); 
				robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 70); // these coordinates work :) 
				
				if (Double.parseDouble(bar.getAttribute("height").toString()) > 10.0) {   //if (!(bar.getAttribute("height").toString().equals("0"))){
					bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
					//firstBar = false;
				}
				
				if (Double.parseDouble(bar.getAttribute("height").toString()) < 10.0) {  //if (bar.getAttribute("height").toString().equals("0")){
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
					
				
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
				
				Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country/vendor shown on the tooltip
				String vendorNameFound = tooltip.get(0).getText();
				String vendorNameExpected = "";
				
				//if (vendorsInChartNames.get(indexVendorInChart).equals("Other"))
				//	firstLineExpected = "Other";
				//else
					vendorNameExpected = vendorsInChartNames.get(indexVendorInChart);
					
				Assert.assertEquals(vendorNameFound, vendorNameExpected);  //listOneMonthData.get(indexVendorInChart).getVendorName());  //vendorsInChartList.get(indexHighchart-1));
				
				System.out.println("firstLineFound: " + vendorNameFound + ", firstLineExpected: " + vendorNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for(int i = 1; i <= legends.size(); i++){
				
					int index =  i * 3 - 1;
									
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
					
					String valueExpected = "";
					String labelExpected = "";
					
					// Verify the labels' text and amounts shown on the tooltip 					
					if (barChartId == UsageHelper.totalUsageDomesticChart) {
						
						if (index == 2) {
							
							valueExpected = domesticValue.get(vendorNameExpected);    //get(indexVendorInChart);
							labelExpected = "Domestic";
							
							Assert.assertEquals(labelFound, labelExpected);
							Assert.assertEquals(valueFound, valueExpected);
							System.out.println(" Assert Domestic");
						}
						
						if (categorySelector == UsageHelper.categoryVoice && index == 5) {
							
							valueExpected = overageValue.get(vendorNameExpected);
							labelExpected = "Domestic Overage";
							
							Assert.assertEquals(labelFound, labelExpected);
							Assert.assertEquals(valueFound, valueExpected);
							System.out.println(" Assert Overage");
						}
						
					} else if (barChartId == UsageHelper.totalUsageRoamingChart) {
						
						valueExpected = roamingValue.get(vendorNameExpected);
						labelExpected = "Roaming";
						
						Assert.assertEquals(labelFound, labelExpected);
						Assert.assertEquals(valueFound, valueExpected);
						System.out.println(" Assert Roaming");
					}
					
					System.out.println("  labelFound: " + labelFound + ", labelExpected: " + labelExpected);
					System.out.println("  valueFound: " + valueFound + ", valueExpected: " + valueExpected);
	
				}
				
				indexHighchart++;
				indexVendorInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((vendorsSelectedCheckBox.size() - indexVendorSelected) > 1)
				indexVendorSelected++;
			
		}	
		
	}



	
}
