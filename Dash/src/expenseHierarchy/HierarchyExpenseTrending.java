package expenseHierarchy;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;


public class HierarchyExpenseTrending extends BaseClass {

		
	// Verifies the content of the tooltips displayed on charts under Hierarchy Expense Trending charts
	public static void verifyExpenseTrendingChartTooltip(int chartNum, List<HierarchyTrendData> allValuesFromFile, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		// List "allValuesFromFile" has all 13 months listed on pulldown. 
				
		String chartId = UsageHelper.getChartId(chartNum);
		
		GeneralHelper.moveDown(chartId);
	
		HierarchyHelper.selectCategoryHierarchy(HierarchyHelper.getCategoryName(categorySelector));
		Thread.sleep(2000);
		
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));

		int amount = highchartSeries.size();
		//System.out.println("amount: " + amount);
		
		int indexHighchart = 1;
		
		List<String> monthYearList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		int indexMonth = monthYearList.size()-1;
		
		
		// Set up list with expected values
		List<HashMap<String, String>> expectedValues = getListExpectedValues(allValuesFromFile, categorySelector);
		
		// Set up list with expected labels
		List<String> expectedLabels = new ArrayList<>();
		
		// Expected labels are: 
		expectedLabels.add(HierarchyHelper.directlyAllocated);
		expectedLabels.add(HierarchyHelper.allocatedChildren);
					
		
		// Verify the info contained on each of the tooltips for the 13 months 		
		
		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(false, firstBar, chartNum, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name>
			// 3 <amount shown for the vendor>
						
			int factor = 2;
			
			int expectedAmountItemsTooltip = (amount * factor) + 1;
			int amountItemsTooltipFound = tooltip.size();
			
			GeneralHelper.verifyExpectedAndActualValues(amountItemsTooltipFound, expectedAmountItemsTooltip);
			

			// For each vendor listed in the tooltip verify the label and the amount shown
			for (int i = 1; i <= legends.size(); i++) {
			
				int index =  i * factor;
				
				// Get the label and remove colon at the end of its text
				String labelFound = tooltip.get(index).getText().split(":")[1].trim();

				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: $15 256 985. Value needed is: $15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim();

				String valueFoundRounded = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(GeneralHelper.getNumericValue(valueFound)), true);
				
				// Get the expected label 
				String labelExpected = expectedLabels.get(i-1);
				
				// Get the expected value 
				String valueExpected = expectedValues.get(indexMonth).get(labelExpected);
				 		
				// Verify the labels' text and amounts shown on the tooltip
				
				ShowText("labelFound: " + labelFound + ", labelExpected: " + labelExpected);
				ShowText("valueFound: " + valueFoundRounded + ", valueExpected: " + valueExpected);
				
				GeneralHelper.verifyExpectedAndActualLabels(labelFound, labelExpected);
				GeneralHelper.verifyExpectedAndActualValues(valueFoundRounded, valueExpected);

			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			String monthYearExpected = monthYearList.get(indexMonth);
			
			ShowText("monthYearFound: " + monthYearFound + ", monthYearExpected: " + monthYearExpected);
			GeneralHelper.verifyExpectedAndActualLabels(monthYearFound, monthYearExpected);
						
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
