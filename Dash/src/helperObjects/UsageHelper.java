package helperObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;


public class UsageHelper extends BaseClass{

	
	public final static String minutes = "min";
	public final static String messages = "msg";
	public final static String dataB = "B";
	public final static String dataKB = "KB";
	public final static String dataMB = "MB";
	public final static String dataGB = "GB";
	public final static String dataTB = "TB";
	
	public final static String domesticTitleVoice = "Domestic (min)"; 
	public final static String roamingTitleVoice = "Roaming (min)";
	public final static String domesticTitleDataGB = "Domestic (GB)"; 
	public final static String roamingTitleDataGB = "Roaming (GB)";
	public final static String domesticTitleDataTB = "Domestic (TB)"; 
	public final static String roamingTitleDataTB = "Roaming (TB)";
	public final static String domesticTitleMessages = "Domestic"; 
	public final static String roamingTitleMessages = "Roaming";
	
	public final static String domesticLegend = "Domestic";
	public final static String domesticOverageLegend = "Domestic Overage";
	public final static String roamingLegend = "Roaming";
	
	public final static String totalUsageByVendor = "Total Usage by Vendor - ";
	public final static String totalUsageByCountry = "Total Usage by Country - ";
	public final static String usageTrendingByVendor = "Usage by Vendor - ";	
	public final static String usageTrendingByCountry = "Usage by Country - ";
	
	public final static String colorLegendEnabled = "color: rgb(51, 51, 51)";
	public final static String colorLegendDisabled = "color: rgb(204, 204, 204)";
	

	public static void selectVendorView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-0"), MediumTimeout, "Vendor View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-0")).click();
		
	}

	
	public static void selectCountryView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-1"), MediumTimeout, "Country View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-1")).click();
		
	}
	
	
	// It returns the id of the WebElement chart. E.g.: "highchart-45"
	public static String getChartId(int num){
		
		// Get all the charts listed in the page
		List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of specified chart 
		String chartId = charts.get(num).getAttribute("id");
		
		return chartId;
			
		
	}
	
	
	
	
	
	
}
