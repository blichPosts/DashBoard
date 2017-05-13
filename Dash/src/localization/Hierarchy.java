package localization;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

public class Hierarchy extends BaseClass 
{

	// public static String kpiOneTitle = "";	
	//public static List<String> tempList = new ArrayList<String>();
	//public static String chartId = "";
	//public static String startsWith = "[es]";
	//public static List<WebElement> eleList;	
	
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

	public static void KpiTitleTitleAndMainTitle()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-kpi__title'])[1]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[2]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[3]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[4]");
		VerifyTextXpath(tempList);
		
		// this is title above the KPIs.
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-kpi__header>span")).getText().startsWith(startsWith));
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
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[4]/h4");
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
		tempList.clear();
		
		if(sixMonthVisible)
		{
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[7]");
		}
		else
		{
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		}
		
		
		VerifyTextXpath(tempList);
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
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[8]");
		VerifyTextXpath(tempList);
	}

	public static void KpiTileAllocation()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[1]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[2]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[3]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[4]");
		VerifyTextXpath(tempList);
	}
	
	public static void OddsAndEnds() throws Exception
	{
		// 'view by vendor link'
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith));
		
		// countries - not in hierarchy.
		// Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));
		
		// none text - not in hierarchy.
		// Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText().startsWith(startsWith));

		// month selector
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));

		// hierarchy text by pulldown -- NOTE!!! -when get tags see what comes back - are there tags in the pulldown. 
		//Assert.assertTrue(driver.findElement(By.csXXXXXXXXXXXXXXXXsSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));
		//ShowText(driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText());
		//String []  strArray = driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText().split("\n");
		//ShowArray(strArray);
		
		// dependent units.
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));

		// hierarchy label next to pulldown
		Assert.assertTrue(driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText().split("\n")[0].startsWith(startsWith));
		
		// hierarchy label next to pulldown
		//ShowText(driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText().split("\n")[0]);
		
		//String []  strArray = driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText().split("\n");
		//ShowArray(strArray);
		
		// //div[@class='tdb-space--top']
		
		
		// max displayed left column
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-text--xsmall.tdb-text--lighter")).getText().startsWith(startsWith));
		
		// max displayed right side
		Assert.assertTrue(driver.findElement(By.xpath("//span[@class='tdb-text--lighter']")).getText().startsWith(startsWith));
		
		//ShowText(driver.findElement(By.cssSelector(".tdb-text--xsmall.tdb-text--lighter")).getText());
		
		// .tdb-text--xsmall.tdb-text--lighter
		// //div[@class='tdb-space--top']
	}	
	
	public static void LineDetailsText() throws Exception
	{
		// view line details report
		//ShowText(driver.findElement(By.xpath("(//div/a[contains(@class,'tdb-button--flat')])[2]")).getText());
		Assert.assertTrue(driver.findElement(By.xpath("(//div/a[contains(@class,'tdb-button--flat')])[2]")).getText().startsWith(startsWith));
	}
	
	
	public static void TileMapText() throws Exception
	{
		
		// main titles tile map and expense trend
		//ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText());
		//ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(2)")).getText());
		
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(2)")).getText().startsWith(startsWith));
	
		// sub title expense trend
		//ShowText(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]/span[1]")).getText());
		Assert.assertTrue(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]/span[2]")).getText().startsWith(startsWith));
		
		// text above tile map
		// ShowText(driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3>span:nth-of-type(1)")).getText());
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3>span:nth-of-type(1)")).getText().startsWith(startsWith));

		// max displayed above tile map
		//ShowText(driver.findElement(By.xpath("//span[@class='tdb-text--lighter']")).getText());
		Assert.assertTrue(driver.findElement(By.xpath("//span[@class='tdb-text--lighter']")).getText().startsWith(startsWith));
		
		// selectors above tile map
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[1]/div[1]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[1]/div[2]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[1]/div[3]")).getText().startsWith(startsWith));
		
		// selectors below tile map
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[3]/div[1]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[3]/div[2]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-card']/div)[3]/div[3]")).getText().startsWith(startsWith));
	}
	
	public static void ExpenseTrendingLegends()
	{
		chartId = UsageHelper.getChartId(1);

		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText());
		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText());
		
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText().startsWith(startsWith));
	}
	
	
	public static void InitVisibilityTileMapAverages() throws Exception
	{
		rollingAverageVisible = false;
		threeMonthVisible = false;
		sixMonthVisible = false;
		
		ShowText("Start InitVisibilityTileMapAverages");
		
		rollingAverageVisible = WaitForElementPresentNoThrow(By.xpath("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4"), MiniTimeout);
		
		if(!rollingAverageVisible)
		{
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 8)
		{
			threeMonthVisible = true;
			sixMonthVisible = true;
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 4)
		{
			threeMonthVisible = true;
			System.out.println(rollingAverageVisible);
			System.out.println(threeMonthVisible);
			System.out.println(sixMonthVisible);
			return;
		}
	}	
	
	
	public static void LoopThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			ShowText("Selected Month is " + ele.getText() + " ****************************************************");

			HierarchyHelper.WaitForProgressBarInactive(TenTimeout);

			Thread.sleep(3000);
			
			InitVisibilityTileMapAverages(); // find what is showing in the KPI tiles and set booleans.
			
			if(driver.findElement(By.cssSelector(".tdb-h3>span:nth-of-type(1)")).getText().contains("Top"))
			//if(Integer.parseInt(driver.findElement(By.xpath("(//div[@class='tdb-kpi__footerItem'])[1]/span[1]")).getText()) > 0)
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
		ShowText("Passed.");
	}
	

	
	
	// loop through the hierarchies 
	public static void LoopThroughHierarchies() throws Exception 
	{
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		ShowText(" ------------ Start Looping Through Hierarchies With Drilldowns. -----------------\n\n");
		
		// go through the available hierarchies one at a time.
		for(WebElement ele : hierarchyList)
		{
				ShowText(" -------------------------Hierarchy Name: " + ele.getText() + " ---------------------------------------- ");
				ele.click(); 

				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);

				LoopThroughCatergories();
				// Thread.sleep(2000); // wait for tile map numbers to fill in.
				

				
		}
		ShowText("Test Passed");
	}

	public static void LoopThroughCatergories() throws Exception 
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		Thread.sleep(500);
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			// if(x != 0){continue;}	// DEBUG - use this if you only want one category selected. 
			
			ShowText("Start test for category --- " + values[x].name() +".");
			
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.

			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
			Thread.sleep(2000); // wait for tile map numbers to fill in.
			
			ShowText("Pass complete for category --- " + values[x].name() +".");
		}
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
			// ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
		}
	}
	
	public static void RunTests() throws Exception
	{
		
	}
	
	
	public static void RunExpenseLocalizationTagTests() throws Exception
	{
		KpiTitleTitleAndMainTitle();
		KpiTileRolling();
		KpiTileThreeMonth();
		KpiTileSixMonth();
		KpiTileAllocation();
		OddsAndEnds();
		LineDetailsText();
		TileMapText();
		ExpenseTrendingLegends();
		
		// miss step.
	}	
	
	public static void RunExpenseLocalizationNoDataTagTests() throws Exception
	{
		KpiTitleTitleAndMainTitle();
		KpiTileRolling();
		KpiTileThreeMonth();
		KpiTileSixMonth();
		KpiTileAllocation();
		OddsAndEnds();
		TileMapText();
		ExpenseTrendingLegends();
	}
}
