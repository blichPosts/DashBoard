package usage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageOneMonth;


public class UsageKPITilesActions extends BaseClass{

	public static void verifyKPItileValues(String domesticVoiceUsage, String domesticVoiceOverageUsage, String domesticMessagesUsage, String domesticDataUsage, String roamingDataUsage) {
		
		List<WebElement> kpiTileValues = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		double domesticVoice = Double.parseDouble(domesticVoiceUsage) + Double.parseDouble(domesticVoiceOverageUsage);   
		double domesticMessages = Double.parseDouble(domesticMessagesUsage);
		double domesticData = Double.parseDouble(domesticDataUsage);
		double roamingData = Double.parseDouble(roamingDataUsage);
		
		String voiceAmountKPICalculated = UsageCalculationHelper.convertUnits(domesticVoice);
		String voiceAmountKPIFromDash = kpiTileValues.get(0).getText();
		
		//System.out.println("KPI Tile Voice From Dash: " + voiceAmountKPIFromDash + ",  KPI Tile Voice Calculated (from spreadsheet): " + voiceAmountKPICalculated); 
		Assert.assertEquals(voiceAmountKPIFromDash, voiceAmountKPICalculated);
		
		String dataAmountKPICalculated = UsageCalculationHelper.convertDataUnits(domesticData);
		String dataAmountKPIFromDash = kpiTileValues.get(1).getText();
		
		//System.out.println("KPI Tile Data From Dash: " + dataAmountKPIFromDash + ",  KPI Tile Data Calculated (from spreadsheet): " + dataAmountKPICalculated); 
		Assert.assertEquals(dataAmountKPIFromDash, dataAmountKPICalculated);
		
		String msgAmountKPICalculated = UsageCalculationHelper.convertUnits(domesticMessages);
		String msgAmountKPIFromDash = kpiTileValues.get(2).getText();
		
		//System.out.println("KPI Tile Mesages From Dash: " + msgAmountKPIFromDash + ",  KPI Tile Messages Calculated (from spreadsheet): " + msgAmountKPICalculated); 
		Assert.assertEquals(msgAmountKPIFromDash, msgAmountKPICalculated);
		
		String roamingDataAmountKPICalculated = UsageCalculationHelper.convertDataUnits(roamingData);
		String roamingDataAmountKPIFromDash = kpiTileValues.get(3).getText();
		
		//System.out.println("KPI Tile Roaming Data From Dash: " + roamingDataAmountKPIFromDash + ",  KPI Tile Roaming Data Calculated (from spreadsheet): " + roamingDataAmountKPICalculated); 
		Assert.assertEquals(roamingDataAmountKPIFromDash, roamingDataAmountKPICalculated);
		
		
	}

	
	//  ***** CONTINUE HERE ***********
	// ************** rolling averages need to be rounded in order to be compared -- DONE!!
	// Trend Percentage Change = ABS[(KPI- KPI3Mavg)/ KPI3Mavg]
	public static void verifyTrendingValues(List<UsageOneMonth> valuesForRollingAvg) {
		
		
		double rollingAvgVoice = calculateRollingAverage(valuesForRollingAvg.get(0).getDomesticVoice(), valuesForRollingAvg.get(1).getDomesticVoice(), valuesForRollingAvg.get(2).getDomesticVoice());
		double rollingAvgMsg = calculateRollingAverage(valuesForRollingAvg.get(0).getDomesticMessages(), valuesForRollingAvg.get(1).getDomesticMessages(), valuesForRollingAvg.get(2).getDomesticMessages());
		double rollingAvgData = calculateRollingAverage(valuesForRollingAvg.get(0).getDomesticDataUsageKb(), valuesForRollingAvg.get(1).getDomesticDataUsageKb(), valuesForRollingAvg.get(2).getDomesticDataUsageKb());
		double rollingAvgRoamingData = calculateRollingAverage(valuesForRollingAvg.get(0).getRoamingDataUsageKb(), valuesForRollingAvg.get(1).getRoamingDataUsageKb(), valuesForRollingAvg.get(2).getRoamingDataUsageKb());
		
		/*System.out.println("rollingAvgVoice: " + rollingAvgVoice);
		System.out.println("rollingAvgMsg: " + rollingAvgMsg);
		System.out.println("rollingAvgData: " + rollingAvgData);
		System.out.println("rollingAvgRoamingData: " + rollingAvgRoamingData);
		*/
		
		
		for(int i = 1; i <= 4; i++){
			
			List<WebElement> trendingElementKpi = driver.findElements(By.xpath("(//*[@class='tdb-kpi__trend'])[" + i + "]/span"));
			
			WebElement threeMonthValue = null;
			boolean threeMonthDisplayed = true;
			
			try{
				threeMonthValue = driver.findElement(By.xpath("(//div[text()='3 months'])[" + i +"]/following-sibling::div"));
				//System.out.println("threeMonthDisplayed true");
			}catch(Exception e){
				// If 3 month rolling average is not displayed, set the variable to false
				//System.out.println("threeMonthDisplayed false");
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
				
				String kpiWithNoUnits = UsageCalculationHelper.getNumericValue(kpiValueString);
				
				double kpiValue = Double.parseDouble(kpiWithNoUnits);
				
				// Get the 3 month value displayed on the KPI tile
				String threeMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(threeMonthValue.getText());
				System.out.println("threeMonthAvgActual: " + threeMonthAvgActual);
				
				String threeMonthAvgExpected = "";
				
				switch(i){
					case 1:
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(rollingAvgVoice);
						//System.out.println("case 1 - avg: " + rollingAvgVoice);
						//System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
						break;
					case 2:
						threeMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgData);
						//System.out.println("case 2 - avg: " + rollingAvgData);
						//System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
						break;
					case 3:
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(rollingAvgMsg);
						//System.out.println("case 3 - avg: " + rollingAvgMsg);
						//System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
						break;
					case 4:
						threeMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgRoamingData);
						//System.out.println("case 4 - avg: " + rollingAvgRoamingData);
						//System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
						break;
				}
				System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
				Assert.assertEquals(threeMonthAvgActual, threeMonthAvgExpected);
				
				/*
				String threeMonthNoUnits = UsageCalculationHelper.getNumericValue(threeMonthAvgActual);
				double threeMonthAverage = Double.parseDouble(threeMonthNoUnits);
				
				
				// If value on KPI is different from the 3 month rolling average, then trending % will different from 0%.
				// If trending % is 0%, it won't be displayed
				if (!kpiValueString.equals(threeMonthAvgActual)){
				  
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
						//Assert.assertTrue(trend.endsWith("%"));
						System.out.println("Trending element: " + trend);
					}
				}*/
				
				//System.out.println("3 months is displayed"); 
				
			}else{
				
				//System.out.println("3 months is NOT displayed"); 
				
			}
							
		}
		
		
		
	}

	
	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2)/3
	private static double calculateRollingAverage(String valueCurrentMonth, String valuePreviousMonth, String valueTwoMonthsBefore) {
		
		double average = (Double.parseDouble(valueCurrentMonth) + Double.parseDouble(valuePreviousMonth) + Double.parseDouble(valueTwoMonthsBefore))/3;
		return average;		
		
	}


	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
	}

	
	
	
	
	
}
