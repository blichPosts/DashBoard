package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;


public class PointOfViewVisual extends BaseClass
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void PointOfViewVisualTest() throws Exception
	{
		
		// #1 Observe the month selector in the left pane described in the summary - The month is displayed with MMM-YYYY format.		
		// #2 In the 'Overview Dash board Selector' component, select the 'View Total Expense' tab.
		CommonTestStepActions.GoToExpensePage(); // #2
		CommonTestStepActions.VerifyMonthPullDownFormat(); // #1
		
		// #3  Switch the month selector through various selections - Thirteen descending months are available for selection, starting with the current month/year. 
		CommonTestStepActions.VerifyMonthPullDownSelectionsAndContent();
		CommonTestStepActions.UsePulldownList();

		
		// #4
		// Observe the Country and the Vendor (has check box selectors) in the 'Country Vendor' selector control in the 'Point Of View'.
		
		// The elements of this selector are arranged hierarchically showing countries and vendors, with the vendors corresponding to each country indented inside the corresponding country
		// Each vendor has check box menu items to allow the user to select/de-select the vendors -- NOTE: in automation - this will be tested in later tests. 
		// Each country has a select/un-select �All� menu item to allow the user to select/de-select all vendors under this country at the same time. 
		// The countries are listed in alphabetical order. The Vendors within the countries are listed in alphabetical order within each country.
		CommonTestStepActions.VerifyCountryAndVendorListSorted();
		CommonTestStepActions.VerifyMonthPulldownDetail();
		
		DebugTimeout(9999, "DONE");		
		

		// drilling down into controls.
		//WebElement web;
		//  //web = driver.findElement(By.xpath("//div[@id='highcharts-2']/*/*/*/..[@class='highcharts-axis-labels highcharts-xaxis-labels']"));
		//  //web = driver.findElement(By.xpath("//div[@id='highcharts-2']/*/*/*"));
		//web = driver.findElement(By.xpath("//div[@id='highcharts-2']/*/*/*/.."));
		//web = driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[8]"));
		//String [] strArray= driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[8]")).getText().split("\n");
		//ShowArray(strArray); 
		//strArray= driver.findElement(By.xpath(".//*[@id='highcharts-0']/*/*[@class='highcharts-legend']")).getText().split("\n");
		//ShowArray(strArray);

		
		
		
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



/*
Step #4 - not ready yet in dev
Each country has a select/unselect �All� menu item to allow the user to select/deselect all vendors under this country at the same time. 

step# 2 -  currency tab  to the right is not there in dev, can't verify selected and not selected with 'View Expense View Usage' tabs.


Step #3 - testing is not exact with months.
*/


