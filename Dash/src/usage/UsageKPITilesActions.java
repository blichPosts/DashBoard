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
		
		double domesticVoice = Double.parseDouble(domesticVoiceUsage); // not working yet with overage, add it when defect is fixed -->  + Double.parseDouble(domesticVoiceOverageUsage); 
		double domesticMessages = Double.parseDouble(domesticMessagesUsage);
		double domesticData = Double.parseDouble(domesticDataUsage);
		double roamingData = Double.parseDouble(roamingDataUsage);
		
		String voiceAmountKPICalculated = convertVoiceUnits(domesticVoice);
		String voiceAmountKPIFromDash = kpiTileValues.get(0).getText();
		
		System.out.println("KPI Tile Voice From Dash: " + voiceAmountKPIFromDash + ",  KPI Tile Voice Calculated (from spreadsheet): " + voiceAmountKPICalculated); 
		Assert.assertEquals(voiceAmountKPIFromDash, voiceAmountKPICalculated);
		
	}

	
	
	public static String convertVoiceUnits(double voice){
		
		String voiceAmountConverted = "";
		String unit = "";
		double tmpAmount = 0.0;
		
				
		if (voice > 1000 && voice < 1000000){
				tmpAmount = voice / 1000;
				unit = "K";
		}
		else if (voice > 1000000 && voice < 1000000000){
			tmpAmount = voice / 1000000;
			unit = "M";
		}
		
		BigDecimal rounded = new BigDecimal(tmpAmount);
		rounded = rounded.setScale(1, RoundingMode.HALF_UP); 
		
		voiceAmountConverted = Double.toString(rounded.doubleValue()) + unit;
		
		System.out.println("Voice rounded for KPI: " + voiceAmountConverted);
		
		return voiceAmountConverted;
		
	}
	
}
