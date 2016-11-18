package usage;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;


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
			
			System.out.println("Title found:    " + totalUsageTitleFound);
			System.out.println("Title expected: " + totalUsageTitleExpected);
			
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
			roamingTitleFound = driver.findElement(By.cssSelector(".tdb-flexUnit--1.tdb-align--center>.tdb-charts__label.tdb-text--italic.tdb-text--bold")).getText();
			
			System.out.println("usage trending - Domestic title: " + domesticTitleFound);
			System.out.println("usage trending - Roaming title: " + roamingTitleFound);
			
			
			if (i == 0){
				Assert.assertEquals(domesticTitleFound, UsageHelper.domesticTitleVoice);
				Assert.assertEquals(roamingTitleFound, UsageHelper.roamingTitleVoice);
				System.out.println("Voice...");
				
			} else if (i == 1){
				
				boolean domesticTitle = domesticTitleFound.equals(UsageHelper.domesticTitleDataGB) || domesticTitleFound.equals(UsageHelper.domesticTitleDataTB);  
				Assert.assertTrue(domesticTitle);
				
				boolean roamingTitle = roamingTitleFound.equals(UsageHelper.roamingTitleDataGB) || roamingTitleFound.equals(UsageHelper.roamingTitleDataTB);  
				Assert.assertTrue(roamingTitle);
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
		System.out.println(" Chart: " + chartId);

		int amountItemsSelected = listItemsChecked.size();
		
		// If amount of vendors/countries selected is > than 5, then "Other" must be listed along other 5 vendors/countries.
		// Else the amount of vendors/countries listed in the chart's legend must be equal to the amount of vendors/countries selected
		// and the vendors/countries in the legend must be in the list of selected vendors/countries.
		if(amountItemsSelected > 5){
			
			Assert.assertEquals(itemsFoundInLegend.size(), 6);
			
			for(WebElement label: itemsFoundInLegend){
						
				if(!label.getText().equals("Other"))
					Assert.assertTrue(listItemsChecked.contains(label.getText()));
				
				System.out.println("Item in legend: " + label.getText());
				
			}
			
		}else{
			
			Assert.assertEquals(itemsFoundInLegend.size(), amountItemsSelected);
			
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




	public static void verifyBarsCanBeSwitchedOnOff(int barChartId) throws Exception {
		
		 
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
	
	

	// It works for the Usage Trending Domestic chart - It doesnt work for the Roaming chart... for now .... :| 
	// **** SEE THE ROAMING CHART *****
	public static void verifyUsageTrendingChartTooltip(int barChartId) throws AWTException, InterruptedException, ParseException{
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		System.out.println("amount of highchart series: " + amount); 
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		boolean firstBar = true;
		boolean firstMonth = true;
		
		if(firstMonth && (barChartId == 3)){
		
			firstMonth = false;
			scrollMouseToChart(5);
			System.out.println("First month of Roaming chart");
		
		}
		
		
		// Verify the info contained on each of the tooltips for the 13 months 		
		while(indexHighchart <= monthYearList.size()){
			
			System.out.println("indexHighchart: " + indexHighchart);
			
					
			// The 'bar' WebElement will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-" + (amount-1) + ">rect:nth-of-type(" + indexHighchart + ")"));

			// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
			Point coordinates = bar.getLocation();
			Robot robot = new Robot(); 
			robot.mouseMove(coordinates.getX() + 5, coordinates.getY());
			System.out.println("x: " + coordinates.getX() + ", y: " + coordinates.getY());
			
			if(barChartId == 3){
				robot.mouseMove(coordinates.getX(), coordinates.getY() - 750);
			}
			
			if(firstBar){
				bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
				firstBar = false;
			}
				
			
			
						
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
				System.out.println("Tooltip present");
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
	
	
	
	public static void scrollMouseToChart(int num) throws AWTException{
		
		Robot robot = new Robot(); 
		robot.mouseWheel(num);
		
	}
	
	
}

