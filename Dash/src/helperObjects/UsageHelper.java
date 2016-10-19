package helperObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;

public class UsageHelper extends BaseClass{

	// Units of measure for Usage
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
	


	public static void selectVendorView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-0"), MediumTimeout, "Vendor View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-0")).click();
		
	}
	
	
	public static void verifyMonthYearOnVendorView(){
		
		// Get the month and year from Month selector
		String monthYearSelector = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText();
		//System.out.println("monthYearSelector:" + monthYearSelector);
					
		// Get the month and year from Vendor View
		String monthYearVendorView = driver.findElement(By.cssSelector(".tdb-h2")).getText();
		//System.out.println("monthYearVendorView:" + monthYearVendorView);
					
		Assert.assertEquals(monthYearSelector, monthYearVendorView, "Month and Year displayed for Total Usage by Vendor is not the same as the selection made under Month selector.");		
							
	}

	
	
	public static void verifyTotalUsageTitle(){
		
		String totalUsageTitleExpected = "Total Usage by Vendor - ";
		String totalUsageTitleFound = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));

		
		for(int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click();
			totalUsageTitleFound = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
			
			//System.out.println("Title found:    " + totalUsageTitleFound);
			//System.out.println("Title expected: " + totalUsageTitleExpected + categorySelectors.get(i).getText());
			
			Assert.assertEquals(totalUsageTitleExpected + categorySelectors.get(i).getText(), totalUsageTitleFound);
			
		}

		
	}
	
	
	public static void verifyBarChartTitlesUsageByVendor(){
		
				
		List<WebElement> barChartTitles;
		
		String domesticTitleFound; 
		String roamingTitleFound;
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));

		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			barChartTitles = driver.findElements(By.cssSelector(".tdb-charts__label.tdb-text--italic.tdb-text--bold"));
			domesticTitleFound = barChartTitles.get(0).getText(); 
			roamingTitleFound = barChartTitles.get(1).getText();
			
			if (i == 0){
				Assert.assertEquals(domesticTitleFound, domesticTitleVoice);
				Assert.assertEquals(roamingTitleFound, roamingTitleVoice);
				System.out.println("Voice...");
				
			} else if (i == 1){
				
				boolean domesticTitle = domesticTitleFound.equals(domesticTitleDataGB) || domesticTitleFound.equals(domesticTitleDataTB);  
				Assert.assertTrue(domesticTitle);
				
				boolean roamingTitle = roamingTitleFound.equals(roamingTitleDataGB) || roamingTitleFound.equals(roamingTitleDataTB);  
				Assert.assertTrue(roamingTitle);
				System.out.println("Data...");
								
			} else if (i == 2){
			
				Assert.assertEquals(domesticTitleFound, domesticTitleMessages);
				Assert.assertEquals(roamingTitleFound, roamingTitleMessages);
				System.out.println("Messages...");
			
			}
			
		}
				
		
	}
	
	
	
	
	
}
