package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsageActions;


public class TotalUsageByVendorActions extends BaseClass {
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		//login();
		//CommonTestStepActions.switchToContentFrame();
	}
	
	
	@Test
	public static void TotalUsageByVendorActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Select vendors from PoV - one by one - and verify that they are added to graphs (tooltip) and legend,
		// # for Voice, Data and Messages
		
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("August 2016");
				
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Domestic Usage"
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		TotalUsageActions.vendorsAddedToCharts(0, 0);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Data
		TotalUsageActions.vendorsAddedToCharts(0, 1);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Messages
		TotalUsageActions.vendorsAddedToCharts(0, 2);
			
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Roaming Usage"
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		TotalUsageActions.vendorsAddedToCharts(1, 0);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Data
		TotalUsageActions.vendorsAddedToCharts(1, 1);
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		// Messages
		TotalUsageActions.vendorsAddedToCharts(1, 2);
		
		
		// #3 Unselect vendors from PoV - one by one - and verify that they are removed from graphs (tooltip) and legend,
		// - for Voice, Data and Messages 			
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Total Usage by Vendor Actions test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
