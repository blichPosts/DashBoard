package expenseHierarchy;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralTopTenHelper;
import helperObjects.HierarchyHelper;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyTopTenData;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;


public class HierarchyTopTenValues extends BaseClass{

	
	public static void verifyTopTenChartValues(String hierarchyId, int barChartId, int category) throws Exception {
		
		// Select category
		HierarchyHelper.selectCategoryTopTen(barChartId, category);
		
		// Wait for the data to be updated on chart
		if (HierarchyHelper.waitForChartToLoad(HierarchyHelper.topTenChart)) {
			
			Thread.sleep(2000);
			
			// Get data from JSON
			List<HierarchyTopTenData> valuesExpected = ReadFilesHelper.getJsonDataTopTen(category, hierarchyId); 
			
			// Verify values on the selected Top Ten chart and for the selected category
			if (!GeneralTopTenHelper.allValuesAreZeroHierarchy(valuesExpected)) {
			
				verifyTooltipTopTenChart(valuesExpected, barChartId, category);
				
			}
		
		}
		
	}
	
	
	
	public static void verifyTooltipTopTenChart(List<HierarchyTopTenData> topTenValues, int barChartId, int category) throws ParseException, AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> chartElementNames = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> chartLabelsFound = new ArrayList<String>();
		
		for (int i = 0; i < chartElementNames.size(); i++) {
			chartLabelsFound.add(chartElementNames.get(i).getText());
			ShowText(chartElementNames.get(i).getText());
		}
		
		
		List<String> expectedLabels = new ArrayList<>();
		List<String> expectedValuesList = new ArrayList<>();
		HashMap<String, String> expectedValues = new HashMap<>();
		
				
		// Set up lists with expected values and labels		
		for (HierarchyTopTenData data: topTenValues) {
			
			String expectedLabel = getExpectedLabel(data);
			expectedLabels.add(expectedLabel);
			
	    	String expectedValue = UsageCalculationHelper.roundNoDecimalDigits(data.getValue(), false);
	    	expectedValuesList.add(expectedValue);
	    	expectedValues.put(expectedLabel, expectedValue);
	    	
		}

		System.out.println("Expected labels:");
		for (String s: expectedLabels) {
			
			System.out.println(s);
			
		}
		
		
		// Verify that the values are sorted in descendant order by "value"
		GeneralTopTenHelper.verifyValuesSortedDescendantOrder(expectedValuesList); 
		
		// Verify that the element "Average" exists on the Top Ten chart.
		Assert.assertTrue(chartLabelsFound.contains("Average"), "Average is not included in the chart.");
		
		// Verify the info contained on each of the tooltips for the values listed on the chart 	
		
		int indexHighchart = 1;
		int barsAmount = chartElementNames.size();
		
		while (indexHighchart <= barsAmount) {
			
			GeneralHelper.moveMouseToBar(chartId, indexHighchart, true);
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"));
			
			// Verify the label and the amount shown in the tooltip 
			
			// Get the label and remove colon at the end of its text
			String labelFoundTmp = tooltip.get(1).getText().split(":")[0].trim();
			String labelFound = labelFoundTmp;
			
			if (!labelFoundTmp.equals("Average")) {
			
				labelFound = GeneralTopTenHelper.formatPhoneNumberUI(labelFoundTmp);
				
			}
			
			// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
			String valueFound = tooltip.get(1).getText().split(":")[1].trim().replace(" ", "");
			
			// Get the expected value 
			String valueExpected = expectedValues.get(labelFound);
			
			System.out.println("Label Found: " + labelFound);
			System.out.println("Value Found: " + valueFound + ", Value Expected: " + valueExpected);
					
			// The verification of the expected label is made by verifying that the label found is included in the list of expected labels.
			// They cannot be verified by order, since if there are 2 elements that have the same value (expenses value) the order in which they'll be listed cannot be known
			Assert.assertTrue(expectedLabels.contains(labelFound), "Label found " + labelFound + " is not included in the 'Expected Labels' list");
			GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
			
			indexHighchart++;
				
		}
		
	}




	// Set up the label according to whether the type is Employee, Department or Average 
	private static String getExpectedLabel(HierarchyTopTenData data) {
		
		String type = data.getType();
		String label = "";
		String serviceNumber = "";
		String firstName = "";
		String lastName = "";
		String employeeName = "";
		
    	switch (type) {
    	
    		case "EMPLOYEE":
    			
    			serviceNumber = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
    			
    			if (!data.getEmployeeFirstname().isEmpty()) 
    				firstName = data.getEmployeeFirstname().substring(0, 1); 
    			
    			if (!data.getEmployeeLastname().isEmpty()) {
    			
    				lastName = data.getEmployeeLastname().replace(" ", "");
    			 	lastName = lastName.replace("-", "");
    			 	
    			}
    			
    			if (!firstName.isEmpty() && !lastName.isEmpty()) {
    			
    				employeeName = firstName + "." + lastName;
    				
    			} else if (firstName.isEmpty() && !lastName.isEmpty()) {
    			
    				employeeName = lastName;
    			
    			}
    			
    			label = serviceNumber + employeeName;
    			break;
    			
    		case "DEPARTMENT":
    			
    			serviceNumber = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
    			label = serviceNumber + data.getDepartmentName();
    			break;

    		case "AVERAGE":
    			
    			label = "Average";
    			break;
    	
    	}
    	
		return label;
		
	}



	public static void verifyTopTenChartReport(String hierarchyId, int barChartId, int category) throws Exception {
		
		
		// Select category
		HierarchyHelper.selectCategoryTopTen (barChartId, category);
		
		// Wait for the data to be updated on chart
		HierarchyHelper.waitForChartToLoad(HierarchyHelper.topTenChart);
	
		// Get data from JSON
		List<HierarchyTopTenData> valuesExpected = ReadFilesHelper.getJsonDataTopTen(category, hierarchyId); 
		
		// Verify values on the selected Top Ten chart and for the selected category
		verifyValuesOnReportMatchChart(valuesExpected, barChartId, category);
			
	}
	
	
	public static void verifyValuesOnReportMatchChart(List<HierarchyTopTenData> topTenValues, int barChartId, int category) throws ParseException, AWTException, InterruptedException {
		
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> elementLabels = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text"));
		List<String> servNumbersLabels = new ArrayList<>(); 
		
		for (WebElement element: elementLabels) {
			servNumbersLabels.add(GeneralTopTenHelper.getOnlyPhoneNumberFormatted(element.getText()));
		}
		
		
		HashMap<String, HierarchyTopTenData> topTenDataMap = new HashMap<>();
		
		for (HierarchyTopTenData data: topTenValues) {
		
			if (!data.getType().equals("AVERAGE")) {
		
				String key = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
				topTenDataMap.put(key, data);
		
			}
		}
		
		String label = "";
		int indexHighchart = 1;
				
		// Select a random bar to be clicked
		do {
			double random = 0;
			do {
				random = Math.random();
				
			} while (random == 0);
			
			indexHighchart =  (int) (random * elementLabels.size());
			label = topTenValues.get(indexHighchart-1).getType();
				
		} while (label.equals("AVERAGE") || indexHighchart == 0);
		
		
		ShowText("Service Number to be clicked: " + topTenValues.get(indexHighchart-1).getServiceNumber());
		
		
		HierarchyTopTenData expectedValues = topTenDataMap.get(servNumbersLabels.get(indexHighchart-1));
		
		// Set up Expected values
		String fullNameExpected = "";
		
		if(label.equals("EMPLOYEE")) {
			fullNameExpected = expectedValues.getEmployeeLastname() + ", " + expectedValues.getEmployeeFirstname();
		}
		
		else if(label.equals("DEPARTMENT")) {
			if (expectedValues.getDepartmentName().equals("Unknown")) {
				fullNameExpected = "N/A";
			} else {
				fullNameExpected = expectedValues.getDepartmentName();
			}
		}
		
		String hierarchyExpected = driver.findElement(By.cssSelector("div.tdb-kpi__header>span>span")).getText();
		String serviceNumberExpected = expectedValues.getServiceNumber();
		String totalChargesValueExpected = "$" + UsageCalculationHelper.roundTwoDecimalDigits(expectedValues.getValue()); 
		
		if (totalChargesValueExpected.contains(".")) {
			if (totalChargesValueExpected.split("\\.")[1].length() == 1) {
				totalChargesValueExpected = totalChargesValueExpected + "0";
			}
		} else {
			totalChargesValueExpected = totalChargesValueExpected + ".00";
		}
		
		
		driver.findElement(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")")).click();
		waitForReportToLoad();
			
		// Values found on Report
		String fullNameFound = driver.findElement(By.cssSelector(".pad-l5r5>a")).getText();
		fullNameFound = fullNameFound.split(" ")[0] + " " + fullNameFound.split(" ")[1];
		String hierarchyFound = driver.findElement(By.cssSelector("tr.top>.pad-r5:nth-of-type(3)")).getText().split("\n")[1];
		String serviceNumberFound = GeneralTopTenHelper.formatPhoneNumber(driver.findElement(By.cssSelector("tr.data-row>td:nth-of-type(2)>a")).getText());
		String totalChargesValueFound = driver.findElements(By.cssSelector(".data-cell.data")).get(4).getText().replace(",", "").replace(" ", "");
//		String totalChargesValueFound = driver.findElements(By.cssSelector(".data-cell.datar>strong")).get(6).getText().replace(",", ""); // --> OLD 
	
		
		ShowText("  * fullNameFound: " + fullNameFound + " - * fullNameExpected: " + fullNameExpected);
		ShowText("  * hierarchyFound: " + hierarchyFound + " - * hierarchyExpected: " + hierarchyExpected);
		ShowText("  * serviceNumberFound: " + serviceNumberFound + " - * serviceNumberExpected: " + serviceNumberExpected);
		ShowText("  * totalChargesValueFound: " + totalChargesValueFound + " - * totalChargesValueExpected: " + totalChargesValueExpected);
		
		GeneralHelper.verifyExpectedAndActualLabels(fullNameFound, fullNameExpected);
		GeneralHelper.verifyExpectedAndActualLabels(hierarchyFound, hierarchyExpected);
		GeneralHelper.verifyExpectedAndActualLabels(serviceNumberFound, serviceNumberExpected);
		GeneralHelper.verifyExpectedAndActualLabels(totalChargesValueFound, totalChargesValueExpected);
		
	}



	private static void waitForReportToLoad() throws InterruptedException {
		
		driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
	    		
		try {
			WaitForElementPresent(By.cssSelector(".template-pageTitle"), MainTimeout);
		} catch (Exception e) {
			ShowText("Report was not displayed on time...");
		}
		
	}

	
	
}
