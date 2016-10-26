package usage;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
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
			
			//System.out.println("Title found:    " + totalUsageTitleFound);
			//System.out.println("Title expected: " + totalUsageTitleExpected);
			
			Assert.assertEquals(totalUsageTitleExpected, totalUsageTitleFound, "Title found is different from title expected.");
			
		}

	}
		
	
	
	
	public static void verifyBarChartTitlesUsageTrending(){
		
		
		String domesticTitleFound; 
		String roamingTitleFound;
	
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));

		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			domesticTitleFound = driver.findElement(By.cssSelector(".tdb-charts__label.tdb-text--italic>.tdb-text--bold")).getText();   //barChartTitles.get(0).getText(); 
			roamingTitleFound = driver.findElement(By.cssSelector(".tdb-flexUnit--1.tdb-align--center>.tdb-charts__label.tdb-text--italic.tdb-text--bold")).getText();    //barChartTitles.get(1).getText();
			
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

	
	
	public static void verifyLegendsOfChartsUsageTrending() {
		
		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = CommonTestStepActions.getVendorsSelectedInPointOfView();
		
		// Get the charts to get the id of the chart - I hope this works! -- it worked :) 
		List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of the "Usage Trending Domestic" chart (THIRD chart)
		String chartId = charts.get(2).getAttribute("id");
		verifyLegendsInUsageTrendingCharts(chartId, listVendorsChecked);
				
		// Get the id of the "Usage Trending Roaming" chart (FOURTH chart)
		chartId = charts.get(3).getAttribute("id");
		verifyLegendsInUsageTrendingCharts(chartId, listVendorsChecked);
		
	}



	private static void verifyLegendsInUsageTrendingCharts(String chartId, List<String> listVendorsChecked) {
		
		// Get the vendors that are listed on the vertical axis of the "Total Usage by Vendor (Domestic)" chart
		List<WebElement> vendorsInLegend = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));  // if it doesnt find the text add >tspan at the end
		
		System.out.println("# Vendors: " + vendorsInLegend.size());
		System.out.println("Chart: " + chartId);

		int amountItemsSelected = listVendorsChecked.size();
		
		// If amount of vendors/countries selected is > than 5, then "Other" must be listed along other 5 vendors/countries.
		// Else the amount of vendors/countries listed in the chart's legend must be equal to the amount of vendors/countries selected
		// and the vendors/countries in the legend must be in the list of selected vendors/countries.
		if(amountItemsSelected > 5){
			
			Assert.assertEquals(vendorsInLegend.size(), 6);
			
			for(WebElement label: vendorsInLegend){
						
				if(!label.getText().equals("Other"))
					Assert.assertTrue(listVendorsChecked.contains(label.getText()));
				
				System.out.println("Item in legend: " + label.getText());
				
			}
			
		}else{
			
			Assert.assertEquals(vendorsInLegend.size(), amountItemsSelected);
			
		}
		
	}


	
	public static void verifyMonthYearInUsageTrendingCharts() throws ParseException{
		
		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listMonths = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		// Get the charts to get the id of the chart - I hope this works! -- it worked :) 
		List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of the "Usage Trending Domestic" chart (THIRD chart)
		String chartId = charts.get(2).getAttribute("id");
		verifyMonthYearHorizontalAxisUsageTrendingCharts(chartId, listMonths);
				
		// Get the id of the "Usage Trending Roaming" chart (FOURTH chart)
		chartId = charts.get(3).getAttribute("id");
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
					
			Assert.assertTrue(listMonths.contains(label.getText()));
			
			System.out.println("Item in legend: " + label.getText());
			
		}
		
	}
	
	
	
}
