package TestSuite1;

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
	public static void PointOfViewVisualTest() throws Exception
	{
		
		CommonTestStepActions.GoToExpensePage();
		
		
		// #1 Observe the KPI components - There are three KPI components, 'Total Expense', 'Count of Service Numbers', and Cost Per Service Number.
		// Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi")).size(), 3);
		
		DebugTimeout(5, "Wait 5");
		
		WaitForElementVisible(By.xpath("(//*[@id='highcharts-376']/*/*[@class='highcharts-axis'])[1]/*"), MediumTimeout);
		
		// (//*[@id='highcharts-376']/*/*[@class='highcharts-axis'])[1]/*

		// (//*[@id='highcharts-376']/*/*[@class='highcharts-axis'])[1]
		
		DebugTimeout(5, "Wait 10");
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi > h3"));
		for(WebElement webEle : webList)
		{DebugTimeout(0, webEle.getText());}
		
		// (//*[@id='highcharts-376']/*/*[@class='highcharts-axis'])[1]
		

		System.out.println(driver.findElement(By.xpath("(//h3[@class='tdb-kpi__title'])[1]")).getText());
		
		
		
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
