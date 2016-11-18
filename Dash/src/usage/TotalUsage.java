package usage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;

public class TotalUsage extends BaseClass{

	
	public static void verifyMonthYearOnUsageView(){
		
		// Get the month and year from Month selector
		String monthYearSelector = new Select(driver.findElement(By.cssSelector(".tbd-flexContainer.tdb-flexContainer--center>select"))).getFirstSelectedOption().getText();
		//System.out.println("monthYearSelector:" + monthYearSelector);
					
		// Get the month and year from Vendor View
		String monthYearVendorView = driver.findElements(By.cssSelector(".tdb-h2")).get(0).getText();
		System.out.println("monthYearVendorView:" + monthYearVendorView);
					
		Assert.assertEquals(monthYearSelector, monthYearVendorView, "Month and Year displayed for Total Usage by Vendor is not the same as the selection made under Month selector.");		
							
	}

	
	
	public static void verifyTotalUsageTitle(String titleFirstPart){
		
		String totalUsageTitleExpected;
		String categoryLabel;
		String totalUsageTitleFound;
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));

		
		for(int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click();
			totalUsageTitleFound = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
			categoryLabel = categorySelectors.get(i).getText();
			totalUsageTitleExpected = titleFirstPart + categoryLabel;
			
			//System.out.println("Title found:    " + totalUsageTitleFound);
			//System.out.println("Title expected: " + totalUsageTitleExpected);
			
			Assert.assertEquals(totalUsageTitleExpected, totalUsageTitleFound, "Title found is different from title expected.");
			
		}

		
	}
	
	
			
	public static void verifyBarChartTitlesTotalUsage(){
		
		
		String domesticTitleFound; 
		String roamingTitleFound;
		
		List<WebElement> barChartTitles;
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));

		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			barChartTitles = driver.findElements(By.cssSelector(".tdb-charts__label.tdb-text--italic.tdb-text--bold"));
			domesticTitleFound = barChartTitles.get(0).getText(); 
			roamingTitleFound = barChartTitles.get(1).getText();
			
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

	
	public static void verifyLegendsOfCharts() {
		
		String domesticLegendFound; 
		String roamingLegendFound;
		String domesticOverageLegendFound;
		
		List<WebElement> legends;
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));
		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			legends = driver.findElements(By.xpath(".//*[@class='highcharts-legend-item']"));
			// System.out.println("legends size: " + domesticLegends.size());
							
			domesticLegendFound = legends.get(0).getText(); 
			Assert.assertEquals(domesticLegendFound, UsageHelper.domesticLegend);
			
			if (i == 0){
				
				domesticOverageLegendFound = legends.get(1).getText();
				Assert.assertEquals(domesticOverageLegendFound, UsageHelper.domesticOverageLegend);
			
				roamingLegendFound = legends.get(2).getText();
				Assert.assertEquals(roamingLegendFound, UsageHelper.roamingLegend);
				
				System.out.println("Voice Legends:..." + legends.get(0).getText() + ", " + legends.get(1).getText() + ", " + legends.get(2).getText());
				
			} 
			
			if (i == 1){
			
				roamingLegendFound = legends.get(1).getText();
				Assert.assertEquals(roamingLegendFound, UsageHelper.roamingLegend);
				
				System.out.println("Data Legend..." + legends.get(0).getText() + ", " + legends.get(1).getText());
				
			}
				
			if (i == 2){
				
				roamingLegendFound = legends.get(1).getText();
				Assert.assertEquals(roamingLegendFound, UsageHelper.roamingLegend);
				
				System.out.println("Messages Legend..." + legends.get(0).getText() + ", " + legends.get(1).getText());
				
			}
				
			
		}
		
		
	}


		
	public static void verifyVendorsInUsageByVendorCharts() {
		
		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = CommonTestStepActions.getVendorsSelectedInPointOfView();
		String chartId ;
		
		// Get the charts to get the id of the chart - I hope this works! -- it worked :) 
		//List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of the "Total Usage by Vendor (DOMESTIC)" chart (FIRST chart)
		chartId = UsageHelper.getChartId(0);  //charts.get(0).getAttribute("id");
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listVendorsChecked);
				
		// Get the id of the "Total Usage by Vendor (ROAMING)" chart (SECOND chart)
		chartId = UsageHelper.getChartId(1);  //charts.get(1).getAttribute("id");
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listVendorsChecked);
				
		
		
	}

	

	public static void verifyCountriesInUsageByCountryCharts() {
	
		// this list will have the names of the countries that have been SELECTED on the Point of View section
		List<String> listCountriesChecked = CommonTestStepActions.getCountriesSelectedInPointOfView();
		String chartId;
		
		// Get the charts to get the id of the chart - I hope this works! -- it worked :) 
		//List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of the "Total Usage by Vendor (DOMESTIC)" chart (FIRST chart)
		chartId = UsageHelper.getChartId(0);  //charts.get(0).getAttribute("id");
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listCountriesChecked);
				
		// Get the id of the "Total Usage by Vendor (ROAMING)" chart (SECOND chart)
		chartId = UsageHelper.getChartId(1);  //charts.get(1).getAttribute("id");
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listCountriesChecked);
		
	}
	
	

	// This method is used to verify the labels on the vertical axis of both, Total Usage by Vendor charts, and Total Usage by Country charts.
	public static void verifyLabelsInVerticalAxisTotalUsageCharts(String chartId, List<String> listItemsSelected){
		
		// Get the vendors that are listed on the vertical axis of the "Total Usage by Vendor (Domestic)" chart
		List<WebElement> labelsVerticalAxis = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		System.out.println("# Vendors: " + labelsVerticalAxis.size());
		System.out.println("Chart: " + chartId);

		int amountItemsSelected = listItemsSelected.size();
		
		// If amount of vendors/countries selected is > than 5, then "Other" must be listed along other 5 vendors/countries.
		// Else the amount of vendors/countries listed in the chart must be equal to the amount of vendors/countries selected
		// and the vendors/countries in the vertical axis must be in the list of selected vendors/countries.
		if(amountItemsSelected > 5){
			
			Assert.assertEquals(labelsVerticalAxis.size(), 6);
			
			for(WebElement label: labelsVerticalAxis){
						
				if(!label.getText().equals("Other"))
					Assert.assertTrue(listItemsSelected.contains(label.getText()));
				
				System.out.println("Item in legend: " + label.getText());
				
			}
			
		}else{
			
			Assert.assertEquals(labelsVerticalAxis.size(), amountItemsSelected);
			
		}
		
	}
	
	
}
