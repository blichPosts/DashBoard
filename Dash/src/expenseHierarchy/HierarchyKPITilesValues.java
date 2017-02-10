package expenseHierarchy;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.HierarchyTrendData;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageOneMonth;


public class HierarchyKPITilesValues extends BaseClass{

	public static void verifyKPItileValues(String totalExpense, String optimizableExpense, String roamingExpense, String totalExpenseRollup, 
			String optimizableExpenseRollup, String roamingExpenseRollup, String numberOfLinesRollUp) {
		
		List<WebElement> kpiTileMainValues = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		List<WebElement> kpiTileDirectlyAllocatedValues = driver.findElements(By.cssSelector(".tdb-kpi__statistic--secondary"));
		
		// Main value on KPI 
		double totalExpenseMainValue = Double.parseDouble(totalExpenseRollup);   
		double optimizableExpenseMainValue = Double.parseDouble(optimizableExpenseRollup);
		double roamingExpenseMainValue = Double.parseDouble(roamingExpenseRollup);
		double costPerServiceNumberMainValue = (Double.parseDouble(totalExpenseRollup) / Long.parseLong(numberOfLinesRollUp));
		
		// Secondary Value on KPI (below main value)
		double totalExpenseDirectlyAllocatedValue = Double.parseDouble(totalExpense);   
		double optimizableExpenseDirectlyAllocatedValue = Double.parseDouble(optimizableExpense);
		double roamingExpenseDirectlyAllocatedValue = Double.parseDouble(roamingExpense);
		long numberOfLinesValue = Long.parseLong(numberOfLinesRollUp);
		
		
		// Verify the main values on KPIs
		
		String totalExpenseKPICalculated = UsageCalculationHelper.convertUnitsExpense(totalExpenseMainValue);
		String totalExpenseKPIFromDash = kpiTileMainValues.get(0).getText();
		
		System.out.println("KPI Tile Total Expense Actual: " + totalExpenseKPIFromDash + ",  KPI Tile Total Expense Expected: " + totalExpenseKPICalculated); 
//		Assert.assertEquals(totalExpenseKPIFromDash, totalExpenseKPICalculated);
		
		String optimizableKPICalculated = UsageCalculationHelper.convertUnitsExpense(optimizableExpenseMainValue);
		String optimizableKPIFromDash = kpiTileMainValues.get(1).getText();
		
		System.out.println("KPI Tile Optimizable Actual: " + optimizableKPIFromDash + ",  KPI Tile Optimizable Expected: " + optimizableKPICalculated); 
//		Assert.assertEquals(optimizableKPIFromDash, optimizableKPICalculated);
		
		String roamingKPICalculated = UsageCalculationHelper.convertUnitsExpense(roamingExpenseMainValue);
		String roamingKPIFromDash = kpiTileMainValues.get(2).getText();
		
		System.out.println("KPI Tile Roaming Actual: " + roamingKPIFromDash + ",  KPI Tile Roaming Expected: " + roamingKPICalculated); 
//		Assert.assertEquals(roamingKPIFromDash, roamingKPICalculated);
		
		String costPerServNumKPICalculated = UsageCalculationHelper.convertUnitsExpense(costPerServiceNumberMainValue);
		String costPerServNumKPIFromDash = kpiTileMainValues.get(3).getText();
		
		System.out.println("KPI Tile Cost Per Service Number Actual: " + costPerServNumKPIFromDash + ",  KPI Tile Cost Per Service Number Expected: " + costPerServNumKPICalculated); 
//		Assert.assertEquals(costPerServNumKPIFromDash, costPerServNumKPICalculated);
		
		
		// Verify the secondary values on KPIs
		
		String totalExpenseDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(totalExpenseDirectlyAllocatedValue);
		String totalExpenseDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(0).getText();
		
		System.out.println("KPI Tile Total Expenses Directly Allocated Actual: " + totalExpenseDirectlyAllocatedKPIFromDash + ",  KPI Tile Total Expenses Directly Allocated Expected: " + totalExpenseDirectlyAllocatedKPICalculated); 
//		Assert.assertEquals(totalExpenseDirectlyAllocatedKPIFromDash, totalExpenseDirectlyAllocatedKPICalculated);
		
		String optimizableDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(optimizableExpenseDirectlyAllocatedValue);
		String optimizableDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(1).getText();
		
		System.out.println("KPI Tile Optimizable Expenses Directly Allocated Actual: " + optimizableDirectlyAllocatedKPIFromDash + ",  KPI Tile Optimizable Expenses Directly Allocated Expected: " + optimizableDirectlyAllocatedKPICalculated); 
//		Assert.assertEquals(optimizableDirectlyAllocatedKPIFromDash, optimizableDirectlyAllocatedKPICalculated);
		
		String roamingDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(roamingExpenseDirectlyAllocatedValue);
		String roamingDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(2).getText();
		
		System.out.println("KPI Tile Roaming Expenses Directly Allocated Actual: " + roamingDirectlyAllocatedKPIFromDash + ",  KPI Tile Roaming Expenses Directly Allocated Expected: " + roamingDirectlyAllocatedKPICalculated); 
//		Assert.assertEquals(roamingDirectlyAllocatedKPIFromDash, roamingDirectlyAllocatedKPICalculated);
		
		String amountOfServNumKPICalculated = UsageCalculationHelper.convertUnitsExpense(numberOfLinesValue);
		String amountOfServNumKPIFromDash = kpiTileDirectlyAllocatedValues.get(3).getText();
		
		System.out.println("KPI Tile Amount of Service Numbers Actual: " + amountOfServNumKPIFromDash + ",  KPI Tile Amount of Service Numbers Expected: " + amountOfServNumKPICalculated); 
//		Assert.assertEquals(amountOfServNumKPICalculated, amountOfServNumKPIFromDash);
		
		
	}

	
	
	
	public static void verifyThreeMonthRollingAverageAndTrendingValues(List<HierarchyTrendData> valuesForRollingAvg) {
	
		List<String> totalExpenseValues = new ArrayList<String>();
		List<String> optimizableExpenseValues = new ArrayList<String>();
		List<String> roamingExpenseValues = new ArrayList<String>();
		List<String> costPerServiceNumberValues = new ArrayList<String>();
		
		for (HierarchyTrendData monthValues: valuesForRollingAvg) {
			
			totalExpenseValues.add(monthValues.getTotalExpenseRollup());
			optimizableExpenseValues.add(monthValues.getOptimizableExpenseRollup());
			roamingExpenseValues.add(monthValues.getRoamingExpenseRollup());
			
			double totalExpense = Double.parseDouble(monthValues.getTotalExpenseRollup());
			double numLines = Double.parseDouble(monthValues.getNumberOfLinesRollup());
			double costServNumber = 0;
			
			if (numLines != 0){
				costServNumber = totalExpense / numLines;
			}
			
			costPerServiceNumberValues.add(Double.toString(costServNumber));
			
		}
		
		double rollingAvgTotalExpense = calculateRollingAverage(totalExpenseValues);
		double rollingAvgOptimizable = calculateRollingAverage(optimizableExpenseValues);
		double rollingAvgRoaming = calculateRollingAverage(roamingExpenseValues);
		double rollingAvgCostPerServiceNumber = calculateRollingAverage(costPerServiceNumberValues);
		
		
//		System.out.println("rollingAvgTotalExpense: " + rollingAvgTotalExpense);
//		System.out.println("rollingAvgOptimizable: " + rollingAvgOptimizable);
//		System.out.println("rollingAvgRoaming: " + rollingAvgRoaming);
//		System.out.println("rollingAvgCostPerServiceNumber: " + rollingAvgCostPerServiceNumber);
		
		
		
		for(int i = 1; i <= 4; i++){
			
			List<WebElement> trendingElementKpi = driver.findElements(By.xpath("(//*[@class='tdb-kpi__trend'])[" + i + "]/span"));
			
			WebElement threeMonthValue = null;
			boolean threeMonthDisplayed = true;
			
			try{
				threeMonthValue = driver.findElement(By.xpath("(//div[text()='3 months'])[" + i +"]/following-sibling::div"));
				//System.out.println("threeMonthDisplayed true");
			}catch(Exception e){
				// If 3 month rolling average is not displayed, set the variable to false
				//System.out.println("threeMonthDisplayed false");
				threeMonthDisplayed = false;
			}
			
			
			// If "3 months" rolling average exists, then the trending percentage can be calculated
			if(threeMonthDisplayed){
				
				// Get the 3 month value displayed on the KPI tile
				String threeMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(threeMonthValue.getText());
				String threeMonthAvgExpected = "";
				
				double rollingAverage = 0;
				double kpiValue = 0;
				
				switch(i) {
				
					case 1:  // Total Expense
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgTotalExpense);
						rollingAverage = rollingAvgTotalExpense;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getTotalExpenseRollup());
						break;
						
					case 2:  // Optimizable Expense
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgOptimizable);
						rollingAverage = rollingAvgOptimizable;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getOptimizableExpenseRollup());
						break;
						
					case 3:  // Roaming Expense
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgRoaming);
						rollingAverage = rollingAvgRoaming;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getRoamingExpenseRollup());
						break;
						
					case 4:  // Cost per Service Number
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgCostPerServiceNumber);
						rollingAverage = rollingAvgCostPerServiceNumber;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getRoamingExpenseRollup());
						break;
						
				}
				
				System.out.println("3 Month Avg Actual: " + threeMonthAvgActual + ", 3 Month Avg Expected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
//				Assert.assertEquals(threeMonthAvgActual, threeMonthAvgExpected);
				
				// If KPI value is different from the 3 month rolling average, then trending % will be different from 0%.
				// --> If trending % is 0%, it won't be displayed
				if(kpiValue != rollingAverage){
				  
					// System.out.println("kpiValue: "  + kpiValue);
					// System.out.println("rollingAverage: "  + rollingAverage);
					
					int trendValueExpected = calculateTrendingPercentage(kpiValue, rollingAverage);
					//int trendTemp = calculateTrendingPercentage(kpiValue, rollingAverage);
					//int trendCalculated = Math.abs(trendTemp);
					
					// System.out.println("Trend calculated: " + trendCalculated);
					
					
					if (getTrendingValueWithNoSymbol(trendingElementKpi.get(1).getText()) > 0){	// //if(trendValueExpected > 0){ <-- put back commented line
						
						String trend = trendingElementKpi.get(1).getText();
						int trendValueActual = getTrendingValueWithNoSymbol(trend); 
						
						Assert.assertTrue(trendingElementKpi.size() == 2);
//						Assert.assertEquals(trendValueActual, trendValueExpected);   
						Assert.assertTrue(trend.endsWith("%"));
																		
						System.out.println("Trend Value Actual: " + trendValueActual + "%, Trend Value Expected: " + trendValueExpected + "%");
 
					}
					
				}
				
			}
			
		}
	
	}

	
	
	public static void verifySixMonthRollingAverage(List<HierarchyTrendData> valuesForRollingAvg) {
		
		
		List<String> totalExpenseValues = new ArrayList<String>();
		List<String> optimizableExpenseValues = new ArrayList<String>();
		List<String> roamingExpenseValues = new ArrayList<String>();
		List<String> costPerServiceNumberValues = new ArrayList<String>();
		
		for (HierarchyTrendData monthValues: valuesForRollingAvg) {
			
			totalExpenseValues.add(monthValues.getTotalExpenseRollup());
			optimizableExpenseValues.add(monthValues.getOptimizableExpenseRollup());
			roamingExpenseValues.add(monthValues.getRoamingExpenseRollup());
			
			double totalExpense = Double.parseDouble(monthValues.getTotalExpenseRollup());
			double numLines = Double.parseDouble(monthValues.getNumberOfLinesRollup());
			double costServNumber = 0;
			
			if (numLines != 0){
				costServNumber = totalExpense / numLines;
			}
			
			costPerServiceNumberValues.add(Double.toString(costServNumber));
			
		}
		
		double rollingAvgTotalExpense = calculateRollingAverage(totalExpenseValues);
		double rollingAvgOptimizable = calculateRollingAverage(optimizableExpenseValues);
		double rollingAvgRoaming = calculateRollingAverage(roamingExpenseValues);
		double rollingAvgCostPerServiceNumber = calculateRollingAverage(costPerServiceNumberValues);
		
		
//		System.out.println("rollingAvgTotalExpense: " + rollingAvgTotalExpense);
//		System.out.println("rollingAvgOptimizable: " + rollingAvgOptimizable);
//		System.out.println("rollingAvgRoaming: " + rollingAvgRoaming);
//		System.out.println("rollingAvgCostPerServiceNumber: " + rollingAvgCostPerServiceNumber);
		
		
		for(int i = 1; i <= 4; i++){
			
			WebElement sixMonthValue = null;
			boolean sixMonthDisplayed = true;
			
			try{
				
				sixMonthValue = driver.findElement(By.xpath("(//div[text()='6 months'])[" + i +"]/following-sibling::div"));
				
			}catch(Exception e){
				// If 6 month rolling average is not displayed, set the variable to false
				//System.out.println("Six Month Displayed false");
				sixMonthDisplayed = false;
			}
			
			
			// If "6 months" rolling average exists
			if(sixMonthDisplayed){
							
				// Get the 6 months rolling average value displayed on the KPI tile
				String sixMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(sixMonthValue.getText());
				String sixMonthAvgExpected = "";
				
				switch(i){
				
					case 1: // Total Expense
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgTotalExpense);
						break;
				
					case 2: // Optimizable Expense
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgOptimizable);
						break;
					
					case 3: // Roaming Expense
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgRoaming);
						break;
					
					case 4: // Cost per Service Number
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgCostPerServiceNumber);
						break;
						
				}
				
				System.out.println("6 Month Avg Actual: " + sixMonthAvgActual + ", 6 Month Avg Expected: " + sixMonthAvgExpected);
				
				// Verifies that the '6 months rolling average' displayed equals to the '6 months rolling average' calculated
//				Assert.assertEquals(sixMonthAvgActual, sixMonthAvgExpected);
				
			}
			
		}
		
	}
	

	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2) / 3
	// 6 month rolling average = (KPI n + KPI n-1 + KPI n-2++ KPI n-3 + KPI n-4++ KPI n-5) / 6
	private static double calculateRollingAverage(List<String> values) {
		
		double sum = 0;
		
		for(String v: values) {
			sum += Double.parseDouble(v);
		}
		
		double average = sum / values.size();
		return average;
		
	}


	// Trend Percentage Change = ABS[(KPI- KPI3Mavg) / KPI3Mavg]
	public static int calculateTrendingPercentage(double kpiValue, double rollingAverage){
		
		return (int) Math.round((Math.abs((kpiValue - rollingAverage)/rollingAverage) * 100));
		//return (int) Math.round(((kpiValue - rollingAverage)/rollingAverage) * 100);
		
	}
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
		
	}


	

	
	
}
