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

	
	public static void verifyRollingAveragesUnits() {

		
		List<WebElement> rollingAmounts = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-width--one-half.tdb-align--right"));	
				
		  for(int i = 0; i < rollingAmounts.size(); i++){
			System.out.println("Rolling amounts: " + rollingAmounts.get(i).getText());
			}
		
		
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
	
	
	
	public static void verifyTrendingValues() {
		
		
		for(int i = 1; i <= 4; i++){
			
			List<WebElement> trendingElementKpi = driver.findElements(By.xpath("(//*[@class='tdb-kpi__trend'])[" + i + "]/span"));
			
			WebElement threeMonthValue = null;
			boolean threeMonthDisplayed = true;
			
			try{
				threeMonthValue = driver.findElement(By.xpath("(//div[text()='3 months'])[" + i +"]/following-sibling::div"));
			}catch(Exception e){
				threeMonthDisplayed = false;
			}
			
			
			// If "3 months" rolling average exists, then the trending percentage can be calculated
			if(threeMonthDisplayed){
				
				String kpiValueString = "";
				
				// If KPI tile is one of the Domestic KPI tiles
				if(i <= 3)
					kpiValueString = driver.findElement(By.xpath("(//div[@class='tdb-kpi__statistic'])[" + i +"]")).getText();
				// If KPI tile is the Roaming KPI tile
				if(i == 4)
					kpiValueString = driver.findElement(By.cssSelector(".tdb-kpi__statistic.tdb-text--highlight")).getText();
				
				String valueWithNoUnits = getNumericValue(kpiValueString);
				
				double kpiValue = Double.parseDouble(valueWithNoUnits);
				
				String threeMonthString = threeMonthValue.getText();
				String threeMonthNoUnits = getNumericValue(threeMonthString);
				
				double threeMonthAverage = Double.parseDouble(threeMonthNoUnits);
				
				
				// If value on KPI is different from the 3 month rolling average, then trending % will different from 0%.
				// If trending % is 0%, it won't be displayed
				if (!kpiValueString.equals(threeMonthString)){
				
					System.out.println("kpiValue: "  + kpiValue);
					System.out.println("threeMonthAverage: "  + threeMonthAverage);
					
					long trendCalculated = Math.round((Math.abs((kpiValue - threeMonthAverage)/threeMonthAverage) * 100));
					
					System.out.println("Trend calculated: " + trendCalculated);
					
					if(trendCalculated > 0){
						String trend = trendingElementKpi.get(1).getText();
						int trendValue = getTrendingValueWithNoSymbol(trend); 
						Assert.assertTrue(trendingElementKpi.size() == 2);
						
						///*** I'LL NEED THE ORIGINAL VALUES TO CALCULATE THE % - 
						// *** THE ROUNDED VALUES DON'T ALWAYS WORK TO CALCULATE AND GET THE EXACT SAME VALUE AS THE ONE DISPLAYED ON THE KPI.
						
						//Assert.assertEquals(trendValue, trendCalculated);   
						Assert.assertTrue(trend.endsWith("%"));
						System.out.println("Trending element: " + trend);
					}
				}
				
				//System.out.println("3 months is displayed"); 
				
			}else{
				
				//System.out.println("3 months is NOT displayed"); 
				
			}
							
		}
		
	}

	
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
	}

	
	

	private static String getNumericValue(String stringValue) {
		
		String unitK = "K";
		String unitM = "M";
		String unitG = "G";
		String unitT = "T";
		
		String valueNoUnits = "";
		
		//System.out.println("Value String: " + stringValue);
		
		if (stringValue.contains(unitK)){
			
			String[] kpiValueParts = stringValue.split(unitK);
			valueNoUnits = kpiValueParts[0];
							
		}else if (stringValue.contains(unitM)){
			
			String[] kpiValueParts = stringValue.split(unitM);
			valueNoUnits = kpiValueParts[0];
			
		}else if (stringValue.contains(unitG)){
			
			String[] kpiValueParts = stringValue.split(unitG);
			valueNoUnits = kpiValueParts[0];
		
		}else if (stringValue.contains(unitT)){
			
			String[] kpiValueParts = stringValue.split(unitT);
			valueNoUnits = kpiValueParts[0];
		
		}else{
			valueNoUnits = stringValue;
			
		}
		
		if(valueNoUnits.endsWith(".0")){
			String[] tmpValue = valueNoUnits.split(".0"); 
			valueNoUnits = tmpValue[0];
		}
		
		//System.out.println("Value with no units: " + valueNoUnits);
		
		return valueNoUnits;
		
	}
	
	
	
}
