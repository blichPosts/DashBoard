package usage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralHelper;
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
		
		System.out.println("KPI Tile Voice Actual: " + voiceAmountKPIFromDash + ",  KPI Tile Voice Expected: " + voiceAmountKPICalculated); 

		
		String dataAmountKPICalculated = UsageCalculationHelper.convertDataUnits(domesticData);
		String dataAmountKPIFromDash = kpiTileValues.get(1).getText();
		
		System.out.println("KPI Tile Data Actual: " + dataAmountKPIFromDash + ",  KPI Tile Data Expected: " + dataAmountKPICalculated); 

		
		String msgAmountKPICalculated = UsageCalculationHelper.convertUnits(domesticMessages);
		String msgAmountKPIFromDash = kpiTileValues.get(2).getText();
		
		System.out.println("KPI Tile Messages Actual: " + msgAmountKPIFromDash + ",  KPI Tile Messages Expected: " + msgAmountKPICalculated); 

		
		String roamingDataAmountKPICalculated = UsageCalculationHelper.convertDataUnits(roamingData);
		String roamingDataAmountKPIFromDash = kpiTileValues.get(3).getText();
		
		System.out.println("KPI Tile Roaming Data Actual: " + roamingDataAmountKPIFromDash + ",  KPI Tile Roaming Data Expected: " + roamingDataAmountKPICalculated); 

		
		GeneralHelper.verifyExpectedAndActualValues(voiceAmountKPIFromDash, voiceAmountKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(dataAmountKPIFromDash, dataAmountKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(msgAmountKPIFromDash, msgAmountKPICalculated);
		GeneralHelper.verifyExpectedAndActualValues(roamingDataAmountKPIFromDash, roamingDataAmountKPICalculated);
		
		
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
		
				
		// Round up rolling averages for Voice and Messages, since they cannot have decimal points (minutes and messages units must be integers) 
		// ** IS THIS REALLY NEEDED?? ** 
		long avgTmpVoice = Math.round(rollingAvgVoice); 
//		ShowText("Voice rolling avg after Math.round: " + avgTmpVoice);
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
				
				String threeMonthAvgExpected = "";
				
				double rollingAverage = 0;
				double kpiValue = 0;
				
				switch(i){
					case 1:  // Voice
						// Amount of minutes must be an integer. This rounding is needed in case the amount of minutes is < 10, 
						// since the rolling average most likely will have a decimal point if it's < 10, and this is incorrect. 
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(rollingAvgVoice);  // convertUnits(avgTmpVoice);
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
						threeMonthAvgExpected = UsageCalculationHelper.convertUnits(rollingAvgMsg);  // convertUnits(avgTmpMsg);
						rollingAverage = rollingAvgMsg;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getDomesticMessages());
						break;
					case 4:  // Roaming Data
						threeMonthAvgExpected = UsageCalculationHelper.convertDataUnitsAndAddPrefix(rollingAvgRoamingData);
						rollingAverage = rollingAvgRoamingData;
						kpiValue = Double.parseDouble(valuesForRollingAvg.get(0).getRoamingDataUsageKb());
						break;
				}
				
				System.out.println("3 Month Avg Actual: " + threeMonthAvgActual + ", 3 Month Avg Expected: " + threeMonthAvgExpected);
				
				// Verifies that the '3 month rolling average' displayed equals to the '3 month rolling average' calculated
				GeneralHelper.verifyExpectedAndActualValues(threeMonthAvgActual, threeMonthAvgExpected);
				
				// If KPI value is different from the 3 month rolling average, then trending % will be different from 0%.
				// --> If trending % is 0%, it won't be displayed
				if(kpiValue != rollingAverage){
				  
					// System.out.println("kpiValue: "  + kpiValue);
					// System.out.println("rollingAverage: "  + rollingAverage);
					
					int trendValueExpected = calculateTrendingPercentage(kpiValue, rollingAverage);
					// System.out.println("Trend calculated: " + trendCalculated);
					
					if(trendValueExpected > 0){
						
						String trend = trendingElementKpi.get(1).getText();
						int trendValueActual = getTrendingValueWithNoSymbol(trend); 
						
						Assert.assertTrue(trendingElementKpi.size() == 2, "The trending percentage is missing.");
						Assert.assertTrue(trend.endsWith("%"), "% symbol is missing.");
						
						GeneralHelper.verifyExpectedAndActualValues(trendValueActual, trendValueExpected);   
												
						System.out.println("Trend Value Actual: " + trendValueActual + "%, Trend Value Expected: " + trendValueExpected + "%");
						
					}
					
				}
				
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
		
				
		// Round up rolling averages for Voice and Messages, since they cannot have decimal points (minutes and messages units must be integers) 
		long avgTmpVoice = Math.round(rollingAvgVoice); 
		long avgTmpMsg = Math.round(rollingAvgMsg); 
		
		for(int i = 1; i <= 4; i++){
			
			WebElement sixMonthValue = null;
			boolean sixMonthDisplayed = true;
			
			try{
				sixMonthValue = driver.findElement(By.xpath("(//div[text()='6 months'])[" + i +"]/following-sibling::div"));
				
			}catch(Exception e){
				// If 6 month rolling average is not displayed, set the variable to false
				//System.out.println("Six Month Displayed false");
				sixMonthDisplayed = false;
			}
			
			
			// If "6 months" rolling average exists
			if(sixMonthDisplayed){
							
				// Get the 6 months rolling average value displayed on the KPI tile
				String sixMonthAvgActual = UsageCalculationHelper.getNumericValueWithPrefixes(sixMonthValue.getText());
				
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
				
				System.out.println("6 Month Avg Actual: " + sixMonthAvgActual + ", 6 Month Avg Expected: " + sixMonthAvgExpected);
				
				// Verifies that the '6 months rolling average' displayed equals to the '6 months rolling average' calculated
				GeneralHelper.verifyExpectedAndActualValues(sixMonthAvgActual, sixMonthAvgExpected);
				
			}
			
		}
		
	}
	
	
	
	// 3 month rolling average = (KPI n + KPI n-1 + KPI n-2) / 3
	// 6 month rolling average = (KPI n + KPI n-1 + KPI n-2++ KPI n-3 + KPI n-4++ KPI n-5) / 6
	private static double calculateRollingAverage(List<String> values) {
		
		double sum = 0;
		
		for (String v: values) {
			
			sum += Double.parseDouble(v);
		}
		
		double average = sum / values.size();

		return average;
		
	}


	// Trend Percentage Change = ABS[(KPI- KPI3Mavg) / KPI3Mavg]
	public static int calculateTrendingPercentage(double kpiValue, double rollingAverage){
		
		return (int) Math.round((Math.abs((kpiValue - rollingAverage)/rollingAverage) * 100));
		
	}
	
	
	private static int getTrendingValueWithNoSymbol(String trend) {
		
		String[] trendParts = trend.split("%");
		return Integer.parseInt(trendParts[0]);
		
	}

	
}
