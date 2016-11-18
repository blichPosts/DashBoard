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
		login();
		CommonTestStepActions.switchToContentFrame();
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
				
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
				
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Domestic Usage"
		// Voice
		
		TotalUsageActions.countriesAddedToCharts(0, 0);
		
		
		// Unselect all vendors - so previous test will be run for Roaming usage
/*		CommonTestStepActions.UnSelectAllVendors();
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Roaming Usage"
		// Voice
		TotalUsageActions.vendorsAddedToCharts(1, 0);
				

		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();

		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Domestic Usage"
		// Data
		TotalUsageActions.vendorsAddedToCharts(0, 1);
		
		
		// Unselect all vendors - so previous test will be run for Roaming usage
		CommonTestStepActions.UnSelectAllVendors();
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Roaming Usage"
		// Data
		TotalUsageActions.vendorsAddedToCharts(1, 1);

		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();

		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Domestic Usage"
		// Messages
		TotalUsageActions.vendorsAddedToCharts(0, 2);
		
		// Unselect all vendors - so previous test will be run for Roaming usage
		CommonTestStepActions.UnSelectAllVendors();
		
		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Roaming Usage"
		// Messages
		TotalUsageActions.vendorsAddedToCharts(1, 2);
		*/
		
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
