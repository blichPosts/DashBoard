package testSuiteExpense;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;


public class KPITilesVisual extends BaseClass
{
	public static String rollingAverages = "Rolling Averages";
	public static String monthsThree = "3 months";	
	public static String monthsTwo = "2 months";
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void KpiTilesVisualTest() throws Exception
	{
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// prereq - select all of the vendors. - this is already done in dev instance 
		// driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).click();
		
		// #1 Observe the KPI components - There are three KPI components, 'Total Expense', 'Count of Service Numbers', and Cost Per Service Number.
		ExpenseHelper.VerifyThreeComponents();
		
		// # 2
		//Observe the first component from the left.

		//The top says 'Total Expense'		
		//The middle section shows the total expense amount with K indicator for selected vendor or country.
		ExpenseHelper.VerifyTotalExpenseCostAndTitle();
		
		//Next there is a trend icon (up/down) arrow.
		ExpenseHelper.TotalExpenseTrendVerifyTrend();
		
		// Next there is a Rolling average with rolling averages for three and six months.
		ExpenseHelper.VerifyRollingAverageTotalExpense();
		
		//DebugTimeout(0, driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText());
		
		// # 3
		// Observe the second component from the left.
		ExpenseHelper.VerifyCountOfServiceNumbersAndTitle();
		 
	    // The top says 'Count of Service Numbers'
	    // The middle section shows the total count of service numbers managed by the selected vendors in the selected month.
		
		
		
		DebugTimeout(9999, "DONE");		
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
