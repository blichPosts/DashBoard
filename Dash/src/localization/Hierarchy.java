package localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import expenseHierarchy.HierarchyNumbersDependents;
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

	public static enum PageStatus
	{
		hasDependents,
		hasNoDependents
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

	// this is the small text  directly under the KPIs to the left.
	public static void CurrencySection() throws Exception
	{
	
		//Pause("top");
		
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[1]/span[2]")).getText());
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[2]")).getText());
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[1]")).getText());		
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[3]")).getText());
		
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[1]/span[2]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[2]")).getText().startsWith(startsWith));
		
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[1]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[3]")).getText().startsWith(startsWith));

		
		
		
		
		/*
		(//div[@class='tdb-flexContainer']/div)[1]/div[1]/span[2] // service nums -- OK
		(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[2] // invoices -- no
		(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[1] // currency -- OK
		(//div[@class='tdb-flexContainer']/div)[1]/div[3]/span[3]// currency name -- OK
		*/

		// Pause("");
		
	}
	
	public static void KpiTitleTitleAndMainTitle() throws Exception
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
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith.toUpperCase()));
		
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
		Assert.assertTrue(driver.findElement(By.xpath("(//div/a[contains(@class,'tdb-button--flat')])[2]")).getText().startsWith(startsWith.toUpperCase()));
	}
	
	public static void TileMapText() throws Exception
	{
		// main titles tile map and expense trend
		//ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText());
		//ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(2)")).getText());
		
		// pulldown month label.
		
		
		
		// month
		if(startsWith.equals("[ja]"))
		{
			Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText().split(" ")[1].startsWith(startsWith));			
		}
		else
		{
			driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText().startsWith(startsWith);
		}

			
		// trend main title
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(2)")).getText().startsWith(startsWith));
	
		// sub title expense trend
		//ShowText(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]/span[1]")).getText());
		Assert.assertTrue(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]/span[1]")).getText().startsWith(startsWith));
		
		// text above tile map
		// ShowText(driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3>span:nth-of-type(1)")).getText());
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3>span:nth-of-type(1)")).getText().startsWith(startsWith));

		// max displayed above dependent units. 
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-text--xsmall.tdb-text--lighter")).getText().startsWith(startsWith));
		
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

		
		// spend category.
		
		// #highcharts-gi9txod-58>svg>g.highcharts-legend>g>g>g
		
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g")); 
		
		ShowWebElementListText(eleList);
		
		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText());
		//ShowText(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText());
		
		//Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(1)>text>tspan")).getText().startsWith(startsWith));
		//Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(2)>text>tspan")).getText().startsWith(startsWith));
	}

	/*
	public static void TabSelectors() throws Exception // done in TileMapText
	{
		ShowText(driver.findElement(By.cssSelector("(//div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')])[1]/div[1]")).getText());
		
		Pause("Look ----");
		
		Assert.assertTrue(driver.findElement(By.cssSelector("(//div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')])[1]/div[1]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.cssSelector("(//div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')])[1]/div[2]")).getText().startsWith(startsWith));
		Assert.assertTrue(driver.findElement(By.cssSelector("(//div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')])[1]/div[3]")).getText().startsWith(startsWith));
		// (//div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')])[1]/div[1]
		
	}
	*/
	// //div[@class='tdb-card']/div[contains(@class,'tdb-boxSelector')]
	
	
	public static void VerifyDependentUnits() throws Exception
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector(".tdb-pov__itemList>li")); 
		
		for(WebElement ele : eleList)
		{

			//ShowText(ele.getText().split("\\[")[1]);
			//ShowText(ele.getText());
			Assert.assertTrue(ele.getText().split("\\[")[1].startsWith(insideLanguageTag + "]"));
		}
		ShowText("Dependent Units Done");		
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
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 8)
		{
			threeMonthVisible = true;
			sixMonthVisible = true;
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 4)
		{
			threeMonthVisible = true;
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
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

			HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout);

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
	// ////////////////////////////////////////////////////////////////
	// 					This has Drill Down
	// ////////////////////////////////////////////////////////////////
	public static void LoopThroughMonthsTwo() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			ShowText("Selected Month is " + ele.getText() + " ****************************************************");

			HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout);

			Thread.sleep(3000);
			
			InitVisibilityTileMapAverages(); // find what is showing in the KPI tiles and set booleans.
			
			if(driver.findElement(By.cssSelector(".tdb-h3>span:nth-of-type(1)")).getText().contains("Top"))
			//if(Integer.parseInt(driver.findElement(By.xpath("(//div[@class='tdb-kpi__footerItem'])[1]/span[1]")).getText()) > 0)
			{
				ShowText("Month " + ele.getText() + " - running test with data. ");
				//RunExpenseLocalizationTagTests();
				Hierarchy.DrillDownDependentUnitsTwo(3); // bladdzzz
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

				HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout);

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

			HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout);
			Thread.sleep(2000); // wait for tile map numbers to fill in.
			
			LoopThroughMonths();
			
			ShowText("Pass complete for category --- " + values[x].name() +".");
		}
	}
	
	
	// bladdzzz
	public static void DrillDownDependentUnitsTwo(int maxNumberOfLevels) throws Exception  
	{
		int dependentUnitToSelect;
		int cntr = 0;
		int numberOfDependentUnits = 0;
		
		Random rand = new Random();
		
		ShowText("%%%%%%%%%% STARTING  DRILL DOWN.");
		// Pause("Starting drill down in dependent units.");
		
		// drill down to maxNumberOfLevels or to a page where there are no dependent units listed. 
		while (cntr != maxNumberOfLevels)
		{
			numberOfDependentUnits =  driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)).size(); 

			if(numberOfDependentUnits == 0)
			{
				ShowText("No dependents found -- break from drill down loop. Ending drill down");
				cntr++;

				// clear the bread crumbs if they are present.
				// WaitForElementVisible(By.cssSelector(".breadcrumbs>span"), MediumTimeout); 
				//WaitForElementClickable(By.cssSelector(".breadcrumbs>span"), MediumTimeout, ""); 
				//driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type("  + 1 + ")")).click();

				break;
			}
			
			// System.out.println("# of dependents before click. " + numberOfDependentUnits);
			
			// get list of dependent units from the UI. get a random number to be used to pick one of the dependent unit.
			List<WebElement> unitsList = driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)); 
			dependentUnitToSelect = rand.nextInt(numberOfDependentUnits);
			
			System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **");
			
			// store this for checking string above tile map later.
			// currentLevelName = unitsList.get(dependentUnitToSelect).getText().split("\n")[0];  
			
			unitsList.get(dependentUnitToSelect).click(); // select dependent unit.
			
			// move to top of page to make the testing visible.
			WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
			new Actions(driver).moveToElement(topSection).perform();
			
			HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout); 

			DebugTimeout(3, "Wait 3 before run actual test.");
			
			ShowText("*********** RUN ALL TESTS **********");
			RunExpenseLocalizationTagTests();
			// RunExpenseLocalizationTagTests();
			
			// bladdzzz
			// wait for the new bread crumb to be added.
			// Assert.assertTrue(HierarchyNumbersDependents.WaitForCorrectBreadCrumbCount(cntr + 1, ShortTimeout), "Fail in wait for breadcrump count. Method is HierarchyNumbersDependents.WaitForCorrectBreadCrumbCount");
			
			ShowText("Checking to see if have hit end of drill down. HAVE RUN  TAG TEST ---------------------------------------."); 
			
			// bladdzzz
			// if have hit a page with no dependents then break.
			if(HierarchyNumbersDependents.WaitForNoDependentsInPage(cntr))
			{
				break;
			}
			cntr++;
		}
		
		ShowText("Have hit End Of Drill Down Loop. Remove breadcrumbs if they exist.");

		if(WaitForElementPresentNoThrow(By.cssSelector(".breadcrumbs>span"), MediumTimeout)) // bladdxx
		{
			// bladdzz
			WaitForElementClickable(By.cssSelector(".breadcrumbs>span"), MediumTimeout, ""); // sometimes getting "stale element reference" in 'numBreadCrumbs' line below.
			driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type("  + 1 + ")")).click();
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
			//ShowText(driver.findElement(By.xpath(str)).getText());
			if(driver.findElement(By.xpath(str)).getText().contains("allocated") || driver.findElement(By.xpath(str)).getText().contains("service"))
			{
				Assert.assertTrue(driver.findElement(By.xpath(str)).getText().split(" ")[1].startsWith(startsWith));
			}
			else
			{
				Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
			}
			
			
		}
	}
	
	//public static void RunTests() throws Exception
	//{
	//	
	//}
	
	
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
		//ExpenseTrendingLegends();
		CurrencySection();		
		VerifyDependentUnits();
		
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

		// ExpenseTrendingLegends();
	}
}
