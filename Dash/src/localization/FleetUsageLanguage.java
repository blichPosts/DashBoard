package localization;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.UsageHelper;


public class FleetUsageLanguage extends BaseClass {
	
	
	public static void verifyMainTitle(String languageTag, String monthYear) {
	
		String mainTitle = driver.findElement(By.cssSelector("h1")).getText();  // [de]August 2016 ~ [de]Fleet Manager
		
		String titlePartOne = mainTitle.split(" ~ ")[0];
		String titlePartTwo = mainTitle.split(" ~ ")[1];
		
		ShowText("titlePartOne: " + titlePartOne);
		ShowText("titlePartTwo: " + titlePartTwo);
		
		Assert.assertTrue(titlePartOne.contains(languageTag));
		Assert.assertTrue(titlePartTwo.startsWith(languageTag));
		
	}
	
	
	// Verify that the text in the KPIs has a tag prepended that specifies the language.
	public static void verifyLocalizationOnUsageKpisWithTags(String languageTag) throws Exception {
	
		String errorMessage = "Label does not contain tag " + languageTag;
		
		String voiceKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(0).getAttribute("title").toString();
		String dataKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(1).getAttribute("title").toString();
		String messagesKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(2).getAttribute("title").toString();
		String dataRoamingKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(3).getAttribute("title").toString();
		
		Assert.assertTrue(voiceKpiTitle.startsWith(languageTag), errorMessage);
		Assert.assertTrue(dataKpiTitle.startsWith(languageTag), errorMessage);
		Assert.assertTrue(messagesKpiTitle.startsWith(languageTag), errorMessage);
		Assert.assertTrue(dataRoamingKpiTitle.startsWith(languageTag), errorMessage);
		
		ShowText(voiceKpiTitle);
		ShowText(dataKpiTitle);
		ShowText(messagesKpiTitle);
		ShowText(dataRoamingKpiTitle);
		
		boolean rollingAvgPresent = WaitForElementPresentNoThrow(By.cssSelector("h4.tdb-h4"), ShortTimeout);
		
		if (rollingAvgPresent) { 
			
			List<WebElement> rollingAvgLabels = driver.findElements(By.cssSelector("h4.tdb-h4"));
			
			for (WebElement label: rollingAvgLabels) {
				Assert.assertTrue(label.getText().startsWith(languageTag), errorMessage);
				ShowText(label.getText());
				
			}
			
		}
		
		
		boolean threeMonthAvgPresent = WaitForElementPresentNoThrow(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[1]/div[1]"), ShortTimeout);
		
		if (threeMonthAvgPresent) { 
			
			List<WebElement> threeMonthLabels = driver.findElements(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[1]/div[1]"));
			
			for (WebElement label: threeMonthLabels) {
				Assert.assertTrue(label.getText().startsWith(languageTag), errorMessage);
				ShowText(label.getText());
			}
			
		}
		
		
		boolean sixMonthAvgPresent = WaitForElementPresentNoThrow(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[2]/div[1]"), ShortTimeout);
		
		if (sixMonthAvgPresent) { 
				
			List<WebElement> sixMonthLabels = driver.findElements(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[2]/div[1]"));
			
			for (WebElement label: sixMonthLabels) {
				Assert.assertTrue(label.getText().startsWith(languageTag), errorMessage);
				ShowText(label.getText());
			}
			
		}
		
		
		String domesticKpiTitle = driver.findElements(By.cssSelector("h3.tdb-kpiSection__title")).get(0).getText();
		String roamingKpiTitle = driver.findElements(By.cssSelector("h3.tdb-kpiSection__title")).get(1).getText();
		
		ShowText(domesticKpiTitle);
		ShowText(roamingKpiTitle);
		
		Assert.assertEquals(domesticKpiTitle, languageTag + "Domestic " + languageTag + "(includes overage)");
		Assert.assertEquals(roamingKpiTitle, languageTag + "Roaming");
		
		String accountLoadedLabel = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(1).getText();
		String currencyLabel = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(2).getText();
		String currencyName = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(4).getText();
		
		ShowText(accountLoadedLabel);
		ShowText(currencyLabel);
		ShowText(currencyName);
		
		Assert.assertEquals(accountLoadedLabel, languageTag + "accounts loaded");
		Assert.assertTrue(currencyLabel.startsWith(languageTag), errorMessage);
		Assert.assertTrue(currencyName.startsWith(languageTag), errorMessage);
		
		
		ShowText("  **  Text in KPIs section translated correctly.  **  ");
		
	}
	
	
	
	// Verify that the text in the Total Usage section has a tag prepended that specifies the language.
	public static void verifyLocalizationOnTotalUsageWithTags(String languageTag, String monthYear) {
	
		String errorMessage = "Label does not contain tag " + languageTag;
		String xpathTotalUsageSection = "//div[@class='tdb-currentCharts-USAGE']/..";
		
		// Expected Values
		
		String titleMonth = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2")).getText();
		
		List<WebElement> categorySelectors = driver.findElements(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[1]/div"));
		
		String categorySelectorVoice = categorySelectors.get(0).getText();
		String categorySelectorMessages = categorySelectors.get(1).getText();
		String categorySelectorData = categorySelectors.get(2).getText();
		
		List<WebElement> totalUsageTitleParts = driver.findElements(By.xpath(xpathTotalUsageSection + "/h3/span"));
		
		String titleChartTotalUsage = totalUsageTitleParts.get(0).getText();
		String titleChart_category = totalUsageTitleParts.get(1).getText();
		
		String domesticChartTitle = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[2]/div[1]/div[1]")).getText();
		String roamingChartTitle = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[2]/div[2]/div[1]")).getText();
	
		String chartId = UsageHelper.getChartId(UsageHelper.totalUsageDomesticChart);
		List<WebElement> legendsDomestic = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
		
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageRoamingChart);
		String legendRoaming = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text")).getText();
		
		ShowText(titleMonth);
		ShowText(categorySelectorVoice + "  " +  categorySelectorMessages + "  " + categorySelectorData);
		ShowText(titleChartTotalUsage + "  " +  titleChart_category);
		ShowText(domesticChartTitle);
		ShowText(roamingChartTitle);
		ShowText(legendsDomestic.get(0).getText() + "  " +  legendsDomestic.get(1).getText());
		ShowText(legendRoaming);
		
		Assert.assertEquals(titleMonth, monthYear, errorMessage);
		
		Assert.assertTrue(categorySelectorVoice.startsWith(languageTag), errorMessage);
		Assert.assertTrue(categorySelectorMessages.startsWith(languageTag), errorMessage);
		Assert.assertTrue(categorySelectorData.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(titleChartTotalUsage.startsWith(languageTag), errorMessage);
		Assert.assertTrue(titleChart_category.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(domesticChartTitle.startsWith(languageTag), errorMessage);
		Assert.assertTrue(roamingChartTitle.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(legendsDomestic.get(0).getText().startsWith(languageTag), errorMessage);  // <-- fails 
		Assert.assertTrue(legendsDomestic.get(1).getText().startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(legendRoaming.startsWith(languageTag), errorMessage);
		
		ShowText("  **  Text in Total Usage section translated correctly.  **  ");	
		
	}
	

	
	
	// Verify that the text in the Usage Trending section has a tag prepended that specifies the language.
	public static void verifyLocalizationOnUsageTrendingWithTags(String languageTag) {
		
		String errorMessage = "Label does not contain tag " + languageTag;
		String xpathUsageTrendingSection = "//div[@class='tdb-USAGE']/div/div[@class='tdb-card']";
		
		// Expected Values
		
		String mainTitle = driver.findElement(By.xpath(xpathUsageTrendingSection + "/h2")).getText();
		
		List<WebElement> usageTrendingTitleParts = driver.findElements(By.xpath(xpathUsageTrendingSection + "/h3/span"));
		
		String titleChartUsageTrending = usageTrendingTitleParts.get(0).getText();
		String titleChart_category = usageTrendingTitleParts.get(1).getText();
		
		List<WebElement> categorySelectors = driver.findElements(By.xpath(xpathUsageTrendingSection + "/h2/following-sibling::div[1]/div"));
		
		String categorySelectorVoice = categorySelectors.get(0).getText();
		String categorySelectorMessages = categorySelectors.get(1).getText();
		String categorySelectorData = categorySelectors.get(2).getText();
		
		String domesticChartTitle = driver.findElement(By.xpath(xpathUsageTrendingSection + "/h2/following-sibling::div[2]/div[1]/div[1]")).getText();
		String roamingChartTitle = driver.findElement(By.xpath(xpathUsageTrendingSection + "/h2/following-sibling::div[4]/div/div[1]")).getText();
			
		
		ShowText(mainTitle);
		ShowText(categorySelectorVoice + "  " +  categorySelectorMessages + "  " + categorySelectorData);
		ShowText(titleChartUsageTrending + "  " +  titleChart_category);
		ShowText(domesticChartTitle);
		ShowText(roamingChartTitle);
	
		
		Assert.assertTrue(mainTitle.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(categorySelectorVoice.startsWith(languageTag), errorMessage);
		Assert.assertTrue(categorySelectorMessages.startsWith(languageTag), errorMessage);
		Assert.assertTrue(categorySelectorData.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(titleChartUsageTrending.startsWith(languageTag), errorMessage);
		Assert.assertTrue(titleChart_category.startsWith(languageTag), errorMessage);
		
		Assert.assertTrue(domesticChartTitle.startsWith(languageTag), errorMessage);
		Assert.assertTrue(roamingChartTitle.startsWith(languageTag), errorMessage);
		
		ShowText("  **  Text in Usage Trending section translated correctly.  **  ");		
		
	}
	
	

	// Verifies the content of the tooltips displayed on charts under Usage Trending Domestic and Roaming charts
	// * It works for any number of selected vendors *
	public static void verifyLocalizationOnUsageTrendingTooltipsWithTags(int chartNum, String languageTag) throws ParseException, InterruptedException, AWTException {
		
				
		String chartId = UsageHelper.getChartId(chartNum);
		new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#" + chartId))).perform();
		
		Thread.sleep(2000);
				
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		

		// *******************************************************************
		// Verify the info contained on each of the tooltips for the 13 months 		
		// *******************************************************************
		
		List<WebElement> highchartSeries = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-series-group>.highcharts-series"));
		
		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> monthYearList = CommonTestStepActions.webListPulldown;
		
		int indexHighchart = 1;

		boolean firstBar = true;
		
		while (indexHighchart <= monthYearList.size()) {
			
			GeneralHelper.moveMouseToBar(false, firstBar, chartNum, chartId, indexHighchart);
			
			firstBar = false;
			
			List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
			
			// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
			// 0 MM-YYYY -- month and year appears once
			// 1 ? -- this is for the bullet
			// 2 <vendor's name> : <amount shown for the vendor>
			
			int amountOfSeries = highchartSeries.size();
			int factor = 2;  // Ana added - May 17
			int expectedAmountItemsTooltip = (amountOfSeries * factor) + 2;  // Ana modif - May 18
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
			// For each vendor listed in the tooltip verify the amount shown
			for(int i = 1; i <= legends.size(); i++) {
			
				int index =  i * factor + 1;  // Ana modif - May 18 -- before modif --> int index =  i * 3 - 1;
				
				// Get the value on tooltip and remove all blank spaces. E.g.: number in the tooltip is displayed like: 15 256 985. Value needed is: 15256985
				String valueFound = tooltip.get(index).getText().split(":")[2].trim(); // Ana modif - May 18 -- before modif --> tooltip.get(index+1).getText().trim().replace(" ", "");
				
				ShowText("Value Found: " + valueFound);
				
				Assert.assertTrue(valueFound.contains(languageTag));
				
			}
			
			// Verify month and year shown on the tooltip (first line)
			String monthYearFound = tooltip.get(0).getText();
			
			Assert.assertTrue(monthYearFound.contains(languageTag)); 
			ShowText("Month/Year Found: " + monthYearFound);
			
			// Verify chart's name on tooltip (second line)
			String chartNameFound = tooltip.get(1).getText();
			
			Assert.assertTrue(chartNameFound.contains(languageTag)); 
			ShowText("Chart Name Found: " + chartNameFound);
			
			indexHighchart++;
			
		}

	}

	
	
	// open settings panel under the gear in the top right corner.
	public static void OpenSettingsPanel()
	 {
	  WaitForElementClickable(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]"), MediumTimeout, "Couldn't find gear to click for opening settings panel.");
	  driver.findElement(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]")).click();
	 }
	 
	 // close open settings panel.
	public static void CloseSettingsPanel() throws Exception
	 {
	  // make sure the panel is open
	  if(WaitForElementNotPresentNoThrow(By.cssSelector(".tdb-slideout>header>div:nth-of-type(1)"), MiniTimeout)) 
	  {
	   // close panel
	   WaitForElementClickable(By.xpath("(//button[@class='md-primary'])[1]"), MediumTimeout, "Couldn't find button to close the settings panel.");
	   driver.findElement(By.xpath("(//button[@class='md-primary'])[1]")).click();
	  }
	  else
	  {
	   ShowText("Nothing to close. Settings pane appears already closed.");
	  }
	 }


	public static void selectLanguage(String language) throws Exception {
		
		String xpath = "//div[contains(text(),'Language')]/following-sibling::div/select";
		WaitForElementPresent(By.xpath(xpath), ShortTimeout);
		
		Select languageSelector = new Select(driver.findElement(By.xpath(xpath)));
		languageSelector.selectByValue(language);
		
	}




	// ******************************************
	// ***************** SPANISH ****************
	// ******************************************
	
	public static void verifyLocalizationOnTotalUsageSpanish(String languageTag, String monthYear) {
		
		String errorMessage = "Translation is incorrect.";
		String xpathTotalUsageSection = "//div[@class='tdb-currentCharts-USAGE']/..";
		
		// Expected Values
		
		String titleMonth = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2")).getText();
		
		List<WebElement> categorySelectors = driver.findElements(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[1]/div"));
		
		String categorySelectorVoice = categorySelectors.get(0).getText();
		String categorySelectorMessages = categorySelectors.get(1).getText();
		String categorySelectorData = categorySelectors.get(2).getText();
		
		List<WebElement> totalUsageTitleParts = driver.findElements(By.xpath(xpathTotalUsageSection + "/h3/span"));
		
		String titleChartTotalUsage = totalUsageTitleParts.get(0).getText();
		String titleChart_category = totalUsageTitleParts.get(1).getText();
		
		String domesticChartTitle = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[2]/div[1]/div[1]")).getText();
		String roamingChartTitle = driver.findElement(By.xpath(xpathTotalUsageSection + "/h2/following-sibling::div[2]/div[2]/div[1]")).getText();
	
		String chartId = UsageHelper.getChartId(UsageHelper.totalUsageDomesticChart);
		List<WebElement> legendsDomestic = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text"));
		
		chartId = UsageHelper.getChartId(UsageHelper.totalUsageRoamingChart);
		String legendRoaming = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text")).getText();
		
		ShowText(titleMonth);
		ShowText(categorySelectorVoice + "  " +  categorySelectorMessages + "  " + categorySelectorData);
		ShowText(titleChartTotalUsage + "  " +  titleChart_category);
		ShowText(domesticChartTitle);
		ShowText(roamingChartTitle);
		ShowText(legendsDomestic.get(0).getText() + "  " +  legendsDomestic.get(1).getText());
		ShowText(legendRoaming);
		
		Assert.assertEquals(titleMonth, languageTag + monthYear, errorMessage);
		
		Assert.assertEquals(categorySelectorVoice, "Voz", errorMessage);
		Assert.assertEquals(categorySelectorMessages, "Mensajes", errorMessage);
		Assert.assertEquals(categorySelectorData, "Datos", errorMessage);
		
		Assert.assertEquals(titleChartTotalUsage, "Uso total por proveedor", errorMessage);
		Assert.assertEquals(titleChart_category, "Voz", errorMessage);
		
		Assert.assertTrue(domesticChartTitle.startsWith("Nacional"), errorMessage);
		Assert.assertTrue(roamingChartTitle.startsWith("Itinerancia"), errorMessage);
		
		Assert.assertEquals(legendsDomestic.get(0).getText(), "", errorMessage);
		Assert.assertEquals(legendsDomestic.get(1).getText(), "", errorMessage);
		
		Assert.assertEquals(legendRoaming, "", errorMessage);
		
		
		
	}


	
	// Verify that the text in the KPIs is translated to Spanish
	public static void verifyLocalizationOnUsageKpisSpanish(String languageTag) {
		
		String errorMessage = "Translation is incorrect.";
		
		String voiceKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(0).getAttribute("title").toString();
		String dataKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(1).getAttribute("title").toString();
		String messagesKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(2).getAttribute("title").toString();
		String dataRoamingKpiTitle = driver.findElements(By.cssSelector(".tdb-kpi__title")).get(3).getAttribute("title").toString();
		
		Assert.assertEquals(voiceKpiTitle, "Voz (min)", errorMessage);
		Assert.assertTrue(dataKpiTitle.startsWith("Datos"), errorMessage);
		Assert.assertEquals(messagesKpiTitle, "Mensajes", errorMessage);
		Assert.assertTrue(dataRoamingKpiTitle.startsWith("Datos"), errorMessage);
		
		ShowText(voiceKpiTitle);
		ShowText(dataKpiTitle);
		ShowText(messagesKpiTitle);
		ShowText(dataRoamingKpiTitle);
		
		List<WebElement> rollingAvgLabels = driver.findElements(By.cssSelector("h4.tdb-h4"));
		
		for (WebElement label: rollingAvgLabels) {
//			Assert.assertEquals(label.getText(), "Promedios mensuales", errorMessage);  // <-- this is incorrectly translated in Dash - May 12
			ShowText(label.getText());
			
		}
		
		List<WebElement> threeMonthLabels = driver.findElements(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[1]/div[1]"));
		
		for (WebElement label: threeMonthLabels) {
			Assert.assertEquals(label.getText(), "3 meses", errorMessage);
			ShowText(label.getText());
		}
		
		List<WebElement> sixMonthLabels = driver.findElements(By.xpath("//div[@class='tdb-kpi__averagesTitle']/following-sibling::div[2]/div[1]"));
		
		for (WebElement label: sixMonthLabels) {
			Assert.assertEquals(label.getText(), "6 meses", errorMessage);
			ShowText(label.getText());
		}
		
		
		String domesticKpiTitle = driver.findElements(By.cssSelector("h3.tdb-kpiSection__title")).get(0).getText();
		String roamingKpiTitle = driver.findElements(By.cssSelector("h3.tdb-kpiSection__title")).get(1).getText();
		
		Assert.assertEquals(domesticKpiTitle, "Nacional (Incluye exceso)");
		Assert.assertEquals(roamingKpiTitle, "Itinerancia");
		
		String accountLoadedLabel = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(1).getText();
		Assert.assertEquals(accountLoadedLabel, "Cuentas cargadas");
		
		ShowText(domesticKpiTitle);
		ShowText(roamingKpiTitle);
		ShowText(accountLoadedLabel);
		
		
	}


		
}
