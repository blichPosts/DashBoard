package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import expenses.TotalExpenseByVendorSpendCategory;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;

public class TotalExpensesTrendVisual extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void TotalExpenseByVendorCarrierVisualTest() throws Exception
	{
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		CommonTestStepActions.SelectAllVendors();

		
		// #1
		// In the 'Total Spend By Vendor'  component:
	    // There are these selections in a horizontal selector bar with the title 'Expense Trending'
	    // All
	    // Voice
	    // Data
	    // Messages
	    // Roaming
	    // Equipment
	    // Taxes
	    // Other
	    // Account 
		TotalExpensesTrend.VerifyTrendValues();
		
	    // Below the Expense Trending bar there there is a title 'Expense by Vendor - All Categories'.
		TotalExpensesTrend.VerifyTitlesVendorView();

		// #2
		// Select all the vendors in the Point Of View and select all of the legends in the 'Expense by Vendor' section.
		

	    // There is a vertical bar graph for the last 13 months in time with the months from the pulldown listed.
	    // The legends below the bar graph show each vendor.
	    // It there are six or more vendors selected, there is a legend called 'other'.
	    // Each vertical bar graph has a value for each of  the legends.
		TotalExpensesTrend.VerifyMonths();
	    TotalExpensesTrend.VerifyVendorsCountries();
		TotalExpensesTrend.VerifyNumLegendsMatchNumBarSections();
		
		// an addition.
		
		// .//*[@id='highcharts-4']/*/*[@class='highcharts-legend']/*/*/* // <<<< ---  xpath for  legends
		
		
		
		
		DebugTimeout(9999, "Freeze");
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	
	
}
