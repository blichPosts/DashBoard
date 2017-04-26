package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.xml.transform.Templates;

import org.apache.commons.el.VariableResolverImpl;
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
	public static int x_iFrame;
	public static int y_iFrame;

	
	public static String dependentUnits = "";
	public static String nameAndIds = "";
	public static String numericValue = "";
	public static String tempString = "";
	public static String currentDependentUnitInfo = "";
	public static String hoverInfo = "";
	public static String filterString = "";
	public static String chartId = "";
	public static String currentHierarchyId = "";
	public static String currentLevelName = "";
	public static String currentCategorySelection = "";
	public static String totalCount = "";
	public static String requestedTileMapsToTest = "";	
	
	public static Stack<String> aboveTileStack = new Stack<String>();
	public static Stack<String> aboveKpiStack = new Stack<String>();	
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
	
	public static boolean tileMapInteractive = true;
	
	public static enum DrillDownPageType
	{
		topTen,
		expense,
	}
	
	public static enum TileMapTestType
	{
		phaseOne,
		months,
		drillDownCommand,
		commandSmoke,
		commandAllCatergories
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
	public static void RunTilesInCommand() throws Exception 
	{
		int x = 0;
		int loopCntr = 0;
		
		// get the number of tiles in the tile map and setup loop counter.
		loopCntr = NumberOfTilesToTestTwo();
		
		if(tileMapInteractive)
		{
			loopCntr = UserNumberTileMapsToTest();
			if(loopCntr == 0)
			{
				loopCntr = NumberOfTilesToTestTwo();
			}
		}

		
		System.out.println("loopCntr max = " + loopCntr);
		
		// ////////////////////////////////////////////////////////////////////////////////////
		// loop through tile maps.
		// ////////////////////////////////////////////////////////////////////////////////////
        for(int y = 1; y <= loopCntr; y++) 
		{ 
        	hoverInfo = GetTooltipText(y);
			// ShowText("hover info back: " + hoverInfo);
			
        	// get the cost type string to be filtered out for creating 'numericValue' string below. 
			filterString = BuildStringForFilteringText();
			
			// these two calls get the name info and the cost info from the dependent user in the UI. both of these are put in currentDependentUnitInfo string. 
			// the type of cost string ("total", "optimizable", or "roaming") is filtered out of dependentUnitInfo string. 
			nameAndIds = driver.findElement(By.cssSelector(HierarchyHelper.dependentsListCssLocator +  ":nth-of-type(" + y + ")>a")).getText(); // name and id(s).
			numericValue = driver.findElement(By.cssSelector(HierarchyHelper.dependentsListCssLocator +  ":nth-of-type(" + y + ")>span")).getText().replace(filterString,""); // numeric value (cost type removed).

			// put name, id, and cost together.
			currentDependentUnitInfo = nameAndIds + " " + numericValue; 
			
			ShowText("hover text - " + hoverInfo + " text created from UI - " + currentDependentUnitInfo);
			
			// make sure dependent unit from UI is not cost = $0.
			if(!DependentUnitValueIsZero(currentDependentUnitInfo))
			{
				Assert.assertEquals(hoverInfo, currentDependentUnitInfo);				
			}
		}

		// Pause("loop through tile maps done.");
        // System.out.println("Number of Tile maps tested = " + (x  - 1));
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
			nameAndIds = driver.findElement(By.cssSelector(HierarchyHelper.dependentsListCssLocator +  ":nth-of-type(" + x + ")>a")).getText(); // name and id(s).
			numericValue = driver.findElement(By.cssSelector(HierarchyHelper.dependentsListCssLocator + ":nth-of-type(" + x + ")>span")).getText().replace(filterString,""); // numeric value (cost type removed).

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
	public static String GetTooltipTextOld(int index) throws AWTException, InterruptedException
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
	
	// written by Ana:
	// this will return the string value for the tile map number hovered hovered in the tile map. 
	public static String GetTooltipText(int index) throws AWTException, InterruptedException
	{
		WebElement tileNumber = driver.findElement(By.cssSelector("#" + chartId + ">svg>g>g.highcharts-label:nth-of-type(" + index + ")")); // select tile map number
        
        Point p = getAbsoluteLocationTileMap(tileNumber); 
        
        //Point p = GeneralHelper.getAbsoluteLocation(tileNumber);
        
        int x_offset = tileNumber.getSize().getHeight() / 2;
        int y_offset = tileNumber.getSize().getWidth() / 2;
        
        int a = p.getX() + x_offset;
        int b = p.getY() + y_offset;
        
        Robot robot = new Robot();
        robot.mouseMove(a, b);
        
        Thread.sleep(2000); // orig
        //Thread.sleep(1000); 
        
        WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip")); 
		
        String retString = tooltip.getText();
        
        // ShowText("Full Value tooltip " + retString.substring(retString.indexOf(".") + 1, retString.length()).trim());
        
        // this removes the leading numbering (i.e. "1. " before the name and cost);
        return retString.substring(retString.indexOf(".") + 1, retString.length()).trim();
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
	
	// this builds the json request string manual test 
	public static String BuildJsonRequestPathManualTest()
	{
		return "hierarchy." + currentHierarchyId +  ".child.payload.rows";
	}
	
	
	// this builds the json request string for getting the total count from json. 
	public static String BuildTotalCount()
	{
		return "hierarchy." + currentHierarchyId +  ".child.payload.totalCount";
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

		// get the expected total count.
		tempString = BuildTotalCount();
		totalCount  =   (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('" + tempString + "')");
		
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

		int actualInt = 0;
		int expectedInt = 0;
		int loopCntr = 0;
		int latestCost = -9999;
		int x = 0;
		String costSelectorString = "";
		String tempString = "";
		
		// get the actual dependent units from the list of dependent units in the UI.
		List<WebElement> eleList = driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList));
		
		// verify text above tile map.
		VerifyTextAboveTileMapAndKpi();
		
		// go through the web element list of dependent units from the list of dependent units in the UI. 
		// verify the actual list of data from the UI is the same as the sorted (expected) list created from the json response.  
		for(WebElement ele : eleList)  
		{
			// get the actual value (from UI) and the expected value (from sorted json list) into variables.  
			
			// with real data, there is a decimal cents (ex: $45.39). The .39 is the decimal cents. need to remove the decimal cents.
			// NeedsCorrected method tells if the number to the right of the dollar sign contains a decimal.
			if(ele.getText().contains(".") && NeedsCorrected(ele.getText()))
			{
				tempString =  TrimDecimal(ele);
			}
			else // there is no decimal.
			{
				tempString = ele.getText();
			}			
			
			// convert string int (decimal has been removed) to real int.
			try
			{
				actualInt = Integer.valueOf(tempString.split("\n")[1].replace(GetCostFilterString(),""));				
			}
			catch (Exception ex)
			{
				ShowText("tempString that failed parsing = " + tempString);
				//ShowInt(actualInt);
				ShowText(ex.getMessage());
			    JOptionPane.showMessageDialog(frame, "FROZEN AT RANDOM ERROR ----------------------. ");
			}
			
			expectedInt  = childList.get(loopCntr).cost; // store expected 
			// System.out.println("Actual Int: " + actualInt + " Expected Int: " + expectedInt); // DEBUG			
			
			// verify actual and expected values are equal.
			Assert.assertEquals(actualInt, expectedInt, "Fail in sorting compare for numeric cost in HierarchyNumbersDependents.VerifyActualExpectedDependentUnits"); // verify cost in json list equals cost in actual list. 

			// compare names. sometimes the numeric values will match and the names won't match. this happens when two or more
			// units have the same numeric value in each list but the units are in different orders.  
			try
			{
				// in some cases (one tenant/month so far) there will be two spaces between the first and last name in the json response and the UI 
				// will have only one space between the first and last name. This couldn't find two spaces ===> if(childList.get(loopCntr).childName.contains("     "));
				// the if statement below was found on this stack trace link directly below. it is some type of regular expression.
				// http://stackoverflow.com/questions/19711689/how-to-detect-if-a-string-input-has-more-than-one-consecutive-space
				if ((childList.get(loopCntr).childName.matches(".*  .*")))
				{
					ShowText("found name with two space separator. Name is: " + childList.get(loopCntr).childName);
					childList.get(loopCntr).childName = childList.get(loopCntr).childName.replace("  ",  " "); 
				}

				Assert.assertEquals(ele.getText().split("\n")[0], childList.get(loopCntr).childName, ""); // orig		
				// ShowText(ele.getText().split("\n")[0] + "  " +   childList.get(loopCntr).childName); // DEBUG
			}
			catch (AssertionError sertErr) // sometimes two or more units have the same numeric value in each list but the units are in different orders.
			{
				//ShowText("Try catch"); 
				//ShowText("Actual:    " + ele.getText().split("\n")[0]); 
				//ShowText("Expected:  " + childList.get(loopCntr).childName);
				
				if(childList.get(loopCntr).cost != latestCost)
				{
					if(expectedList.size() != 0)
					{
						// this verifies all the names have the same value, the actual and expected lists are the same size, and the all have the expacted cost value.
						//ShowExpectedList();
						//ShowActualList();
						VerifyAllDependentChildren(GetCostFromString(expectedList.get(0)));  
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
	
	// this checks dependent units that have the same numeric value. the expected same numeric value is passed in. 
	// this verifies a list of actual and a list expected units have the same values. it doesn't worry about the 
	// order of the lists.  
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
		
		if(!expectedTotal.equals("0")) // bladdd - if expected cost is '0' there is no decimal to remove.
		{
			RemoveDecimalValueFromActualValueList();
		}
		
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
			ShowText("Before Remove " + actualList.get(x));
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
			// x = tempString.length() - tempString.indexOf(".");
			x = tempString.length() - tempString.lastIndexOf("."); // bladdd
			tempString = tempString.substring(0, tempString.length() - x);
		}

		return tempString;
	}		
	
	public static boolean NeedsCorrected(String fullString) // bladdd
	{
		String rightOfDollarSign = ""; 
		
		rightOfDollarSign = fullString.split("\\$")[1];
		
		if(rightOfDollarSign.contains("."))
		{
			return true;			
		}
		else
		{
			return false;
		}
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
		Pause("Doing Drill down test.");
		
		
		while (cntr != maxNumberOfLevels)
		{
			tileToSelect = rand.nextInt(totalNumberOfTilesShown) + 1;
			System.out.println("\n** Selecting tile number " + tileToSelect + " **\n");
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + tileToSelect + ")")).click();
			
			// if have hit a page with no dependents then break.
			if(WaitForNoDependentsInPage(cntr))
			{
				break;
			}
			
			HierarchyNumbersDependents.RunTilesInCommand();  // run tests.
			cntr++;
		}
	}
	
	
	// 					------------ this does the drill down test ------------ 
	// * this drills down until it reaches the point where no more drilling down can be done, or, until it 
	//   reaches the maxNumberOfLevels passed in.
	// * each time a level is drilled down to some test are run that test the three cost filters.
	public static void DrillDownAcrossCostFiltersTileMapCommand(int maxNumberOfLevels, int totalNumberOfTilesShown) throws Exception 
	{
		int tileToSelect;
		int cntr = 0;

		int numberOfDependentUnits =  driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)).size(); 
		Random rand = new Random();
		
		while (cntr != maxNumberOfLevels)
		{

			System.out.println("Current number of dependents (tile maps) to select from = " + numberOfDependentUnits  );
			
			tileToSelect = rand.nextInt(numberOfDependentUnits);
			
			// hack because of tile sizes extreme variance, this should find a tile number that can be selected.
			tileToSelect = AdjustTileMapSelection(tileToSelect + 1);
			
			System.out.println("\n** Selecting tile number " + tileToSelect + " **\n");
			

			// tile maps with zero cost value are not shown in the tile map. it's possible an attempt to click a tile map that's not there 
			// because of zero value could happen, or, the tile map is too small to click. to get around this a try/catch catches this condition and 
			// tile map #1 is clicked, if it also doesn't have a zero value.
			try
			{
				driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + tileToSelect + ")")).click();				
			}
			catch (Exception ex)
			{
				ShowText("Tile to select is too small or has a zero value - trying to select tile one if it exists.");

				// get string value for dependent unit 1. 
				String firstTileMap =  driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)).get(0).getText(); 
				
				// see if dependent unit 1 doesn't have a zero cost. if it does, drilling down is stopped.
				if(DependentUnitValueIsZero(firstTileMap)) 
				{
					break;
				}
				else
				{
					driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + 1 + ")")).click();
				}
				
				driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + 1 + ")")).click();
			}
			
			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
			Thread.sleep(2000); // give time for tile map to load.
			
			// Pause("Freeze After Clicking Tile");
			
			// wait for the new bread crumb to be added.
			Assert.assertTrue(WaitForCorrectBreadCrumbCount(cntr + 1, ShortTimeout), "Fail in wait for breadcrumb count. Method is HierarchyNumbersDependents.WaitForCorrectBreadCrumbCount");

			// if have hit a page with no dependents then break. 
			if(WaitForNoDependentsInPage(cntr))
			{
				break;
			}
			
			HierarchyNumbersDependents.LoopThroughCatergoriesForTileMapCommand();
			//RunTilesInCommand(); // bladdxx tile map
			
			// get dependent numbers size after drilling down. this will be used to  
			numberOfDependentUnits =  driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)).size();
			cntr++;
		}
	}
	
	// 					------------ this does the drill down test for COMMAND tile map------------ 
	// * this drills down until it reaches the point where no more drilling down can be done, or, until it 
	//   reaches the maxNumberOfLevels passed in.
	// * each time a level is drilled down to some test are run that test the three cost filters.
	public static void DrillDownCommandTileMap(int maxNumberOfLevels, int totalNumberOfTilesShown) throws Exception  
	{
		int tileToSelect;
		int cntr = 0;
		
		Random rand = new Random();
		
		ShowText("Doing Drill down test command." );
		
		while (cntr != maxNumberOfLevels)
		{
			tileToSelect = rand.nextInt(totalNumberOfTilesShown) + 1;
			System.out.println("\n** Selecting tile number " + tileToSelect + " **\n");
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + tileToSelect + ")")).click();

			// if have hit a page with no dependents then break.			
			if(WaitForNoDependentsInPage(cntr))
			{
				break;
			}

			HierarchyNumbersDependents.RunTilesInCommand();
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
		int numberOfDependentUnits = 0;
		
		Random rand = new Random();
		
		ShowText("Starting drill down in dependent units.");
		// Pause("Starting drill down in dependent units.");
		
		// drill down to maxNumberOfLevels or to a page where there are no dependent units listed. 
		while (cntr != maxNumberOfLevels)
		{
			numberOfDependentUnits =  driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)).size(); 
			
			// System.out.println("# of dependents before click. " + numberOfDependentUnits);
			
			// get list of dependent units from the UI. get a random number to be used to pick one of the dependent unit.
			List<WebElement> unitsList = driver.findElements(By.cssSelector(HierarchyHelper.dependentsListCssLocator)); 
			dependentUnitToSelect = rand.nextInt(numberOfDependentUnits);
			
			Thread.sleep(1000);
			
			System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **");
			
			// store this to for checking string above tile map later.
			currentLevelName = unitsList.get(dependentUnitToSelect).getText().split("\n")[0];  
			
			unitsList.get(dependentUnitToSelect).click(); // select dependent unit.
			
			// move to top of page to make the testing visible.
			WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
			new Actions(driver).moveToElement(topSection).perform();
			
			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout); 

			// wait for the new bread crumb to be added.
			Assert.assertTrue(WaitForCorrectBreadCrumbCount(cntr + 1, ShortTimeout), "Fail in wait for breadcrump count. Method is HierarchyNumbersDependents.WaitForCorrectBreadCrumbCount");
			
			ShowText("dead spot wait after click to see if have hit end of drill down."); 
			
			// if have hit a page with no dependents then break.
			if(WaitForNoDependentsInPage(cntr))
			{
				break;
			}
			
			HierarchyNumbersDependents.LoopThroughCatergoriesDependentUnits(); // this test loops through all of the category selectors.
		
			cntr++;
		}
	}
	
	public static void ShowChildList()
	{
		ShowText("------------- Start -----------------");
		for(Child chl : HierarchyNumbersDependents.childList){chl.Show();} // DEBUG
		ShowText("------------- Done -----------------");
	}

	// go through each category selector in  the tile map section.
	public static void LoopThroughCatergoriesDependentUnits() throws Exception 
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		Thread.sleep(500);
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			// if(x != 0){continue;}	// DEBUG - use this if you only want one category selected. 
			
			ShowText("Start test for category --- " + values[x].name() +".");
			
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.
			Thread.sleep(2000);
			
			currentCategorySelection = values[x].name(); // store current category for later testing of text above tile map.
			
			// create list of dependent units from Json call.
			BuildDependentChildObjects(); 
			
			// ShowChildList(); // DEBUG
			
			// sort list of dependent units from Json call.
			Collections.sort(HierarchyNumbersDependents.childList, new Child()); 
			Thread.sleep(1000);
			
			// ShowChildList(); // DEBUG
			// Pause("Freeze.."); // DEBUG
			
			// this verifies the json dependents list sent in matches the list shown in the UI, after the Json list is sorted.
			HierarchyNumbersDependents.VerifyActualExpectedDependentUnits();
			
			// this is needed if the last of the dependent list has users with common cost values.
			HierarchyNumbersDependents.FinishFinalTest();
			childList.clear();
			actualList.clear(); // bladdd
			expectedList.clear(); // bladdd

			// Pause("one pass through test if dependents list."); // DEBUG
			
			ShowText("Pass complete for category --- " + values[x].name() +".");
		}
	}
	
	// go through each category selector in  the tile map section.
	public static void LoopThroughCatergoriesFor_Lists_Up_Down() throws Exception 
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		Thread.sleep(1000);
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.
			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
			currentCategorySelection = values[x].name();
			ShowText("STARTING test for ---- " + values[x].name());
			Thread.sleep(1000);
			DrillDown_Up_DependentUnits();				
			ShowText("Pass complete for ---- " + values[x].name() +".");
		}
	}
	
	
	// go through the hierarchies and run a single level tile map test.
	public static void LoopThroughCatergoriesForTileMapCommand() throws Exception  
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.
			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
			Thread.sleep(2000); // wait for tile map to fill in.
			
			ShowText("Start category selector --- " + values[x].name() + ".");
			//HierarchyHelper.WaitForProgressBarInactive(TenTimeout); 
			//Thread.sleep(3000); // let tile map load.
			HierarchyNumbersDependents.RunTilesInCommand();
			ShowText("Pass complete for --- " + values[x].name() +".");
		}
	}
	
	// go through the hierarchies and run a single level tile map test.
	public static void LoopThroughCatergoriyDrillDownsForTileMapCommand() throws Exception // bladdxx - new tile map  
	{
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values(); // get tab selectors from enum.
		
		// this loops through the category selectors one at a tile.  
		for(int x = 0; x < values.length; x++)
		{
			ExpenseHelper.SetHierarchyCostFilter(values[x]); // select category selector tab.
			HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
			Thread.sleep(2000); // wait for tile map to fill in.
			
			ShowText("Start category selector --- " + values[x].name() + ".");
			DrillDownAcrossCostFiltersTileMapCommand(maxLevelsToDrillDownTo, 50);
			
			
			ShowText("Pass complete for --- " + values[x].name() +".");
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
				
				// store away the current hierarchy name. this is used later on as part of verifying the text above the tile map.
				currentLevelName = ele.getText();   
				
				currentHierarchyId = hierarchyIdsList.get(hierarchyCntr); // set the current hierarchy Id. this hierarchyId is global to this class.  
				ele.click(); 

				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
				// Pause("hierarchy selected");
				
				Thread.sleep(2500); // wait for tile map numbers to fill in.
				
				VerifyMainTitle(ele.getText());
				
				DrillDownDependentUnitsTwo(maxLevelsToDrillDownTo); // run the drill down tests for each category selector.
				hierarchyCntr++;
		}
	}
	
	public static void LoopThroughHierarchiesDependentUnitsDrill_Down_Up() throws Exception 
	{
		// get list of web elements, one for each hierarchy.
		List<WebElement> hierarchyList = driver.findElements(By.cssSelector(".tdb-space--top>select>option"));
		
		ShowText(" ------------ Start Looping Through Hierarchies With Drilldowns. -----------------\n\n");
		
		int hierarchyCntr = 0; // this is used for debug.

		// got through the available hierarchies one at a time. call 'DrillDownDependentUnitsTwo()' on each loop.
		for(WebElement ele : hierarchyList)
		{
				ShowText(" ===================== Hierarchy Name: " + ele.getText() + " =========================== ");
				ele.click(); 
				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
				Thread.sleep(1000);
				LoopThroughCatergoriesFor_Lists_Up_Down();
				hierarchyCntr++;
		}
	}
	
	// this loops through each hierarchy and runs a one of the tile map tests. the tile map test to run is set in the test case that calls this method.
	public static void LoopThroughTileMapTests() throws Exception  
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
				ele.click();
				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
				// Thread.sleep(1000);
				RunTileMapTest(currentTileMapTestType); // run test.
				//LoopThroughCatergoriyDrillDownsForTileMapCommand(); // bladdxx 
				hierarchyCntr++;
		}	
	}
	
	public static void LoopThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			
			//ShowText("current " + ele.getText());
			
			Thread.sleep(3000); 
			
			if(!LookForNoDependentsFound())
			{
				ShowText("current minth:" + ele.getText());
				// HierarchyNumbersDependents.LoopThroughHierarchiesDependentUnits(); // for ref app.
			}
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
				ShowText("\nHierarchy Name: " + ele.getText());
				currentLevelName = ele.getText();
				currentHierarchyId = hierarchyIdsList.get(hierarchyCntr);
				ShowText(currentHierarchyId  + "\n");
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
		int drillDownCntr = 0;
		int numBreadCrumbs;
		
		ClearDrillDownUpStringPair(); // clear web element list and text list that are used as temporary holders of information. 
		listsOfPreviousDependentUnits.clear(); // clear list that will hold the list of dependent users' lists. 
		
		aboveKpiStack.clear();
		aboveTileStack.clear();
		
		PushCurrentList(); // this puts the current dependents list in the UI onto 'listsOfPreviousDependentUnits' list.
		Thread.sleep(500); // wait for the list above to load.
		//System.out.println("List size before drill down is being done = " + listsOfPreviousDependentUnits.size());
		
		Thread.sleep(2000); // need extra time here. the title above tile map moves around and takes time to completely fill in.
		
		aboveKpiStack.push(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveKpiTilesCssLocator)).getText()); // store current text shown above KPIs.
		ShowText("@@@@@  push text above tile map @@@@ = " + driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveTileMapCssLocator)).getText());
		aboveTileStack.push(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveTileMapCssLocator)).getText()); // store current text shown above tile map.
		
		// this loops through the drilling down clicks, drilling down into the dependent units 'maxLevelsToDrillDownTo' times.
		for(drillDownCntr = 0; drillDownCntr < maxLevelsToDrillDownTo; drillDownCntr++)
		{
			// this if statement calls the method that does the drill down. If the drill-down method returns false, click the bottom bread crumb and leave for loop.
			if(!DrillDownIntoDependentUnit(drillDownCntr)) 
			{
				WaitForElementVisible(By.cssSelector(".breadcrumbs>span"), MediumTimeout); // sometimes getting "stale element reference" in 'numBreadCrumbs' line below.
				WaitForElementClickable(By.cssSelector(".breadcrumbs>span"), MediumTimeout, ""); // sometimes getting "stale element reference" in 'numBreadCrumbs' line below.
				numBreadCrumbs = driver.findElements(By.cssSelector(".breadcrumbs>span")).size(); // get number of bread crumbs shown.
				driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type("  + numBreadCrumbs + ")")).click();// click the bottom bread crumb to leave the page with no dependent units shown.  
				// Pause("Clicked bottom crumb");
				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
				Thread.sleep(1000);
				break;
			}
			
			PushCurrentList(); // this puts the current dependents list on the page drilled into onto 'listsOfPreviousDependentUnits' list.
			Thread.sleep(500); // wait for the list above to load.
			// System.out.println("List size after drill down. = " + listsOfPreviousDependentUnits.size());
		}
		
		// ShowListsOfDependentUnitsStoredAway(); // this will show all lists on the list that were added in 'AddDependentUnitList' method.
		
		//  this works its way back up by clicking the bread crumbs until there are no bread crumbs left.
		for(int y = drillDownCntr; y >= 0; y--)
		{
			ShowText("---- POP " + listsOfPreviousDependentUnits.get(y).get(0).replace("\n",  " "));
			
			// verify the text above the tile map is the same as when drilling down. 
			Assert.assertEquals(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveTileMapCssLocator)).getText(), aboveTileStack.pop(), "");
			
			// verify the text above the KPIs is the same as when drilling down.
			Assert.assertEquals(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveKpiTilesCssLocator)).getText(), aboveKpiStack.pop(), "");
			
			// get the list of dependent units currently showing in the UI into a temporary list of strings. 
			CreateTempCurrentDependentsList(); 
			
			// pops item 'y' in the list holding the history of dependent unit lists (expected) and compare to the current dependent unit list that is showing (actual).   
			VerifyDependentUsersListsAreEqual(tempStrList, listsOfPreviousDependentUnits.get(y)); 
			
			// DEBUG: this will do list compares using looping and log all errors to console. This does not raise an error.
			//VerifyDependentUsersListsViewResults(tempStrList, listsOfPreviousDependentUnits.get(y)); 
			
			ClearDrillDownUpStringPair();  // clear web element list and text list that are used as temporary holders of information.
			
			if(y != 0) // if not back at the top, click the lowest bread crumb.
			{
				driver.findElement(By.cssSelector(".breadcrumbs>span:nth-of-type(" + y + ")")).click();				
				//Pause("bread crumb was just clicked.");
				HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
				Thread.sleep(2500); // need extra time here. the title above tile map moves around and takes time to completely fill in.
			}
		}
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

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 																HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int UserNumberTileMapsToTest()
	{
		requestedTileMapsToTest = JOptionPane.showInputDialog("Enter number of tile maps to test.");
		
		if(requestedTileMapsToTest.equals("0"))
		{
			return 0;
		}
		else
		{
			return Integer.parseInt(requestedTileMapsToTest);			
		}

	}
	
	
	
	// this checks to see if the current dependent unit to be selected in the tile map is zero.
	public static boolean DependentUnitValueIsZero(String dependentUnit)
	{
		if(dependentUnit.split("\\$")[1].equals("0"))
		{
			ShowText("found zero value.");
			return true;
		}
		
		return false;
	}
	
	/*
	// when the progress bar is active, tag name 'md-progress-bar' is visible and when the progress bar is inactive 'md-progress-bar' is not visible.
	// this will wait the amount of seconds passed in. if the timeout is exceeded the assertTrue will fail.
	public static void WaitForProgressBarInactive(int howLongToWait) throws Exception 
	{
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.tagName("md-progress-bar"), howLongToWait), 
				          "Failed to process wait for progress bar in WaitForProgressBarInactive() method.");
	}
	*/
	// this sees if there is a page with no dependent units on the current page. it has a hard coded two second wait to see if this is true.  
	public static boolean WaitForNoDependentsInPage(int level) throws Exception
	{
		if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), TinyTimeout))
		{
			System.out.println("Finished drill down testing to level " + level);
			System.out.println("The last click found the 'No Dependents' message\n");
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// this verifies the main title is correct. the expected hierarchy name is passed in.
	public static void VerifyMainTitle(String hierarchyName)
	{
		// build expected string
		String expectedMainTitle =  ExpenseHelper.desiredMonth + " " + "~ Hierarchy: " + hierarchyName;
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1")).getText(), expectedMainTitle, "");
	}
	
	// this sees if there is a page with no dependent units on the current page. it has a hard coded two second wait to see if this is true.
	// this also takes a second parameter that is the last dependent unit selected.
	public static boolean WaitForNoDependentsInPage(int level, String currentDependentUnitClicked) throws Exception
	{
		if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), TinyTimeout))
		{
			System.out.println("Finished drill down testing to level " + level);
			System.out.println("The last click found the 'No Dependents' message\n");
			
			String expectedTextInEmptyTileMap = "Top 0 (out of 0) dependent units of " +   currentDependentUnitClicked.split("\n")[0] + " - " +  currentCategorySelection + " " + "Expense  ";
			
			//ShowText("UI :" + driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3")).getText().replace("\n",""));
			//ShowText("Expect :" + expectedTextInEmptyTileMap);

			Assert.assertEquals( driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3")).getText().replace("\n",""), expectedTextInEmptyTileMap, "");
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public static int AdjustTileMapSelection(int tileToSelect) 
	{
		tileToSelect = tileToSelect/2;
		
		if(tileToSelect == 0) 
		{
			tileToSelect = 1;
		}
		
		return tileToSelect;
	}
	
	
	// this is used to wait  
	public static boolean WaitForCorrectBreadCrumbCount(int numCrumbs, int waitTimeInSeconds) throws Exception
	{
		// System.out.println("Current number of crumbs in  'WaitForCorrectBreadCrumbCount' = "  + numCrumbs); 
		
		// get current time.
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime + waitTimeInSeconds * 1000;
		boolean haveFoundCorrectBreadCrumbCount = false;
		
		// wait for number of bread crumbs to equal numCrumbs.		
		while(System.currentTimeMillis() < endTime) 
		{
			if(driver.findElements(By.xpath("//div[@class='breadcrumbs']/span")).size() == numCrumbs)
			{
				haveFoundCorrectBreadCrumbCount = true;
				break;
			}
			Thread.sleep(1000);
		}
		
		// wait for the bottom bread crumb to be clickable.
		WaitForElementClickable(By.cssSelector(".tdb-breadcrumbs>app-hierarchy-breadcrumbs>div>span:nth-of-type(" + numCrumbs + ")"), ShortTimeout, "");
		
		if(haveFoundCorrectBreadCrumbCount)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	
	// this gets the number of tiles shown in the tile map and determines how many to hover test.
	public static int NumberOfTilesToTestTwo() throws Exception
	{
		List<Double> dblList = new ArrayList<Double>();
		double totalOfDpendents = 0;
		
		int loopCntrOrig = driver.findElements(By.cssSelector("#" + chartId + ">svg>g>g.highcharts-label")).size(); // get the number of tiles.
    	int loopCntr = loopCntrOrig/2;

    	if(loopCntr == 1)
    	{
    		return loopCntr;
    	}
    	
		ClearDrillDownUpStringPair();
		CreateTempCurrentDependentsList();

		// go through dependents list and put cost values onto double list.
		for(String str : tempStrList)
		{
			dblList.add(Double.parseDouble(str.split("\\$")[1]));
		}
		
		// total up all the costs.
		for(Double dbl :  dblList)
		{
			totalOfDpendents += dbl;
		}

		// ////////////////////////////////////////////////////////////////////////////////
		// Below -- try to compensate for large tiles taking all of the tile map space.
		// ////////////////////////////////////////////////////////////////////////////////
		
    	if(loopCntr > 10) // make the test short.
    	{
    		loopCntr = 10;
    	}
    	
    	if(loopCntr < 10) // maybe helps with small amounts.
    	{
    		loopCntr = 4;
    	}
		
		if(dblList.get(0) >= .5 * totalOfDpendents) 
		{
			loopCntr = 3;
		}		
		
		if(dblList.size() > 1)
		{
			if(dblList.get(1) >= .4 * totalOfDpendents)
			{
				loopCntr = 2;
			}
		}

		if(dblList.size() < 2)
		{
			loopCntr = 1;
			return loopCntr;
		}
		
		if(dblList.get(0) >= .4 * totalOfDpendents && dblList.get(1) >= .3 * totalOfDpendents)
		{
			loopCntr = 2;
		}
		
		return loopCntr;
	}
	
	// this trims the decimal value off a cost that's related to a dependent unit.
	public static String TrimDecimal(WebElement ele) 
	{
		String localTmp = "";
		int x = ele.getText().length() - ele.getText().lastIndexOf(".");
		localTmp = ele.getText().substring(0, ele.getText().length() - x);
		return localTmp;
	}
	
	// Get the location of the element on the UI 
	public static Point getAbsoluteLocationTileMap(WebElement element) throws InterruptedException  
	{
        int x = x_iFrame;
        int y = y_iFrame;
        
        WebElement header = driver.findElement(By.cssSelector("header.tdb-flexContainer"));
		int headerHeight = header.getSize().getHeight();
        
        Point elementLoc = element.getLocation();

        if (loginType.equals(LoginType.ReferenceApp)) 
        {
        	
            x += elementLoc.getX();
            y += elementLoc.getY() + headerHeight;
            
        	
        }
        else if (loginType.equals(LoginType.Command)) 
        {
        	x += elementLoc.getX();
            y += elementLoc.getY() + headerHeight * 3.2;
        }
        
        Point p = new Point(x, y);
        return p; 
	}
	
	
	// this verifies the text above the tile map and text above the KPIs.
	public static void VerifyTextAboveTileMapAndKpi() throws Exception
	{
		// get actual from UI and create expected for text above tile map.
		String actualTextAboveTileMap =  driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText(); 
		
		String expectedTextAboveTileMap  = "Top " + HierarchyNumbersDependents.childList.size() + " (out of " + totalCount + ") " + "dependent units of " +  currentLevelName +  
				            " - " + currentCategorySelection + " Expense  "; 
		
		// DEBUG - show actual and expected text above tile map.
		//ShowText("expected text above tile maps: " + expectedTextAboveTileMap);
		//ShowText("actual text above tile maps: " + actualTextAboveTileMap);
		
		// verify the text above tile map.
		Assert.assertEquals(actualTextAboveTileMap,  expectedTextAboveTileMap, "Failed to verify text above the tile map in HierarchyNumbersDependents.VerifyTotalCount");

		// get actual from UI and create expected for text above KPI tiles.
		String actualTitleAboveKpiTiles = driver.findElement(By.cssSelector(".tdb-kpi__header.tdb-kpi__header.tdb-text--bold>span:nth-of-type(1)")).getText();

		String expectedTitleAboveKpiTiles = "Expenses for " + currentLevelName + " and its dependent units";

		// DEBUG - show actual and expected text above KPIs.
		//ShowText("expected text above KPIs: " + expectedTitleAboveKpiTiles);
		//ShowText("actual text above KPIs: " + actualTitleAboveKpiTiles);
		
		// verify text above KPI tiles.
		Assert.assertEquals(actualTitleAboveKpiTiles,  expectedTitleAboveKpiTiles, "Failed to verify text above KPI in HierarchyNumbersDependents.VerifyTotalCount");
		
		// Pause("Check numbers in VerifyTextAboveTileMap() passed."); // DEBUG
	}
	
	// this is used after a page is drilled into when doing dependent units drill down. it is done after waiting for the progress bar to be inactive.
	public static void WaitForPageTransition(String unitNameToWaitFor) throws Exception
	{
		String tempString = "";
		int waitForIndex = 0;
		int size = 0;

		// get current time.
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime+10000;

		
		// wait for 'Top 0 dependent units of' to not be visible.		
		while(System.currentTimeMillis() < endTime) 
		{
			tempString = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
			
			if(!tempString.contains("Top 0 dependent units of"))
			{
				break;
			}
			Thread.sleep(1000);
		}

		// get current time.
		currentTime= System.currentTimeMillis();
		endTime = currentTime+10000;

		// wait for expected name in tile map section. 
		while(System.currentTimeMillis() < endTime) 
		{
			tempString = driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText();
			
			if(!tempString.contains("dependent units of " + unitNameToWaitFor))
			{
				//ShowText("Found: dependent units of " + unitNameToWaitFor);
				break;
			}
		}
		
		// get the size of the dependent units list and verify last element is visible.  
		size = driver.findElements(By.cssSelector(".tdb-povGroup>div>ol>li")).size();
		WaitForElementVisible(By.cssSelector(".tdb-povGroup>div>ol>li:nth-of-type(" + size + ")"  ), MediumTimeout);
	}

	public static boolean LookForNoDependentsFound() throws Exception
	{
		// wait to see if 'No Dependents' message is found.
		if(WaitForElementPresentNoThrow(By.cssSelector(".tdb-charts__contentMessage"), ShortTimeout))
		{
			System.out.println("Have found the 'No Dependents' message in expense page.\n");
			return true;
		}		
		return false;
	}
	
	// this gets the dependent units in the UI and adds them to a list of lists.  
	public static void PushCurrentList()
	{
		CreateTempCurrentDependentsList(); // this gets the list of dependent units currently showing in the UI into a temporary list of strings.
		AddDependentUnitList(tempStrList); // store away the list of dependent units currently showing.
		ShowText("--- PUSH -- this is the first item of list pushed: "  + tempStrList.get(0).replace("\n",  " "));
		ClearDrillDownUpStringPair();  // clear web element list and text list that are used as temporary holders of information.
	}
	

	// this is for debug.
	public static void VerifyDependentUsersListsViewResults(List<String> actualList, List<String> expectedList)
	{
		ShowText("Start loop tests --------- ");

		// verify list sizes match.
		Assert.assertEquals(actualList.size(), expectedList.size(), "Actual and Expected list sizes are not equal in HierarchyNumbersDependents.VerifyDependentUsersListsAreEqual.");

		for(int x = 0; x < actualList.size(); x++)
		{
			if(!actualList.get(x).equals(expectedList.get(x)))
			{
				ShowText("Fail:: \n" + "actual: " + actualList.get(x) + "\n" + "expected: " + expectedList.get(x));
			}
		}
		ShowText("Done loop test --------- ");
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

	public static boolean DrillDownIntoDependentUnit(int cntr) throws Exception  
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

		// get name associated with the dependent unit to be clicked.
		String dependentNameToDrillTo = unitsList.get(dependentUnitToSelect).getText().trim(); 

		// click on dependent unit.
		System.out.println("** Selecting Dependent Unit " + (dependentUnitToSelect + 1) + " **"); 
		unitsList.get(dependentUnitToSelect).click(); // select dependent unit. 
		
		// move to top of page to make the testing visible.
		WebElement topSection = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1"));
		new Actions(driver).moveToElement(topSection).perform();

		HierarchyHelper.WaitForProgressBarInactive(MediumTimeout);
		
		// wait for correct number of bread crumbs and also wait for the bottom bread crumb to be clickable.
		Assert.assertTrue(WaitForCorrectBreadCrumbCount(cntr + 1, ShortTimeout), "Fail in wait for breadcrump count. Method is HierarchyNumbersDependents.WaitForCorrectBreadCrumbCount");		
		
		// Pause("Wait after drill down click."); // **********************************************************************************
		
		// this waits to see if the bottom of the drill-downs has been found.
		if(drillDownPageType == DrillDownPageType.expense)
		{
			// if have hit a page with no dependents then break (return from this method call).
			if(WaitForNoDependentsInPage(cntr, dependentNameToDrillTo))
			{
				return false;
			}
			else
			{
				WaitForPageTransition(dependentNameToDrillTo); // extra check after progress bar inactive.
				//aboveTileStack.push(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText()); // save off the text above the tile map in the page that was clicked into. 
				//aboveKpiStack.push(driver.findElement(By.cssSelector(".tdb-kpi__header.tdb-kpi__header.tdb-text--bold>span:nth-of-type(1)")).getText()); // store current text shown above KPIs.

				aboveTileStack.push(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveTileMapCssLocator)).getText()); // save off the text above the tile map in the page that was clicked into. 
				aboveKpiStack.push(driver.findElement(By.cssSelector(HierarchyHelper.textShownAboveKpiTilesCssLocator)).getText()); // store current text shown above KPIs.
				
				
				
				
				// send text name of unit being clicked 
			}
		}
		else
		{
			if(!WaitForTopTenDrillDown())
			{
				System.out.println("Dependents list in Top Ten Is empty.\n");
				Pause("See No dependents");
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean WaitForTopTenDrillDown() throws Exception
	{
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime+6000;
		boolean listIsVisible = false;

		while(System.currentTimeMillis() < endTime) 
		{
			  Thread.sleep(1000);
			  if(driver.findElements(By.cssSelector(ExpenseHelper.hierarchyDependentsList)).size() > 0)
			  {
				  listIsVisible = true;
				  Pause("Can drill down again.");
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
				break;
			}
			
			case months:
			{
				TestPhaseWithMonths();
				break;
			}
			
			case drillDownCommand:
			{
				DrillDownAcrossCostFiltersTileMapCommand(maxLevelsToDrillDownTo, 50);
				break;
			}
			 
			case commandSmoke:
			{
				HierarchyNumbersDependents.RunTilesInCommand();
				break;
			}
			
			case commandAllCatergories:
			{
				LoopThroughCatergoriesForTileMapCommand();
				break;
			}
			
			default:
			{
				Assert.fail("Incorrect case sent to HierarchyNumbersDepedendent.RunTileMapTest");
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
}
