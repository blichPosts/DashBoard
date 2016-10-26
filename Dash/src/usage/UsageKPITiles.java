package usage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.UsageHelper;


public class UsageKPITiles extends BaseClass{

	
	public static void verifyDomesticRoamingLabels() {
		
		
		List<WebElement> kpiDomesticRoamingTitles = driver.findElements(By.cssSelector(".tdb-h3.tdb-kpiSection__title"));
		
		Assert.assertEquals(kpiDomesticRoamingTitles.get(0).getText(), "Domestic (includes overage)");
		Assert.assertEquals(kpiDomesticRoamingTitles.get(1).getText(), "Roaming");
		
		
	}
	
	
	public static void verifyKPItilesLabels() {
		
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi__title")).size(), 4);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi__title")); // get the names of the three 'Domestic' KPI components. 

		for(int x = 0; x < UsageKpiNames.length; x++)
		{
			//System.out.println("From web list: " + webList.get(x).getText());
			//System.out.println("From UsageKPINames: " + UsageKpiNames[x]);
			Assert.assertTrue(webList.get(x).getText().contains(UsageKpiNames[x]));
		}
		
	}
	
	
	public static void verifyRollingAveragesLabels() {
		
		// Rolling Averages label
		Assert.assertEquals(driver.findElement(By.xpath("//h4")).getText(), rollingAverages, "Rolling averages label is incorrect.");
						
		// 3 months label
		Assert.assertEquals(driver.findElement(By.xpath("//div[text()='3 months']")).getText(), rollingMonthsThree, "Rolling '3 months' label is incorrect.");
				
		// 6 months label
		Assert.assertEquals(driver.findElement(By.xpath("//div[text()='6 months']")).getText(), rollingMonthsSix, "Rolling '6 months' label is incorrect.");		
		
	}

	
	public static void verifyRollingAveragesAmounts() {

		
		List<WebElement> rollingAmounts = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-width--one-half.tdb-align--right"));
		Assert.assertEquals(rollingAmounts.size(), 8);
				
		/*
		 *  for(int i = 0; i < rollingAmounts.size(); i++){
			System.out.println("Rolling amounts: " + rollingAmounts.get(i).getText());
			}
		*/
		
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
		
		Assert.assertTrue(unitsOfData);
		
		rollingAmt = rollingAmounts.get(3).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertTrue(unitsOfData);
		
		// Roaming Data
		rollingAmt = rollingAmounts.get(6).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertTrue(unitsOfData);
		
		rollingAmt = rollingAmounts.get(7).getText();
		unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
				|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
		
		Assert.assertTrue(unitsOfData);
						
		
	}
	
	
	
	public static void verifySymbolOnTrendingValue() {
		
		List<WebElement> trendingElements = driver.findElements(By.cssSelector(".tdb-kpi__trend>span"));
		Assert.assertEquals(trendingElements.size(), 8);
		
		/*
		 * for(int i = 0; i < trendingElements.size(); i++){
			System.out.println("Trending element: " + trendingElements.get(i).getText());
		}
		*/
		
		Assert.assertTrue(trendingElements.get(1).getText().endsWith("%"));
		Assert.assertTrue(trendingElements.get(3).getText().endsWith("%"));
		Assert.assertTrue(trendingElements.get(5).getText().endsWith("%"));
		Assert.assertTrue(trendingElements.get(7).getText().endsWith("%"));

		
	}
	
	
	
}
