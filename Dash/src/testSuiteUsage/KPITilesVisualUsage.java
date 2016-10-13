package testSuiteUsage;

import java.util.List;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;


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
		Assert.assertEquals(driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[1]/h3")).getText(), "Domestic (includes overage)");
		Assert.assertEquals(driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[2]/h3")).getText(), "Roaming");
		

		// #2 Observe the KPI components - There are three KPI tiles under Domestic: 'Voice', 'Data', 'Messages'; and one KPI tile under Roaming: 'Data'
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi__title")).size(), 4);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi__title")); // get the names of the three 'Domestic' KPI components. 

		for(int x = 0; x < UsageKpiNames.length; x++)
		{
			//System.out.println("From web list: " + webList.get(x).getText());
			//System.out.println("From UsageKPINames: " + UsageKpiNames[x]);
			Assert.assertEquals(UsageKpiNames[x], webList.get(x).getText());
		}
		
		// 
		
		
		
		
		//DebugTimeout(9999, "DONE");		
				
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK to close browser.");
		driver.close();
		driver.quit();
	}


}
