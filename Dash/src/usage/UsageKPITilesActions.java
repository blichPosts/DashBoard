package usage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;


public class UsageKPITilesActions extends BaseClass{

	public static void verifyKPItileValues(String domesticVoiceUsage, String domesticVoiceOverageUsage,
			String domesticMessagesUsage, String domesticDataUsage, String roamingDataUsage) {
		
		List<WebElement> kpiTileValues = driver.findElements(By.cssSelector(".tdb-kpi__statistic"));
		
		double domesticVoice = Double.parseDouble(domesticVoiceUsage) + Double.parseDouble(domesticVoiceOverageUsage);   
		double domesticMessages = Double.parseDouble(domesticMessagesUsage);
		double domesticData = Double.parseDouble(domesticDataUsage);
		double roamingData = Double.parseDouble(roamingDataUsage);
		
		String voiceAmountKPICalculated = convertVoiceUnits(domesticVoice);
		String voiceAmountKPIFromDash = kpiTileValues.get(0).getText();
		
		System.out.println("KPI Tile Voice From Dash: " + voiceAmountKPIFromDash + ",  KPI Tile Voice Calculated (from spreadsheet): " + voiceAmountKPICalculated); 
		Assert.assertEquals(voiceAmountKPIFromDash, voiceAmountKPICalculated);
		
		String dataAmountKPICalculated = convertDataUnits(domesticData);
		String dataAmountKPIFromDash = kpiTileValues.get(1).getText();
		
		System.out.println("KPI Tile Data From Dash: " + dataAmountKPIFromDash + ",  KPI Tile Data Calculated (from spreadsheet): " + dataAmountKPICalculated); 
		Assert.assertEquals(dataAmountKPIFromDash, dataAmountKPICalculated);
		
		String msgAmountKPICalculated = convertVoiceUnits(domesticMessages);
		String msgAmountKPIFromDash = kpiTileValues.get(2).getText();
		
		System.out.println("KPI Tile Mesages From Dash: " + msgAmountKPIFromDash + ",  KPI Tile Messages Calculated (from spreadsheet): " + msgAmountKPICalculated); 
		Assert.assertEquals(msgAmountKPIFromDash, msgAmountKPICalculated);
		
		String roamingDataAmountKPICalculated = convertDataUnits(roamingData);
		String roamingDataAmountKPIFromDash = kpiTileValues.get(3).getText();
		
		System.out.println("KPI Tile Roaming Data From Dash: " + roamingDataAmountKPIFromDash + ",  KPI Tile Roaming Data Calculated (from spreadsheet): " + roamingDataAmountKPICalculated); 
		Assert.assertEquals(roamingDataAmountKPIFromDash, roamingDataAmountKPICalculated);
		
		
	}

	
	// Used to convert Data units
	private static String convertDataUnits(double amount) {
		
		String amountConverted = "";
		//String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		//System.out.println("Original amount: " + amount);
			
		if (amount < 1000.0){
			tmpAmount = amount;
		}
		else if (amount > 1000.0 && amount < 1000000.0){
			tmpAmount = amount / 1024;
			//unit = "K";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / Math.pow(1024, 2);
			//unit = "M";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / Math.pow(1024, 3);
			//unit = "G";
		}
		else if (amount > 1000000000000.0){
			tmpAmount = amount / Math.pow(1024, 4);
			//unit = "T";
		}
		
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue());  // + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue());  // + unit;
				
			}
			
		}else{
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue);  // + unit;
						
		}
		
		
		//System.out.println("Value rounded for KPI: " + amountConverted);
		
		return amountConverted;
		
	}


	// Used to convert voice and messages units
	private static String convertVoiceUnits(double amount){
		
		String amountConverted = "";
		String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		//System.out.println("Original amount: " + amount);
			
		if (amount < 1000.0){
			tmpAmount = amount;
		}
		else if (amount > 1000.0 && amount < 1000000.0){
			tmpAmount = amount / 1000;
			unit = "K";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / 1000000;
			unit = "M";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / 1000000000;
			unit = "G";
		}
		else if (amount > 1000000000000.0){
			tmpAmount = amount / 1000000000000.0;
			unit = "T";
		}
		
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue()) + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue()) + unit;
				
			}
			
		}else{
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue) + unit;
						
		}
		
		
		//System.out.println("Value rounded for KPI: " + amountConverted);
		
		return amountConverted;
		
	}
	
	
	
	
	
}
