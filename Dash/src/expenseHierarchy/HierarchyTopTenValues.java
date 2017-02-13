package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralHelper;
import helperObjects.UsageHelper;

public class HierarchyTopTenValues extends BaseClass{

	
	public static void verifyTopTenValues(int barChartId, int category) throws ParseException, AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> pointOfViewNames = driver.findElements(By.cssSelector("li.tdb-pov__item>a"));
		List<WebElement> pointOfViewValues = driver.findElements(By.cssSelector("li.tdb-pov__item>span"));;
		List<WebElement> chartElementNames = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels>text>tspan"));

		
		List<String> povNames = new ArrayList<String>();
		List<String> povValues = new ArrayList<String>();
		List<String> chartLabels = new ArrayList<String>();
		List<String> chartValues = new ArrayList<String>();
		
		
		for (int i = 0; i < povNames.size(); i++) {
			
			povNames.add(pointOfViewNames.get(i).getText());
			povValues.add(pointOfViewValues.get(i).getText());
			
		}
		
		for (int i = 0; i < chartElementNames.size(); i++) {

			System.out.println("label: " + chartElementNames.get(i).getText());
			chartLabels.add(chartElementNames.get(i).getText());
//			chartValues.add(chartElementValues.get(i).getText());
			
		}
		
				
		// Verify that the element "Average" exists on the Top Ten chart.
		Assert.assertTrue(chartLabels.contains("Average"));
		
		
		int indexHighchart = 1;
		
		while (indexHighchart <= chartElementNames.size()) {
			
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
						
			// WebElement 'bar' will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));

			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
			
			int x_offset = (int) (bar.getSize().width * 0.5);
			int y_offset = (int) (bar.getSize().height * 0.7);
			
			int x = coordinates.getX() + x_offset;
			int y = coordinates.getY() + y_offset;

//			System.out.println("x: " + coordinates.getX() + ", y: " + coordinates.getY() + ", x_offset: " + x_offset + ", y_offset: " + y_offset);
			
			Robot robot = new Robot();
			robot.mouseMove(x, y);
			Thread.sleep(500);
//			robot.mouseMove(x + 10, y);  // <-- If needed, uncomment. It replaces the mouse press and release, since in this chart the mouse click on the bar should redirect to a different page in CMD.
			
			
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"), MainTimeout);
//				System.out.println("Tooltip present");
			} catch (Exception e) {
				System.out.println("Tooltip NOT present");
				e.printStackTrace();
			}
					
			
			
			WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"));
			
			// Verify the label and the amount shown in the tooltip 
			
			// Get the label and remove colon at the end of its text
			String labelFound = tooltip.getText().split(":")[0].trim();

			// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
			String valueFound = tooltip.getText().split(":")[1].trim().replace(" ", "");
			
			// Get the expected label 
			String labelExpected = chartLabels.get(indexHighchart-1);
			
			// Get the expected value 
			String valueExpected = ""; //expectedValues.get(indexMonth).get(labelExpected);
			 
					
			// Verify the labels' text and amounts shown on the tooltip
			Assert.assertEquals(labelFound, labelExpected);
			
			// Assert.assertEquals(valueFound, valueExpected);
				
			
			System.out.println("labelFound: " + labelFound + ", labelExpected: " + labelExpected);
			System.out.println("valueFound: " + valueFound + ", valueExpected: " + valueExpected);

			indexHighchart++;
				
		}
		
		
		
	}

}
