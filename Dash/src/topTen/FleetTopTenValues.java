package topTen;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.FleetTopTenData;
import helperObjects.GeneralTopTenHelper;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;


public class FleetTopTenValues extends BaseClass {

	
	
	public static void verifyTopTenChartValues(int barChartId, int category) throws Exception {
		
		// Select category
		HierarchyHelper.selectCategoryTopTen(barChartId, category);
		
		List<FleetTopTenData> valuesExpected = new ArrayList<>();
		
		// Wait for the data to be updated on chart
		// Run test only if there's data displayed on chart
		if (HierarchyHelper.waitForChartToLoad(HierarchyHelper.topTenChart)) {
			
			Thread.sleep(2000);
			
			boolean chartHasData = false;
			
			// Get data from JSON
			valuesExpected = ReadFilesHelper.getJsonDataTopTenFleet(barChartId, category);
			
			if (valuesExpected.size() > 0) {
				chartHasData = true;
				  
			} else if (valuesExpected.size() == 0) {
				chartHasData = false;
				  
			}
			
			// Verify values on the selected Top Ten chart and for the selected category
			if (!GeneralTopTenHelper.allValuesAreZero(valuesExpected) && chartHasData) {
			
				verifyTooltipTopTenChart(valuesExpected, barChartId, category);
				
			}
		
		}
		
	}
	
	
	
	public static void verifyTooltipTopTenChart(List<FleetTopTenData> topTenValues, int barChartId, int category) throws AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		WebElement topTenChart = driver.findElement(By.cssSelector("#" + chartId));
		new Actions(driver).moveToElement(topTenChart).perform();
		
		Thread.sleep(2000);
		
		List<WebElement> chartElementNames = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> chartLabelsFound = new ArrayList<String>();
		
		List<String> expectedValuesList = new ArrayList<>();
		HashMap<String, String> expectedValues = new HashMap<>();
		List<String> expectedLabels = new ArrayList<>();
		
		
		for (int i = 0; i < chartElementNames.size(); i++) {

			chartLabelsFound.add(chartElementNames.get(i).getText());
			
		}
			
		 ShowText("expected labels:");
		// Set up lists with expected values and labels		
		for (FleetTopTenData data: topTenValues) {
			
			String label = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
	    	expectedLabels.add(label);
	    	 ShowText("label:  " + label);
	    	
	    	String value = UsageCalculationHelper.roundNoDecimalDigits(data.getValue(), false);
	    	expectedValuesList.add(value);
	    	expectedValues.put(label, value);
	    	
		}

		
		// Verify that there are up to 10 elements listed on the chart: the Top Ten Service Numbers
		Assert.assertTrue(chartElementNames.size() <= 10);
		
		// Verify that the values are sorted in descendant order by "value"
//		FleetTopTenHelper.verifyValuesSortedDescendantOrder(expectedValuesList); 
		
		// Verify the info contained on each of the tooltips for the values listed on the chart 	
		
		int indexHighchart = 1;
		
		while (indexHighchart <= chartElementNames.size()) {
	
			GeneralHelper.moveMouseToBar(chartId, indexHighchart, true);
	
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"));
			
			// Verify the label and the amount shown in the tooltip 
			
			// Get the label and remove colon at the end of its text
			String labelFoundTmp = tooltip.get(1).getText().split(":")[0].trim();
			
			String labelFound = labelFoundTmp;
			
			if (!labelFoundTmp.equals("Average")) {
			
				labelFound = GeneralTopTenHelper.formatPhoneNumberUI(labelFoundTmp);
				
			}
			
			// ShowText("labelFound:  " + labelFound);
			
			// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
			String valueFound = tooltip.get(1).getText().split(":")[1].trim().replace(" ", "");
			
			// Get the expected value 
			String valueExpected = expectedValues.get(labelFound);
			
			 ShowText("Label Found: " + labelFound); 
			 ShowText("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
					
			// The verification of the expected label is made by verifying that the label found is included in the list of expected labels.
			// They cannot be verified by order, since if there are 2 elements that have the same value (expenses value) the order in which they'll be listed cannot be known
			Assert.assertTrue(expectedLabels.contains(labelFound));
			GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
			
			indexHighchart++;
				
		}
		
		
	}

}
