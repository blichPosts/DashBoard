package usage;

import java.util.ArrayList;
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

	
	
	
	public static void verifyThreeMonthRollingAverageAndTrendingValues(List<UsageOneMonth> valuesForRollingAvg) {
	
		List<String> voiceValues = new ArrayList<String>();
		List<String> dataValues = new ArrayList<String>();
		List<String> messagesValues = new ArrayList<String>();
		List<String> roamingDataValues = new ArrayList<String>();
		
		for(UsageOneMonth monthValues: valuesForRollingAvg){
			
			voiceValues.add(Double.toString(Double.parseDouble(monthValues.getDomesticVoice()) + Double.parseDouble(monthValues.getDomesticOverageVoice())));
			dataValues.add(monthValues.getDomesticDataUsageKb());
			messagesValues.add(monthValues.getDomesticMessages());
			roamingDataValues.add(monthValues.getRoamingDataUsageKb());
			
		}
		
		double rollingAvgVoice = calculateRollingAverage(voiceValues);
		double rollingAvgData = calculateRollingAverage(dataValues);
		double rollingAvgMsg = calculateRollingAverage(messagesValues);
		double rollingAvgRoamingData = calculateRollingAverage(roamingDataValues);
		
		/*
		System.out.println("rollingAvgVoice: " + rollingAvgVoice);
		System.out.println("rollingAvgMsg: " + rollingAvgMsg);
		System.out.println("rollingAvgData: " + rollingAvgData);
		System.out.println("rollingAvgRoamingData: " + rollingAvgRoamingData);
		*/
		
		// Round up rolling averages for Voice and Messages, since they cannot have decimal points (minutes and messages units must be integers) 
		long avgTmpVoice = Math.round(rollingAvgVoice); 
		long avgTmpMsg = Math.round(rollingAvgMsg); 
		
		
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
				
				// Get the 3 month value displayed on the KPI tile
				String threeMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(threeMonthValue.getText());
				// System.out.println("threeMonthAvgActual: " + threeMonthAvgActual);
				
				String threeMonthAvgExpected = "";
				
				double rollingAverage = 0;
				double kpiValue = 0;
				
				switch(i){
					case 1:  // Voice
						// Amount of minutes must be an integer. This rounding is needed in case the amount of minutes is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpVoice);
						rollingAverage = rollingAvgVoice;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getDomesticVoice()) + Double.parseDouble(valuesForRollingAvg.get(0).getDomesticOverageVoice());
						break;
					case 2:  // Data
						threeMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgData);
						rollingAverage = rollingAvgData;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getDomesticDataUsageKb());
						break;
					case 3:  // Messages
						// Amount of messages must be an integer. This rounding is needed in case the amount of messages is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpMsg);
						rollingAverage = rollingAvgMsg;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getDomesticMessages());
						break;
					case 4:  // Roaming Data
						threeMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgRoamingData);
						rollingAverage = rollingAvgRoamingData;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getRoamingDataUsageKb());
						break;
				}
				
				//System.out.println("threeMonthAvgExpected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
				Assert.assertEquals(threeMonthAvgActual, threeMonthAvgExpected);
				
				// If KPI value is different from the 3 month rolling average, then trending % will be different from 0%.
				// --> If trending % is 0%, it won't be displayed
				if(kpiValue != rollingAverage){
				  
					// System.out.println("kpiValue: "  + kpiValue);
					// System.out.println("rollingAverage: "  + rollingAverage);
					
					long trendCalculated = calculateTrendingPercentage(kpiValue, rollingAverage);
					
					// System.out.println("Trend calculated: " + trendCalculated);
					
					if(trendCalculated > 0){
						
						String trend = trendingElementKpi.get(1).getText();
						int trendValue = getTrendingValueWithNoSymbol(trend); 
						
						Assert.assertTrue(trendingElementKpi.size() == 2);
						Assert.assertEquals(trendValue, trendCalculated);   
						Assert.assertTrue(trend.endsWith("%"));
						// System.out.println("Trending element: " + trend);
						
					}
				}
				
				//System.out.println("3 months is displayed"); 
				
			}else{
				
				//System.out.println("3 months is NOT displayed"); 
				
			}
							
		}
	
	}

	
	
	public static void verifySixMonthRollingAverage(List<UsageOneMonth> valuesForRollingAvg) {
		
		
		List<String> voiceValues = new ArrayList<String>();
		List<String> dataValues = new ArrayList<String>();
		List<String> messagesValues = new ArrayList<String>();
		List<String> roamingDataValues = new ArrayList<String>();
		
		for(UsageOneMonth monthValues: valuesForRollingAvg){
			
			voiceValues.add(Double.toString(Double.parseDouble(monthValues.getDomesticVoice()) + Double.parseDouble(monthValues.getDomesticOverageVoice())));
			dataValues.add(monthValues.getDomesticDataUsageKb());
			messagesValues.add(monthValues.getDomesticMessages());
			roamingDataValues.add(monthValues.getRoamingDataUsageKb());
			
		}
		
		double rollingAvgVoice = calculateRollingAverage(voiceValues);
		double rollingAvgData = calculateRollingAverage(dataValues);
		double rollingAvgMsg = calculateRollingAverage(messagesValues);
		double rollingAvgRoamingData = calculateRollingAverage(roamingDataValues);
		
		/*System.out.println("rollingAvgVoice: " + rollingAvgVoice);
		System.out.println("rollingAvgMsg: " + rollingAvgMsg);
		System.out.println("rollingAvgData: " + rollingAvgData);
		System.out.println("rollingAvgRoamingData: " + rollingAvgRoamingData);
		*/
		
		// Round up rolling averages for Voice and Messages, since they cannot have decimal points (minutes and messages units must be integers) 
		long avgTmpVoice = Math.round(rollingAvgVoice); 
		long avgTmpMsg = Math.round(rollingAvgMsg); 
		
		for(int i = 1; i <= 4; i++){
			
			WebElement sixMonthValue = null;
			boolean sixMonthDisplayed = true;
			
			try{
				sixMonthValue = driver.findElement(By.xpath("(//div[text()='6 months'])[" + i +"]/following-sibling::div"));
				//System.out.println("threeMonthDisplayed true");
			}catch(Exception e){
				// If 6 month rolling average is not displayed, set the variable to false
				//System.out.println("threeMonthDisplayed false");
				sixMonthDisplayed = false;
			}
			
			
			// If "6 months" rolling average exists
			if(sixMonthDisplayed){
							
				// Get the 6 months rolling average value displayed on the KPI tile
				String sixMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(sixMonthValue.getText());
				// System.out.println("sixMonthAvgActual: " + sixMonthAvgActual);
				
				String sixMonthAvgExpected = "";
				
				switch(i){
					case 1: // Voice
						// Amount of minutes must be an integer. This rounding is needed in case the amount of minutes is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						sixMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpVoice);
						break;
					case 2: // Data
						sixMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgData);
						break;
					case 3: // Messages
						// Amount of messages must be an integer. This rounding is needed in case the amount of messages is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						sixMonthAvgExpected = UsageCalculationHelper.convertUnits(avgTmpMsg);
						break;
					case 4: // Roaming Data
						sixMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgRoamingData);
						break;
				}
				
				// System.out.println("sixMonthAvgExpected: " + sixMonthAvgExpected);
				
				// Verifies that the '6 months rolling average' displayed equals to the '6 months rolling average' calculated
				Assert.assertEquals(sixMonthAvgActual, sixMonthAvgExpected);
				
			}
			
		}
		
	}
	
	
	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2) / 3
	// 6 month rolling average = (KPI n + KPI n-1 + KPI n-2++ KPI n-3 + KPI n-4++ KPI n-5) / 6
	private static double calculateRollingAverage(List<String> values) {
		
		double sum = 0;
		
		for(String v: values){
			sum += Double.parseDouble(v);
		}
		
		double average = sum / values.size();
		return average;
		
	}


	// Trend Percentage Change = ABS[(KPI- KPI3Mavg) / KPI3Mavg]
	public static long calculateTrendingPercentage(double kpiValue, double rollingAverage){
		
		return Math.round((Math.abs((kpiValue - rollingAverage)/rollingAverage) * 100));
		
	}
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
	}


	

	
	
}
