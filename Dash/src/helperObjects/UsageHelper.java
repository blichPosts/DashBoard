package helperObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.thoughtworks.selenium.webdriven.commands.GetText;

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
	
	
	
	

	

	public static void selectVendorView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-0"), MediumTimeout, "Vendor View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-0")).click();
		
	}

	
	
	public static void verifyDomesticRoamingLabels() {
		
		Assert.assertEquals(driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[1]/h3")).getText(), "Domestic (includes overage)");
		Assert.assertEquals(driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[2]/h3")).getText(), "Roaming");
		
		
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
		
		
		String domesticTitleFound; 
		String roamingTitleFound;
		
		List<WebElement> barChartTitles;
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


	public static void verifyLegendsOfCharts() {
		
		String domesticLegendFound; 
		String roamingLegendFound;
		String domesticOverageLegendFound;
		
		List<WebElement> legends;
		List<WebElement> categorySelectors = driver.findElements(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option"));
		
		for (int i = 0; i < 3; i++){
			
			categorySelectors.get(i).click(); 
			legends = driver.findElements(By.xpath(".//*[@class='highcharts-legend-item']"));
			// System.out.println("legends size: " + domesticLegends.size());
							
			domesticLegendFound = legends.get(0).getText(); 
			Assert.assertEquals(domesticLegendFound, domesticLegend);
			
			if (i == 0){
				
				domesticOverageLegendFound = legends.get(1).getText();
				Assert.assertEquals(domesticOverageLegendFound, domesticOverageLegend);
			
				roamingLegendFound = legends.get(2).getText();
				Assert.assertEquals(roamingLegendFound, roamingLegend);
				
				System.out.println("Voice Legends:..." + legends.get(0).getText() + ", " + legends.get(1).getText() + ", " + legends.get(2).getText());
				
			} 
			
			if (i == 1){
			
				roamingLegendFound = legends.get(1).getText();
				Assert.assertEquals(roamingLegendFound, roamingLegend);
				
				System.out.println("Data Legend..." + legends.get(0).getText() + ", " + legends.get(1).getText());
				
			}
				
			if (i == 2){
				
				roamingLegendFound = legends.get(1).getText();
				Assert.assertEquals(roamingLegendFound, roamingLegend);
				
				System.out.println("Messages Legend..." + legends.get(0).getText() + ", " + legends.get(1).getText());
				
			}
				
			
			
		}
		
		
	}


	
	// ******* CONTINUE HERE *****************
	
	public static void verifyVendorsListedInCharts() {
		
		
		
		
		
	}





	



	


	


	
	
	
	
	
	
}
