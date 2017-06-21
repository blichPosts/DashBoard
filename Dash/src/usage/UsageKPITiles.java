package usage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.UsageHelper;


public class UsageKPITiles extends BaseClass{

	
	public static void verifyLabelsAboveKPIs() {
		
		List<WebElement> labelsAboveKPIs = driver.findElements(By.cssSelector(".tdb-kpiSection__title"));
		
		Assert.assertEquals(labelsAboveKPIs.get(0).getText(), "Domestic (includes overage)");
		Assert.assertEquals(labelsAboveKPIs.get(1).getText(), "Roaming");
		ShowText("Labels above KPIs: " + labelsAboveKPIs.get(0).getText() + ", " + labelsAboveKPIs.get(1).getText());
		
	}
	
	
	public static void verifyKPItilesLabels() {
		
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi__title")).size(), 4);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi__title")); // get the names of the three 'Domestic' KPI components. 

		for(int i = 0; i < UsageKpiNames.length; i++)
		{
			ShowText("KPI title: " + webList.get(i).getText());
			Assert.assertTrue(webList.get(i).getText().contains(UsageKpiNames[i]));
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

	
	public static void verifyRollingAveragesUnits() {

		
		for (int i = 1; i <= 3; i++) {
			
			String xpath = "//div[@class='tdb-kpi'][" + i + "]/div/div[@class='tdb-kpi__averages']/div/div[@class='tdb-inlineBlock']/following-sibling::div";
			boolean found = true;
			List<WebElement> rollingAmounts = new ArrayList<WebElement>();
			
			try {
				rollingAmounts = driver.findElements(By.xpath(xpath));

			} catch (Exception e) {
				found = false;

			}
			

			if (found && rollingAmounts.size() == 2) {
				
				ShowText(rollingAmounts.get(0).getText());
				ShowText(rollingAmounts.get(1).getText());
				
				switch (i) {
				
					case 1: 
						// Voice 
						Assert.assertTrue(rollingAmounts.get(0).getText().endsWith(UsageHelper.minutes));
						Assert.assertTrue(rollingAmounts.get(1).getText().endsWith(UsageHelper.minutes));
						break; 
						
					case 2:
						// Data
						String rollingAmt = rollingAmounts.get(0).getText();
						boolean unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
								|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
						
						Assert.assertTrue(unitsOfData);
						
						rollingAmt = rollingAmounts.get(1).getText();
						unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
								|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
						
						Assert.assertTrue(unitsOfData);
						break; 
						
					case 3:
						// Messages
						Assert.assertTrue(rollingAmounts.get(0).getText().endsWith(UsageHelper.messages));
						Assert.assertTrue(rollingAmounts.get(1).getText().endsWith(UsageHelper.messages));
						break;
						
				}
									
			}
			
		}
		
		
		
		// Roaming Data
		
		String xpath = "//div[@class='tdb-kpiBlock-USAGE__roaming']/div/div/div[@class='tdb-kpi__averages']/div/div[@class='tdb-inlineBlock']/following-sibling::div";
		boolean found = true;
		
		List<WebElement> rollingAmounts = new ArrayList<WebElement>();
		
		try {
			rollingAmounts = driver.findElements(By.xpath(xpath));

		} catch (Exception e) {
			found = false;

		}
	
		
		if (found && rollingAmounts.size() == 2) {
			
			ShowText(rollingAmounts.get(0).getText());
			ShowText(rollingAmounts.get(1).getText());
			
			String rollingAmt = rollingAmounts.get(0).getText();
			boolean unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
					|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
			
			Assert.assertTrue(unitsOfData);
			
			rollingAmt = rollingAmounts.get(1).getText();
			unitsOfData = (rollingAmt.endsWith(UsageHelper.dataB) || rollingAmt.endsWith(UsageHelper.dataKB) || rollingAmt.endsWith(UsageHelper.dataMB) 
					|| rollingAmt.endsWith(UsageHelper.dataGB) || rollingAmt.endsWith(UsageHelper.dataTB));
			
			Assert.assertTrue(unitsOfData);	
			
		}			
		
	}
	
	
	

	
	
	
	

	
	


	
	
	
}
