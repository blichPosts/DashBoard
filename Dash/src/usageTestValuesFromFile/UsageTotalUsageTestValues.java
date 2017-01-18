package usageTestValuesFromFile;

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
import usage.TotalUsageActions;


public class UsageTotalUsageTestValues extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
 		// CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		// CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void UsageTotalUsageTestValuesTest() throws Exception
	{

		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
		
		//String vendor = "Telstra Australia";
		//String vendor = "Vivo Brazil";
		//String vendor = "Rogers";
		//String vendor = "Telcel Mexico";
		//String vendor = "SingTel Singapore";
		//String vendor = "Etisalat";
		//String vendor = "O2 UK";
		//String vendor = "Vodafone United Kingdom";
		//String vendor = "AT&T Mobility";
		//String vendor = "Tangoe, Inc.";
		//String vendor = "Verizon Wireless";
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		for(String vendorSelected: vendorNames){
			
			String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendor);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			System.out.println("Path: " + completePath);
			
			System.out.println("  **  RUNNING TOTAL USAGE TEST FOR VENDOR: " + vendor + "  **");
			
			CommonTestStepActions.GoToUsagePageDetailedWait();
				
			// #1 Select Vendor View 
			UsageHelper.selectVendorView();
			
			
			// #2 Read data from file
			List<UsageOneMonth> valuesFromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
			
				
			// #3 Select only one vendor
			CommonTestStepActions.UnSelectAllVendors();
			CommonTestStepActions.selectOneVendor(vendor);
			
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			
			UsageOneMonth oneMonthData;
			String year =  "";
			String month = "";
			String monthYearToSelect = "";
						
			int indexMonth = 0;
			
			do {
			
				oneMonthData = valuesFromFile.get(indexMonth);   
				
				String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
				month = monthYear[0];
				year = monthYear[1];
				
				monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
				System.out.println("Month Year: " + monthYearToSelect);
				
				// #4 Select month on month/year selector
				CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
				
				Thread.sleep(2000);
				
				// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryVoice);
				Thread.sleep(1000);
				/*
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryVoice);
				Thread.sleep(1000);				
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryData);
				Thread.sleep(1000);
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryData);
				Thread.sleep(1000);				
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryMessages);
				Thread.sleep(1000);
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryMessages);
				Thread.sleep(1000);
				*/				
				indexMonth++;
				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
						
		}
	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Total Usage values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
