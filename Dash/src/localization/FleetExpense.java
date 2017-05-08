package localization;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

import org.testng.Assert;

public class FleetExpense extends BaseClass 
{

	// public static String kpiOneTitle = "";	
	public static List<String> tempList = new ArrayList<String>();
	public static String chartId = "";
	public static String startsWith = "[es]";
	public static List<WebElement> eleList;
	
	public static void selectLanguage(String language) throws Exception 
	{
		  String xpath = "//div[contains(text(),'Language')]/following-sibling::div/select";
		  WaitForElementPresent(By.xpath(xpath), ShortTimeout);
		  new Select(driver.findElement(By.xpath(xpath))).selectByValue(language);
	 }
	
	public static void KpiTitleTitle()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-kpi__title'])[1]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[2]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[3]");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileRolling()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileThreeMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileSixMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		VerifyTextXpath(tempList);
	}
	
	public static void TwoMainTitles()
	{
		tempList.clear();
		tempList.add("(//h2[@class='tdb-h2'])[1]");
		tempList.add("(//h2[@class='tdb-h2'])[2]");
		VerifyTextXpath(tempList);
	}

	
	public static void SubTitles()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-h3'])[1]");
		tempList.add("(//h3[@class='tdb-h3'])[2]");
		tempList.add("(//h3[@class='tdb-h3'])[3]"); // 2 occurs
		tempList.add("(//h3[@class='tdb-h3'])[4]"); // 2 occurs
		tempList.add("(//h3[@class='tdb-h3'])[5]");
		
		int[] intArr = new int[] {1, 1, 2, 2, 1}; // this is for the expected number of 'startsWith'. 
		
		VerifyTextXpathWithCount(tempList, intArr);
	}
	
	public static void TopSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.xpath("(//div[@class='tdb-card'])[2]/div[1]/div"));
		ShowWebElementListText(eleList);
		
		//VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void BottomSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(3)"));
		//ShowWebElementListText(eleList);
		//VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void VendorSpendLegends()
	{
		chartId = UsageHelper.getChartId(1);
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		ShowWebElementListText(eleList);
		//VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void PieLegends()
	{
		chartId = UsageHelper.getChartId(0);
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		ShowWebElementListText(eleList);
		//VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void AllTrendLegends()
	{
		chartId = UsageHelper.getChartId(2); // expense ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));
		
		ShowWebElementListText(eleList);  
		//VerifyListOfElementsStartsWith(eleList);
		
		chartId = UsageHelper.getChartId(3); // cost per service numbers ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		//VerifyListOfElementsStartsWith(eleList);
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		
		chartId = UsageHelper.getChartId(4); // count of service numbers ---------------------------------------------------------------------------- 
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));		
		
		ShowWebElementListText(eleList);
		//VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void OddsAndEnds()
	{
		// 'view by hierarchy link'
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith));
		
		// countries
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));
		
		// none text
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText().startsWith(startsWith));

		// vendor view
		Assert.assertTrue(driver.findElement(By.cssSelector("#md-tab-label-1-0>span")).getText().startsWith(startsWith));

		// country view
		Assert.assertTrue(driver.findElement(By.cssSelector("#md-tab-label-1-1>span")).getText().startsWith(startsWith));
		
	}
	
	// verify language text titles and 'Display' text.
	public static void VerifySomeTextInSettingsPanel() throws Exception
	{
		WaitForElementVisible(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)"), TenTimeout);
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)")).getText(), "Language:", "");
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(3)>div:nth-of-type(1)")).getText(), "Currency:", "");
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(4)")).getText(), "Display", "");
	}	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												Helpers 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyTextXpath(List<String> listIn)
	{
		for(String str : listIn)
		{
			ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
		}
	}
	
	public static void VerifyTextXpathWithCount(List<String> listIn, int[] arrIn)
	{
		int x = 0;
		
		for(String str : listIn)
		{
			ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
			
			GetNumberOf(driver.findElement(By.xpath(str)).getText());

			Assert.assertTrue(GetNumberOf(driver.findElement(By.xpath(str)).getText()) == arrIn[x], "Falied to find correct number of 'startsWith' string.");
			x++;
		}
	}
	
	// this verifies list of web elements text sent in start with variable 'startsWith'.
	public static void VerifyListOfElementsStartsWith(List<WebElement> listIn)
	{
		for(WebElement ele : eleList)
		{
			Assert.assertTrue(ele.getText().startsWith(startsWith));
		}
	
	}
	
	// http://stackoverflow.com/questions/24288809/stringutils-countmatches-words-starting-with-a-string
	public static int GetNumberOf(String strIn) // common methods.
	{
		 Matcher matcher = Pattern.compile("\\[" + "es" + "\\]").matcher(strIn);
		
		int countr = 0;
		while (matcher.find()) 
		{
		    countr++;
		}	
		
		return countr;			
	}
	
	public static void ClearEleList() 
	{
		if(eleList != null)
		{
			eleList.clear();
		}
	}
	
	
	public static void LoopThroughMonths() throws InterruptedException
	{
		CommonTestStepActions.initializeMonthSelector();
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			ShowText("Selected Month is " + ele.getText());
			
			Thread.sleep(3000);

			if(Integer.parseInt(driver.findElement(By.xpath("(//div[@class='tdb-kpi__footerItem'])[1]/span[1]")).getText()) > 0)
			{
				RunExpenseLocalizationTagTests();		
			}
			else
			{
				ShowText("Month " + ele.getText() + " is skipped.");
			}
			
			ShowText("Month Complete.");
		}
		
	}
	
	public static void RunExpenseLocalizationTagTests()
	{
		// TITLE !!!!! ACCOUNTS LOADED ----- Currency: USD (United States Dollar) 
		//FleetExpense.KpiTitleTitle();
		//FleetExpense.KpiTileRolling();
		//FleetExpense.KpiTileThreeMonth();
		//FleetExpense.KpiTileSixMonth();
		//FleetExpense.TwoMainTitles();
		// FleetExpense.SubTitles();
		// Above are complete.
		
		//FleetExpense.TopSelectors();
		//FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();
		// FleetExpense.VendorSpendLegends();
		FleetExpense.AllTrendLegends();
	}
}
