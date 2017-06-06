package helperObjects;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class FleetHelper extends BaseClass {

	public final static int totalExpenseSection = 0;
	public final static int expenseTrendingSection = 1;
	
	public final static int expenseTotalExpensePieChart = 0;
	public final static int expenseTotalExpenseBarChart = 1;
	public final static int expenseByVendorChart = 2;
	public final static int costPerServiceNumberChart  = 3;
	public final static int countServiceNumbersChart = 4;
	
	public final static int expenseCategoryAll = 1;
	public final static int expenseCategoryVoice = 2;
	public final static int expenseCategoryData = 3;
	public final static int expenseCategoryMessages = 4;
	public final static int expenseCategoryRoaming = 5;
	public final static int expenseCategoryEquipment = 6;
	public final static int expenseCategoryTaxes = 7;
	public final static int expenseCategoryOther = 8;
	public final static int expenseCategoryAccount = 9;

	public final static int usageCategoryVoice = 1;
	public final static int usageCategoryData = 2;
	public final static int usageCategoryMessages = 3;
	
	
	public static void selectCategory(int category){
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}
	
	
	public static List<List<UsageOneMonth>> getExpenseUsageDataForTest() throws Exception {
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		ShowText("vendors.size(): " + vendors.size());
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
			ShowText(vendor.getText());
		}

			
		int amountOfVendors = 10; //GeneralHelper.getAmountOfVendorsToSelect(vendors.size()); // If we don't want the prompt to show up, just replace the assignment with a numeric value. 

		if (amountOfVendors > vendorNames.size())
			amountOfVendors = vendorNames.size();
		 
						
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = new ArrayList<List<UsageOneMonth>>(); 
		
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			
			// Read data from the ajax call response
			List<UsageOneMonth> valuesFromAjaxCall = ReadFilesHelper.getJsonDataExpenseUsage(vendor);			
			listSelectedDataForMonthListUnified.add(valuesFromAjaxCall);
				
			// Select one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
		}
		
		return listSelectedDataForMonthListUnified;
		
	}


	
	public static List<List<UsageOneMonth>> getExpenseUsageDataAllMonths(List<List<UsageOneMonth>> listSelectedDataForMonthListUnified) throws ParseException {
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<List<UsageOneMonth>>();
		
		for (List<UsageOneMonth> list: listSelectedDataForMonthListUnified) {
			
			List<UsageOneMonth> valuesFromFileOneVendor = UsageHelper.addMissingMonthsForVendor(list);
			listVendorsSelectedData.add(valuesFromFileOneVendor);
		
		}
		
		return listVendorsSelectedData;
		
	}


	public static List<List<UsageOneMonth>> getListsWithDataPerMonth(List<List<UsageOneMonth>> listVendorsSelectedData) throws ParseException {
		
		List<List<UsageOneMonth>> dataForExpenseTrending = new ArrayList<>();
		
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
		int indexMonth = 0;
		
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
		
		return dataForExpenseTrending;
		
	}


	
	public static void waitForPoVSectionToBeLoaded() throws Exception {
		
		WaitForElementPresent(By.cssSelector(".tdb-povGroup>.tdb-povGroup"), ExtremeTimeout);
		
	}
	
	
}
