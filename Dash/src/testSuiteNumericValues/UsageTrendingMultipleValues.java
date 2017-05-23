package testSuiteNumericValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageTrendingValues;

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
		}

		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		String path = UsageHelper.path;
		int amountOfVendors = 10;

		if (amountOfVendors > vendorNames.size())
			amountOfVendors = vendorNames.size();
		
		System.out.println("Amount of Vendors Selected: " + amountOfVendors);
		
		
		// #1 Select Vendor View and Unselect all vendors  
		UsageHelper.selectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = new ArrayList<>();
		
		// Run the test for each vendor 
		for(int i = 0; i < amountOfVendors; i++){
			
			String vendor = vendorNames.get(i);
			String vendorSelected = vendorNames.get(i);
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
						
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFileTmp = ReadFilesHelper.getJsonDataExpenseUsage(vendor);		
			listSelectedDataForMonthListUnified.add(valuesFromFileTmp);
				
			// #3 Select one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
		}
		
		Thread.sleep(3000);
		
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
			
		List<List<UsageOneMonth>> dataForUsageTrending = UsageTrendingValues.getListWithData(listSelectedDataForMonthListUnified);
		
		int indexMonthToSelect = 0;
		String monthYearToSelect = "";
		
		// Add to a list the months that have data for at least one vendor. 
		// Months that have no data won't be selected on the month selector, since no data is displayed on the Dashboard.
		List<String> monthsWithDataToSelectPulldown = UsageHelper.getMonthListUnifiedForVendorsSelected(listSelectedDataForMonthListUnified);
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		
		do {
					
			monthYearToSelect = monthsWithDataToSelectPulldown.get(indexMonthToSelect);  
			System.out.println("Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			
			// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			
			try {
				
				// Domestic chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				// Roaming chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				new Actions(driver).moveToElement(driver.findElement(By.cssSelector(".tdb-pov__month"))).perform();
				
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
