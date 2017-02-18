package expenseHierarchy;

import java.awt.Robot;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.crypto.ShortBufferException;
import javax.swing.JOptionPane;
import javax.tools.DocumentationTool.Location;

import org.bouncycastle.asn1.icao.CscaMasterList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;
import helperObjects.UsageHelper;

public class VisualPageLoad extends BaseClass 
{
	String leadingTextForAboveKpis = "Expenses for ";
	String trailingTextForAboveKpis = " and its dependent units ";	
	
	public static String []  strArrayOne;
	public static String []  strArrayTwo;
	public static String tempString;
	public static String parentUnitPullDownText = "";
	public static String fullTitleAboveKpiTiles = "";
	
	// for title above KPI tiles
	public static String  firstPartTitleAboveKpiTiles = "Expenses for ";
	public static String  lastPartTitleAboveKpiTiles = " and its dependent units";
	public static String  maximumDisplaedExpected = "10";
	
	// for title above tile map.
	public static String  tileMapTitlePartOne = "Top ";
	public static String  tileMapTitlePartTwo = " (out of 100) dependent units of ";

	// these are used for the end text for some titles that depend on the cost type selector selection.  
	public static String  totalExpenseEnd = " - Total Expense";
	public static String  optimizableExpenseEnd = " - Optimizable Expense";
	public static String  roamingExpenseEnd = " - Roaming Expense";

	public static JavascriptExecutor js = (JavascriptExecutor) driver; // bladdxx
	
	
	// for Expense Trend title.
	public static String  expenseTrendingPartOne = "Expense Trend for ";
	
	public static void SelectAndWaitForPageLoad() throws Exception
	{
		js.executeScript("__TANGOE__setShouldCaptureTestData(true)"); // bladdxx
		WaitForElementClickable(By.xpath("//a[text()='View by Hierarchy']"), MediumTimeout, "");
		driver.findElement(By.xpath("//a[text()='View by Hierarchy']")).click();
		WaitForElementVisible(By.xpath("//h2[text()='" + ExpenseHelper.desiredMonth + "']"), MediumTimeout); // this is month in top left corner tiles.
		WaitForElementVisible(By.xpath("//span[text()='Total Expense']"), MediumTimeout); // this is text in top left corner tiles. 
		WaitForElementVisible(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"), MediumTimeout); // this is drop down in top left corner POV.
	}
	
	public static void VerifyInitialStatesAfterPageLoad() throws Exception 
	{
		Assert.assertEquals(CommonTestStepActions.GetPulldownTextSelected(), ExpenseHelper.desiredMonth, "");
		
		// wait for parent unit pulldown visible.
		WaitForElementVisible(By.cssSelector(".tdb-kpi__header.tdb-h3.tdb-kpiSection__title.tdb-text--bold>span:nth-of-type(1)"), MediumTimeout);
		
		// /////////////////////
		// verify all titles
		// /////////////////////
		
		// this gets the complete string above the KPI tiles.
		fullTitleAboveKpiTiles =  driver.findElement(By.cssSelector(".tdb-kpi__header.tdb-h3.tdb-kpiSection__title.tdb-text--bold>span:nth-of-type(1)")).getText();
		
		// now get the parent unit name in the parent unit pulldown. 
		parentUnitPullDownText =  new Select(driver.findElement(By.cssSelector(".tdb-space--half--top>select"))).getFirstSelectedOption().getText();
		
		// verify the full text above the KPI tiles is correct.  
		Assert.assertEquals(fullTitleAboveKpiTiles, firstPartTitleAboveKpiTiles + parentUnitPullDownText + lastPartTitleAboveKpiTiles,
							"Failed check for title above KPI toles in VisualPageLoad.VerifyInitialStatesAfterPageLoad.");

		// get complete title above tile map
		tempString = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>h3")).getText();
		
		// verify title above tile map. 
		Assert.assertEquals(tileMapTitlePartOne + maximumDisplaedExpected + tileMapTitlePartTwo + parentUnitPullDownText + totalExpenseEnd, tempString, 
				            "Failed check for title above tile map in VisualPageLoad.VerifyInitialStatesAfterPageLoad.");
		
		// verify 'Expense Trend' part of expense trending title.
		WaitForElementVisible(By.xpath("//h2[text()='Expense Trending']"), ShortTimeout);
		
		// get complete title above the expense trending. 
		tempString = driver.findElement(By.xpath("(//h2[text()='Expense Trending']/following ::h3)[1]")).getText();
		
		// verify title below 'Expense Trending'.
		Assert.assertEquals(expenseTrendingPartOne +  parentUnitPullDownText + totalExpenseEnd, tempString, 
				"Failed check for long title above 'Expense Trending' in VisualPageLoad.VerifyInitialStatesAfterPageLoad.");
		
		// //////////////////////////////////
		// verify max displayed pull downs.
		// //////////////////////////////////

		// get the value of the POV 'maximum displayed' pulldown and verify it is the expected value. 
		tempString = new Select(driver.findElement(By.xpath("(//span[text()='Maximum Displayed:'])[1]/following::select"))).getFirstSelectedOption().getText();
		Assert.assertEquals(tempString, maximumDisplaedExpected, "Failed check for expected 'maximum diaplayed' for POV in VisualPageLoad.VerifyInitialStatesAfterPageLoad.");
		
		// get the tile map 'maximum displayed' pulldown and verify it is the expected value. 
		tempString = new Select(driver.findElement(By.xpath("(//span[text()='Maximum Displayed:'])[2]/following::select"))).getFirstSelectedOption().getText();
		Assert.assertEquals(tempString, maximumDisplaedExpected, "Failed check for expected 'maximum diaplayed' for  in VisualPageLoad.VerifyInitialStatesAfterPageLoad.");

		
		// driver.findElement(By.cssSelector("")
		
		
		// .tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(1)
		
		
	}
	
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
	
	public static void foo() throws Exception
	{
			
			boolean runLoop = true;
		
			WaitForElementClickable(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(1)"), ShortTimeout, "");
			//driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(1)")).click(); // total  // old
			//driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(2)")).click(); // optimizable  // old		
			// driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div:nth-of-type(3)")).click(); // roaming  // old
			//driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(1)")).click(); // total			
			//driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(2)")).click(); // optimizable
			driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(3)")).click(); // roaming
			
			new Select(driver.findElement(By.xpath("(//span[text()='Maximum Displayed:'])[2]/following::select"))).selectByVisibleText("100");
			
			
			// select Hierarchy
			new Select(driver.findElement(By.cssSelector(" .tdb-space--top>select"))).selectByVisibleText("Approval");
			
			// DebugTimeout(9999, "9999");
		
			// JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("__TANGOE__setShouldCaptureTestData(true)"); 
		
			while(runLoop)
			{
				JOptionPane.showMessageDialog(frame, "THIS IS THE BEGINNING>");
				
	
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child')");
				// String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.SECONDARY.child')");
				String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.TERTIARY.child')");
				// ED said to try this sometime ....
				//Object testObject =  js.executeScript("return __TANGOE__getCapturedTestData('hierarchy.PRIMARY.child')");
				//if(testObject != null){	ShowText("good");}
				//else{ShowText("null");}
				
				ShowText(fleetJsonData);
				
				JOptionPane.showMessageDialog(frame, "THIS IS START --  get json response and run utility with it.");

				JOptionPane.showMessageDialog(frame, "COMPLETE for selenium part.");
				
				ConsoleOutForFileDiffActualValues();
				
				int result = JOptionPane.showConfirmDialog(null,  "Are you sure you wish to exit?",null, JOptionPane.YES_NO_OPTION);
				
				if(result == JOptionPane.YES_OPTION) 
				{
					runLoop = false;
				} 		
			}			
			
			ShowText("Exiting Loop.");
	}

	public static void Hover() throws Exception
	{
		
		List<WebElement> eleList;
		
		String tempString = UsageHelper.getChartId(0);
		
		// maybe for x/y coordinates.
		//ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(5)>g>g>rect:nth-of-type(1)")).getAttribute("x"));
		//ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(5)>g>g>rect:nth-of-type(1)")).getAttribute("y"));
		
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// this tries to use this x/y  -- NOPE --  off the charts. 
		// <rect class="highcharts-point highcharts-color-1 " x="6.5" y="-0.5" width="168" height="216" fill="rgb(7,30,74)" stroke="#e6e6e6" stroke-width="1"/>
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//DebugTimeout(8, "five");
		//double x =   Double.valueOf(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(5)>g>g>rect:nth-of-type(1)")).getAttribute("x"));
		//double y =   Double.valueOf(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(5)>g>g>rect:nth-of-type(1)")).getAttribute("y"));

		//Robot robot = new Robot(); 
		//robot.mouseMove((int)x, (int)y);
		
		// robot.
		
		
		// highlights numbers.
		// #highcharts-ahgmc1i-37>svg>g:nth-of-type(6)>g:nth-of-type(1)

		// ///////////////////////////////
		//  click below with number one. 
		// ///////////////////////////////

		/*
		ShowText((driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(1)>a")).getText())); // ---------- get before click()

		driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(6)>g:nth-of-type(1)")).click(); // ------------- CLICK 
		
		Thread.sleep(2000);
		
		ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText()); // ---------- SHOW
		// ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(2)")).getText());

		ShowText((driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(2)>a")).getText())); // ---------- get before click()

		driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(6)>g:nth-of-type(2)")).click(); // ------------- CLICK

		Thread.sleep(2000);
		
		ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText()); // ---------- SHOW
		// ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(2)")).getText());

		
		DebugTimeout(9999, "9999");
		*/

		String dependentUnits = (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child.payload.rows')");
		
		ShowText(dependentUnits);
		
		// Get the rows with data
		// String dependentUnits = tempDependentUnits.split("\"rows\":")[1];

		JSONArray array = new JSONArray(dependentUnits); 
		JSONObject jsonObj = array.getJSONObject(0);
		ShowText(jsonObj.getString("id"));
		
		
		//for (int i = 0; i < array.length(); i++)
		//{
              //JSONObject jsonObj  = array.getJSONObject(i);
              //System.out.println(jsonObj.getLong("total_expense_rollup_ex"));
		//}
		
		
		
		
		for(int x = 1; x < 9; x++)
		{ 
			ShowText((driver.findElement(By.cssSelector(".tdb-pov__itemList>li:nth-of-type(" + x + ")>a")).getText())); // ---------- show before click()
			
			Thread.sleep(2000);
			
			driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(6)>g:nth-of-type(" + x + ")")).click(); // ------------- CLICK
			
			Thread.sleep(2000);
			
			ShowText(driver.findElement(By.cssSelector("#" + tempString + ">svg>g:nth-of-type(10)>text>tspan:nth-of-type(1)")).getText()); // ---------- SHOW AFTER
			
			driver.findElement(By.cssSelector(".breadcrumbs>span>a")).click(); // ------------- kill bread crumb
			
			Thread.sleep(2000);
			
		}
	}
}
