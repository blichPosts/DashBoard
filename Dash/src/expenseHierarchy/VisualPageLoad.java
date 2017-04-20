package expenseHierarchy;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.expenseFilters;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

public class VisualPageLoad extends BaseClass 
{
	public static String tempString;
	public static String chartId = "";
	public static expenseFiltersLocation currentExpenseFilterLocation;
	
	public static List<String> expectedCostFilters = new ArrayList<>();	
	
	public static JavascriptExecutor js = (JavascriptExecutor) driver; // bladdxx

	
	// locators
	public static String tileMapExpenseSeletorCssLocator = ".tdb-card>div:nth-of-type(1)>div"; // this is selectors above tile map
	public static String expenseTrendExpenseSeletorCssLocator = ".tdb-card>div:nth-of-type(3)>div"; // this is selectors above expense trend.
	//.tdb-card>div:nth-of-type(3)
	//.tdb-card>div:nth-of-type(1)
	
	// this is for indicating which trend graph expense filter is being tested. 
	public static enum expenseFiltersLocation
	{
		TileMap,
		ExpenseTrend,
	}

	public static void SetupExpectedCostFilters()
	{
		expectedCostFilters.add("Total");
		expectedCostFilters.add("Optimizable");
		expectedCostFilters.add("Roaming");
	}
	
	// this sets currentExpenseFilter to what expense filter is being used to click each selection.
	public static void SetExpenseFilter(expenseFiltersLocation expFilter)
	{
		currentExpenseFilterLocation = expFilter;
	}	
	
	
	// this uses the 'currentExpenseFilter' value to decide which set of cost filters will be used to make the clicks across all the filters. 
	// the 'ClickThroughFiltersAndVerify' method does the clicks and then calls the method that handles all the testing. 
	public static void VerifySpendCateoryFilter() throws Exception 
	{
		switch(currentExpenseFilterLocation)
		{
			case TileMap:
			{
				ClickThroughFiltersAndVerify(tileMapExpenseSeletorCssLocator);
				break;
			}
			case ExpenseTrend:
			{
				ClickThroughFiltersAndVerify(expenseTrendExpenseSeletorCssLocator);
				break;
			}
			default:
			{
				Assert.fail("Bad case sent to sent to ExpenseHelper.VerifySpendCateoryFilter.");
			}
		}
	}
	
	// this clicks through the 
	public static void ClickThroughFiltersAndVerify(String css) throws Exception
	{
		// get a list of the web elements that are related to the xPath passed in.
		// the xPath is pointing to one of the category filters. 
		List<WebElement> listToClickThrough = driver.findElements(By.cssSelector(css));
		
		Assert.assertTrue(listToClickThrough.size() != 0, "Found empty list in ClickThroughFiltersAndVerify.");
		
		int x = 0;
		
		// go through the each filter selection and select them one at a time.
		for(WebElement ele : listToClickThrough)
		{
			ele.click();
			
			Thread.sleep(1000);
			
			Assert.assertEquals(ele.getText(), expectedCostFilters.get(x), "");
			
			// this verifies other category selector that is not being clicked through. 
			VerifyRemainingCategorySelectors(x);

			Thread.sleep(1000);
			
			x++;
		}
	}
	
	// this is called after each tab click. this verifies the category selector tabs that are not being clicked through. 
	// variable x passed in is the tab selection that should be selected/enabled.
	public static void VerifyRemainingCategorySelectors(int x) 
	{

		List<WebElement> tempListOne;

		switch(currentExpenseFilterLocation)
		{
			case TileMap: // the tabs above the tile map are being clicked through.
			{
				tempListOne = driver.findElements(By.cssSelector(expenseTrendExpenseSeletorCssLocator));   
				VerifyCorrectSelection(tempListOne, x); // verify status of other set of tabs.
				break;
				
			}
			case ExpenseTrend: // the tabs above the expense trending are being clicked through.
			{
				tempListOne = driver.findElements(By.cssSelector(tileMapExpenseSeletorCssLocator));
				VerifyCorrectSelection(tempListOne, x); // verify status of other set of tabs.
				break;
				
			}
			default:
			{
				Assert.fail("Bad case parameter in VerifyRemainingCostSelectors.");
			}
		}
	}
	
	// this is sent a web list of selectors to look through. one of them is selected and the rest aren't.
	// the one that is selected is indicated by the integer sent in. the rest of the cost filters are not selected.
	// this verifies that the list element with the index 'x' is enabled and all the other list elements are not.  
	public static void VerifyCorrectSelection(List<WebElement> eleList,  int x)
	{
		String errMessage = "Failed testing of enabled/disabled cost filters in VerifyCorrectSelection";
		
		Assert.assertTrue(eleList.size() != 0, "Method VerifyCorrectSelection has been paseed an empty web element list.");
		
		for(int y = 0; y < eleList.size(); y++)
		{
			if(y == x)
			{
				Assert.assertTrue(eleList.get(y).getAttribute("class").contains("option--selected"),errMessage); 
			}
			else
			{
				Assert.assertFalse(eleList.get(y).getAttribute("class").contains("option--selected"), errMessage);
			}
		}
	}
	
	
	
	// this waits for the page to load.
	public static void SelectAndWaitForPageLoad() throws Exception
	{
		js.executeScript("__TANGOE__setShouldCaptureTestData(true)"); // bladdxx
		WaitForElementClickable(By.xpath("//a[text()='View by Hierarchy']"), MediumTimeout, "");
		driver.findElement(By.xpath("//a[text()='View by Hierarchy']")).click();
		WaitForElementVisible(By.xpath("//h2[text()='" + ExpenseHelper.desiredMonth + "']"), MediumTimeout); // this is month in top left corner tiles.
		WaitForElementVisible(By.xpath("//span[text()='Total Expense']"), MediumTimeout); // this is text in top left corner tiles. 
		WaitForElementVisible(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"), MediumTimeout); // this is drop down in top left corner POV.
		chartId =  UsageHelper.getChartId(0);
		HierarchyHelper.WaitForProgressBarInactive(TenTimeout);
		Thread.sleep(2000); // time for tile map to load.
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// /////////////////////////////////////////////////////////////////////////////////
	// NOTE BELOW - can eventually remove - was used for partial automated testing.
	// /////////////////////////////////////////////////////////////////////////////////
	
	
	public static void ConsoleOutForFileDiffActualValues() throws Exception
	{
		
		hierarchyTileMapTabSelection localSelection = hierarchyTileMapTabSelection.Total;
		
		hierarchyTileMapTabSelection[] values = hierarchyTileMapTabSelection.values();
		
		List<WebElement> eleList = 	 driver.findElements(By.cssSelector(".tdb-pov__itemList>li")); // get web element list that holds items in the POV.

		int localCntr = 0;
		
		String outFile = "C:\\LichPublic\\_NewDash\\FileDiffs\\Two.txt";
		
		PrintWriter pw = new PrintWriter(new FileWriter(outFile));
		
		// .tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(2)
		// .tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div
		
		// BELOW NEEDS  FIXED -- get correction for this -- By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div") in foo().  
		
		// List<WebElement> tabsList = driver.findElements(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div"));

		List<WebElement> tabsList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(1)>div"));		
		
		
		Assert.assertTrue(tabsList.size() == 3); // for later.
		
		for(int x = 0; x < tabsList.size(); x++)
		{
			if(tabsList.get(x).getAttribute("class").contains("option--selected"))
			{
				localSelection = values[x];
			}
			
		}
		
		ShowText("Tab Name " + localSelection.name());
		
		for(WebElement ele : eleList)
		{

			tempString = ele.getText();
			
			// ShowText(tempString);
			//ShowText(tempString.replace("\n", ""));
			
			//ShowText(tempString.replace("\n", "").replace("Total:", " ").replace("$", ""));
			//pw.write(tempString.replace("\n", "").replace("Total:", " ").replace("$", "") + "\n");
			

			if(localSelection.name() == hierarchyTileMapTabSelection.Total.name())
			{
				ShowText(tempString.replace("\n", "").replace("Total:", " ").replace("$", ""));
				pw.write(tempString.replace("\n", "").replace("Total:", " ").replace("$", "") + "\n");
			}
			else if(localSelection.name() == hierarchyTileMapTabSelection.Optimizable.name())
			{
				//ShowText(tempString);
				ShowText(tempString.replace("\n", "").replace("Optimizable:", " ").replace("$", ""));
				pw.write(tempString.replace("\n", "").replace("Optimizable:", " ").replace("$", "") + "\n");
			}
			else if(localSelection.name() == hierarchyTileMapTabSelection.Roaming.name())
			{
				ShowText(tempString.replace("\n", "").replace("Roaming:", " ").replace("$", ""));
				pw.write(tempString.replace("\n", "").replace("Roaming:", " ").replace("$", "") + "\n");
			}

			
			/*
			ShowText("ELE " + " " + ele.getText()); // DEBUG
			strArrayOne = ele.getText().split(" ");
			String id = strArrayOne[0]; // "1:81"
			String lastNameSection = strArrayOne[1]; // "Zhang,"
			// remove the "\n" from array element [2]. 
			// element[2] now looks like this ' Sandra:86644221Total:$82390'.
			strArrayOne[2] =   strArrayOne[2].replace("\n", "");    
			String firstName = strArrayOne[2].split(":")[0].trim(); 
			String cost = strArrayOne[2].split(":")[2].replace("$","").trim();
			localCntr++;
			// System.out.println(localCntr + " " + lastNameSection + " " + firstName + " " + cost);
			 */
		}
		pw.close();
	}
	
	public static void ManualDependencyUnits() throws Exception
	{
			
			boolean runLoop = true;
		
			WaitForElementClickable(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(1)"), ShortTimeout, "");
			//driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(1)")).click(); // total			
			//driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(2)")).click(); // optimizable
			driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(3)")).click(); // roaming
			
			new Select(driver.findElement(By.xpath("(//span[text()='Maximum Displayed:'])[2]/following::select"))).selectByVisibleText("100");
			
			
			// select Hierarchy
			new Select(driver.findElement(By.cssSelector(" .tdb-space--top>select"))).selectByVisibleText("Philips");
			
			// DebugTimeout(9999, "9999");
		
			// JavascriptExecutor js = (JavascriptExecutor)driver;
			// js.executeScript("__TANGOE__setShouldCaptureTestData(true)"); 
		
			while(runLoop)
			{
				JOptionPane.showMessageDialog(frame, "THIS IS THE BEGINNING>");
				
	
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child')");
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.SECONDARY.child')");
				
				HierarchyNumbersDependents.hierarchyIdsList = HierarchyHelper.getHierarchiesValues();
				HierarchyNumbersDependents.currentHierarchyId = HierarchyNumbersDependents.hierarchyIdsList.get(0);

				
				// this gets the json request text to be used below. this depends on what selection is in the hierarchy pulldown.
				tempString = HierarchyNumbersDependents.BuildJsonRequestPathManualTest(); 

				ShowText("String to use " + tempString);
				HierarchyNumbersDependents.Pause("Freeze");
				
				// get json into one string.
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.TERTIARY.child')");
				
				String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('tempString')");
				
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child')"); // EXPERIMENTING.
				
				// ED said to try this sometime ....
				//Object testObject =  js.executeScript("return __TANGOE__getCapturedTestData('hierarchy.PRIMARY.child')");
				//if(testObject != null){	ShowText("good");}
				//else{ShowText("null");}
				
				ShowText(fleetJsonData);
				
				HierarchyNumbersDependents.Pause("Freeze");
				
				JOptionPane.showMessageDialog(frame, "THIS IS START --  get json response and run utility with it.");

				JOptionPane.showMessageDialog(frame, "COMPLETE for selenium part.");
				
				ConsoleOutForFileDiffActualValues();
				
				int result = JOptionPane.showConfirmDialog(null,  "Are you sure you wish to exit?",null, JOptionPane.YES_NO_OPTION);
				
				if(result == JOptionPane.YES_OPTION) 
				{
					runLoop = false;
				} 		
			}			
			
			ShowText("Exiting Loop BYE.");
	}
}
