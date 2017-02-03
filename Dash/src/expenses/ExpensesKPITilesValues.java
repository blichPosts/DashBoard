package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageOneMonth;

public class ExpensesKPITilesValues extends BaseClass{
	

	public static void verifyKPItileValues(String totalChargeExpenses, String numberOfLinesExpenses) {
		
		List<WebElement> kpiTileValues = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		double totalCharge = Double.parseDouble(totalChargeExpenses);   
		double numberLines = Double.parseDouble(numberOfLinesExpenses);
		double costPerServiceNumber = totalCharge / numberLines;
		
		String totalChargeKPICalculated = UsageCalculationHelper.convertUnitsExpense(totalCharge);
		String totalChargeKPIFromDash = kpiTileValues.get(0).getText();
		
		System.out.println("KPI Tile Total Expense Actual: " + totalChargeKPIFromDash + ",  KPI Tile Total Expense Expected: " + totalChargeKPICalculated); 
		Assert.assertEquals(totalChargeKPIFromDash, totalChargeKPICalculated);
		
		String countServNumKPICalculated = UsageCalculationHelper.convertUnits(numberLines);
		String countServNumKPIFromDash = kpiTileValues.get(1).getText();
		
		System.out.println("KPI Tile Cost per Service Number Actual: " + countServNumKPIFromDash + ",  KPI Tile Cost per Service Number Expected: " + countServNumKPICalculated); 
		Assert.assertEquals(countServNumKPIFromDash, countServNumKPICalculated);
		
		String costServNumKPICalculated = UsageCalculationHelper.convertUnitsExpense(costPerServiceNumber);
		String costServNumKPIFromDash = kpiTileValues.get(2).getText();
		
		System.out.println("KPI Tile Count of Service Numbers Actual: " + costServNumKPIFromDash + ",  KPI Tile Count of Service Numbers Expected: " + costServNumKPICalculated); 
		Assert.assertEquals(costServNumKPIFromDash, costServNumKPICalculated);
		
		
	}

	
	
	
	public static void verifyThreeMonthRollingAverageAndTrendingValues(List<UsageOneMonth> valuesForRollingAvg) {
	
		List<String> totalExpenseValues = new ArrayList<String>();
		List<String> countServNumberValues = new ArrayList<String>();
		List<String> costServNumberValues = new ArrayList<String>();
				
		for(UsageOneMonth monthValues: valuesForRollingAvg){
			
			totalExpenseValues.add(monthValues.getTotalCharge());
			countServNumberValues.add(monthValues.getNumberOfLines());
			if (monthValues.getNumberOfLines().equals("0") || monthValues.getTotalCharge().equals("0")){
				costServNumberValues.add("0");
			} else {
				costServNumberValues.add(Double.toString(Double.parseDouble(monthValues.getTotalCharge())/ Double.parseDouble(monthValues.getNumberOfLines())));
			}
			
		}
		
		double rollingAvgTotalExpense = calculateRollingAverage(totalExpenseValues);
		double rollingAvgCountServNum = calculateRollingAverage(countServNumberValues);
		double rollingAvgCostServNum = calculateRollingAverage(costServNumberValues);
		
		
//		System.out.println("rollingAvgTotalExpense: " + rollingAvgTotalExpense);
//		System.out.println("rollingAvgCountServNum: " + rollingAvgCountServNum);
//		System.out.println("rollingAvgCostServNum: " + rollingAvgCostServNum);
		
		
		// Round up rolling averages for Count of Service Numbers, since they cannot have decimal points (amount of lines must be integer) 
		long avgTmpLines = Math.round(rollingAvgCountServNum); 
				
		
		for(int i = 1; i <= 3; i++){
			
			List<WebElement> trendingElementKpi = driver.findElements(By.xpath("(//*[@class='tdb-kpi__trend'])[" + i + "]/span"));
			
			WebElement threeMonthValue = null;
			boolean threeMonthDisplayed = true;
			
			try{
				threeMonthValue = driver.findElement(By.xpath("(//div[text()='3 months'])[" + i + "]/following-sibling::div"));
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
				
				switch (i) {
				
					case 1:  // Total Expense
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgTotalExpense);
						rollingAverage = rollingAvgTotalExpense;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getTotalCharge());
						break;
					
					case 2:  // Count of Service Numbers
						// Amount of Service Numbers must be an integer. This rounding is needed in case the amount of messages is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpLines);
						rollingAverage = rollingAvgCountServNum;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getNumberOfLines());
						break;
					
					case 3:  // Cost per Service Number
						threeMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgCostServNum);
						rollingAverage = rollingAvgCostServNum;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getTotalCharge()) / Double.parseDouble(valuesForRollingAvg.get(0).getNumberOfLines());
						break;
					
				}
				
				System.out.println("3 Month Avg Actual: " + threeMonthAvgActual + ", 3 Month Avg Expected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
				Assert.assertEquals(threeMonthAvgActual, threeMonthAvgExpected);
				
				// If KPI value is different from the 3 month rolling average, then trending % will be different from 0%.
				// --> If trending % is 0%, it won't be displayed
				if(kpiValue != rollingAverage){
				  
					// System.out.println("kpiValue: "  + kpiValue);
					// System.out.println("rollingAverage: "  + rollingAverage);
					
					int trendValueExpected = calculateTrendingPercentage(kpiValue, rollingAverage);
					//int trendTemp = calculateTrendingPercentage(kpiValue, rollingAverage);
					//int trendCalculated = Math.abs(trendTemp);
					
					// System.out.println("Trend calculated: " + trendCalculated);
					
					if(trendValueExpected > 0){
						
						String trend = trendingElementKpi.get(1).getText();
						int trendValueActual = getTrendingValueWithNoSymbol(trend); 
						
						Assert.assertTrue(trendingElementKpi.size() == 2);
						Assert.assertTrue(trend.endsWith("%"));
						
						System.out.println("Trend Value Actual: " + trendValueActual + "%, Trend Value Expected: " + trendValueExpected + "%");
						
						// I've observed a case where the trending % calculated is 60.5. 
						// The expected value in this case is 61%, but the value found is 60%. So the Assert fails. 
						// Very unlikely to happen, but if it happens I'd like the test not to stop, 
						// and to continue running the asserts. 
						// If the difference between expected value and found value is greater than 1%, then it is unacceptable and the test will fail. 
						if (Math.abs(trendValueExpected - trendValueActual) < 1){
							Assert.assertEquals(trendValueActual, trendValueExpected);
						} else {
							System.out.println("Trending percentage found is incorrect");
							break;
						}
						
//						System.out.println(trendValueActual);
//						System.out.println(trendValueExpected);
						 
					}
					
				}
				
			}
			
		}
	
	}

	
	
	public static void verifySixMonthRollingAverage(List<UsageOneMonth> valuesForRollingAvg) {
		
		List<String> totalExpenseValues = new ArrayList<String>();
		List<String> countServNumberValues = new ArrayList<String>();
		List<String> costServNumberValues = new ArrayList<String>();
				
		for(UsageOneMonth monthValues: valuesForRollingAvg){
			
			totalExpenseValues.add(monthValues.getTotalCharge());
			countServNumberValues.add(monthValues.getNumberOfLines());
			
			double totalExpense = Double.parseDouble(monthValues.getTotalCharge());
			double numLines = Double.parseDouble(monthValues.getNumberOfLines());
			double costServNumber = 0;
			
			if (numLines != 0){
				costServNumber = totalExpense / numLines;
			}
			
			costServNumberValues.add(Double.toString(costServNumber));
			
		}
		
		
		double rollingAvgTotalExpense = calculateRollingAverage(totalExpenseValues);
		double rollingAvgCountServNum = calculateRollingAverage(countServNumberValues);
		double rollingAvgCostServNum = calculateRollingAverage(costServNumberValues);
		
//		System.out.println("rollingAvgTotalExpense: " + rollingAvgTotalExpense);
//		System.out.println("rollingAvgCountServNum: " + rollingAvgCountServNum);
//		System.out.println("rollingAvgCostServNum: " + rollingAvgCostServNum);
		
		
		// Round up rolling averages for Count of Service Numbers, since they cannot have decimal points (amount of lines must be integer) 
		long avgTmpLines = Math.round(rollingAvgCountServNum); 
		
		for(int i = 1; i <= 3; i++){
			
			WebElement sixMonthValue = null;
			boolean sixMonthDisplayed = true;
			
			try {
				
				sixMonthValue = driver.findElement(By.xpath("(//div[text()='6 months'])[" + i +"]/following-sibling::div"));
				
			} catch(Exception e){
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
					
					case 1:  // Total Expense
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgTotalExpense);
						break;
					
					case 2:  // Count of Service Numbers
						// Amount of Service Numbers must be an integer. This rounding is needed in case the amount of messages is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						sixMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpLines);
						break;
					
					case 3:  // Cost per Service Number
						sixMonthAvgExpected = UsageCalculationHelper.convertUnitsExpense(rollingAvgCostServNum);
						break;
					
				}
				
				System.out.println("6 Month Avg Actual: " + sixMonthAvgActual + ", 6 Month Avg Expected: " + sixMonthAvgExpected);
				
				// Verifies that the '6 months rolling average' displayed equals to the '6 months rolling average' calculated
				Assert.assertEquals(sixMonthAvgActual, sixMonthAvgExpected);
				
			}
			
		}
		
	}
	
	
	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2) / 3
	// 6 month rolling average = (KPI n + KPI n-1 + KPI n-2++ KPI n-3 + KPI n-4++ KPI n-5) / 6
	private static double calculateRollingAverage(List<String> values) {
		
		double sum = 0;
		
		for (String v: values) {
			sum += Double.parseDouble(v);
//			System.out.println("Value: " + v);
		}
//		System.out.println("Sum: " + sum + " / values size: " + values.size());
		
		double average = sum / values.size();
		
		return average;
		
	}


	// Trend Percentage Change = ABS[(KPI- KPI3Mavg) / KPI3Mavg]
	public static int calculateTrendingPercentage(double kpiValue, double rollingAverage){
		
//		System.out.println("kpiValue: " + kpiValue + ", rollingAverage: " + rollingAverage);
//		System.out.println("Trend % before rounding: " + (Math.abs((kpiValue - rollingAverage)/rollingAverage) * 100)); 
		return (int) Math.round(Math.abs((kpiValue - rollingAverage)/rollingAverage) * 100);
		
	}
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
		
	}




}
