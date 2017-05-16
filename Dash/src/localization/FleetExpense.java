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
	public static String startsWith = "";
	public static String insideLanguageTag = "";
	public static List<WebElement> eleList;
	public static boolean rollingAverageVisible = false;
	public static boolean sixMonthVisible = false;
	public static boolean threeMonthVisible = false;
	public static String expectedCurrency = ""; 
	
	// public static viewType vType;
	
	
	/*
	public static enum viewType
	{
		country,
		vendor
	}
	*/
	
	/*
	public static void SetViewType (viewType vtyp)
	{
		vType = vtyp;
	}
	*/
	
	
	public static void ExpenseTrendingLegends()
	{
		chartId = UsageHelper.getChartId(1);

		
		// spend category.
		
		// #highcharts-gi9txod-58>svg>g.highcharts-legend>g>g>g
		
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g")); 
		
		ShowWebElementListText(eleList);
		
		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText());
		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText());
		
		//Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText().startsWith(startsWith));
		//Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText().startsWith(startsWith));
	}

	
	public static void SetupLanguageTag(String languageTag) throws Exception 
	{
		startsWith = languageTag;
	}
	
	public static void SetupInsideTag(String insideTag) throws Exception 
	{
		insideLanguageTag = insideTag;
	}
	
	
	public static void SetCurrency(String currencyString) throws Exception 
	{
		expectedCurrency = currencyString;
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
		if(!rollingAverageVisible)
		{
			return;
		}
		ShowText("Run Rolling");
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[2]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[3]/h4");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileThreeMonth()
	{
		if(!rollingAverageVisible)
		{
			return;
		}
		if(!threeMonthVisible)
		{
			return;
		}

		if(sixMonthVisible)
		{
			ShowText("Run three month");
			tempList.clear();
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
			VerifyTextXpath(tempList);
		}
		else
		{
			ShowText("Run three month");
			tempList.clear();
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			VerifyTextXpath(tempList);
			
		}
	}
	
	public static void KpiTileSixMonth()
	{
		if(!rollingAverageVisible)
		{
			return;
		}
		if(!sixMonthVisible)
		{
			return;
		}
		ShowText("Run six month");
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		VerifyTextXpath(tempList);
	}
	
	public static void TwoMainTitles()
	{
		tempList.clear();
		//tempList.add("");
		tempList.add("(//h2[@class='tdb-h2'])[2]");
		VerifyTextXpath(tempList);
	
		if(startsWith.equals("[ja]"))
		{
			String []  strData = driver.findElement(By.xpath("//h2[@class='tdb-h2']")).getText().split(" ");
			Assert.assertTrue(strData[1].startsWith(startsWith));
		}
		else
		{
			////h2[@class='tdb-h2'])[2]
			Assert.assertTrue(driver.findElement(By.xpath("(//h2[@class='tdb-h2'])[1]")).getText().startsWith(startsWith));
		}
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
		// ShowWebElementListText(eleList);
		
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void BottomSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(3)"));
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void VendorSpendLegends() throws Exception
	{
		chartId = UsageHelper.getChartId(1);
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
		// Pause("");
	}
	
	public static void PieLegends()
	{
		chartId = UsageHelper.getChartId(0);
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void AllTrendLegends()
	{
		chartId = UsageHelper.getChartId(2); // expense ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));
		
		// ShowWebElementListText(eleList);  
		VerifyListOfElementsStartsWith(eleList);
		
		chartId = UsageHelper.getChartId(3); // cost per service numbers ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		
		chartId = UsageHelper.getChartId(4); // count of service numbers ---------------------------------------------------------------------------- 
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));		
		
		// ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void OddsAndEnds() throws Exception
	{
		
		// 'view by hierarchy link' - this is already converted.
		// ShowText("-- " + driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText());
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith.toUpperCase()));

		
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-pov__heading")).getText());		
		// countries
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));


		// none text
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText().startsWith(startsWith.toUpperCase()));
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText());
		
		// month selector
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText());

		// below gets the text for vendor and country view strings.
		String [] strArray = driver.findElement(By.cssSelector(".md-tab-header")).getText().split("\n");
		
		for(String str : strArray)
		{
			Assert.assertTrue(str.startsWith(startsWith.toUpperCase()));
			// ShowText(str);
		}
		
		
		// .tdb-pov__monthLabel
		

		
			// below gets the text for vendor and country view strings.
			//String [] strArray = driver.findElement(By.cssSelector(".md-tab-header")).getText().split("\n");
			//ShowArray(strArray);
			

		//Pause("");

		
	}
	


	
	// verify language text titles and 'Display' text.
	public static void VerifySomeTextInSettingsPanel() throws Exception
	{
		DebugTimeout(5, "Wait 5 in error spot");
		
		//WaitForElementVisible(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)"), TenTimeout);
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)")).getText(), "Language:", "");
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(3)>div:nth-of-type(1)")).getText(), "Currency:", "");
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(4)")).getText(), "Display", "");
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
			// ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
			
			GetNumberOf(driver.findElement(By.xpath(str)).getText());

			Assert.assertTrue(GetNumberOf(driver.findElement(By.xpath(str)).getText()) == arrIn[x], "Falied to find correct number of 'startsWith' string.");
			x++;
		}
	}
	
	// this verifies list of web elements text sent in start with variable 'startsWith'.
	public static void VerifyListOfElementsStartsWith(List<WebElement> listIn)
	{
		for(WebElement ele : listIn)
		{
			Assert.assertTrue(ele.getText().startsWith(startsWith));
		}
	
	}
	
	// http://stackoverflow.com/questions/24288809/stringutils-countmatches-words-starting-with-a-string
	public static int GetNumberOf(String strIn) // common methods.
	{
		 Matcher matcher = Pattern.compile("\\[" + insideLanguageTag + "\\]").matcher(strIn);
		
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
	
	
	public static void LoopThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		
		ShowInt(CommonTestStepActions.webListPulldown.size());
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			ShowText("Selected Month is " + ele.getText() + " ****************************************************");
			
			Thread.sleep(3000);

			InitVisibilityTileMapAverages(); // find what is showing in the KPI tiles and set booleans.
			
			if(Integer.parseInt(driver.findElement(By.xpath("(//div[@class='tdb-kpi__footerItem'])[1]/span[1]")).getText()) > 0)
			{
				ShowText("Month " + ele.getText() + " - running test with data. ");
				RunExpenseLocalizationTagTests();		
			}
			else
			{
				ShowText("Month " + ele.getText() + " - no data test being run. ");
				RunExpenseLocalizationNoDataTagTests();
			}
			
			ShowText("Month Complete.");
			// Pause("Check Flags.");
		}
		
	}
	
	public static void Currency() throws Exception
	{
		//(//span[@class='tdb-kpi__footerItem--value'])[2]
		
		Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='tdb-kpi__footerItem--value'])[2]")).getText(), expectedCurrency,"");
	}
	
	public static void InitVisibilityTileMapAverages() throws Exception
	{
		rollingAverageVisible = false;
		threeMonthVisible = false;
		sixMonthVisible = false;
		
		rollingAverageVisible = WaitForElementPresentNoThrow(By.xpath("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4"), MiniTimeout);
		
		if(!rollingAverageVisible)
		{
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 6)
		{
			threeMonthVisible = true;
			sixMonthVisible = true;
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 3)
		{
			threeMonthVisible = true;
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
	}
	
	public static void VerifyCurrency()
	{

		
		
	}
	
	public static void RunExpenseLocalizationTagTests() throws Exception
	{
		// TITLE !!!!! ACCOUNTS LOADED ----- Currency: USD (United States Dollar) 
		FleetExpense.KpiTitleTitle();
		FleetExpense.KpiTileRolling();
		FleetExpense.KpiTileThreeMonth();
		FleetExpense.KpiTileSixMonth();
		FleetExpense.TwoMainTitles(); // bladdxx
		FleetExpense.SubTitles();
		// Above are complete.
		
		FleetExpense.TopSelectors();
		FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();
		FleetExpense.VendorSpendLegends();
		//FleetExpense.AllTrendLegends();
		FleetExpense.OddsAndEnds();
		Currency();
	}
	
	// only test what's showing when no data is available.
	public static void RunExpenseLocalizationNoDataTagTests() throws Exception
	{
		// TITLE !!!!! ACCOUNTS LOADED ----- Currency: USD (United States Dollar) 
		FleetExpense.KpiTitleTitle();
		FleetExpense.KpiTileRolling();
		FleetExpense.KpiTileThreeMonth();
		FleetExpense.KpiTileSixMonth();
		FleetExpense.TwoMainTitles(); //  bladdxx
		FleetExpense.SubTitles();
		Currency();
		
		FleetExpense.TopSelectors();
		FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();  // --------------- comment
		FleetExpense.VendorSpendLegends();
		//FleetExpense.AllTrendLegends(); // ------------ comment
		FleetExpense.OddsAndEnds(); // -------------- comment
	}
	
}
