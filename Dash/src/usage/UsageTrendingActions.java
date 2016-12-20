package usage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;

public class UsageTrendingActions extends BaseClass{

	
	// Verifies that if a vendor is selected on the PoV section, and the value to be shown is greater than zero, the vendor name is listed on the legend's chart,
	// unless there are already 5 vendors listed. In this case the value for the selected vendor may be included under "Other"  
	public static void vendorsAddedToCharts(int chartNum, int kpiNum) throws InterruptedException {
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames(); 
		List<WebElement> kpiTiles = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		String currentKPIvalue = "";
		String previousKPIvalue = "";
		
		//WebElement usageTrendingSection = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)"));
		//new Actions(driver).moveToElement(usageTrendingSection).perform();
		
		String chartId = UsageHelper.getChartId(chartNum);
		UsageHelper.moveToChart(chartId);
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));
		
		categorySelectors.get(kpiNum).click();
		
		int count = 1;
		
		for (WebElement v : vendors){
		
			String vendorName = v.getText();
			
			// Select one vendor
			CommonTestStepActions.selectOneVendor(vendorName);
			//System.out.println("Vendor selected: " + vendorName);
			
			// Wait 2 seconds to the values to get updated on Dashboard, after the vendor selection
			Thread.sleep(2000);
			
			currentKPIvalue = kpiTiles.get(kpiNum).getText(); 
			//System.out.println("Current KPI tile value: " + currentKPIvalue);
			
			List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
			
			
			// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
			if(count == 1){
				
				//System.out.println("First Vendor..");
				
				// Verify that vendor is listed on chart's legend if value is NOT zero 
				if(!currentKPIvalue.equals("0")){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: legends){
						System.out.println("Label in legend: " + label.getText());
						
						if(label.getText().equals(vendorName)){
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
				}
				
				
			} else if (count > 1 && count < 6) { // From second vendor. If KPI value doesn't change, it means value for the last selected vendor is zero and it won't be listed on the chart.
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: legends){
						System.out.println("Label in legend: " + label.getText());
						if(label.getText().equals(vendorName)){
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
					
				}
				
			} else if (count >= 6) {
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					boolean vendorInChart = false; 
					
					for (WebElement label: legends){
						//System.out.println("Label in legend: " + label.getText());
						
						if(label.getText().equals(vendorName) || label.getText().equals("Other")){
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


	
	// Verifies that if a country is selected on the PoV section, and the value to be shown is greater than zero, the country name is listed on the legend's chart,
	// unless there are already 5 countries listed. In this case the value for the selected country may be included under "Other"  
	public static void countriesAddedToCharts(int chartNum, int kpiNum) throws InterruptedException {

		List<WebElement> kpiTiles = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		List<List<WebElement>> countriesAndVendors = UsageHelper.getCountriesAndVendors();
		
		String currentKPIvalue = "";
		String previousKPIvalue = "";
		
		String chartId = UsageHelper.getChartId(chartNum);
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));
		
		categorySelectors.get(kpiNum).click();
		
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
				
				currentKPIvalue = kpiTiles.get(kpiNum).getText(); 
				//System.out.println("Current KPI tile value: " + currentKPIvalue);
				
				List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
				
				/*System.out.println("amount of legends: " + legends.size());
				for(WebElement leg: legends){
					System.out.println(leg.getText());
				}*/
				
				// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
				if(vendorCount == 1 && countryCount == 1){
					
					// Verify that vendor is listed on chart if value is NOT zero 
					if(!currentKPIvalue.equals("0")){
						
						boolean countryInChart = false; 
						
						for (WebElement label: legends){
							System.out.println("Label in legend: " + label.getText());
							
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
						
						for (WebElement label: legends){
							System.out.println("Label in legend: " + label.getText());
							
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
						
						for (WebElement label: legends){
							System.out.println("Label in legend: " + label.getText());
							
							if(label.getText().equals(countryName) || label.getText().equals("Other")){
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

		
	
	// Verify that if a vendor is unselected from the Point of View section, then it's removed from the Usage Trending chart 
	public static void vendorsUnselectedRemovedFromChart(int chartNum) throws InterruptedException{
	
		String chartId = UsageHelper.getChartId(chartNum);
		UsageHelper.moveToChart(chartId);
		
		// Just to make sure all the vendors are selected
		CommonTestStepActions.SelectAllVendors();
		
		// All the vendors listed on the Point of View section
		List<WebElement> vendors = driver.findElements(By.cssSelector(".md-checkbox-label"));
		
		// Checkboxes on the Point of View section
		List<WebElement> checkboxes = driver.findElements(By.cssSelector(".md-checkbox-inner-container"));
		
		// Vendors listed on Usage Trending legends 
		List<WebElement> legendsOriginal = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
		List<String> legendStrings = new ArrayList<String>();
		
		for(WebElement legend: legendsOriginal){
			legendStrings.add(legend.getText());
		}
		
		
		for(int i = 0; i < vendors.size(); i++){
			
			// Unselect vendor from Point of View section
			checkboxes.get(i).click();
			
			// Get name of unselected vendor
			String vendorUnselected = vendors.get(i).getText();
			
			System.out.println("Vendor unselected: " + vendorUnselected);
			
			// If the vendor unselected was included in the legends of the Usage Trending chart
			if(legendStrings.contains(vendorUnselected)){
				
				Thread.sleep(2000);
				
				// Get the vendors listed on legends after the vendor was unselected
				List<WebElement> currentLegends = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
			
				for(WebElement le: currentLegends){
					
					String legend = le.getText();
					
					System.out.println("Vendor in legend: " + legend);
					
					// Verify that the vendor that was unselected, is removed from the chart's legends
					Assert.assertNotEquals(legend, vendorUnselected);
					
				}
				
			}
			
		}
		
	}
	
	
	
	/// *** CONTINUE HERE *** 
	// Verify that if all the vendors of a country are unselected from the Point of View section, then the country is removed from the Usage Trending chart
	public static void countriesUnselectedRemovedFromChart(int chartNum) throws InterruptedException{
	
		String chartId = UsageHelper.getChartId(chartNum);
		UsageHelper.moveToChart(chartId);
		
		// Just to make sure all the vendors are selected
		CommonTestStepActions.SelectAllVendors();
		
		// All the countries listed on the Point of View section
		List<WebElement> countries = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead"));
		List<List<WebElement>> countriesAndVendors = UsageHelper.getCountriesAndVendors();
		
		
		// Checkboxes on the Point of View section
		List<WebElement> checkboxes = driver.findElements(By.cssSelector(".md-checkbox-inner-container"));
		
		// Countries listed on Usage Trending legends 
		List<WebElement> legendsOriginal = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
		List<String> legendStrings = new ArrayList<String>();
		
		for(WebElement legend: legendsOriginal){
			legendStrings.add(legend.getText());
		}
		
		
		for(int i = 0; i < countries.size(); i++){
			
			// Unselect vendor from Point of View section
			checkboxes.get(i).click();
			
			// Get name of unselected vendor
			String vendorUnselected = countries.get(i).getText();
			
			System.out.println("Vendor unselected: " + vendorUnselected);
			
			// If the vendor unselected was included in the legends of the Usage Trending chart
			if(legendStrings.contains(vendorUnselected)){
				
				Thread.sleep(2000);
				
				// Get the vendors listed on legends after the vendor was unselected
				List<WebElement> currentLegends = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
			
				for(WebElement le: currentLegends){
					
					String legend = le.getText();
					
					System.out.println("Vendor in legend: " + legend);
					
					// Verify that the vendor that was unselected, is removed from the chart's legends
					Assert.assertNotEquals(legend, vendorUnselected);
					
				}
				
			}
			
			
			
		}
		
		
		
	}
		
		
	
	
	
	
	
}
