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
		
		
		// #3 Verify the Rolling Averages section
		
		// Rolling Averages label
		Assert.assertEquals(driver.findElement(By.xpath("//h4")).getText(), rollingAverages, "Rolling averages label is incorrect.");
				
		// 3 months label
		Assert.assertEquals(driver.findElement(By.xpath("//div[text()='3 months']")).getText(), rollingMonthsThree, "Rolling '3 months' label is incorrect.");
		
		// 6 months label
		Assert.assertEquals(driver.findElement(By.xpath("//div[text()='6 months']")).getText(), rollingMonthsSix, "Rolling '6 months' label is incorrect.");		
		
		
		List<WebElement> rollingAmounts = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-width--one-half.tdb-align--right"));
		Assert.assertEquals(rollingAmounts.size(), 8);
		
		/*
		 *  for(int i = 0; i < rollingAmounts.size(); i++){
			System.out.println("Rolling amounts: " + rollingAmounts.get(i).getText());
		}
		*/
		
		
		// Verify the units of measure for the rolling amounts. 
		// Voice
		Assert.assertEquals(rollingAmounts.get(0).getText().endsWith(UsageHelper.minutes), true);
		Assert.assertEquals(rollingAmounts.get(1).getText().endsWith(UsageHelper.minutes), true);
		
		// Messages
		Assert.assertEquals(rollingAmounts.get(4).getText().endsWith(UsageHelper.messages), true);
		Assert.assertEquals(rollingAmounts.get(5).getText().endsWith(UsageHelper.messages), true);
		
		// Data
		String rollingAmt = rollingAmounts.get(2).getText();
		boolean unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertEquals(unitsOfData, true);
		
		rollingAmt = rollingAmounts.get(3).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertEquals(unitsOfData, true);
		
		// Roaming Data
		rollingAmt = rollingAmounts.get(6).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertEquals(unitsOfData, true);
		
		rollingAmt = rollingAmounts.get(7).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertEquals(unitsOfData, true);
		
		
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
