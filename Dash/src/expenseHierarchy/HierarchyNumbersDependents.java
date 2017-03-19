package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.xml.transform.Templates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.jetty.servlet.Debug;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
	public static int tileNumberToStartLoop = 0; // remove?
	public static int maxLevelsToDrillDownTo = 0; 

	
	public static String dependentUnits = "";
	public static String nameAndIds = "";
	public static String numericValue = "";
	public static String tempString = "";
	public static String currentDependentUnitInfo = "";
	public static String hoverInfo = "";
	public static String filterString = "";
	public static String chartId = "";
	public static String currentHierarchyId = "";
	
	public static List<Child> childList = new ArrayList<Child>();
	public static List<String> hierarchyIdsList = new ArrayList<>();	
	
	public static List<String> actualList = new ArrayList<String>();
	public static List<String> expectedList = new ArrayList<String>();
	
	public static double expectedValueDouble = 0;
	public static double actualValueDouble = 0;
	public static int createErrorCounter = 0; // this can be used in 'GetExpectedValueTwo'method to create an intentional error. 

	public static JavascriptExecutor js = (JavascriptExecutor) driver;	

	public static List<List<String>> listsOfPreviousDependentUnits = new ArrayList<List<String>>();
	public static List<WebElement> tempWebElementList = new ArrayList<WebElement>();
	public static List<String> tempStrList = new ArrayList<String>();

	public static TileMapTestType currentTileMapTestType;
	public static DrillDownPageType drillDownPageType;
	
	public static enum DrillDownPageType
	{
		topTen,
		expense,
	}
	
	public static enum TileMapTestType
	{
		phaseOne,
		months,
		drillDown
	}
	
	public static void SetDrillDownPageType(DrillDownPageType type)
	{
		drillDownPageType = type;
	}
	
	public static void SetCurrrentTileMapTestType(TileMapTestType type)
	{
		currentTileMapTestType = type;
	}
	
	
	public static void SetMaxNumberOfLevelsToDrillDown(int numLevels)
	{
		maxLevelsToDrillDownTo = numLevels;
	}

	public static void TestPhaseOne() throws Exception
	{
		
		ShowText("PHASE ONE TEST:\n");
		
		ShowText("Run Total Cost Filter --");
		
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total); 
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesThree();
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
		ShowText("MONTHS TEST\n");
		
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
	public static void RunAllTilesThree() throws Exception
	{
		int x = 0;
		
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
			
			// IMPORTANT NOTE -- this may not always work because the json sorting can be different then ED's sort when it comes to 
			//                   dependent units with the same numeric value.
			//                -- next sections below are commented.
			
			// now get the dependent unit user cost value in the json array that was stored before the click to get hover info. 
			// send in nameAndId, the user name info, and this call will return the expected value as double.
			//actualValueDouble = GetExpectedValueTwo(array, nameAndIds, ExpenseHelper.currentHierarchyCostFilter);
			
			// get the double value found in the dependent info 
			//expectedValueDouble = Double.valueOf(currentDependentUnitInfo.split("\\$")[1]);
			
			//Assert.assertEquals(actualValueDouble, expectedValueDouble,"ERR");
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

        // DebugTimeout(9999, "Freeze in GetTooltipText");
        
        Thread.sleep(2000); // orig - back to orig.
        //Thread.sleep(1500); 
        
        
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
	
	// this builds the json request string for getting the json list of dependent unit values. 
	public static String BuildJsonRequestPathTwo()
	{
		return "hierarchy." + currentHierarchyId +  ".child.payload.rows";
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
	
	public static void BuildDependentChildObjects() throws Exception 
	{
		JSONObject obj;
		double tempDouble;
		
		Thread.sleep(2000);
		
		// this gets the json request text to be used below. this depends on what selection is in the hierarchy pulldown.
		tempString = BuildJsonRequestPathTwo(); 
		
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
	
	// * get the dependent units list using a json call. this list is sorted by numeric value. (expected list)
	// * get the dependent units list in the UI. (Actual list)
	// * go through  the lists and verify the numeric values are sorted identically between the actual and expected.
	// * go through  the lists and verify the names info is identical between the actual and expected. sometimes when
	//   there are two or more users with the same numeric value the sorting of the names can be in different order, 
	//   between the actual and expected list. in this case, store these names into  their own separate actual and expected 
	//   lists into and send both lists to the 'VerifyAllDependentChildren' method.
	// * the 'VerifyAllDependentChildren' method will verify that the names between the two list are identical and the numeric
	//   values are all the same.
	public static void VerifyActualExpectedDependentUnits() throws Exception 
	{

		ShowText ("Verifying Actual/Expected Dependents");  
		
		// get the actual dependent units from the list of dependent units in the UI.
		List<WebElement> eleList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList));
		
		int actualInt = 0;
		int expectedInt = 0;
		int loopCntr = 0;
		int latestCost = -9999;
		int x;
		String costSelectorString = "";
		String tempString = "";
		
		for(WebElement ele : eleList)
		{
			// get the actual value (from UI) and the expected value (from sorted json list) into variables.  
			
			// with real data, there is a decimal cents (ex: $45.39 - .39 is the decimal cents). need to remove the decimal cents.
			if(ele.getText().contains("."))
			{
				x = ele.getText().length() - ele.getText().indexOf(".");
				tempString = ele.getText().substring(0, ele.getText().length() - x);
			}
			else
			{
				tempString = ele.getText();
			}			
			
			// random error - need to catch and see. 
			try
			{
				actualInt = Integer.valueOf(tempString.split("\n")[1].replace(GetCostFilterString(),""));				
			}
			catch (Exception ex)
			{
				ShowText(ex.getMessage());
				ShowText("tempString " + tempString);
			    JOptionPane.showMessageDialog(frame, "FROZEN AT RANDOM ERROR ----------------------. ");
			}
			
			expectedInt  = childList.get(loopCntr).cost; 
			
			// System.out.println("Actual Int: " + actualInt + " Expected Int: " + expectedInt); DEBUG			
			
			Assert.assertEquals(actualInt, expectedInt, "Fail in sorting compare for numeric cost in HierarchyNumbersDependents.VerifyActualExpectedDependentUnits"); // verify cost in json list equals cost in actual list. 

			// compare names. sometimes the numeric values will match and the names won't match. this happens when two or more
			// units have the same numeric value in each list but the units are in different orders.  
			try
			{
				Assert.assertEquals(ele.getText().split("\n")[0], childList.get(loopCntr).childName, "");				
			}
			catch (AssertionError sertErr)
			{
				// ShowText("Try catch"); // DEBUG
				
				if(childList.get(loopCntr).cost != latestCost)
				{
					if(expectedList.size() != 0)
					{
						// this verifies all the names have the same value, the actual and expected lists are the same size, and 
						VerifyAllDependentChildren(GetCostFromString(expectedList.get(0)));  
						//ShowExpectedList();
						//ShowActualList();
					}
					expectedList.clear();
					actualList.clear();
					latestCost = childList.get(loopCntr).cost;
				}
				
				// this gets the string to be filtered out of the actual data.
				costSelectorString = HierarchyNumbersDependents.GetCostFilterString();  
				
				actualList.add(ele.getText().split("\n")[0]  + ele.getText().split("\n")[1].replace(costSelectorString, " "));
				expectedList.add(childList.get(loopCntr).childName + " " +  String.valueOf(expectedInt));
			}
			loopCntr++;
		}
	}

	public static void FinishFinalTest()
	{
		if(actualList.size() != 0)
		{
			VerifyAllDependentChildren(GetCostFromString(expectedList.get(0)));
			//ShowExpectedList();
			//ShowActualList();
		}
	}
	
	public static void VerifyAllDependentChildren(String expectedTotal) 
	{
		// ShowText("VerifyAllDependentChildren START"); // debug
		// ShowText("Verify Try Catch Items -------------- "); // debug
		
		Assert.assertTrue(actualList.size() == expectedList.size(), "");
		
		// verify all actual values have total value equal to 'expectedTotal' passed in.
		for(String str : actualList)
		{
			// Assert.assertEquals(GetCostFromString(str), expectedTotal, "Actual total failed actual: " + GetCostFromString(str) +   " expected "  + expectedTotal);
			Assert.assertEquals(GetCostFromStringTwo(str), expectedTotal, "Actual total failed actual: " + GetCostFromStringTwo(str) +   " expected "  + expectedTotal); 
		}
		
		// verify all expected values have total value equal to 'expectedTotal' passed in.
		for(String str : expectedList)
		{
			Assert.assertEquals(GetCostFromString(str), expectedTotal, "Expected total failed actual: " + GetCostFromString(str) +   " expected "  + expectedTotal);
		}
		
		RemoveDecimalValueFromActualValueList(); 
		
		// verify cross check.
		for(String str : expectedList)
		{
			if(!actualList.contains(str))
			{
				Assert.fail("Actual list does not contain item in expected list. Item not found in actual list is " + str);
			}
		}
		// ShowText("VerifyAllDependentChildren DONE"); // DEBUG
	}
	
	public static void RemoveDecimalValueFromActualValueList() 
	{
		List<String> tempList = new ArrayList<String>();
		
		for(int x = 0 ; x < actualList.size(); x++)
		{
			tempList.add(RemoveDecimal(actualList.get(x)));
		}
		actualList.clear();
		actualList.addAll(tempList);
	}
	
	public static String RemoveDecimal(String stringWithExpectedCost) 
	{
		String tempString = "";
		int x = 0;
		tempString = stringWithExpectedCost;
		
		if(tempString.contains("."))
		{
			x = tempString.length() - tempString.indexOf(".");
			tempString = tempString.substring(0, tempString.length() - x);
		}

		return tempString;
	}		
	
	public static String GetCostFromString(String stringWithExpectedCost)
	{
		String [] arr = stringWithExpectedCost.split(" "); 
		return arr[arr.length - 1];
	}
	
	public static String GetCostFromStringTwo(String stringWithExpectedCost) 
	{
		String tempString = "";
		int x = 0;
		String [] arr = stringWithExpectedCost.split(" "); 
		tempString = arr[arr.length - 1];

		if(tempString.contains("."))
		{
			x = tempString.length() - tempString.indexOf(".");
			tempString = tempString.substring(0, tempString.length() - x);
		}
		return tempString;
	}	
	
	
	// 					------------ this does the drill down test ------------ 
	// * this drills down until it reaches the point where no more drilling down can be done, or, until it 
	//   reaches the maxNumberOfLevels passed in.
	// * each time a level is drilled down to some test are run that test the three cost filters.
	public static void DrillDownAcrossCostFiltersTileMap(int maxNumberOfLevels, int totalNumberOfTilesShown) throws Exception 
	{
		int tileToSelect;
		int cntr = 0;
		
		Random rand = new Random();
		
		ShowText("Doing Drill down test." );
		
		
		while (cntr != maxNumberOfLevels)
		{
			tileToSelect = rand.nextInt(totalNumberOfTilesShown) + 1;
			System.out.println("\n** Selecting tile number " + tileToSelect + " **\n");
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
	
	// 					------------ this does the drill down test for dependent units ------------ 
	// * this drills down until it reaches the point where no more drilling down can be done, or, until it 
	//   reaches the maxNumberOfLevels passed in.
	// * each time a level is drilled down to, a tests is run that tests the three cost filters.
	public static void DrillDownDependentUnitsTwo(int maxNumberOfLevels) throws Exception  
	{
		int dependentUnitToSelect;
		int cntr = 0;

		DebugTimeout(3, "wait three for page load."); 
		
		int numberOfDependentUnits = 0;
		
		Random rand = new Random();
		
		ShowText("Starting drill down in dependent units.");
		
		while (cntr != maxNumberOfLevels)
		{
			numberOfDependentUnits =  driver.findElements(By.cssSelector(".tdb-pov__itemList>li")).size(); 
			
			System.out.println("# of dependents before click. " + numberOfDependentUnits);
			
			// get list of dependent units from the UI. get a random number to be used to pick one of the dependent unit.
			List<WebElement> unitsList = driver.findElements(By.cssSelector(".tdb-pov__itemList>li")); 
			dependentUnitToSelect = rand.nextInt(numberOfDependentUnits);
			
			Thread.sleep(500);
			
			System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **");
			
			unitsList.get(dependentUnitToSelect).click(); // select dependent unit.
			
			
			Pause("");
			// DebugTimeout(3, "wait three after click to drill down."); 			

			// wait to see if 'No Dependents' message is found.
			if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), ShortTimeout))
			{
				System.out.println("Finished drill down testing to level " + cntr);
				System.out.println("The last click found the 'No Dependents' message\n");
				break;
			}

			// move to top of page to make the testing visible.
			WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
			new Actions(driver).moveToElement(topSection).perform();

			HierarchyNumbersDependents.LoopThroughCatergoriesDependentUnits(); // this test loops through all of the category selectors.
		
			cntr++;
		}
	}
	
	public static void ShowChildList()
	{
		for(Child chl : HierarchyNumbersDependents.childList){chl.Show();} // DEBUG
	}

	// go through each category selector in  the tile map section.
	public static void LoopThroughCatergoriesDependentUnits() throws Exception 
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		Thread.sleep(1000);
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.
			Thread.sleep(1000);
			
			BuildDependentChildObjects(); // create list of dependent units from Json call.
			
			// ShowChildList(); // DEBUG
			
			Collections.sort(HierarchyNumbersDependents.childList, new Child()); // sort list of dependent units from Json call.
			Thread.sleep(1000);

			// ShowChildList(); // DEBUG
			
			HierarchyNumbersDependents.VerifyActualExpectedDependentUnits();
			
			HierarchyNumbersDependents.FinishFinalTest();
			childList.clear();
			
			ShowText("Pass complete for " + values[x].name() +".");
		}
	}
	
	// this loops through the hierarchies and does a series of drill down tests for each hierarchy. 
	public static void LoopThroughHierarchiesDependentUnitsDrillDown() throws Exception 
	{
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		// this method get the Ids for the hierarchy pulldown values. The Ids are used as a key in the Json request (example: "hierarchy." + currentHierarchyId +  ".child.payload.rows").   
		hierarchyIdsList = HierarchyHelper.getHierarchiesValues();
		
		ShowText(" ------------ Start Looping Through Hierarchies With Drilldowns. -----------------\n\n");
		
		int hierarchyCntr = 0; // this is used to index hierarchy Ids from 'hierarchyIdsList' created above.

		// got through the available hierarchies one at a time. call 'DrillDownDependentUnitsTwo()' on each loop.
		for(WebElement ele : hierarchyList)
		{
				ShowText(" -------------------------Hierarchy Name: " + ele.getText() + " ---------------------------------------- ");
				
				currentHierarchyId = hierarchyIdsList.get(hierarchyCntr); // set the current hierarchy Id. this hierarchyId is global to this class.  
				ele.click(); 
				Thread.sleep(1000);
				DrillDownDependentUnitsTwo(maxLevelsToDrillDownTo); // run the drill down tests for each category selector.
				hierarchyCntr++;
		}
	}
	
	public static void LoopThroughHierarchiesDependentUnitsDrill_Down_Up() throws Exception 
	{
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		
		ShowText(" ------------ Start Looping Through Hierarchies With Drilldowns. -----------------\n\n");
		
		int hierarchyCntr = 0; // this is used to index hierarchy Ids from 'hierarchyIdsList' created above.

		// got through the available hierarchies one at a time. call 'DrillDownDependentUnitsTwo()' on each loop.
		for(WebElement ele : hierarchyList)
		{
				ShowText(" -------------------------Hierarchy Name: " + ele.getText() + " ---------------------------------------- ");
				ele.click(); 
				//DebugTimeout(3, "Wait 3 in hierarchy switch");
				Pause("Hierarchy Switch Just Done");
				/*
				if(hierarchyCntr == 0 || hierarchyCntr == 1)
				{
					Pause("Skip first hierarchy ----------------");
					hierarchyCntr++;
					continue;
				}
				*/
				DrillDown_Up_DependentUnits();  
				hierarchyCntr++;
		}
	}
	
	// this loops through each hierarchy and runs a one of the tile map tests. the tile map test to run is set in the test case that calls this method.
	public static void LoopThroughHierarchiesTileMapTests() throws Exception  
	{
	
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		// this method get the Ids for the hierarchy pulldown values. The Ids are used as a key in the Json request (example: "hierarchy." + currentHierarchyId +  ".child.payload.rows").   
		hierarchyIdsList = HierarchyHelper.getHierarchiesValues();
		
		ShowText(" ------------ Start Looping Through Hierarchies. -----------------");
		
		int hierarchyCntr = 0;

		// got through the available hierarchies one at a time. call 'LoopThroughCatergoriesDependentUnits()' on each loop.
		for(WebElement ele : hierarchyList)
		{
				ShowText("Hierarchy Name: " + ele.getText() + "\n");
				currentHierarchyId = hierarchyIdsList.get(hierarchyCntr);
				ele.click();
				Thread.sleep(1000);
				RunTileMapTest(currentTileMapTestType); // run test.
				hierarchyCntr++;
		}	
	}
	
	public static void LoopThroughHierarchiesDependentUnits() throws Exception 
	{
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		// this method get the Ids for the hierarchy pulldown values. The Ids are used as a key in the Json request (example: "hierarchy." + currentHierarchyId +  ".child.payload.rows").   
		hierarchyIdsList = HierarchyHelper.getHierarchiesValues();
		
		ShowText(" ------------ Start Looping Through Hierarchies. -----------------");
		
		int hierarchyCntr = 0;

		// got through the available hierarchies one at a time. call 'LoopThroughCatergoriesDependentUnits()' on each loop.
		for(WebElement ele : hierarchyList)
		{
				ShowText("Hierarchy Name: " + ele.getText());
				currentHierarchyId = hierarchyIdsList.get(hierarchyCntr);
				ele.click();
				Thread.sleep(1000);
				LoopThroughCatergoriesDependentUnits();
				hierarchyCntr++;
		}
	}
	
	public static void GoToViewTop10() throws Exception
	{
		WaitForElementClickable(By.xpath("//a[text()='View Top 10']"), MediumTimeout, "");
		driver.findElement(By.xpath("//a[text()='View Top 10']")).click();
		WaitForElementVisible(By.xpath("//div[text()='Expenses $']"), MediumTimeout);
	}
	
	// this does the drilling down and going back up.
	public static void DrillDown_Up_DependentUnits() throws Exception
	{
		int tempSize = 0;
		int drillDownCntr = 0;
		boolean drillDownStoppedEarly = false;
		int numBreadCrumbs;
		
		ClearDrillDownUpStringPair(); // clear web element list and text list that are used as temporary holders of information. 
		listsOfPreviousDependentUnits.clear(); // clear list that will hold the list of dependent users' lists. 
		
		//DebugTimeout(3, "wait 3 before start test.");

		PushCurrentList(); // this puts the current dependents list in the UI onto 'listsOfPreviousDependentUnits' list.
		
		// this loops through the drilling down clicks.  
		// drilling down into the dependent units 'maxLevelsToDrillDownTo' times.
		for(drillDownCntr = 0; drillDownCntr < maxLevelsToDrillDownTo; drillDownCntr++)
		{
			if(!DrillDown_Up_DependentUnitsTwo()) // this does the drill down.
			{
				numBreadCrumbs = driver.findElements(By.cssSelector(".breadcrumbs>span")).size();
				driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type("  + numBreadCrumbs + ")")).click();
				Pause("have clicked breadcrumb after finding no dependent units after a drill down.");
				break;
			}
			
			PushCurrentList(); // this puts the current dependents list in the UI onto 'listsOfPreviousDependentUnits' list.
		}
		
		// this lets 
		// ShowListsOfDependentUnitsStoredAway(); // this will show all lists on the list that were added in 'AddDependentUnitList' method.
		
		//  this clicks the bread crumbs until there are no bread crumbs left.
		//  MORE comments !!!!!
		for(int y = drillDownCntr; y >= 0; y--)
		{
			ShowText("---- POP " + listsOfPreviousDependentUnits.get(y).get(0).replace("\n",  " "));
			
			// get the list of dependent units currently showing in the UI into a temporary list of strings. 
			CreateTempCurrentDependentsList(); 
			
			// pops item 'y' in the list holding the history of dependent unit lists (expected) and compare to the current dependent unit list that is showing (actual).   
			VerifyDependentUsersListsAreEqual(tempStrList, listsOfPreviousDependentUnits.get(y)); 

			ClearDrillDownUpStringPair();  // clear web element list and text list that are used as temporary holders of information.
			
			if(y != 0) // if not back at the top, click the lowest bread crumb.
			{
				driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type(" + y + ")")).click();				
				Pause("bread crumb was just clicked.");
			}

			// DebugTimeout(5, "Wait five after breadcrumb click");
		}

		// NOTE: this pop has to be here because  
		
		/*
		ShowText("----------------------------------------------  POP " + listsOfPreviousDependentUnits.get(0).get(0));
		// verify current UI list equals list stored away. 
		CreateTempCurrentDependentsList(); // this gets the list of dependent units currently showing in the UI into a temporary list of strings.
		VerifyDependentUsersListsAreEqual(tempStrList, listsOfPreviousDependentUnits.get(0));
		ClearDrillDownUpStringPair();  // clear web element list and text list that are used as temporary holders of information.
		
		Assert.assertTrue(driver.findElements(By.cssSelector(".breadcrumbs>span")).size() == 0);
		 */
		
	}
	
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
	
	// this gets the dependent units in the UI and adds them to a list of lists.  
	public static void PushCurrentList()
	{
		CreateTempCurrentDependentsList(); // this gets the list of dependent units currently showing in the UI into a temporary list of strings.
		AddDependentUnitList(tempStrList); // store away the list of dependent units currently showing.
		ShowText("PUSH -- this is the first item of list pushed: "  + tempStrList.get(0).replace("\n",  " "));
		// ShowInt(tempStrList.size());
		ClearDrillDownUpStringPair();  // clear web element list and text list that are used as temporary holders of information.
	}
	
	public static void VerifyDependentUsersListsAreEqual(List<String> actualList, List<String> expectedList)
	{
		ShowText("verify sizes   First list item of lisy being verified  -----   " + expectedList.get(0).replace("\n", " "));
		//ShowInt(actualList.size());
		//ShowInt(expectedList.size());
		
		Assert.assertEquals(actualList.size(), expectedList.size(), "Actual and Expected list sizes are not equal in HierarchyNumbersDependents.VerifyDependentUsersListsAreEqual.");
		Assert.assertEquals(actualList, expectedList, "List are not equal in HierarchyNumbersDependents.VerifyDependentUsersListsAreEqual.");
	}
	
	// this gets the current list of dependent units showing in UI into a list of strings.
	public static void ShowListsOfDependentUnitsStoredAway()
	{
		int tempSize = listsOfPreviousDependentUnits.size();
		
		ShowText("Start showing what is on list/list");
		ShowInt(tempSize);
		
		for(int x = 0 ; x < tempSize; x++)
		{
			ShowText("**************** List Start ********************************");
			ShowListOfStrings(listsOfPreviousDependentUnits.get(x));
			ShowText("**************** List End ********************************\n");
		}
	}
	
	
	// this gets the current list of dependent units showing in UI into a list of strings.
	public static void CreateTempCurrentDependentsList()
	{
		tempWebElementList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList));
		CopyWebElementListToTextList(tempWebElementList, tempStrList);	
	}
	
	
	
	public static void AddDependentUnitList(List<String> strList)
	{
		List<String> myList;		
		myList = new ArrayList<String>();
		myList.addAll(tempStrList);
		listsOfPreviousDependentUnits.add(myList);
	}
	
	public static boolean DrillDownDependentUnits() throws Exception  
	{
		int dependentUnitToSelect;
		int numberOfDependentUnits =  100;
		
		Random rand = new Random();
		
		ShowText("Drill down one level in dependent units.");

		// get list of dependent units from the UI. get a random number to be used to pick one of the dependent unit.
		List<WebElement> unitsList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)); 
		dependentUnitToSelect = rand.nextInt(numberOfDependentUnits);
		
		Thread.sleep(500);
		
		// System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **"); // DEBUG
		
		unitsList.get(dependentUnitToSelect).click(); // select dependent unit.

		// move to top of page to make the testing visible.
		WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
		new Actions(driver).moveToElement(topSection).perform();
		
		
		if(drillDownPageType == DrillDownPageType.expense)
		{
			// wait to see if 'No Dependents' message is found.
			if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), TinyTimeout))
			{
				System.out.println("Have found the 'No Dependents' message.\n");
				return false;
			}
		}
		else
		{
			if(!WaitForTopTenDrillDown())
			{
				System.out.println("Dependents list in Top Ten Is empty.\n");
				return false;
			}
		}
		return true;
	}
	
	public static boolean DrillDown_Up_DependentUnitsTwo() throws Exception  
	{
		int dependentUnitToSelect = 0;
		int numberOfDependentUnits =  0;

 		// get number of dependents.
		numberOfDependentUnits = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)).size();
		Random rand = new Random();
		
		// get list of dependent units from the UI. get a random number to be used to pick one of the dependent unit.
		List<WebElement> unitsList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)); 
		dependentUnitToSelect = rand.nextInt(numberOfDependentUnits);
		
		Thread.sleep(1000);
		
		// click on dependent unit.
		System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **"); // DEBUG
		unitsList.get(dependentUnitToSelect).click(); // select dependent unit. // bladdyy

		//System.out.println("** Hard Code click on item four. **"); // DEBUG
		//unitsList.get(3).click(); // select dependent unit. // bladdyy
		
		// move to top of page to make the testing visible.
		WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
		new Actions(driver).moveToElement(topSection).perform();

		Pause("Wait after drill down click.");
		
		if(drillDownPageType == DrillDownPageType.expense)
		{
			// wait to see if 'No Dependents' message is found.
			if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), TinyTimeout))
			{
				System.out.println("Have found the 'No Dependents' message in expense page.\n");
				return false;
			}
		}
		else
		{
			if(!WaitForTopTenDrillDown())
			{
				System.out.println("Dependents list in Top Ten Is empty.\n");
				return false;
			}
		}
		return true;
	}
	
	public static boolean WaitForTopTenDrillDown() throws Exception
	{
		ShowText("WAIT");
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime+10000;
		boolean listIsVisible = false;

		while(System.currentTimeMillis() < endTime) 
		{
			  Thread.sleep(3000);
			  if(driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)).size() > 0)
			  {
				  ShowText("set visible true");
				  ShowInt(driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)).size());
				  listIsVisible = true;
				  break;
			  }
		}
		
		return listIsVisible;
		
	}
	
	// this clears the two lists used in the drill down up test. 
	public static void ClearDrillDownUpStringPair() 
	{
		ClearWebElementList(tempWebElementList);
		ClearStringList(tempStrList);
	}
	
	public static void ClearWebElementList(List<WebElement> eleList)
	{
		if(eleList != null)
		{
			eleList.clear();
		}
	}
	
	public static void ClearStringList(List<String> strList)
	{
		if(strList != null)
		{
			strList.clear();
		}
	}
	
	public static void CopyWebElementListToTextList(List<WebElement> eleList, List<String> strList)
	{
		if(eleList == null)
		{
			Assert.fail("Method CopyWebElementListToTextList received a null web element list.");
		}
		
		if(strList == null)
		{
			Assert.fail("Method CopyWebElementListToTextList received a null String list.");
		}
		
		Assert.assertTrue(strList.size() == 0, "Method CopyWebElementListToTextList received a String list that is not empty.");
		
		for(WebElement ele : eleList)
		{
			strList.add(ele.getText());
		}
	}
	
	public static void ShowWebelEmentListText(List<WebElement> eleList)
	{
		if(eleList == null)
		{
			Assert.fail("Method ShowWebelementListText in HierarchyNumbersDependents received a null web element list.");
		}
		
		for(WebElement ele : eleList)
		{
			ShowText(ele.getText());
		}
	}
	
	// this decides which tile map test to call depending on the input.
	public static void RunTileMapTest(TileMapTestType type) throws Exception
	{
		switch(type)
		{
			case phaseOne:
			{
				TestPhaseOne();
			}
			
			case months:
			{
				TestPhaseWithMonths();
			}
			
			case drillDown:
			{
				DrillDownAcrossCostFiltersTileMap(maxLevelsToDrillDownTo, 50);
			}
		}
	}
	
	public static void ShowDebugList(List<Integer> iList)
	{
		for(int i : iList)
		{
			ShowInt(i);
		}
	}
	
	public static void ShowExpectedList()
	{
		for(String str : expectedList)
		{
			ShowText(str);
		}
	}
	
	public static void ShowActualList()
	{
		for(String str : actualList)
		{
			ShowText(str);
		}
	
	}
	
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
				Assert.fail("switch/case fail in HierarchyNumbersDependents.GetCostFilterString.");
				return "";
			}
		}
	}
	
	public static void Freeze() throws Exception
	{
		DebugTimeout(9999, "9999");
	}

	public static void Pause(String moreInfo) throws Exception
	{
	    JOptionPane.showMessageDialog(frame, "PAUSE.... " + moreInfo);
	}
	// 	    JOptionPane.showMessageDialog(frame, "Select OK. Test Done and Passed.");
	
}
