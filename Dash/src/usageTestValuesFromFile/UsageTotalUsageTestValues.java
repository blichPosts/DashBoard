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
 		//CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		//CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void UsageTotalUsageTestValuesTest() throws Exception
	{

		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}
		
		//vendor = "AT&T Mobility";
		//vendor = "Etisalat";
		//vendor = "O2 UK";
		//String vendor = "Rogers";
		
		String path = UsageHelper.path;

		// Run the test for each vendor 
		for(String vendorSelected: vendorNames){
			
			String vendor = vendorSelected;
			String vendorFileName = UsageHelper.removePunctuationCharacters(vendor);  //(vendorSelected);
			
			String file = vendorFileName + ".txt";
			String completePath = path + file;
			
			System.out.println("Path: " + completePath);
			
			System.out.println("  **  RUNNING KPI TILE TEST FOR VENDOR: " + vendor + "  **");
			
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
			String monthYear = "";
						
			int indexMonth = 0;
			
			do {
			
				oneMonthData = valuesFromFile.get(indexMonth);   
				
				/*
				 * This has been modified on the source file. Month is the month listed on the file, it doesn't refer to the previous month.
				 * E.g.: “9/1/2016” now refers to September instead of August  
				 * if(oneMonthData[1].equals("1")){
					month = "12";
					year = Integer.toString(Integer.parseInt(oneMonthData[0]) - 1);
				}else{
					month = Integer.toString((Integer.parseInt(oneMonthData[1]) - 1));
					year =  oneMonthData[0];
				}*/
				
				// The following two lines replace the commented above. Will need to find out which version is the correct....
				month = oneMonthData.getOrdinalMonth();
				year =  oneMonthData.getOrdinalYear();
				
				monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
				System.out.println("Month Year: " + monthYear);
				
				// #4 Select month on month/year selector
				CommonTestStepActions.selectMonthYearPulldown(monthYear);
				
				Thread.sleep(2000);
				
				// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryVoice);
				Thread.sleep(2000);				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryData);
				Thread.sleep(2000);
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryData);
				Thread.sleep(2000);				
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, oneMonthData, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, oneMonthData, UsageHelper.categoryMessages);
				Thread.sleep(2000);
								
				indexMonth++;
				
			} while (!monthYear.equals(lastMonthListedMonthSelector));
						
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
