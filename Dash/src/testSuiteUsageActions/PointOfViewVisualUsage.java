package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import usage.PointOfViewActions;


public class PointOfViewVisualUsage extends BaseClass
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	
	@Test
	public static void PointOfViewVisualUsageTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		
		// #1 Select different months and verify that the month labels get updated according to selection. 
		
		PointOfViewActions.verifyMonthLabel();
		
		
		Thread.sleep(2000);
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Point of View (Usage) finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}


}
