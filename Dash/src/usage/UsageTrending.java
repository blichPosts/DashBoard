package usage;

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
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class UsageTrending extends BaseClass {

	
	public static void verifyUsageTrendingTitle(String titleFirstPart){
		
		String totalUsageTitleExpected;
		String categoryLabel;
		String totalUsageTitleFound;
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(even)>div"));

		
		for(int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click();
			totalUsageTitleFound = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]")).getText();
			categoryLabel = categorySelectors.get(i).getText();
			totalUsageTitleExpected = titleFirstPart + categoryLabel;
			
			//System.out.println("Title found:    " + totalUsageTitleFound);
			//System.out.println("Title expected: " + totalUsageTitleExpected);
			
			Assert.assertEquals(totalUsageTitleFound, totalUsageTitleExpected, "Title found is different from title expected.");
			
		}

	}
		
	
	
	
	public static void verifyBarChartTitlesUsageTrending(){
		
		
		String domesticTitleFound; 
		String roamingTitleFound;
	
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));

		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			domesticTitleFound = driver.findElement(By.cssSelector(".tdb-charts__label.tdb-text--italic>.tdb-text--bold")).getText(); 
			roamingTitleFound = driver.findElement(By.cssSelector(".tdb-trendingCharts-USAGE>.tdb-flexUnit>.tdb-charts__label.tdb-text--italic.tdb-text--bold")).getText();  //.tdb-flexUnit--1.tdb-align--center>.tdb-charts__label.tdb-text--italic.tdb-text--bold")).getText();
			
			System.out.println("usage trending - Domestic title: " + domesticTitleFound);
			System.out.println("usage trending - Roaming title: " + roamingTitleFound);
			
			
			if (i == 0){
				Assert.assertEquals(domesticTitleFound, UsageHelper.domesticTitleVoice);
				Assert.assertEquals(roamingTitleFound, UsageHelper.roamingTitleVoice);
				System.out.println("Voice...");
				
			} else if (i == 1){
				
				/*boolean domesticTitle = domesticTitleFound.equals(UsageHelper.domesticTitleDataGB) || domesticTitleFound.equals(UsageHelper.domesticTitleDataTB);  
				Assert.assertTrue(domesticTitle);
				
				boolean roamingTitle = roamingTitleFound.equals(UsageHelper.roamingTitleDataGB) || roamingTitleFound.equals(UsageHelper.roamingTitleDataTB);  
				Assert.assertTrue(roamingTitle);*/
				
				// In Usage Trending charts the data usage is always represented in GB
				Assert.assertTrue(domesticTitleFound.equals(UsageHelper.domesticTitleDataGB));
				Assert.assertTrue(roamingTitleFound.equals(UsageHelper.roamingTitleDataGB));
				
				System.out.println("Data...");
								
			} else if (i == 2){
			
				Assert.assertEquals(domesticTitleFound, UsageHelper.domesticTitleMessages);
				Assert.assertEquals(roamingTitleFound, UsageHelper.roamingTitleMessages);
				System.out.println("Messages...");
			
			}
			
		}
				
		
	}

	
	
	public static void verifyVendorsInLegend_ChartsUsageTrending() {
		
		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = CommonTestStepActions.getVendorsSelectedInPointOfView();
		String chartId;
		
		// Get the id of the "Usage Trending Domestic" chart (THIRD chart)
		chartId = UsageHelper.getChartId(2);
		verifyLegendsInUsageTrendingCharts(chartId, listVendorsChecked);
				
		// Get the id of the "Usage Trending Roaming" chart (FOURTH chart)
		chartId = UsageHelper.getChartId(3);
		verifyLegendsInUsageTrendingCharts(chartId, listVendorsChecked);
		
	}


	public static void verifyCountriesInLegend_ChartsUsageTrending() {
		
		// this list will have the names of the countries that have been SELECTED on the Point of View section
		List<String> listCountriesChecked = CommonTestStepActions.getCountriesSelectedInPointOfView();
		String chartId;
		
		// Get the id of the "Usage Trending Domestic" chart (THIRD chart)
		chartId = UsageHelper.getChartId(2);
		verifyLegendsInUsageTrendingCharts(chartId, listCountriesChecked);
				
		// Get the id of the "Usage Trending Roaming" chart (FOURTH chart)
		chartId = UsageHelper.getChartId(3);
		verifyLegendsInUsageTrendingCharts(chartId, listCountriesChecked);
		
	}
	
	
	private static void verifyLegendsInUsageTrendingCharts(String chartId, List<String> listItemsChecked) {
		
		// Get the vendors/countries that are listed on the horizontal axis of the "Usage Trending (Domestic)" chart
		List<WebElement> itemsFoundInLegend = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
		
		System.out.println("# Items: " + itemsFoundInLegend.size());
		//System.out.println(" Chart: " + chartId);

			
		for(WebElement label: itemsFoundInLegend){
					
			if(!label.getText().equals("Other"))
				Assert.assertTrue(listItemsChecked.contains(label.getText()));
			
			System.out.println("Item in legend: " + label.getText());
			
		}
		
	}


	
	public static void verifyMonthYearInUsageTrendingCharts() throws ParseException{
		
		// this list will have the months that are listed on Month selector on the Point of View section
		List<String> listMonths = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		String chartId;
				
		// Get the id of the "Usage Trending Domestic" chart (THIRD chart)
		chartId = UsageHelper.getChartId(2);
		verifyMonthYearHorizontalAxisUsageTrendingCharts(chartId, listMonths);
				
		// Get the id of the "Usage Trending Roaming" chart (FOURTH chart)
		chartId = UsageHelper.getChartId(3);
		verifyMonthYearHorizontalAxisUsageTrendingCharts(chartId, listMonths);
		
	}
	

	public static void verifyMonthYearHorizontalAxisUsageTrendingCharts(String chartId, List<String> listMonths) {
		
		// Get the months that are listed on the horizontal axis of the "Usage Trending" chart
		List<WebElement> labelsHorizontalAxis = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		System.out.println("# Months: " + labelsHorizontalAxis.size());
		System.out.println(" Chart: " + chartId);

		int amountItemsSelected = labelsHorizontalAxis.size();
		
		// The amount of months must be 13.
		Assert.assertEquals(amountItemsSelected, 13);
		
		
		for(WebElement label: labelsHorizontalAxis){
					
			//System.out.println("Item in legend: " + label.getText());
			Assert.assertTrue(listMonths.contains(label.getText()));
			
		}
		
	}




	public static void verifyBarsCanBeSwitchedOnOff(int barChartId) {
		
		 
		List<WebElement> legends = new ArrayList<>();
		List<WebElement> bars = new ArrayList<>();
		String chartId = UsageHelper.getChartId(barChartId);
		
		legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		bars = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
		
			
		int totalAmountOfVendorsInLegends = legends.size();
		int totalAmountOfBars = bars.size();

		
		// The amount of stacked bars should be equal than the amount of legends for the chart
		Assert.assertEquals(totalAmountOfVendorsInLegends, totalAmountOfBars);
		
		
		for(int i = 0; i < legends.size(); i++){
			
			// Disable the legend.. 
			legends.get(i).click();
			
			// ..verify that the bar is removed from chart, i.e.: visibility = hidden 
			Assert.assertEquals(bars.get(i).getAttribute("visibility"), "hidden");
			
			// ..verify that the legend is grayed out
			Assert.assertTrue(legends.get(i).getAttribute("style").startsWith(UsageHelper.colorLegendDisabled));
		
			
			// Enable the legend.. 
			legends.get(i).click();
			
			// ..verify that the bar is added back to chart, i.e.: there's no 'visibility' attribute 
			Assert.assertNull(bars.get(i).getAttribute("visibility"));
			
			// ..verify that the legend is not grayed out anymore
			Assert.assertTrue(legends.get(i).getAttribute("style").startsWith(UsageHelper.colorLegendEnabled));
			
		}
		
	}
	
	

	// Verifies the content of the tooltips displayed on charts under Usage Trending Domestic and Roaming charts
	// It does not verify the amounts... SEE following method
	public static void verifyUsageTrendingChartTooltip(int barChartId) throws InterruptedException, ParseException, AWTException{
		
		String chartId = UsageHelper.getChartId(barChartId);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		//System.out.println("amount: " + amount);
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		
		Thread.sleep(1000);
		
		// Verify the info contained on each of the tooltips for the 13 months 		
		while(indexHighchart <= monthYearList.size()){
			
			//String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-" + (amount-1) + ">rect:nth-of-type(" + indexHighchart + ")";
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			String cssLine = "#" + chartId + ">svg>g>path:nth-of-type(2)";
			
			// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));
			WebElement line = driver.findElement(By.cssSelector(cssLine));
			
			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// Get the location of the second line of the chart -> to get the "y" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			Point barCoordinates = bar.getLocation();
			Point lineCoordinates = line.getLocation();
			
			Robot robot = new Robot(); 
			robot.mouseMove((barCoordinates.getX() + 5), lineCoordinates.getY());
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
			
			int expectedAmountItemsTooltip = (amount * 3) + 1;
			
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// MM-YYYY -- month and year appears once
			// ? -- this is for the bullet
			// <vendor's name>
			// <amount shown for the vendor> 
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			//System.out.println("Index month : " + indexMonth + "  --Month-year from tooltip: " + tooltip.get(0).getText());
			
			// Verify month and year shown on the tooltip
			Assert.assertEquals(tooltip.get(0).getText(), monthYearList.get(indexMonth));
			
			// Verify the vendor's name and the amount shown on the tooltip
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
								
				// Verify the vendor's name on tooltip
				// Remove colon at the end of legend's name 
				String vendorNameFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
				
				Assert.assertEquals(vendorNameFound, legends.get(i - 1).getText()); 
				//System.out.println("Tooltip text: " + vendorNameFound);
				
				// Verify the amount shown 
				// TBD
				

			}
			
			indexHighchart++;
			indexMonth--;
			
		}
		
	}



	// Verifies the content of the tooltips displayed on charts under Usage Trending Domestic and Roaming charts
	// **** ONLY FOR ONE VENDOR SELECTED ****
	public static void verifyUsageTrendingChartTooltipOneVendor(int barChartId, List<UsageOneMonth> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
		String chartId = UsageHelper.getChartId(barChartId);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
			
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		// System.out.println("amount: " + amount);
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		Thread.sleep(2000);
		
		List<String> domesticValue = new ArrayList<>();
		List<String> roamingValue = new ArrayList<>();
		

		if (barChartId == UsageHelper.usageTrendingDomesticChart) {
		
			if(categorySelector == UsageHelper.categoryVoice){
				
				for(UsageOneMonth usage: allValuesFromFile){
					domesticValue.add(UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usage.getDomesticVoice()) + Double.parseDouble(usage.getDomesticOverageVoice()), false));
				}

			} else if (categorySelector == UsageHelper.categoryData){
				
				for(UsageOneMonth usage: allValuesFromFile){
					domesticValue.add(UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usage.getDomesticDataUsageKb())));
				}
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				for(UsageOneMonth usage: allValuesFromFile){
					domesticValue.add(UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usage.getDomesticMessages()), false));
				}
				
				
			}
			
		} else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
			
			if(categorySelector == UsageHelper.categoryVoice){
				
				for(UsageOneMonth usage: allValuesFromFile){
					roamingValue.add(UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usage.getRoamingVoice()), false));
				}
				
			} else if (categorySelector == UsageHelper.categoryData){
				
				for(UsageOneMonth usage: allValuesFromFile){
					roamingValue.add(UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usage.getRoamingDataUsageKb())));
				}
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				for(UsageOneMonth usage: allValuesFromFile){
					roamingValue.add(UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usage.getRoamingMessages()), false));
				}
				
			}
			
		}
		
		
		// Verify the info contained on each of the tooltips for the 13 months 		
		while(indexHighchart <= monthYearList.size()){
			
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(1)";
			
			// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));
			WebElement line = driver.findElement(By.cssSelector(cssLine));
			
			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// Get the location of the second line of the chart -> to get the "y" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			
			Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);  // bar.getLocation();
			Point lineCoordinates = GeneralHelper.getAbsoluteLocation(line); // line.getLocation();   
			
			Robot robot = new Robot(); 
			
			int x = barCoordinates.getX();
			int y = lineCoordinates.getY();
			
			
			if (loginType.equals(LoginType.Command)) {
				
				if (barChartId == UsageHelper.usageTrendingDomesticChart) {
				
					y = y - 100;  // these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
					System.out.println("domestic chart, y = " + y);
				}  else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
					
					y = y - 400;  // these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
					System.out.println("roaming chart, y = " + y);
				}
				
			} else if (loginType.equals(LoginType.ReferenceApp)) {
				
				// nothing needs to be modified as of now - Dash v.1.1.13 - March 1st
				
			}
			
			robot.mouseMove(x, y);
//			System.out.println("coordinates - x: " + x + "  y: " + y);
			
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
			
			int expectedAmountItemsTooltip = (amount * 3) + 1;
			
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name>
			// 3 <amount shown for the vendor>
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// Verify the vendor's name and the amount shown on the tooltip
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
				
				// Get the label and remove colon at the end of its text
				//System.out.println("tooltip.get(index).getText(): " + tooltip.get(index).getText());
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: 15 256 985. Value needed is: 15256985
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
						
				// Verify the labels' text and amounts shown on the tooltip
				String labelExpected = allValuesFromFile.get(indexMonth).getVendorName();
				Assert.assertEquals(labelFound, labelExpected); 
				
				String valueExpected = "";
				
				if (barChartId == UsageHelper.usageTrendingDomesticChart) {
					
					valueExpected = domesticValue.get(indexMonth);
										
				} else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
					
					valueExpected = roamingValue.get(indexMonth);
										
				}
				
				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
				System.out.println("labelFound: " + labelFound + ", labelExpected: " + labelExpected);
				System.out.println("valueFound: " + valueFound + ", valueExpected: " + valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			Assert.assertEquals(monthYearFound, monthYearExpected); 
			System.out.println("Month/Year Found: " + monthYearFound + ", Month/Year Expected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}

	}
	

	
	
	// Verifies the content of the tooltips displayed on charts under Usage Trending Domestic and Roaming charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyUsageTrendingChartTooltip(int barChartId, List<List<UsageOneMonth>> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
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
		
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		// The HashMap "expectedValues" will have one entry per vendor listed in chart. The key is the vendor's name. 		
		List<HashMap<String, String>> expectedValues = new ArrayList<>();
		
		List<String> expectedLabels = new ArrayList<>(); // it may be removed -- see if legends work as expected label
		
		HashMap<String, Boolean> vendorHasData = GeneralHelper.vendorHasDataForSelectedMonth(allValuesFromFile);
		
				
		for(int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++){
			
			HashMap<String, String> map = new HashMap<>();
			
			for (int j = 0; j < allValuesFromFile.get(indexMonthValues).size(); j++) {
			
				String keyVendor = allValuesFromFile.get(indexMonthValues).get(j).getVendorName();
				
				UsageOneMonth usageOneMonth = allValuesFromFile.get(indexMonthValues).get(j);
				
				if (vendorHasData.get(keyVendor)) {
	
					if (barChartId == UsageHelper.usageTrendingDomesticChart) {
					
						if(categorySelector == UsageHelper.categoryVoice){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDomesticVoice()) 
									+ Double.parseDouble(usageOneMonth.getDomesticOverageVoice()), false));

							
						} else if (categorySelector == UsageHelper.categoryData){
							
							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usageOneMonth.getDomesticDataUsageKb())));
															
						} else if (categorySelector == UsageHelper.categoryMessages){
							
							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getDomesticMessages()), false));
							
						}
						
						
					} else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
						
						if(categorySelector == UsageHelper.categoryVoice){

							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingVoice()), false));
							
						} else if (categorySelector == UsageHelper.categoryData){
							
							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(usageOneMonth.getRoamingDataUsageKb())));
															
						} else if (categorySelector == UsageHelper.categoryMessages){
							
							map.put(usageOneMonth.getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(usageOneMonth.getRoamingMessages()), false));
							
						}
						
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
		
		
		
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String otherVendors = "Other";
		
		// If more than 5 vendors are selected, and there are 6 vendors in chart, then there's an "Other" element.
		// The value for "Other" needs to be calculated.
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
						
			for (int indexMonthValues = 0; indexMonthValues < listUsageAllMonths.size(); indexMonthValues++) {

				double otherTmpSum = 0;
				String valueForOther = "";
								
				for (int i = 0; i < vendorsSelectedCheckBox.size(); i++){
					
					String v = vendorsSelectedCheckBox.get(i).getText();
					
					// ************ SEE NOTE NEXT TO NEXT LINE - CHANGE TO BE MADE **************************
					if (!vendorsInChartNames.contains(v) && vendorHasData.get(v)){ // <-- When Ed's fix is included on Dashboard, remove the "vendorHasData.get(v)" condition 
					
						UsageOneMonth usage = (UsageOneMonth) listUsageAllMonths.get(indexMonthValues).get(v);
							
						if (barChartId == UsageHelper.usageTrendingDomesticChart) {
							
							if(categorySelector == UsageHelper.categoryVoice){
								
								otherTmpSum += (Double.parseDouble(usage.getDomesticVoice()) + Double.parseDouble(usage.getDomesticOverageVoice()));
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							} else if (categorySelector == UsageHelper.categoryData){
								
								otherTmpSum += Double.parseDouble(usage.getDomesticDataUsageKb());
								valueForOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(otherTmpSum);
								
							} else if (categorySelector == UsageHelper.categoryMessages){
								
								otherTmpSum += Double.parseDouble(usage.getDomesticMessages());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							}
							
							
						} else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
							
							if(categorySelector == UsageHelper.categoryVoice){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingVoice());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							} else if (categorySelector == UsageHelper.categoryData){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingDataUsageKb());
								valueForOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(otherTmpSum);
								
							} else if (categorySelector == UsageHelper.categoryMessages){
								
								otherTmpSum += Double.parseDouble(usage.getRoamingMessages());
								valueForOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, false);
								
							}

						}
						
						expectedValues.get(indexMonthValues).put(otherVendors, valueForOther); 
														
					}
					
				}
				
			}
			
			expectedLabels.add(otherVendors);
			
		}
		
	
			

		// *******************************************************************
		// Verify the info contained on each of the tooltips for the 13 months 		
		// *******************************************************************
		
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
		int amountOfSeries = highchartSeries.size();
		int indexHighchart = 1;
		
		while (indexHighchart <= monthYearList.size()) {
			
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(1)";
			
			// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));
			WebElement line = driver.findElement(By.cssSelector(cssLine));
			
			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// Get the location of the second line of the chart -> to get the "y" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);  // bar.getLocation();
			Point lineCoordinates = GeneralHelper.getAbsoluteLocation(line); // line.getLocation();   
			
			Robot robot = new Robot(); 
			
			int x = barCoordinates.getX();
			int y = lineCoordinates.getY();
			
			
			if (loginType.equals(LoginType.Command)) {
				
				if (barChartId == UsageHelper.usageTrendingDomesticChart) {
					
					y = y - 100;  // these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
					
				}  else if (barChartId == UsageHelper.usageTrendingRoamingChart) {
					
					y = y - 400;  // not yet --> these coordinates work on CMD :) - Dash v.1.1.13 - March 1st
					
				}
				
			} else if (loginType.equals(LoginType.ReferenceApp)) {
				
				// nothing needs to be modified as of now - Dash v.1.1.13 - March 1st
				
			}
			
			robot.mouseMove(x, y);
//			System.out.println("coordinates - x: " + x + "  y: " + y);
			
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
			
			int expectedAmountItemsTooltip = (amountOfSeries * 3) + 1;
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the its and the amount shown
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: 15 256 985. Value needed is: 15256985
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
							
				// Verify the labels' text and amounts shown on the tooltip
//				String labelExpected = legends.get(i-1);   //expectedLabels.get(i-1);
//				Assert.assertEquals(labelFound, labelExpected); 
				
				String valueExpected = expectedValues.get(indexMonth).get(labelFound);  //labelExpected);
				
				System.out.println("Vendor: " + labelFound);
				System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
				
				GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			Assert.assertEquals(monthYearFound, monthYearExpected); 
//			System.out.println("Month/Year Found: " + monthYearFound + ", Month/Year Expected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}

	}

	
	
		
	
}

