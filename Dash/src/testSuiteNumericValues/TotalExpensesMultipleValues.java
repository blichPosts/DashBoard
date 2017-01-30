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
import expenses.TotalExpensesValues;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelperMultipleVendors;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class TotalExpensesMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		login();
		// MainLogin();
 		// CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		// CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void TotalExpensesMultipleValuesTest() throws Exception
	{
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		String path = ExpenseHelperMultipleVendors.path;
		int amountOfVendorsToSelect = 3;
		
		// ** The test will be run for only 2 vendors selected at a time, since it's hard to determine the exact coordinates for the different sections of the pie chart.
		// ** In many cases some of the sections of the pie chart are too small and the coordinates used to move the mouse to sometimes are not accurate.  

		if (amountOfVendorsToSelect > vendorNames.size())
			amountOfVendorsToSelect = vendorNames.size();
		
		System.out.println("amountOfVendorsToSelect: " + amountOfVendorsToSelect);
		
		// #1 Select Vendor View and Unselect all vendors  
		CommonTestStepActions.SelectVendorView();
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
		
		int offsetVendorsAmount = 0;
		int i = 0;
		int totalAmountVendors = 0;
		
		do {
		
			CommonTestStepActions.UnSelectAllVendors();
			i = offsetVendorsAmount;
			totalAmountVendors = amountOfVendorsToSelect + offsetVendorsAmount;
			
			// Run the test for each vendor selected
			for (i = offsetVendorsAmount; i < totalAmountVendors; i++) {
				
				String vendor = vendorNames.get(i);
				String vendorSelected = vendorNames.get(i);
				String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
				
				String file = vendorFileName + ".txt";
				String completePath = path + file;
				
				//System.out.println("Path: " + completePath);
				//System.out.println("  **  RUNNING TOTAL USAGE TEST FOR VENDOR: " + vendor + "  **");
				
				// #2 Read data from file
				List<UsageOneMonth> valuesFromFileOneVendor = ReadFilesHelper.getDataFromSpreadsheet(completePath);
				listVendorsSelectedData.add(valuesFromFileOneVendor);
					
				// #3 Select one vendor
				CommonTestStepActions.selectOneVendor(vendor);
				
			}
			
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			String monthYearToSelect = "";
						
			int indexMonth = 0;
		
			List<String> monthsToSelect = UsageHelper.getMonthListUnifiedForVendorsSelected(listVendorsSelectedData);
			
			do {
			
				// List listOneMonthData will have the data for a specific month, for all the vendors previously selected
				
				List<UsageOneMonth> listOneMonthData = new ArrayList<>();
				
				for (List<UsageOneMonth> values: listVendorsSelectedData) {
					
					int indexMonthForVendorSelected = 0;
					boolean dataFoundForMonth = false;
					
					while (indexMonthForVendorSelected < values.size() && !dataFoundForMonth) {
						
						String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
						
						if (monthsToSelect.get(indexMonth).equals(monthYear)) {
							listOneMonthData.add(values.get(indexMonthForVendorSelected));
							dataFoundForMonth = true;
							
						}
							
						indexMonthForVendorSelected++;
					}
						
					
				}
				
				// Vendors are listed in alphabetical order in the vertical axis
				List<UsageOneMonth> listOneMonthSortedByVendor = UsageHelper.sortVendorsAlphabetically(listOneMonthData);
				
				
				// #4 Select month on month/year selector
				CommonTestStepActions.selectMonthYearPulldown(monthsToSelect.get(indexMonth));
				System.out.println("monthYear: " + monthsToSelect.get(indexMonth)); 
				
				Thread.sleep(2000);
				
				// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
				
				TotalExpensesValues.verifyTotalExpensesPieChartTooltip(ExpenseHelperMultipleVendors.expenseTotalExpensePieChart, listOneMonthSortedByVendor);
				Thread.sleep(1000);
				
				TotalExpensesValues.verifyTotalExpensesBarChartTooltip(ExpenseHelperMultipleVendors.expenseTotalExpenseBarChart, listOneMonthSortedByVendor);
				Thread.sleep(1000);
				
				indexMonth++;

				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonth < monthsToSelect.size());
			
			Thread.sleep(2000);		
			
			offsetVendorsAmount += 2;
			
		} while (totalAmountVendors <= vendorNames.size() && offsetVendorsAmount < (totalAmountVendors-1));
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Total Expenses values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
