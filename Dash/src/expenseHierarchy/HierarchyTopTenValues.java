package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
		HierarchyHelper.waitForChartToLoad(HierarchyHelper.topTenChart);
		Thread.sleep(2000);
	
		// Get data from JSON
		List<HierarchyTopTenData> valuesExpected = ReadFilesHelper.getJsonDataTopTen(category, hierarchyId); 
		
		// Verify values on the selected Top Ten chart and for the selected category
		if (!GeneralTopTenHelper.allValuesAreZeroHierarchy(valuesExpected)) {
		
			verifyTooltipTopTenChart(valuesExpected, barChartId, category);
			
		}
		
		
	}
	
	
	
	public static void verifyTooltipTopTenChart(List<HierarchyTopTenData> topTenValues, int barChartId, int category) throws ParseException, AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> chartElementNames = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> chartLabelsFound = new ArrayList<String>();
		
		List<String> expectedValuesList = new ArrayList<>();
		HashMap<String, String> expectedValues = new HashMap<>();
		List<String> expectedLabels = new ArrayList<>();
		
		
		for (int i = 0; i < chartElementNames.size(); i++) {

			chartLabelsFound.add(chartElementNames.get(i).getText());
			
		}
		
		
		// Set up lists with expected values and labels		
		for (HierarchyTopTenData data: topTenValues) {
			
			String type = data.getType();
			String label = "";
			String serviceNumber = "";
			String firstName = "";
			String lastName = "";
			String employeeName = "";
			
			// Set up the label according to whether the type is Employee, Department or Average 
	    	switch (type) {
	    	
	    		case "EMPLOYEE":
	    			
	    			serviceNumber = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
	    			
	    			if (!data.getEmployeeFirstname().isEmpty()) 
	    				firstName = data.getEmployeeFirstname().substring(0, 1); 
	    			
	    			if (!data.getEmployeeLastname().isEmpty()) 
	    				lastName = data.getEmployeeLastname().replace(" ", "");
	    			
	    			if (!firstName.isEmpty() && !lastName.isEmpty())
	    				employeeName = firstName + "." + lastName;
	    			
	    			else if (firstName.isEmpty() && !lastName.isEmpty())
	    				employeeName = lastName;
	    			
	    			label = serviceNumber + employeeName;
	    			expectedLabels.add(label);
	    			
	    			break;
	    			
	    		case "DEPARTMENT":
	    			
	    			serviceNumber = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
	    			label = serviceNumber + data.getDepartmentName();
	    			expectedLabels.add(label);
	    			
	    			break;

	    		case "AVERAGE":
	    			
	    			label = "Average";
	    			expectedLabels.add(label);
	    			
	    			break;
	    	
	    	}
			

	    	String value = UsageCalculationHelper.roundNoDecimalDigits(data.getValue(), false);
	    	expectedValuesList.add(value);
	    	expectedValues.put(label, value);
	    	
		}

		System.out.println("Expected labels:");
		for (String s: expectedLabels) {
			
			System.out.println(s);
			
		}
		
		
		// Verify that the values are sorted in descendant order by "value"
		GeneralTopTenHelper.verifyValuesSortedDescendantOrder(expectedValuesList); 
		
		// Verify that the element "Average" exists on the Top Ten chart.
		Assert.assertTrue(chartLabelsFound.contains("Average"));
		
		// Verify the info contained on each of the tooltips for the values listed on the chart 	
		
		int indexHighchart = 1;
		
		while (indexHighchart <= chartElementNames.size()) {
			
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
						
			// WebElement 'bar' will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));

			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			Point coordinates = GeneralHelper.getAbsoluteLocationTopTenBar(bar);
			
			int x_offset = (int) (bar.getSize().width * 0.5);
			int y_offset = (int) (bar.getSize().height * 0.7);

			if (loginType.equals(LoginType.Command)) {
				if (chartElementNames.size() > 5) {
					y_offset = (int) (bar.getSize().height * 1.5);
				} else {
					y_offset = (int) (bar.getSize().height); // <-- added on 4/6/17. This worked for a graph with 2 bars. Will need to see if it works with more bars  
				}
			}
			
			int x = coordinates.getX() + x_offset + 2;
			int y = coordinates.getY() + y_offset;

			
			Robot robot = new Robot();
			robot.mouseMove(x, y);
			Thread.sleep(500);
			// robot.mouseMove(x + 10, y);  // <-- If needed, uncomment it. It replaces the mouse press and release, since in this chart the mouse click on the bar should redirect to a different page in CMD.
			
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"), MainTimeout);
				// System.out.println("Tooltip present");
			} catch (Exception e) {
				System.out.println("Tooltip NOT present");
				e.printStackTrace();
			}
					
			
			WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"));
			
			// Verify the label and the amount shown in the tooltip 
			
			// Get the label and remove colon at the end of its text
			String labelFoundTmp = tooltip.getText().split(":")[0].trim();
			String labelFound = GeneralTopTenHelper.formatPhoneNumberUI(labelFoundTmp); 
			
			// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
			String valueFound = tooltip.getText().split(":")[1].trim().replace(" ", "");
			
			// Get the expected value 
			String valueExpected = expectedValues.get(labelFound);
			
			System.out.println("labelFound: " + labelFound);
			System.out.println("valueFound: " + valueFound + ", valueExpected: " + valueExpected);
					
			// The verification of the expected label is made by verifying that the label found is included in the list of expected labels.
			// They cannot be verified by order, since if there are 2 elements that have the same value (expenses value) the order in which they'll be listed cannot be known
			Assert.assertTrue(expectedLabels.contains(labelFound));
			GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
			
			indexHighchart++;
				
		}
		
	}



	public static void verifyTopTenChartReport(String hierarchyId, int barChartId, int category) throws Exception {
		
		
		// Select category
		HierarchyHelper.selectCategoryTopTen(barChartId, category);
		
		// Wait for the data to be updated on chart
		HierarchyHelper.waitForChartToLoad(HierarchyHelper.topTenChart);
//		Thread.sleep(2000);
	
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
		
		do {
			
			indexHighchart = (int) (Math.random() * 11);
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
		String totalChargesValueExpected = "$" + expectedValues.getValue(); 
		
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
		String hierarchyFound = driver.findElement(By.cssSelector("tr.top>.pad-r5:nth-of-type(3)")).getText().split("\n")[1];
		String serviceNumberFound = GeneralTopTenHelper.formatPhoneNumber(driver.findElement(By.cssSelector("tr.data-row>td:nth-of-type(2)>a")).getText());
		String totalChargesValueFound = driver.findElements(By.cssSelector(".data-cell.datar>strong")).get(6).getText().replace(",", "");
	
		
		ShowText("  * fullNameFound: " + fullNameFound + " - * fullNameExpected: " + fullNameExpected);
		ShowText("  * hierarchyFound: " + hierarchyFound + " - * hierarchyExpected: " + hierarchyExpected);
		ShowText("  * serviceNumberFound: " + serviceNumberFound + " - * serviceNumberExpected: " + serviceNumberExpected);
		ShowText("  * totalChargesValueFound: " + totalChargesValueFound + " - * totalChargesValueExpected: " + totalChargesValueExpected);
		
		Assert.assertEquals(fullNameFound, fullNameExpected);
		Assert.assertEquals(hierarchyFound, hierarchyExpected);
		Assert.assertEquals(serviceNumberFound, serviceNumberExpected);
		Assert.assertEquals(totalChargesValueFound, totalChargesValueExpected);
		
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
