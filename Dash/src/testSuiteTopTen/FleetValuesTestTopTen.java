package testSuiteTopTen;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTopTenValues;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetTopTenData;
import helperObjects.FleetTopTenHelper;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import topTen.FleetTopTenValues;


public class FleetValuesTestTopTen extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void FleetValuesTestTopTenTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(3);
		Thread.sleep(2000);
	
		// #2 Get the last month listed on month selector
		List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 

		String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
		String monthYearToSelect = "";
		
		int indexMonth = 0;
		
		do {

			monthYearToSelect = monthsInDropdown.get(indexMonth);
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #3 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			
			// Wait for chart to be loaded
			WaitForElementVisible(By.cssSelector("chart>div"), ExtremeTimeout);
 
			
			// #4 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the expected ones
			
			try {
				
				// Run test for "Expense" chart and category "All"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.expenseChart, FleetTopTenHelper.categoryExpenseAll);

				// Run test for "Expense" chart and category "Voice"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.expenseChart, FleetTopTenHelper.categoryExpenseVoice);
						
				// Run test for "Expense" chart and category "Data"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.expenseChart, FleetTopTenHelper.categoryExpenseData);
								
				// Run test for "Expense" chart and category "Messages"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.expenseChart, FleetTopTenHelper.categoryExpenseMessages);
								
				// Run test for "Expense" chart and category "Roaming"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.expenseChart, FleetTopTenHelper.categoryExpenseRoaming);
				
				// Run test for "Domestic Usage" chart and category "Voice"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.domesticUsageChart, FleetTopTenHelper.categoryDomesticUsageVoice);
						
				// Run test for "Domestic Usage" chart and category "Voice Overage"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.domesticUsageChart, FleetTopTenHelper.categoryDomesticUsageVoiceOverage);
								
				// Run test for "Domestic Usage" chart and category "Data"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.domesticUsageChart, FleetTopTenHelper.categoryDomesticUsageData);
								
				// Run test for "Domestic Usage" chart and category "Messages"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.domesticUsageChart, FleetTopTenHelper.categoryDomesticUsageMessages);
				
				// Run test for "Roaming Usage" chart and category "Voice"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.roamingUsageChart, FleetTopTenHelper.categoryRoamingUsageVoice);
												
				// Run test for "Roaming Usage" chart and category "Data"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.roamingUsageChart, FleetTopTenHelper.categoryRoamingUsageData);
								
				// Run test for "Roaming Usage" chart and category "Messages"
				FleetTopTenValues.verifyTopTenChartValues(FleetTopTenHelper.roamingUsageChart, FleetTopTenHelper.categoryRoamingUsageMessages);
				
				
			} catch(NullPointerException e) {
				
				System.out.println("chart not found");
				
			}
			
			indexMonth++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
			
		
			
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Fleet Top Ten values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
}
