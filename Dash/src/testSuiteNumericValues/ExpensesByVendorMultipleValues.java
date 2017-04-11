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
import expenses.ExpenseTrendingMultipleValues;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelperMultipleVendors;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;

public class ExpensesByVendorMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void ExpensesByVendorMultipleValuesTest() throws Exception
	{

		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);
		
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}

		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		String path = ExpenseHelperMultipleVendors.path;
		int amountOfVendors = 12;

		if (amountOfVendors > vendorNames.size())
			amountOfVendors = vendorNames.size();
		
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		// #1 Select Vendor View and Unselect all vendors  
		CommonTestStepActions.SelectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<>();
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = new ArrayList<>();
		
		// Run the test for each vendor 
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			String vendorSelected = vendorNames.get(i);
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileTmp = ReadFilesHelper.getJsonDataExpenseUsage(vendor);  // ReadFilesHelper.getDataFromSpreadsheet(completePath);			
			listSelectedDataForMonthListUnified.add(valuesFromFileTmp);
				
			// #3 Select one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
		}
		
		
		CommonTestStepActions.initializeMonthSelector();
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
		
		
		// Some vendors might not have information for all last 13 months; in that case the value displayed on the Usage Trending chart is zero
		// So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
		for (List<UsageOneMonth> list: listSelectedDataForMonthListUnified) {
			
			List<UsageOneMonth> valuesFromFileOneVendor = UsageHelper.addMissingMonthsForVendor(list);
			listVendorsSelectedData.add(valuesFromFileOneVendor);
		
		}
		
		
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
					
		int indexMonth = 0;
	
		List<List<UsageOneMonth>> dataForExpenseTrending = new ArrayList<>();
		
		do {
		
			// listOneMonthData will have the data for a specific month, for all the vendors previously selected
			List<UsageOneMonth> listOneMonthData = new ArrayList<>();
			
			// values has the 13 months for one vendor
			for (List<UsageOneMonth> values: listVendorsSelectedData){
				
				int indexMonthForVendorSelected = 0;
				boolean dataFoundForMonth = false;
				
				while (indexMonthForVendorSelected < values.size() && !dataFoundForMonth){
					
					String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
					
					if (monthsToSelect.get(indexMonth).equals(monthYear)) {
 
						listOneMonthData.add(values.get(indexMonthForVendorSelected));
						dataFoundForMonth = true;
						
					}
						
					indexMonthForVendorSelected++;
				}
					
			}

			dataForExpenseTrending.add(listOneMonthData);
			
			indexMonth++;
			
		} while (indexMonth < monthsToSelect.size());
			
		
		// Vendors are listed in alphabetical order in the Usage Trending tooltips
			
		List<List<UsageOneMonth>> listAllMonthsSortedByVendor = new ArrayList<>();
		
		for (List<UsageOneMonth> list: dataForExpenseTrending) {
			
			listAllMonthsSortedByVendor.add(UsageHelper.sortVendorsAlphabetically(list));
		
		}
		
		
//		for (List<UsageOneMonth> list: listAllMonthsSortedByVendor) {
//			for (UsageOneMonth u: list) {
//				System.out.println(" Vendor: ** " + u.getVendorName() + " **, Month: " + u.getOrdinalMonth() + ", Year: " + u.getOrdinalYear() + ", Invoice Month: " + u.getInvoiceMonth());
//			}
//			System.out.println("");
//		}
		
		
		int indexMonthToSelect = 0;
		String monthYearToSelect = "";
		List<String> monthsWithDataToSelectPulldown = UsageHelper.getMonthListUnifiedForVendorsSelected(listSelectedDataForMonthListUnified);
		
		do {
					
			monthYearToSelect = monthsWithDataToSelectPulldown.get(indexMonthToSelect);  
			System.out.println(" ** Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);  // --> to give time to the data of the selected month to be displayed
			
			// #5 Verify that the values displayed on the tooltips of "Expense Trending" charts are the same as the ones read from file
			
			try {
				
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryAll);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryAll);
				Thread.sleep(2000);
				
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryVoice);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryVoice);
				Thread.sleep(2000);
				
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryData);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryData);
				Thread.sleep(2000);
				
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryMessages);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryMessages);
				Thread.sleep(2000);
				 
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryRoaming);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryRoaming);
				Thread.sleep(2000);
				
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryEquipment);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryEquipment);
				Thread.sleep(2000);
				 
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryTaxes);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryTaxes);
				Thread.sleep(2000);
				 
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryOther);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryOther);
				Thread.sleep(2000);
				 
				ExpenseHelperMultipleVendors.selectCategory(ExpenseHelperMultipleVendors.categoryAccount);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(ExpenseHelperMultipleVendors.expenseByVendorChart, listAllMonthsSortedByVendor, ExpenseHelperMultipleVendors.categoryAccount);
				Thread.sleep(2000);
				 
				
			} catch (NullPointerException e) {
				
				System.out.println("chart not found");
				
			}
			
			indexMonthToSelect++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonthToSelect < monthsToSelect.size());
			
	
	}
	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Expenses Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
