package TestSuite1;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.Country;
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
		
		// #1 Observe the month selector in the left pane described in the summary. The month is displayed with MMM-YYYY format.		
		// #2 In the 'Overview Dashboard Selector' component, select the 'View Total Expense' tab.
		CommonTestStepActions.GoToExpenses(); // #2
		CommonTestStepActions.VerifyMonthPullDownFormat(); // #1
		
		
		// #3  Switch the month selector through various selections. All twelve months are available for selection. 
		CommonTestStepActions.VerifyMonthPullDownSelectionsAndContent();
		
		
		
		// verify the countries and the vendors for each country are in alphabetical order.
		CommonTestStepActions.VerifyCountryAndVendorListSorted();
		
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
