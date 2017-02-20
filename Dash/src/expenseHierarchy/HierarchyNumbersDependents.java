package expenseHierarchy;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.jetty.servlet.Debug;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;
import helperObjects.UsageHelper;

public class HierarchyNumbersDependents extends BaseClass 
{

	public static int numberOfTileMapsToTest = 0;
	public static int maxNumberOfTileMapsThatCanBeTested = 100;
	public static int maxNumberOfTileMapsThatCanBeShown = 100;
	public static int tileNumberToStartLoop = 0;
	public static String dependentUnits = "";
	public static String tempOne = "";
	public static String tempTwo = "";
	public static String dependentUnitInfo = "";
	public static String hoverInfo = "";
	public static String chartId = "";
	
	public static double expectedValueDouble = 0;
	public static double actualValueDouble = 0;
	public static int createErrorCounter = 0; // this can be used in 'GetExpectedValue'method to create an intentional error. 
	
	public static JavascriptExecutor js = (JavascriptExecutor) driver;	
	
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
		
		ExpenseHelper.SetHierarchyMaxDisplayed(numToDisplay);	
	}

	// need work !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
	
	public static void RunThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> eleList = CommonTestStepActions.webListPulldown;
		
		for(WebElement ele : eleList)
		{
			// ShowText(ele.getText()); // DEBUG
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			Thread.sleep(3000);
			RunAllTiles();
		}
	}
	
	
	
	// this is called after a number of tiles to test has been setup, a month has been selected, and a tile level has been setup (unless the top level is being tested).
	public static void RunAllTiles() throws Exception
	{
		for(int x = 1; x <= numberOfTileMapsToTest; x++) // this loop can't go further than 90.
		{ 
			// get the json of the current tile map being shown
			dependentUnits =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child.payload.rows')");
			Thread.sleep(1000);

			// verify json fetch is OK.
			Assert.assertTrue(dependentUnits != null); 

			// do this here to also make sure json fetch worked
			JSONArray array = new JSONArray(dependentUnits);			

			// ShowText(dependentUnits.substring(0,50)); // DEBUG show some of the json return.

			// these get the name info and the cost from the dependent in the UI. both of these are put in dependentUnitInfo string.
			tempOne = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>a")).getText(); // name and id(s).
			//tempTwo = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>span")).getText().replace("Total:",""); // numeric value and cost type.
			tempTwo = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>span")).getText().replace(ExpenseHelper.hierarchyFilterString,""); // numeric value and cost type.			
			
			dependentUnitInfo = tempOne + " " + tempTwo; 
			
			// Thread.sleep(1000);
			
			// click tile x.
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + x + ")")).click();
			
			Thread.sleep(1000);
			
			// these get the name info and the cost that was in the hover, after the click above. both of these are put in hoverInfo string.
			tempOne = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText().split("\\.")[1].trim();
			tempTwo = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(2)")).getText();
			
			hoverInfo = (tempOne + " " + tempTwo); 
			
			Assert.assertEquals(dependentUnitInfo, hoverInfo); // verify hover value and it's corresponding dependent unit value are equal.  
			
			// get the double value found in the dependent info 
			expectedValueDouble = Double.valueOf(dependentUnitInfo.split("\\$")[1]);
			
			// now get the dependent unit user cost value in the json array that was stored before the click to get hover info. 
			// send in tempOne, the user name info, and this call will return the expected value as double.
			actualValueDouble = GetExpectedValue(array, tempOne);
			actualValueDouble = GetExpectedValueTwo(array, tempOne, ExpenseHelper.currentHierarchyCostFilter);
			
			
			Assert.assertEquals(actualValueDouble, expectedValueDouble,"ERR");
			ShowText("Pass " + String.valueOf(x));
			
			
			// click the bread crumb.
			driver.findElement(By.cssSelector(".breadcrumbs>span>a")).click();
			
			Thread.sleep(1000);
		}

	}
	
	
	public static double GetExpectedValue(JSONArray jArray, String name) throws Exception
	{
		JSONObject obj;
		
		for(int x = 0; x < jArray.length(); x++)
		{
			obj  = jArray.getJSONObject(x);
			
			// ShowText(jArray.getJSONObject(x).getString("name"));
			if(obj.getString("name").equals(name))
			{
				return obj.getDouble("total_expense_rollup_ex");					
			}
		}

		// Assert.fail("FAIL XXXXX");
		DebugTimeout(9999, "Didn't find hover value " + name);

		
		return 0;
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

		// Assert.fail("FAIL XXXXX");
		DebugTimeout(9999, "Didn't find hover value " + name);

		
		return 0;
	}	
	
	
	
	
}
