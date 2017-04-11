package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;


public class HierarchyExpenseTrending extends BaseClass {

	
	// Verifies the content of the tooltips displayed on charts under Hierarchy Expense Trending charts
	public static void verifyExpenseTrendingChartTooltip(int barChartId, List<HierarchyTrendData> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
				
		String chartId = UsageHelper.getChartId(barChartId);
		
		WebElement expenseTrendingSection = driver.findElement(By.cssSelector("#" + chartId));
		new Actions(driver).moveToElement(expenseTrendingSection).perform();
		
		Thread.sleep(2000);
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		//System.out.println("amount: " + amount);
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		
		// Set up list with expected values
		List<HashMap<String, String>> expectedValues = getListExpectedValues(allValuesFromFile, categorySelector);  // new ArrayList<>();
		
		// Set up list with expected labels
		List<String> expectedLabels = new ArrayList<>();
		
		// Expected labels are: 
		expectedLabels.add(HierarchyHelper.directlyAllocated);
		expectedLabels.add(HierarchyHelper.allocatedChildren);
					
		

		// Verify the info contained on each of the tooltips for the 13 months 		
		
		while (indexHighchart <= monthYearList.size()) {
			
			String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
						
			// WebElement 'bar' will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssBar));

			// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
			// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
			
			Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
			int x = coordinates.getX();
			int y = GeneralHelper.getYCoordinate(chartId);
			
			Robot robot = new Robot();
			robot.mouseMove(x, y);
			
			Thread.sleep(500);
			
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				
			
			try {
				WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
				//System.out.println("Tooltip present");
			} catch (Exception e) {
				System.out.println("Tooltip NOT present");
				e.printStackTrace();
			}
					
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name>
			// 3 <amount shown for the vendor>
						
			int expectedAmountItemsTooltip = (amount * 3) + 1;
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			

			// For each vendor listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index =  i * 3 - 1;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
				String valueFoundRounded = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(valueFound.replace("$", "")), true);
				
				// Get the expected label 
				String labelExpected = expectedLabels.get(i-1);
				
				// Get the expected value 
				String valueExpected = expectedValues.get(indexMonth).get(labelExpected);
				 		
				// Verify the labels' text and amounts shown on the tooltip
				
				// System.out.println("labelFound: " + labelFound + ", labelExpected: " + labelExpected);
    			// System.out.println("valueFound: " + valueFoundRounded + ", valueExpected: " + valueExpected);
				
				Assert.assertEquals(labelFound, labelExpected);
				GeneralHelper.verifyExpectedAndActualValues(valueFoundRounded, valueExpected);
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
				
			Assert.assertEquals(monthYearFound, monthYearExpected);
			// System.out.println("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			
			indexHighchart++;
			indexMonth--;
			
		}
		
	}
	

	
	public static List<HashMap<String, String>> getListExpectedValues(List<HierarchyTrendData> allValuesFromFile, int categorySelector) {
		
		List<HashMap<String, String>> expectedValues = new ArrayList<>();
		
		for (int indexMonthValues = 0; indexMonthValues < allValuesFromFile.size(); indexMonthValues++) {
				
			HashMap<String, String> map = new HashMap<>();
			HierarchyTrendData dataOneMonth = allValuesFromFile.get(indexMonthValues);
				
			if (categorySelector == HierarchyHelper.categoryTotal){

				double allocationDependentUnits = Double.parseDouble(dataOneMonth.getTotalExpenseRollup()) - Double.parseDouble(dataOneMonth.getTotalExpense());
				map.put(HierarchyHelper.allocatedChildren, UsageCalculationHelper.roundNoDecimalDigits(allocationDependentUnits, true));
				map.put(HierarchyHelper.directlyAllocated, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(dataOneMonth.getTotalExpense()), true));
				
			} else if (categorySelector == HierarchyHelper.categoryOptimizable){

				double allocationDependentUnits = Double.parseDouble(dataOneMonth.getOptimizableExpenseRollup()) - Double.parseDouble(dataOneMonth.getOptimizableExpense());
				map.put(HierarchyHelper.allocatedChildren, UsageCalculationHelper.roundNoDecimalDigits(allocationDependentUnits, true));
				map.put(HierarchyHelper.directlyAllocated, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(dataOneMonth.getOptimizableExpense()), true));
				
			} else if (categorySelector == HierarchyHelper.categoryRoaming){

				double allocationDependentUnits = Double.parseDouble(dataOneMonth.getRoamingExpenseRollup()) - Double.parseDouble(dataOneMonth.getRoamingExpense());
				map.put(HierarchyHelper.allocatedChildren, UsageCalculationHelper.roundNoDecimalDigits(allocationDependentUnits, true));
				map.put(HierarchyHelper.directlyAllocated, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(dataOneMonth.getRoamingExpense()), true));
				
			}				

			expectedValues.add(map);
			
		}
		
		return expectedValues;
		
	}
	
	
	
}
