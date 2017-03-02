package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.jetty.servlet.Debug;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.Child;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

public class HierarchyNumbersDependents extends BaseClass 
{

	public static int numberOfTileMapsToTest = 0;
	public static int maxNumberOfTileMapsThatCanBeTested = 100;
	public static int maxNumberOfTileMapsThatCanBeShown = 100;
	public static int tileNumberToStartLoop = 0;
	public static int maxLevelsToDo = 4;
	
	public static String dependentUnits = "";
	public static String nameAndIds = "";
	public static String numericValue = "";
	public static String tempString = "";
	public static String currentDependentUnitInfo = "";
	public static String hoverInfo = "";
	public static String filterString = "";
	public static String chartId = "";
	
	public static List<Child> childList = new ArrayList<Child>();
	
	public static double expectedValueDouble = 0;
	public static double actualValueDouble = 0;
	public static int createErrorCounter = 0; // this can be used in 'GetExpectedValueTwo'method to create an intentional error. 

	public static JavascriptExecutor js = (JavascriptExecutor) driver;	
	
	public static void TestPhaseOne() throws Exception
	{
		ShowText("Run Total Cost Filter --");
		
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesThree();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Total Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}

		ShowText("Run Optimizable Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Optimizable);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesThree();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Optimizable Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}

		ShowText("Run Roaming Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Roaming);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesThree();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Roaming Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}
	}
	
	public static void TestPhaseWithMonths() throws Exception
	{
		ShowText("Run Total Cost Filter --");
		
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Total Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}

		ShowText("Run Optimizable Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Optimizable);
		Thread.sleep(1500);
		
		try
		{
				HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Optimizable Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}

		ShowText("Run Roaming Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Roaming);
		Thread.sleep(1500);
		
		try
		{
				HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("ERROR CAUGHT in Try Catch ----- Failed Roaming Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
			DebugTimeout(9999, "9999");
		}
	}
	
	
	public static void SetupNumberOfTilesToTestAndShow(int numToTest, int numToDisplay)
	{
		if(numToTest > maxNumberOfTileMapsThatCanBeTested)
		{
			Assert.fail("Max number of tile maps that can be tested is "  +  maxNumberOfTileMapsThatCanBeTested + ".");
		}

		if(numToDisplay > maxNumberOfTileMapsThatCanBeShown)
		{
			Assert.fail("Max number of tile maps that can be tested is "  +  maxNumberOfTileMapsThatCanBeShown + ".");
		}
		
		numberOfTileMapsToTest = numToTest;
		
		ShowText("The number of tiles to tested is " + numToTest);
		
		ExpenseHelper.SetHierarchyMaxDisplayed(numToDisplay);	
	}

	// needs work !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public static void SetLoopTestStartTile(int startTile)
	{
		if(startTile > maxNumberOfTileMapsThatCanBeTested)
		{
			Assert.fail("Start tile number must be less than or equal to the max number of tile that can be tested "  +  maxNumberOfTileMapsThatCanBeTested + ".");
		}
		
		tileNumberToStartLoop = startTile;
	}

	public static void SetChartId()		
	{
		chartId = UsageHelper.getChartId(0);
	}
	
	// this goes through each month in month pull downs and tests the tile map for each month. 
	public static void RunThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> eleList = CommonTestStepActions.webListPulldown;
		
		for(WebElement ele : eleList)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			Thread.sleep(3000);
			RunAllTilesThree();
			ShowText("Test for Month " + ele.getText() + " Is Complete."); 
		}
	}

	// this is called after a number of tiles to test has been setup, a month has been selected, and a tile level has been setup (unless the top level is being tested).
	public static void RunAllTilesTwo() throws Exception
	{
		for(int x = 1; x <= numberOfTileMapsToTest; x++) // loop through selected tiles.
		{ 
			// this gets the json request text tp be used below. this depends on what selection is in the hierarchy pulldown.
			tempString = BuildJsonRequestPath(); 
			
			// get the json of the current tile map being shown
			dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('" + tempString + "')");
			
			//dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child.payload.rows')");
			// dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child.payload.rows')");
			Thread.sleep(1000);

			// verify json fetch is OK.
			Assert.assertTrue(dependentUnits != null); 

			// do this here to also make sure json fetch worked
			JSONArray array = new JSONArray(dependentUnits);			

			// ShowText(dependentUnits.substring(0,50)); // DEBUG show some of the json return.

			// get the cost type string to be filtered out for creating 'tempTwo' string below. 
			filterString = BuildStringForFilteringText();
			
			// these two calls get the name info and the cost info from the dependent user in the UI. both of these are put in dependentUnitInfo string. 
			// the type of cost string ("total", "optimizable", or "roaming") is filtered out of dependentUnitInfo string. 
			nameAndIds = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>a")).getText(); // name and id(s).
			numericValue = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>span")).getText().replace(filterString,""); // numeric value (cost type removed).
			
			// put name, id, and cost together..
			currentDependentUnitInfo = nameAndIds + " " + numericValue; 
			
			// Thread.sleep(1000);
			
			// click tile number x.
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + x + ")")).click();
			
			Thread.sleep(2000);
			
			// these get the name info and the cost that was in the hover, after the click above. both of these are put in hoverInfo string.
			nameAndIds = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText().split("\\.")[1].trim();
			numericValue = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(2)")).getText();
			
			hoverInfo = (nameAndIds + " " + numericValue); 
			
			Assert.assertEquals(currentDependentUnitInfo, hoverInfo); // verify hover value and it's corresponding dependent unit value are equal.  
			
			// get the double value found in the dependent info 
			expectedValueDouble = Double.valueOf(currentDependentUnitInfo.split("\\$")[1]);
			
			// now get the dependent unit user cost value in the json array that was stored before the click to get hover info. 
			// send in tempOne, the user name info, and this call will return the expected value as double.
			actualValueDouble = GetExpectedValueTwo(array, nameAndIds, ExpenseHelper.currentHierarchyCostFilter);
			 
			
			Assert.assertEquals(actualValueDouble, expectedValueDouble,"ERR");
			ShowText("Pass " + String.valueOf(x));
			
			
			// click the bread crumb.
			driver.findElement(By.cssSelector(".breadcrumbs>span>a")).click();
			
			Thread.sleep(1000);
		}
	}
	
	
	// this is called after a number of tiles to test has been setup, a month has been selected, and a tile level has been setup (unless the top level is being tested).
	public static void RunAllTilesThree() throws Exception
	{
		int x = 0;
		
		// this gets the json request text to be used below. this depends on what selection is in the hierarchy pulldown.
		tempString = BuildJsonRequestPath(); 
		
		// get the json of the current tile map being shown
		dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('" + tempString + "')");
		
		// verify json fetch is OK.
		Assert.assertTrue(dependentUnits != null); 

		// do this here to also make sure json fetch worked
		JSONArray array = new JSONArray(dependentUnits);		
		
		// one more check
		Assert.assertTrue(array.length() ==  100, "Error in HierarchyNumbersDependents.RunAllTilesThree. The json array read in is the wrong size.");

		
		// ////////////////////////////////////////////////////////////////////////////////////
		// loop through tile maps.
		// ////////////////////////////////////////////////////////////////////////////////////
        for(x = 1; x <= numberOfTileMapsToTest; x++) 
		{ 
    		hoverInfo = GetTooltipText(x);
    		
			// get the cost type string to be filtered out for creating 'tempTwo' string below. 
			filterString = BuildStringForFilteringText();
			
			// these two calls get the name info and the cost info from the dependent user in the UI. both of these are put in currentDependentUnitInfo string. 
			// the type of cost string ("total", "optimizable", or "roaming") is filtered out of dependentUnitInfo string. 
			nameAndIds = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>a")).getText(); // name and id(s).
			numericValue = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>span")).getText().replace(filterString,""); // numeric value (cost type removed).

			// put name, id, and cost together.
			currentDependentUnitInfo = nameAndIds + " " + numericValue; 
			
			//ShowText(currentDependentUnitInfo);
			Assert.assertEquals(hoverInfo, currentDependentUnitInfo, "Error in HierarchyNumbersDependents.RunAllTilesThree. "
					                                               + "The hover value doesn't match its corresponding dependent user."
					                                               + "The loop counter is " + x); 
			
			// now get the dependent unit user cost value in the json array that was stored before the click to get hover info. 
			// send in nameAndId, the user name info, and this call will return the expected value as double.
			actualValueDouble = GetExpectedValueTwo(array, nameAndIds, ExpenseHelper.currentHierarchyCostFilter);
			
			// get the double value found in the dependent info 
			expectedValueDouble = Double.valueOf(currentDependentUnitInfo.split("\\$")[1]);
			
			Assert.assertEquals(actualValueDouble, expectedValueDouble,"ERR");
		}
        // System.out.println("Number of Tile maps tested = " + (x  - 1));
	}
	
	// written by Ana:
	// this will return the string value for the tile map number hovered hovered in the tile map. 
	public static String GetTooltipText(int index) throws AWTException, InterruptedException
	{
        WebElement tileNumber = driver.findElement(By.cssSelector("#" + chartId + ">svg>g>g.highcharts-label:nth-of-type(" + index + ")")); // select tile map number
        
        Point p = GeneralHelper.getAbsoluteLocation(tileNumber);
        
        int x_offset = tileNumber.getSize().getHeight() / 2;
        int y_offset = tileNumber.getSize().getWidth() / 2;
        
        int a = p.getX() + x_offset;
        int b = p.getY() + y_offset;
        
        Robot robot = new Robot();
        robot.mouseMove(a, b);
                     
        // Thread.sleep(2000); / orig
        Thread.sleep(1500); 
        
        
        WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip"));
        // System.out.println(tooltip.getText().split("\\.")[1].trim());
		
		return tooltip.getText().split("\\.")[1].trim();
	}
	
	// this builds the json request string for getting the json rows of dependent unit values. 
	public static String BuildJsonRequestPath()
	{
		
		String pullDownValue = ExpenseHelper.GetSelectedHierarchy(); // get the selected hierarchy.
		
		// depending in the 
		switch(pullDownValue)
		{
			case "Cost Center":
			{
				return "hierarchy.PRIMARY.child.payload.rows";

			}
			case "Management":
			{
				return "hierarchy.SECONDARY.child.payload.rows";
				
			}
			case "Approval":
			{
				return "hierarchy.TERTIARY.child.payload.rows";
				
			}
			default:
			{
				Assert.fail("Failed to find correct Json for hierarchy pulldown in HierarchyNumbersDependents.BuildJsonRequestPath");
			}
		}
		
		return "";
	}
	
	// this will find which cost filter is selected. depending on which cost filter is selected, this will return a text string related to the selected cost filter.
	public static String BuildStringForFilteringText()
	{
		List<WebElement> tabsList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(1)>div"));		
		
		Assert.assertTrue(tabsList.size() == 3); // for later.
		
		for(int x = 0; x < tabsList.size(); x++)
		{
			if(tabsList.get(x).getAttribute("class").contains("option--selected"))
			{
				return tabsList.get(x).getText() + ":";
			}
		}
		
		Assert.fail("Fail in HierarchyNumbersDependents.BuildStringToFilterOut. Should have found a filter string.");
		
		return "";
	}
	
	public static double GetExpectedValueTwo(JSONArray jArray, String name, hierarchyTileMapTabSelection selection) throws Exception
	{
		JSONObject obj;
		
		for(int x = 0; x < jArray.length(); x++)
		{
			obj  = jArray.getJSONObject(x);
			
			// ShowText(jArray.getJSONObject(x).getString("name"));
			if(obj.getString("name").equals(name))
			{
				switch(selection)
				{
					case Total:
					{
						return obj.getDouble("total_expense_rollup_ex");
					}
					case Optimizable:
					{
						return obj.getDouble("optimizable_expense_rollup_ex");
					}
					case Roaming:
					{
						return obj.getDouble("roaming_expense_rollup_ex");
					}
				}
			}
		}
		
		String errString = "Didn't find hover value for " + name + " in HierarchyNumbersDependents.GetExpectedValueTwo with cost filter " + selection.name();
		String errStringTwo = "Size of java array is " + jArray.length();
		
		Assert.fail(errString + "\n " + errStringTwo);
		return 0;
	}	
	
	public static void BuildDependentChildObjects() throws Exception // bladd
	{
		JSONObject obj;
		double tempDouble;
		
		Thread.sleep(2000);
		
		// this gets the json request text to be used below. this depends on what selection is in the hierarchy pulldown.
		tempString = BuildJsonRequestPath(); 
		
		// get the json of the current tile map being shown
		dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('" + tempString + "')");

		// verify json fetch is OK.
		Assert.assertTrue(dependentUnits != null); 

		// do this here to also make sure json fetch worked
		JSONArray array = new JSONArray(dependentUnits);			

		for(int x = 0; x < array.length(); x++)
		{
			obj  = array.getJSONObject(x);
			
			switch (ExpenseHelper.currentHierarchyCostFilter)
			{
				case Total:
				{
					childList.add(new Child(obj.getString("name"), obj.getInt("total_expense_rollup_ex")));
					break;
				}
				case Optimizable:
				{
					childList.add(new Child(obj.getString("name"), obj.getInt("optimizable_expense_rollup_ex")));
					break;
				}
				case Roaming:
				{
					childList.add(new Child(obj.getString("name"), obj.getInt("roaming_expense_rollup_ex")));
					break;
				}
				default:
				{
					Assert.fail("Failed to find case stateent in HierarchyNumbersTestsDependents.BuildDependentChildObjects");
				}
			}
		}
	}
	
	public static void VerifyActualExpectedDependentUnits() throws Exception // bladd
	{
		
		// get the actual dependent units from the list of dependent units in the UI.
		List<WebElement> eleList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList));
		
		int actualInt = 0;
		int expectedInt = 0;
		int loopCntr = 0;
		int previousInt = 0;
		
		for(WebElement ele : eleList)
		{
			actualInt = Integer.valueOf(ele.getText().split("\n")[1].replace(GetCostFilterString(),""));
			expectedInt  = childList.get(loopCntr).cost; 
			
			if(previousInt == expectedInt)
			{
				System.out.print("Name mismatch: " + ele.getText().split("\n")[0] + " " + childList.get(loopCntr).childName + " " + expectedInt);
			}
			
			Assert.assertEquals(actualInt, expectedInt, "");
			
			previousInt = expectedInt;

			try
			{
				Assert.assertEquals(ele.getText().split("\n")[0], childList.get(loopCntr).childName, "");				
			}
			catch (AssertionError sertErr)
			{
				System.out.print("Name mismatch: " + ele.getText().split("\n")[0] + " " + childList.get(loopCntr).childName + expectedInt);
			}

			// DEBUG - show both names.
			//System.out.print("Actual: " + ele.getText().split("\n")[0]);
			//System.out.print(" Expected: " +  childList.get(x).childName + "\n");
			//ShowText("------------------------");
			
			ShowInt(loopCntr);
			
			loopCntr++;
		}
	}
	
	// 					------------ this does the drill down test ------------ 
	// * this drills down until it reaches the point where no more drilling down can be done, or, until it 
	//   reaches the maxNumberOfLevels passed in.
	// * each time a level is drilled down to some test are run that test the three cost filters.
	public static void DrillDownAcrossCostFilters(int maxNumberOfLevels, int totalNumberOfTilesShown) throws Exception 
	{
		
		int tileToSelect;
		int cntr = 0;
		
		Random rand = new Random();
		
		while (cntr != maxNumberOfLevels)
		{
			tileToSelect = rand.nextInt(totalNumberOfTilesShown) + 1;
			System.out.println("** Selecting tile number " + tileToSelect + " **");
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + tileToSelect + ")")).click();
			
			if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), ShortTimeout))
			{
				System.out.println("Finished drill down testing to level " + cntr);
				System.out.println("The last click found the 'No Depenents' message\n");
				break;
			}
			HierarchyNumbersDependents.TestPhaseOne();  // run tests.
			cntr++;
		}
	}
	
	public static void ShowChildList()
	{
		for(Child chl : HierarchyNumbersDependents.childList){chl.Show();} // DEBUG
	}
	
	// 		// for(Child chl : HierarchyNumbersDependents.childList){chl.Show();} // DEBUG
	
	
	
	//  THIS IS DEMO FROM ANA.
	public static void HoverThroughTiles() throws AWTException, InterruptedException 
	{	             
           String chartId = UsageHelper.getChartId(HierarchyHelper.treeMapChart);
           
           List<WebElement> tiles = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-series-group>g>g>rect"));
           
           for (int i = 1; i <= tiles.size(); i++) {
                  
                  WebElement tileNumber = driver.findElement(By.cssSelector("#" + chartId + ">svg>g>g.highcharts-label:nth-of-type(" + i + ")"));
                  
                  Point p = GeneralHelper.getAbsoluteLocation(tileNumber);
                  
                  int x_offset = tileNumber.getSize().getHeight() / 2;
                  int y_offset = tileNumber.getSize().getWidth() / 2;
                  
                  int x = p.getX() + x_offset;
                  int y = p.getY() + y_offset;
                  
                  Robot robot = new Robot();
                  robot.mouseMove(x, y);
                               
                  Thread.sleep(2000);
           
                  WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip"));
                  System.out.println(i + ") Tooltip text: " + tooltip.getText());
           }
    
    }



	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 													HELPERS
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String GetCostFilterString()
	{
		switch(ExpenseHelper.currentHierarchyCostFilter)
		{
			case Total:
			{
				return "Total:$";
			}
			case Optimizable:
			{
				return "Optimizable:$";
			}
			case Roaming:
			{
				return "Roaming:$";
			}
			default:
			{
				Assert.fail();
				return "";
			}
		}
	}























}
