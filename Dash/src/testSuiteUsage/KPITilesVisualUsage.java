package testSuiteUsage;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;



public class KPITilesVisualUsage extends BaseClass
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	
	@Test
	public static void KpiTilesVisualUsageTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		// #1 Observe the labels above the KPI components - There are two sections, 'Domestic' and 'Roaming'
		UsageHelper.verifyDomesticRoamingLabels();
		
		
		// #2 Observe the KPI components - There are three KPI tiles under Domestic: 'Voice', 'Data', 'Messages'; and one KPI tile under Roaming: 'Data'
		UsageHelper.verifyKPItilesLabels();
		
		// #3 Verify the Rolling Averages section
		UsageHelper.verifyRollingAveragesLabels();
		
		
		// #4 Verify the units of measure for the rolling amounts.
		UsageHelper.verifyRollingAveragesAmounts();
				
		
		// #5 Verify that the trending values have the '%' symbol, on all the KPI tiles -- icons cannot be verified
		UsageHelper.verifySymbolOnTrendingValue();
		
		
				
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "KPI Tiles test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}


}
