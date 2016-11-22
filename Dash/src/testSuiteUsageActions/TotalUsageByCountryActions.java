package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsageActions;

public class TotalUsageByCountryActions extends BaseClass {
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		//login();
		//CommonTestStepActions.switchToContentFrame();
	}
	
	
	@Test
	public static void TotalUsageByCountryActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
			
		// #1 Select Vendor View 
		UsageHelper.selectCountryView();
		
		
		// #2 Select vendors from PoV - one by one - and verify that the corresponding countries are added to graphs (tooltip) and legend,
		// # for Voice, Data and Messages
		
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("August 2016");
				
		
		// Verify that countries selected are added to the Total Usage by Country chart - "Domestic Usage"
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		TotalUsageActions.countriesAddedToCharts(0, 0);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Data
		TotalUsageActions.countriesAddedToCharts(0, 1);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Messages
		TotalUsageActions.countriesAddedToCharts(0, 2);
			
		
		// Verify that countries selected are added to the Total Usage by Country chart - "Roaming Usage"
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		TotalUsageActions.countriesAddedToCharts(1, 0);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Data
		TotalUsageActions.countriesAddedToCharts(1, 1);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Messages
		TotalUsageActions.countriesAddedToCharts(1, 2);
				
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Total Usage by Country Actions test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
