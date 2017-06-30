package usage;

import org.testng.Assert;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.UsageHelper;


public class TotalUsageActions extends BaseClass{

	
	// Verifies that if a vendor is selected on the PoV section, and the value to be shown is greater than zero, the vendor name is listed on the x axis on the chart,
	// unless there are already 5 vendors listed. In this case the value for the selected vendor may be included under "Other"  
	public static void vendorsAddedToCharts(int chartNum, int kpiNum) throws InterruptedException {
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorsInPoV(); 
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
	public static void verifyTotalUsageChartTooltip(int barChartId, int category) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartList = new ArrayList<String>();
		
		for(int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartList.add(vendorsInChart.get(i).getText());
		}	

		
		int indexHighchart = 1;
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while(indexHighchart <= vendorsInChartList.size()){
			
			GeneralHelper.moveMouseToBar(chartId, indexHighchart, barChartId, category);			
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			int expectedAmountItemsTooltip = 4;
			
			if (barChartId == UsageHelper.totalUsageDomesticChart && category == UsageHelper.categoryVoice) 
				expectedAmountItemsTooltip = 6;
						
			
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
			ShowText("Vendor: " + tooltip.get(0).getText());
			Assert.assertEquals(tooltip.get(0).getText(), vendorsInChartList.get(indexHighchart-1));
			
			String categorySelected = UsageHelper.getNameCategorySelected(category);
			
			ShowText("Category: " + tooltip.get(1).getText());
			Assert.assertTrue(tooltip.get(1).getText().startsWith(categorySelected));	
			
			// It gets the legends for "Domestic" and "Domestic Overage" or Roaming
			List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
			
			
			// Verify the vendor's name and the amount shown on the tooltip
			for (int i = 1; i <= legends.size(); i++) {
			
				int index = i * 2 + 1;
				
				// Get the label and remove colon at the end of its text 
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();

				Assert.assertEquals(labelFound, legends.get(i - 1).getText()); 
				ShowText("Label: " + labelFound);

			}
			
			indexHighchart++;
			
		}
		
	}



	// *** CONTINUE HERE **** 
	
	// Verify that only one category is selected
	public static void verifySelectedCategories(int chartNum, int categorySelected) {
		
		String categoryName = UsageHelper.getNameCategorySelected(categorySelected);
		
		for (int i = 1; i <= 3; i++) {
			
			WebElement selector = driver.findElement(By.xpath("//h2[not(contains(text(), 'Usage Trending'))]/following-sibling::div[1]/div[" + i + "]"));
			
			ShowText("Selector-attribute class: " + selector.getAttribute("class"));
			
			if (selector.getText().equals(categoryName)) {
				
				ShowText("Selector equals category selected - " + categoryName);
				Assert.assertTrue(selector.getAttribute("class").endsWith("tdb-boxSelector__option--selected"));
				
			} else {
				
				ShowText("Selector does NOT equal category selected - " + categoryName);
				Assert.assertTrue(!selector.getAttribute("class").endsWith("tdb-boxSelector__option--selected"));
				
			}
			
		}
		
		
	}
	
	
		
	
}
