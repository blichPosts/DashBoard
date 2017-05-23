package testSuiteNumericValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.ExpensesKPITilesValues;
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;

public class ExpensesKPITilesTestValuesOneVendor extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void ExpensesKPITilesTestValuesOneVendorTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);

		// Wait for countries and vendors to be loaded on PoV section
		WaitForElementPresent(By.cssSelector(".tdb-povGroup>.tdb-povGroup"), ExtremeTimeout);
		
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
			System.out.println(vendor.getText());
		}
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		for(String vendorSelected: vendorNames){
			
			String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
					
			CommonTestStepActions.GoToExpensePageDetailedWait();
				
			// #1 Read data from file
			List<UsageOneMonth> valuesFromFile = ReadFilesHelper.getJsonDataExpenseUsage(vendor);
							
			// #2 Select only one vendor
			CommonTestStepActions.UnSelectAllVendors();
			CommonTestStepActions.selectOneVendor(vendor);
			
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			
			UsageOneMonth oneMonthData;
			String year =  "";
			String month = "";
			String monthYearToSelect = "";
			String totalChargeExpenses; 
			String numberOfLinesExpenses;
			
			int indexMonth = 0;
			
			do {
			
				oneMonthData = valuesFromFile.get(indexMonth);   
								
				String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
				month = monthYear[0];
				year = monthYear[1];
								
				boolean monthYearNull = false; 
				
				try {
					
					if (!(month.equals(null) && year.equals(null))) {

						monthYearNull = false;

					}
							
				} catch (NullPointerException e) {
					
					monthYearNull = true;

				}
					
				if (!monthYearNull) {
				
					monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
					System.out.println("Month Year: " + monthYearToSelect);
					
					// #3 Select month on month/year selector
					CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
					
					Thread.sleep(2000);
					
					totalChargeExpenses = oneMonthData.getTotalCharge();
					numberOfLinesExpenses = oneMonthData.getNumberOfLines();
									
					// #4 Compare the values displayed on the KPIs to the values from spreadsheet
					ExpensesKPITilesValues.verifyKPItileValues(totalChargeExpenses, numberOfLinesExpenses);
										
					// #5 Calculate 3 Month Rolling Averages and Trending Percentage
					// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
					// ONLY if there's data for two months before the current month. 
					// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
					if(indexMonth < valuesFromFile.size()-2){
						
						List<UsageOneMonth> valuesForTrendingValue = new ArrayList<UsageOneMonth>();
						
						// Adds the current month values to the list
						valuesForTrendingValue.add(valuesFromFile.get(indexMonth));
						// Adds the previous month values to the list
						valuesForTrendingValue.add(valuesFromFile.get(indexMonth+1));
						// Adds values from 2 months ago to the list
						valuesForTrendingValue.add(valuesFromFile.get(indexMonth+2));
					 						
						ExpensesKPITilesValues.verifyThreeMonthRollingAverageAndTrendingValues(valuesForTrendingValue);
						
					}
					
					
					// #6 Calculate 6 Month Rolling Averages
					// Get the values needed to calculate the 6 month Rolling Averages
					// ONLY if there's data for five months before the current month. 
					// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
					if(indexMonth < valuesFromFile.size()-5){
						
						List<UsageOneMonth> valuesForSixMonthAverage = new ArrayList<UsageOneMonth>();
						
						// Adds the current month values to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth));
						// Adds the previous month values to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+1));
						// Adds values from 2 months ago to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+2));
						// Adds values from 3 months ago to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+3));
						// Adds values from 4 months ago to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+4));
						// Adds values from 5 months ago to the list
						valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+5));
						
						ExpensesKPITilesValues.verifySixMonthRollingAverage(valuesForSixMonthAverage);
						
					}
					
				}
				
				indexMonth++;
				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
			
			Thread.sleep(2000);
			
		}
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Expenses KPI tiles values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	

}
