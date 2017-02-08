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
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageTrending;

public class UsageTrendingMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void UsageTrendingMultipleValuesTest() throws Exception
	{

		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}

		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		String path = UsageHelper.path;
		int amountOfVendors = 12;

		if (amountOfVendors > vendorNames.size())
			amountOfVendors = vendorNames.size();
		
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		
		// #1 Select Vendor View and Unselect all vendors  
		UsageHelper.selectVendorView();
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
			List<UsageOneMonth> valuesFromFileTmp = ReadFilesHelper.getDataFromSpreadsheet(completePath);			
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
	
		List<List<UsageOneMonth>> dataForUsageTrending = new ArrayList<>();
		
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

			dataForUsageTrending.add(listOneMonthData);
			
			indexMonth++;
			
		} while (indexMonth < monthsToSelect.size());
			
		
		
		int indexMonthToSelect = 0;
		String monthYearToSelect = "";
		
		// Add to a list the months that have data for at least one vendor. 
		// Months that have no data won't be selected on the month selector, since no data is displayed on the Dashboard.
		List<String> monthsWithDataToSelectPulldown = UsageHelper.getMonthListUnifiedForVendorsSelected(listSelectedDataForMonthListUnified);
		
		do {
					
			monthYearToSelect = monthsWithDataToSelectPulldown.get(indexMonthToSelect);  
			System.out.println("Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			
			// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			
			try {
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);				
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);				
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				
			} catch(NullPointerException e) {
				
				System.out.println("chart not found");
				
			}
			
			indexMonthToSelect++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonthToSelect < monthsToSelect.size());
		
			
	}
	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Usage Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
