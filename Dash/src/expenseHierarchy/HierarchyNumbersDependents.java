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
	public static String tempString = "";
	public static String dependentUnitInfo = "";
	public static String hoverInfo = "";
	public static String filterString = "";
	public static String chartId = "";
	
	public static double expectedValueDouble = 0;
	public static double actualValueDouble = 0;
	public static int createErrorCounter = 0; // this can be used in 'GetExpectedValue'method to create an intentional error. 
	
	public static JavascriptExecutor js = (JavascriptExecutor) driver;	
	
	
	public static void TestPhaseOne() throws Exception
	{
		ShowText("Run Total Cost Filter --");
		
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesTwo();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("Failed Total Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
		}
		
		ShowText("Run Optimizable Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Optimizable);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesTwo();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("Failed Optimizable Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
		}

		ShowText("Run Roaming Cost Filter --");
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Roaming);
		Thread.sleep(1500);
		
		try
		{
			HierarchyNumbersDependents.RunAllTilesTwo();
			// HierarchyNumbersDependents.RunThroughMonths();
		}
		catch(AssertionError es)
		{
			ShowText("Failed Roaming Cost Filter");
			ShowText(es.getMessage());
			ExpenseHelper.failedtestNgTest = true;
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
	
	public static void RunThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> eleList = CommonTestStepActions.webListPulldown;
		
		for(WebElement ele : eleList)
		{
			// ShowText(ele.getText()); // DEBUG
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			Thread.sleep(3000);
			RunAllTilesTwo();
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
			filterString = BuildStringToFilterOut();
			
			// these two calls get the name info and the cost info from the dependent user in the UI. both of these are put in dependentUnitInfo string. 
			// the type of cost string ("total", "optimizable", or "roaming") is filtered out of dependentUnitInfo string. 
			tempOne = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>a")).getText(); // name and id(s).
			tempTwo = driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>span")).getText().replace(filterString,""); // numeric value (cost type removed).
			
			// put name, id, and cost together..
			dependentUnitInfo = tempOne + " " + tempTwo; 
			
			// Thread.sleep(1000);
			
			// click tile number x.
			driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(6)>g:nth-of-type(" + x + ")")).click();
			
			Thread.sleep(2000);
			
			// these get the name info and the cost that was in the hover, after the click above. both of these are put in hoverInfo string.
			tempOne = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText().split("\\.")[1].trim();
			tempTwo = driver.findElement(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(2)")).getText();
			
			hoverInfo = (tempOne + " " + tempTwo); 
			
			Assert.assertEquals(dependentUnitInfo, hoverInfo); // verify hover value and it's corresponding dependent unit value are equal.  
			
			// get the double value found in the dependent info 
			expectedValueDouble = Double.valueOf(dependentUnitInfo.split("\\$")[1]);
			
			// now get the dependent unit user cost value in the json array that was stored before the click to get hover info. 
			// send in tempOne, the user name info, and this call will return the expected value as double.
			//actualValueDouble = GetExpectedValue(array, tempOne);
			actualValueDouble = GetExpectedValueTwo(array, tempOne, ExpenseHelper.currentHierarchyCostFilter);
			 
			
			Assert.assertEquals(actualValueDouble, expectedValueDouble,"ERR");
			ShowText("Pass " + String.valueOf(x));
			
			
			// click the bread crumb.
			driver.findElement(By.cssSelector(".breadcrumbs>span>a")).click();
			
			Thread.sleep(1000);
		}
	}
	
	
	// this gets a json array and looks for a json object that has the name passed in.  
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

		DebugTimeout(9999, "Didn't find hover value " + name);
		
		return 0;
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
	public static String BuildStringToFilterOut()
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
	
	
	
	
}
