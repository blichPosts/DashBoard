package usage;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;

public class TotalUsage extends BaseClass{

	
	public static void verifyMonthYearOnUsageView() throws InterruptedException{
		
		// Get the month and year from Month selector
		String monthYearSelector = new Select(driver.findElement(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"))).getFirstSelectedOption().getText();
					
		// Get the month and year from Vendor View
		String monthYearVendorView = driver.findElements(By.cssSelector(".tdb-h2")).get(1).getText();
		
		ShowText("month year:" + monthYearVendorView);
		Thread.sleep(2000);	
		
		Assert.assertEquals(monthYearSelector, monthYearVendorView, "Month and Year displayed for Total Usage by Vendor is not the same as the selection made under Month selector.");		
							
	}

	
	
	public static void verifyTotalUsageTitle(String titleFirstPart) throws InterruptedException{
		
		String categoryLabel;
		String totalUsageTitleFound;
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));

		
		for (int i = 0; i < 3; i++) {
			
			categorySelectors.get(i).click();
			totalUsageTitleFound = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
			categoryLabel = categorySelectors.get(i).getText();
			
			System.out.println("Title found:    " + totalUsageTitleFound);
			//System.out.println("Title expected: " + totalUsageTitleExpected);
			
			Assert.assertTrue(totalUsageTitleFound.startsWith(titleFirstPart), "Title found is different from title expected.");
			Assert.assertTrue(totalUsageTitleFound.endsWith(categoryLabel), "Title found is different from title expected.");
			Thread.sleep(2000);	
			
		}
		
	}
	
	
			
	public static void verifyBarChartTitlesTotalUsage() throws InterruptedException{
		
		
		String domesticTitleFound; 
		String roamingTitleFound;
		
		List<WebElement> barChartTitles;
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));
		
		for (int i = 0; i < 3; i++) {
			
			categorySelectors.get(i).click();
			Thread.sleep(2000);
			
			barChartTitles = driver.findElements(By.cssSelector(".tdb-charts__label.tdb-text--italic.tdb-text--bold"));
			domesticTitleFound = barChartTitles.get(0).getText(); 
			roamingTitleFound = barChartTitles.get(1).getText();
			
			if (i == 0){
				Assert.assertEquals(domesticTitleFound, UsageHelper.domesticTitleVoice);
				Assert.assertEquals(roamingTitleFound, UsageHelper.roamingTitleVoice);
				System.out.println("Voice...");
				
			} else if (i == 1){
				
				Assert.assertTrue(domesticTitleFound.equals(UsageHelper.domesticTitleDataGB));
				Assert.assertTrue(roamingTitleFound.equals(UsageHelper.roamingTitleDataGB));
				
				System.out.println("Data...");
								
			} else if (i == 2){
			
				Assert.assertEquals(domesticTitleFound, UsageHelper.domesticTitleMessages);
				Assert.assertEquals(roamingTitleFound, UsageHelper.roamingTitleMessages);
				System.out.println("Messages...");
			
			}
			
		}
		
		Thread.sleep(2000);			
		
	}

	
	public static void verifyLegendsOfCharts() throws InterruptedException {
		
		
		String chartIdDom = UsageHelper.getChartId(UsageHelper.totalUsageDomesticChart);
		String chartIdRoam = UsageHelper.getChartId(UsageHelper.totalUsageRoamingChart);
		
		for (int i = 1; i <= 3; i++) {
		
			String domesticLegendFound = ""; 
			String roamingLegendFound = "";
			String domesticOverageLegendFound = "";
			
			// Select category, either Voice, Data or Messages
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, i);
			Thread.sleep(2000);
			
			// Get the Domestic legend
			domesticLegendFound = driver.findElement(By.cssSelector("#" + chartIdDom + ">svg>g.highcharts-legend>g>g>g.highcharts-legend-item:nth-of-type(1)>text")).getText();
			Assert.assertEquals(domesticLegendFound, UsageHelper.domesticLegend);
			
			// Get the Roaming legend
			roamingLegendFound = driver.findElement(By.cssSelector("#" + chartIdRoam + ">svg>g.highcharts-legend>g>g>g.highcharts-legend-item>text")).getText();
			Assert.assertEquals(roamingLegendFound, UsageHelper.roamingLegend);
			
			
			if (i == 1) {
				
				// Get the Domestic Overage legend
				domesticOverageLegendFound = driver.findElement(By.cssSelector("#" + chartIdDom + ">svg>g.highcharts-legend>g>g>g.highcharts-legend-item:nth-of-type(2)>text")).getText();
				Assert.assertEquals(domesticOverageLegendFound, UsageHelper.domesticOverageLegend);
				
			}
			
			ShowText("Legends: " + domesticLegendFound + ", " + domesticOverageLegendFound + ", " + roamingLegendFound);
			
		}
		
		Thread.sleep(2000);
		
	}


		
	public static void verifyVendorsInUsageByVendorCharts() {
		
		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = CommonTestStepActions.getVendorsSelectedInPointOfView();
		String chartId;
		
		// Get the id of the "Total Usage by Vendor (DOMESTIC)" chart (FIRST chart)
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageDomesticChart);
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listVendorsChecked);
				
		// Get the id of the "Total Usage by Vendor (ROAMING)" chart (SECOND chart)
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageRoamingChart);
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listVendorsChecked);
					
	}

	

	public static void verifyCountriesInUsageByCountryCharts() {
	
		// this list will have the names of the countries that have been SELECTED on the Point of View section
		List<String> listCountriesChecked = CommonTestStepActions.getCountriesSelectedInPointOfView();
		String chartId;
				
		// Get the id of the "Total Usage by Vendor (DOMESTIC)" chart (FIRST chart)
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageDomesticChart);
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listCountriesChecked);
				
		// Get the id of the "Total Usage by Vendor (ROAMING)" chart (SECOND chart)
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageRoamingChart);
		verifyLabelsInVerticalAxisTotalUsageCharts(chartId, listCountriesChecked);
		
	}
	
	

	// This method is used to verify the labels on the vertical axis of both, Total Usage by Vendor charts, and Total Usage by Country charts.
	public static void verifyLabelsInVerticalAxisTotalUsageCharts(String chartId, List<String> listItemsSelected){
		
		// Get the vendors that are listed on the vertical axis of the "Total Usage by Vendor (Domestic)" chart
		List<WebElement> labelsVerticalAxis = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		System.out.println("# Vendors in chart: " + labelsVerticalAxis.size());
		
		int amountItemsSelected = listItemsSelected.size();
		
		System.out.println("# Vendors selected in PoV section: " + amountItemsSelected);
		
		// The amount of vendors listed on the chart cannot be more than 6 including 'Other'
		Assert.assertTrue(labelsVerticalAxis.size() <= 6);
		
		// The amount of vendors listed on the chart cannot be more than the amount of vendors selected
		Assert.assertTrue(labelsVerticalAxis.size() <= amountItemsSelected);
		
		
		// Verify that the labels in the vertical axis correspond to vendors that have been selected on PoV section 
		for (WebElement e: labelsVerticalAxis) {
			
			String label = e.getText();
			ShowText("label: " + label);
			
			if (!label.equals("Other")) {
			
				assertTrue(listItemsSelected.contains(label));
				
			}
			
		}
		
	}
	
	
}
