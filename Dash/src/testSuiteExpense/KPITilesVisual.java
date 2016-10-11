package testSuiteExpense;

import java.util.List;

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
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi")).size(), 3);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi > h3")); // get the names of the three KPI components. 

		Assert.assertTrue(webList.size() == 3);
		
		// verify the three components.
		for(int x = 0; x < ExpenseKpiNames.length; x++)
		{
			Assert.assertEquals(ExpenseKpiNames[x], webList.get(x).getText());
		}
		
		// # 2
		//Observe the first component from the left.

		//The middle section shows the total expense amount with K indicator for selected vendor or country.
		//Next there is a trend icon (up/down) arrow.
		//Next there is a Rolling average with rolling avreages for three and six months.

		//The top says 'Total Expense'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > h3")).getText(), ExpenseKpiNames[0]);
		
		//The middle section shows the total expense amount with K indicator for selected vendor or country.
		ExpenseHelper.VerifyTotalExpenseCost();
		
		// .tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)
		
		//DebugTimeout(0, driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText());
		
		// .tdb-kpi:nth-of-type(1) > div> div:nth-of-type(2) > span:nth-of-type(2)
		
		// next level.
		// .tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)
		
		
		
		//DebugTimeout(0, driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText());
		//DebugTimeout(0, driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > h3")).getText());		
		
		// .tdb-kpi:nth-of-type(1) > h3
		
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
