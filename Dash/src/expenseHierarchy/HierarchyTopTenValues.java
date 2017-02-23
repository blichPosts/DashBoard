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
import Dash.BaseClass.LoginType;
import helperObjects.GeneralTopTenHelper;
import helperObjects.HierarchyHelper;
import helperObjects.FleetTopTenData;
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
		HierarchyHelper.waitForTopTenChartToLoad();
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
		
		List<WebElement> chartElementNames = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels>text>tspan"));
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
			
			// Set up the label according to whether the type is Employee, Department or Average 
	    	switch (type) {
	    	
	    		case "EMPLOYEE":
	    			serviceNumber = GeneralTopTenHelper.formatPhoneNumber(data.getServiceNumber());
	    			label = serviceNumber + data.getEmployeeFirstname().substring(0, 1) + "." + data.getEmployeeLastname();
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
		
		// Verify that there are 11 elements listed on the chart: the Top Ten Service Numbers + Average
		Assert.assertTrue(chartElementNames.size() == 11);
		
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
				y_offset = (int) (bar.getSize().height * 1.5);
			}
			
			int x = coordinates.getX() + x_offset;
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
			String labelFound = GeneralTopTenHelper.formatPhoneNumber(labelFoundTmp); 
			
			// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
			String valueFound = tooltip.getText().split(":")[1].trim().replace(" ", "");
			
			// Get the expected value 
			String valueExpected = expectedValues.get(labelFound);
			
			System.out.println("labelFound: " + labelFound);
//			System.out.println("valueFound: " + valueFound + ", valueExpected: " + valueExpected);
					
			// The verification of the expected label is made by verifying that the label found is included in the list of expected labels.
			// They cannot be verified by order, since if there are 2 elements that have the same value (expenses value) the order in which they'll be listed cannot be known
			Assert.assertTrue(expectedLabels.contains(labelFound));
			GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
			
			indexHighchart++;
				
		}
		
	}

	
	
}
