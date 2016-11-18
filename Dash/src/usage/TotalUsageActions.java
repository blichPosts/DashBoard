package usage;

import org.testng.Assert;

import java.util.Collection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;


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
		
		categorySelectors.get(kpiNum).click();
		
		int count = 1;
		
		for (WebElement v : vendors){
		
			String vendorName = v.getText();
			
			// Select one vendor
			CommonTestStepActions.selectOneVendor(vendorName);
			System.out.println("Vendor Name just selected: " + vendorName);
			
			// Wait 2 seconds to the values to get updated on Dashboard, after the vendor selection
			Thread.sleep(2000);
			
			currentKPIvalue = kpiTiles.get(kpiNum).getText(); 
			//System.out.println("Current KPI tile value: " + currentKPIvalue);
			
			// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
			if(count == 1){
				
				System.out.println("First Vendor..");
				
				// Verify that vendor is listed on chart if value is NOT zero 
				if(!currentKPIvalue.equals("0")){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							System.out.println("Vendor in chart: " + v.getText());
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
				}
				
				
			} else if (count > 1 && count < 6) { // From second vendor. If KPI value doesn't change, it means value for the last selected vendor is zero and it won't be listed on the chart.
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							System.out.println("Vendor in chart: " + v.getText());
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
					
				}
				
			} else if (count >= 6) {
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText()) || label.getText().equals("Other")){
							System.out.println("Vendor in chart: " + label.getText());
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
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames(); 
		
		List<WebElement> kpiTiles = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		Collection<List> countries = CommonTestStepActions.getCountriesAndVendors();
		
			
		String currentKPIvalue = "";
		String previousKPIvalue = "";
		
		String chartId = UsageHelper.getChartId(chartNum);
		
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(odd)>div"));
		
		categorySelectors.get(kpiNum).click();
		
		int count = 1;
		
		for (WebElement v : vendors){
		
			String vendorName = v.getText();
			
			// Select one vendor
			CommonTestStepActions.selectOneVendor(vendorName);
			System.out.println("Vendor Name just selected: " + vendorName);
			
			// Wait 2 seconds to the values to get updated on Dashboard, after the vendor selection
			Thread.sleep(2000);
			
			currentKPIvalue = kpiTiles.get(kpiNum).getText(); 
			//System.out.println("Current KPI tile value: " + currentKPIvalue);
			
			// For first vendor selected: If KPI tile is zero, vendor will not be listed on the chart. 
			if(count == 1){
				
				System.out.println("First Vendor..");
				
				// Verify that vendor is listed on chart if value is NOT zero 
				if(!currentKPIvalue.equals("0")){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							System.out.println("Vendor in chart: " + v.getText());
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
				}
				
				
			} else if (count > 1 && count < 6) { // From second vendor. If KPI value doesn't change, it means value for the last selected vendor is zero and it won't be listed on the chart.
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText())){
							System.out.println("Vendor in chart: " + v.getText());
							vendorInChart = true;
						}
					}
					
					Assert.assertTrue(vendorInChart);
					
				}
				
			} else if (count >= 6) {
				
				// Verify that vendor is listed on chart if value is NOT zero
				if(!currentKPIvalue.equals(previousKPIvalue)){
					
					List<WebElement> xAxisLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
					
					boolean vendorInChart = false; 
					
					for (WebElement label: xAxisLabels){
						System.out.println("Label in x axis: " + label.getText());
						
						if(label.getText().equals(v.getText()) || label.getText().equals("Other")){
							System.out.println("Vendor in chart: " + label.getText());
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
	
	
	
	
}
