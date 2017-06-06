package expenseHierarchy;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.UsageCalculationHelper;


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
		
		
		// Verify the MAIN values on KPIs
		
		// Total Expense
		String totalExpenseKPICalculated = UsageCalculationHelper.convertUnitsExpense(totalExpenseMainValue);
		String totalExpenseKPIFromDash = kpiTileMainValues.get(0).getText();
		
		ShowText("KPI Tile Total Expense Actual: " + totalExpenseKPIFromDash + ",  KPI Tile Total Expense Expected: " + totalExpenseKPICalculated);	
		
		// Optimizable Expense
		String optimizableKPICalculated = UsageCalculationHelper.convertUnitsExpense(optimizableExpenseMainValue);
		String optimizableKPIFromDash = kpiTileMainValues.get(1).getText();
		
		ShowText("KPI Tile Optimizable Actual: " + optimizableKPIFromDash + ",  KPI Tile Optimizable Expected: " + optimizableKPICalculated);		
		
		// Roaming Expense
		String roamingKPICalculated = UsageCalculationHelper.convertUnitsExpense(roamingExpenseMainValue);
		String roamingKPIFromDash = kpiTileMainValues.get(2).getText();
		
		ShowText("KPI Tile Roaming Actual: " + roamingKPIFromDash + ",  KPI Tile Roaming Expected: " + roamingKPICalculated);
		
		// Cost per Service Number
		String costPerServNumKPICalculated = UsageCalculationHelper.convertUnitsExpense(costPerServiceNumberMainValue);
		String costPerServNumKPIFromDash = kpiTileMainValues.get(3).getText();
		
		ShowText("KPI Tile Cost Per Service Number Actual: " + costPerServNumKPIFromDash + ",  KPI Tile Cost Per Service Number Expected: " + costPerServNumKPICalculated); 
		
		GeneralHelper.verifyExpectedAndActualValues(totalExpenseKPIFromDash, totalExpenseKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(optimizableKPIFromDash, optimizableKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(roamingKPIFromDash, roamingKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(costPerServNumKPIFromDash, costPerServNumKPICalculated);
		
		
		// Verify the secondary values on KPIs
		
		String directlyAllocated = " directly allocated";
		String serviceNumbers = " service numbers";
		
		// Total Expense directly allocated
		String totalExpenseDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(totalExpenseDirectlyAllocatedValue) + directlyAllocated;
		String totalExpenseDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(0).getText();
		
		ShowText("KPI Tile Total Expenses Directly Allocated Actual: " + totalExpenseDirectlyAllocatedKPIFromDash + ",  KPI Tile Total Expenses Directly Allocated Expected: " + totalExpenseDirectlyAllocatedKPICalculated);
		
		// Optimizable Expense directly allocated
		String optimizableDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(optimizableExpenseDirectlyAllocatedValue) + directlyAllocated;
		String optimizableDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(1).getText();
		
		ShowText("KPI Tile Optimizable Expenses Directly Allocated Actual: " + optimizableDirectlyAllocatedKPIFromDash + ",  KPI Tile Optimizable Expenses Directly Allocated Expected: " + optimizableDirectlyAllocatedKPICalculated);
		
		// Roaming Expense directly allocated
		String roamingDirectlyAllocatedKPICalculated = UsageCalculationHelper.convertUnitsExpense(roamingExpenseDirectlyAllocatedValue) + directlyAllocated;
		String roamingDirectlyAllocatedKPIFromDash = kpiTileDirectlyAllocatedValues.get(2).getText();
		
		ShowText("KPI Tile Roaming Expenses Directly Allocated Actual: " + roamingDirectlyAllocatedKPIFromDash + ",  KPI Tile Roaming Expenses Directly Allocated Expected: " + roamingDirectlyAllocatedKPICalculated);
		
		// Amount of Service Numbers
		String amountOfServNumKPICalculated = UsageCalculationHelper.convertUnits(numberOfLinesValue) + serviceNumbers;
		String amountOfServNumKPIFromDash = kpiTileDirectlyAllocatedValues.get(3).getText();
		
		ShowText("KPI Tile Amount of Service Numbers Actual: " + amountOfServNumKPIFromDash + ",  KPI Tile Amount of Service Numbers Expected: " + amountOfServNumKPICalculated);
		
		GeneralHelper.verifyExpectedAndActualValues(totalExpenseDirectlyAllocatedKPIFromDash, totalExpenseDirectlyAllocatedKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(optimizableDirectlyAllocatedKPIFromDash, optimizableDirectlyAllocatedKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(roamingDirectlyAllocatedKPIFromDash, roamingDirectlyAllocatedKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(amountOfServNumKPICalculated, amountOfServNumKPIFromDash);

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
		
				
		for(int i = 1; i <= 4; i++){
			
			List<WebElement> trendingElementKpi = driver.findElements(By.xpath("(//*[@class='tdb-kpi__trend'])[" + i + "]/span"));
			
			WebElement threeMonthValue = null;
			boolean threeMonthDisplayed = true;
			
			try{
				threeMonthValue = driver.findElement(By.xpath("(//div[text()='3 months'])[" + i +"]/following-sibling::div"));
				// ShowText("threeMonthDisplayed true");
			}catch(Exception e){
				// If 3 month rolling average is not displayed, set the variable to false
				// ShowText("threeMonthDisplayed false");
				threeMonthDisplayed = false;
			}
			
			
			// If "3 months" rolling average exists, then the trending percentage can be calculated
			if(threeMonthDisplayed){
				
				// Get the 3 month value displayed on the KPI tile
				String threeMonthAvgActual = threeMonthValue.getText();   // UsageCalculationHelper.getNumericValueWithPrefixes(threeMonthValue.getText());
				String threeMonthAvgExpected = "";
				
				double rollingAverage = 0;
				double kpiValue = 0;
				
				switch (i) {
				
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
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getTotalExpenseRollup()) / Long.parseLong(valuesForRollingAvg.get(0).getNumberOfLinesRollup());
						break;
						
				}
				
				ShowText("3 Month Avg Actual: " + threeMonthAvgActual + ", 3 Month Avg Expected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
				GeneralHelper.verifyExpectedAndActualValues(threeMonthAvgActual, threeMonthAvgExpected);
				
				// If KPI value is different from the 3 month rolling average, then trending % will be different from 0%.
				// --> If trending % is 0%, it won't be displayed
				if(kpiValue != rollingAverage){
				  
					// ShowText("kpiValue: "  + kpiValue);
					// ShowText("rollingAverage: "  + rollingAverage);
					
					int trendValueExpected = calculateTrendingPercentage(kpiValue, rollingAverage);
					
					if (trendValueExpected > 0) {
						
						String trend = trendingElementKpi.get(1).getText();
						int trendValueActual = getTrendingValueWithNoSymbol(trend); 
						
						Assert.assertTrue(trendingElementKpi.size() == 2);
						GeneralHelper.verifyExpectedAndActualValues(trendValueActual, trendValueExpected);
						Assert.assertTrue(trend.endsWith("%"));
																		
						ShowText("Trend Value Actual: " + trendValueActual + "%, Trend Value Expected: " + trendValueExpected + "%");
 
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
		
		
		for(int i = 1; i <= 4; i++){
			
			WebElement sixMonthValue = null;
			boolean sixMonthDisplayed = true;
			
			try{
				
				sixMonthValue = driver.findElement(By.xpath("(//div[text()='6 months'])[" + i +"]/following-sibling::div"));
				
			}catch(Exception e){
				// If 6 month rolling average is not displayed, set the variable to false
				// ShowText("Six Month Displayed false");
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
				
				 ShowText("6 Month Avg Actual: " + sixMonthAvgActual + ", 6 Month Avg Expected: " + sixMonthAvgExpected);
				
				// Verifies that the '6 months rolling average' displayed equals to the '6 months rolling average' calculated
				GeneralHelper.verifyExpectedAndActualValues(sixMonthAvgActual, sixMonthAvgExpected);
				
			}
			
		}
		
	}
	

	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2) / 3
	// 6 month rolling average = (KPI n + KPI n-1 + KPI n-2 + KPI n-3 + KPI n-4 + KPI n-5) / 6
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
		
	}
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
		
	}

	
	public static void verifyServiceNumbersAmount(String serviceNumbersAmount) {
		 
		String servNumFooterFound = driver.findElements(By.cssSelector("span.tdb-kpi__footerItem--value")).get(0).getText();
		String servNumFooterExpected = UsageCalculationHelper.convertUnits(Double.parseDouble(serviceNumbersAmount));
	
		GeneralHelper.verifyExpectedAndActualValues(servNumFooterFound, servNumFooterExpected);
		
	}
	
	
	public static void verifyNumberInvoices(String numberInvoices) {
		 
		String numInvoicesFooterFound = driver.findElements(By.cssSelector("span.tdb-kpi__footerItem--value")).get(1).getText();
		String numInvoicesFooterExpected = UsageCalculationHelper.convertUnits(Double.parseDouble(numberInvoices));
		
		GeneralHelper.verifyExpectedAndActualValues(numInvoicesFooterFound, numInvoicesFooterExpected);
		
	}
	
	
}
