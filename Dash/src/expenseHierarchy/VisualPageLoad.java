package expenseHierarchy;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.crypto.ShortBufferException;
import javax.swing.JOptionPane;
import javax.tools.DocumentationTool.Location;

import org.bouncycastle.asn1.icao.CscaMasterList;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;

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
	
	// for Expense Trend title.
	public static String  expenseTrendingPartOne = "Expense Trend for ";
	
	public static void SelectAndWaitForPageLoad() throws Exception
	{
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
		
		List<WebElement> tabsList = driver.findElements(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(1)>div"));

		Assert.assertTrue(tabsList.size() == 3); // fox later
		
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
			
			//ShowText(tempString.replace("\n", "").replace("Total:", " ").replace("$", ""));
			//pw.write(tempString.replace("\n", "").replace("Total:", " ").replace("$", "") + "\n");
			
			if(localSelection.name() == hierarchyTileMapTabSelection.Total.name())
			{
				ShowText(tempString.replace("\n", "").replace("Total:", " ").replace("$", ""));
				pw.write(tempString.replace("\n", "").replace("Total:", " ").replace("$", "") + "\n");
			}
			else if(localSelection.name() == hierarchyTileMapTabSelection.Optimizable.name())
			{
				ShowText(tempString);
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

			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("__TANGOE__setShouldCaptureTestData(true)"); 
		
			
			JOptionPane.showMessageDialog(frame, "Select  100 and then tile map.");
			

			String fleetJsonData =  (String) js.executeScript("return __TANGOE__getCapturedTestDataAsJSON('hierarchy.PRIMARY.child')");
			ShowText(fleetJsonData);
			
			JOptionPane.showMessageDialog(frame, "Copy Json response ----------------------");
			
			
			ConsoleOutForFileDiffActualValues();
			
			
			JOptionPane.showMessageDialog(frame, "Copy list of data _______________________________");
			
			
			ShowText("Done");
		
		/*
		if (driver instanceof JavascriptExecutor) 
		{
			ShowText("in text");
			//((JavascriptExecutor) driver)
			//	.executeScript("alert('hello world');");
			
			// String myStr = (String)((JavascriptExecutor) driver).executeScript("prompt('hello world');");
			// String myStr = (String)((JavascriptExecutor) driver).executeScript("prompt('hello world');");
			
			//JavascriptExecutor js = (JavascriptExecutor)driver;
			//String sText =  js.executeScript("return document.title;").toString(); 
			
			//JavascriptExecutor js = (JavascriptExecutor)driver;
			//String sText =  js.executeScript("prompt('Hello')").toString(); 
			
 

			//String sText =  js.executeScript("window.prompt('Hello')").toString(); // nope 

			//JavascriptExecutor js = (JavascriptExecutor)driver;
			//Object sText =  js.executeScript("window.prompt('Hello')");
			
			
			//if(sText != null)
			//{ShowText("good");}
			//else{ShowText("is null");}
				
			// ////////////////////
			// BELOW GOOD -- from Ed - uses window object.
			// ////////////////////
			JavascriptExecutor js = (JavascriptExecutor)driver;
			String sText =  js.executeScript("window.hello = function(){return 'Hello';}; return hello();").toString();
			
			ShowText(sText);
			
			if(sText != null)
			{
				ShowText("good");
			}
			else
			{
				ShowText("is null");
			}
		}

		// DebugTimeout(9999, "9999");
		
		//JavascriptExecutor js = (JavascriptExecutor)driver;
		//js.executeScript("alert('hello world');");
		
		// ((JavascriptExecutor) driver).executeScript("alert('hello world');");
	
		
		
		//js.executeScript(alert("HELLO"));
		//if (driver instanceof JavascriptExecutor) {
		//	((JavascriptExecutor) driver).executeScript("alert('hello world');");
		//}
	}
	*/
	}
}
