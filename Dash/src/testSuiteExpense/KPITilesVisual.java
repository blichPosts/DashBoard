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
		
		
		// #1 Observe the KPI components - There are three KPI components, 'Total Expense', 'Count of Service Numbers', and Cost Per Service Number.
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi")).size(), 3);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi > h3")); // get the names of the three KPI components. 

		// verify the three components.
		for(int x = 0; x < ExpenseKpiNames.length; x++)
		{
			Assert.assertEquals(ExpenseKpiNames[x], webList.get(x).getText());
		}
		
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
