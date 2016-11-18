package testSuiteExpense;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CountOfServiceNumbersTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class CountOfServiceNumbersTrendVisual extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void CountOfServiceNumbersTrendVisualTest() throws Exception
	{
		// #1
		// Select vendors view and view the 'Count of Service Numbers' component.

		CommonTestStepActions.GoToExpensePageDetailedWait(); // test
		
		// The title is 'Count of Service Number by Vendor/Country'
		CountOfServiceNumbersTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.vendor);


		// #2 
		// Add all the vendors in point of view. 
		
		
		// The component has vertical bar graphs for each month in the month pulldown.
		// The legends show the same as the legends for the 'Total Expense' control 
		
		
		// Get all the charts listed in the page
		List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));

		for(WebElement wEle : charts)
		{
			// wEle.
			
		}
		
	    //The title is 'Cost per Service Number by Vendor - All Categories'
	    //The last twelve months are shown.
		// .//*[@id='highcharts-8']/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels']/*/* // for last twelve months.
		
		// (//span[text()='Vendor'])[4]  // commit
		// (//span[text()='Vendor'])[4]  // commit		  
		
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	
}
