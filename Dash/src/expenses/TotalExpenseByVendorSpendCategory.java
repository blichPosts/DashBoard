package expenses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.internal.TestMethodWithDataProviderMethodWorker;

import Dash.BaseClass;
import helperObjects.ExpenseHelper;
import helperObjects.GeneralHelper;
import helperObjects.UsageHelper;
import helperObjects.ExpenseHelper.controlType;

public class TotalExpenseByVendorSpendCategory extends BaseClass 
{
	public static List<WebElement> eleList;   
	public static List<String> expectedSpendCategoryLegends = new ArrayList<String>();
	public static List<String> actualSpendCategoryLegends = new ArrayList<String>();
	public static List<String> vendorsList = new ArrayList<String>();
	public static List<String> vendorsListEexpected = new ArrayList<String>();
	public static List<String> vendorsListActual = new ArrayList<String>();
	public static List<WebElement>  legendsList;


	public static int numberOfLegendsInBarChart = 0;
	public static int numberOfLegendsInLegendList = 0;	
	public static String otherString = "Other";
	public static String chartId = "";
	public static String tempString = "";
	
	public static String titleExpense = "Total Expense by Vendor and Spend Category ($)";
	public static String titleCountry = "Total Expense by Country and Spend Category ($)";
	static String errMessage = "";
	
	public static ViewType vType;
	
	// variables for actions
	
	// this holds vendor legend names in expense spend category control (string).
	public static List<String> totalExpenseLegendsList = new ArrayList<String>(); 
	
	// this holds web elements containing the bar graph sections in the 'expense by vendor category' control.
	public static List<WebElement> expenseControlSlicesElemntsList; 
	
	// this is used  
	public static List<String> totalExpenseExpectedLegendsList = new ArrayList<String>(); 	
	
	//  
	public static List<WebElement> totalExpenseLegendsElementList; 	
	
	//
	public static List<String> expectedlList= new ArrayList<String>();
	public static List<String> actuallList= new ArrayList<String>();
	
	public static String tempCategory = "";
	public static String tempCost = "";
	
	
	// this sets the global chartId (global to this class).
	public static void SetChartId(int id)
	{
		chartId = UsageHelper.getChartId(id);
	}
	
	public static void InitializeTotalExpenseSpendCategoryTest() throws Exception 
	{
		if(totalExpenseExpectedLegendsList != null)
		{
			totalExpenseExpectedLegendsList.clear();
		}
		
		totalExpenseExpectedLegendsList = ExpenseHelper.GetTotalExpenseLegends(); // this is needed in 'VerifyToolTipInfo' method.
		
		Assert.assertTrue(totalExpenseExpectedLegendsList.size() != 0, "Error in seeting upt list in TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest");
		
	}
	
	// this verifies the spend category names in the expense vendor spend control reflect what control legends are selected.
	// this starts with all controls selected and then disables them one at a time, and verifies the hover results. 
	public static void VerifyRemovingCategories(ViewType viewType) throws Exception 
	{
		vType = viewType; // setup the view type for use in this 'TotalExpenseByVendorSpendCategory' class.
		
		List<WebElement> currentHoverList; // this holds current hover info.

		BuildLegendsList(); // create a string list (expectedSpendCategoryLegends) of expected legend names in expense spend control.

		ShowText("Disabling Legends");
		
		// move to the spend category control.
		WebElement temp = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>div:nth-of-type(2)")); 
		new Actions(driver).moveToElement(temp).perform();
		// Thread.sleep(500);
		
		// these waits are for the vendor(s) in total expense spend control and total expense control.
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);

		// get the web elements for the legends so the legend text names can be clicked on.
		totalExpenseLegendsElementList = driver.findElements(By.cssSelector("#" + chartId + ExpenseHelper.partialCssForLegendsText)); 
		
		// Thread.sleep(500);
		
		for(int x = 0; x < ExpenseHelper.numOfLegendsInExpenseSpendCategory; x++)
		{
			PerformClick(expenseControlSlicesElemntsList , x); // get hover to show.
			
			// Thread.sleep(500);
			
			// store the info found in the hover.  
			currentHoverList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			VerifyToolTipInfoTwo(expectedSpendCategoryLegends, currentHoverList);
			
			currentHoverList.clear();
			expectedSpendCategoryLegends.remove(0); // this removes the first item from the list of expected legend names.
			
			// Thread.sleep(500);

			totalExpenseLegendsElementList.get(x).click(); // click legend x to disable it. 
		}

		ExpenseHelper.VerifyExpenseCategoryLegendsDisabled(); // verify the spend category control is empty.
	}
	
	// this verifies the spend category names in the expense vendor spend control reflect what control legends are selected.
	// this starts with all control legends disabled, enables them one at a time, and verifies the hover results.  
	public static void VerifyAddingCategories(ViewType viewType) throws Exception 
	{
		vType = viewType; // setup the view type for use in this 'TotalExpenseByVendorSpendCategory' class.
		
		BuildLegendsList(); // create a string list (expectedSpendCategoryLegends) of expected legend names in expense spend control.
		
		List<WebElement> currentHoverList; // this holds current hover info. 
		List<String> currentExpectedList = new ArrayList<String>(); // this holds expected legend names. items are added as the legends are selected.

		ShowText("Enabling Legends");
		
		// move to the spend category control.
		WebElement temp = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>div:nth-of-type(2)")); 
		new Actions(driver).moveToElement(temp).perform();
		Thread.sleep(500);
		
		// get the web elements for the legends so the legend text names can be clicked on.
		totalExpenseLegendsElementList = driver.findElements(By.cssSelector("#" + chartId + ExpenseHelper.partialCssForLegendsText)); 
		
		Thread.sleep(500);
		
		for(int x = 0; x < ExpenseHelper.numOfLegendsInExpenseSpendCategory; x++)
		{
			totalExpenseLegendsElementList.get(x).click(); // click legend x to enable it.
			currentExpectedList.add(expectedSpendCategoryLegends.get(x)); // add the name pf the enabled legend.
			
			Thread.sleep(500);
			
			PerformClick(expenseControlSlicesElemntsList , x); // get hover to show.
			
			Thread.sleep(500);
			
			// store the info found in the hover.  
			currentHoverList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			//ShowText("--------------------");
			//ShowWebElementListText(currentHoverList);
			//ShowText("--------------------");
			//ShowListOfStrings(currentExpectedList);
			//ShowText("DONE");
			
			VerifyToolTipInfoTwo(currentExpectedList, currentHoverList);
			
			currentHoverList.clear();
			//expectedSpendCategoryLegends.remove(0); // this removes the first item from the list of expected legend names.
			
			Thread.sleep(500);
		}
	}
	
	// this verifies the spend category names in the expense spend control reflect what control legends are selected.
	// it does this with all vendors/countries selected. this starts with all trend legends selected and then disables them 
	// one at a time, and verifies the hover results for all graphs each time a legend is removed.  
	public static void VerifyRemovingCategoriesMultipleVendors(ViewType viewType) throws Exception 
	{
		vType = viewType; // setup the view type for use in this 'TotalExpenseByVendorSpendCategory' class.
		
		List<WebElement> currentHoverList; // this holds current hover info.

		// create a string list (expectedSpendCategoryLegends) of expected legend names in expense spend control.
		BuildLegendsList(); 

		// store vendor names currently shown in expense control. these will be the same vendor names shown in the spend control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest();
		
		ShowText("Disabling Legends Multiple Vendors");
		
		// move to the spend category control.
		WebElement temp = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>div:nth-of-type(2)")); 
		new Actions(driver).moveToElement(temp).perform();
		// Thread.sleep(500);
		
		// these waits are for the vendor(s) in total expense spend control and total expense control.
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);

		// ////////////////////////////////////////////////////////////////////////////////////////////
		// first go through and verify vendor/country names in tool tip for each vendor/country. 
		// ////////////////////////////////////////////////////////////////////////////////////////////
		for(int x = 1; x <= ExpenseHelper.maxNumberOfLegends + 1; x++)
		{
			TotalExpenseByVendorSpendCategory.MoveMouseToSpendCategoryMultipleVendors(x); // select vendor/country x

			currentHoverList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo)); // get toll tip info.
			
			Assert.assertEquals(currentHoverList.get(0).getText(),totalExpenseExpectedLegendsList.get(x - 1), 
					            "Verify of vendor/country name in 'TotalExpenseByVendorSpendCategory.VerifyRemovingCategoriesMultipleVendors' failed."); // verify vendor/country name.
			currentHoverList.clear();
		}

		// ////////////////////////////////////////////////////////////////////////////////////////////
		// now do the legend removal testing. 
		// ////////////////////////////////////////////////////////////////////////////////////////////
		
		// get the web elements for the legends so the legend text names can be clicked on. 
		totalExpenseLegendsElementList = driver.findElements(By.cssSelector("#" + chartId + ExpenseHelper.partialCssForLegendsText)); 
		
		// create at string list that can be used for tracking expense trend legends that should be showing in the hovers.
		List<String> expectedLegends = new ArrayList<String>();

		// put the text name of the legends that will be shown in each hover into a text list.
		expectedLegends = GetListOfTextValuesFromWebElementList(totalExpenseLegendsElementList);
		
		// loop through the legends clicking them one at a time and verify hovers for all vendors.
		for(int x = 0; x < totalExpenseLegendsElementList.size(); x++)
		{
			// this clicks through the hover list for each vendor/country.
			for(int y = 1; y <= ExpenseHelper.maxNumberOfLegends + 1; y++)
			{
				TotalExpenseByVendorSpendCategory.MoveMouseToSpendCategoryMultipleVendors(y); // select vendor/country y

				currentHoverList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo)); // get tool tip info.
				
				VerifyToolTipInfoTwo(expectedLegends, currentHoverList); // verify tool tip info to expected.
				
				currentHoverList.clear(); // clear tool tip info.
			}

			totalExpenseLegendsElementList.get(x).click(); // select to delete a legend.
			expectedLegends.remove(0); // remove the same legend from expected legends list.
			
			Thread.sleep(1000);
		}
	}	
	
	// this verifies the spend category names in the expense spend control reflect what control legends are selected. // jnupp - edit this
	// it does this with all vendors/countries selected. this starts with all trend legends selected and then disables them 
	// one at a time, and verifies the hover results for all graphs each time a legend is removed.  
	public static void VerifyAddingCategoriesMultipleVendors(ViewType viewType) throws Exception 
	{
		vType = viewType; // setup the view type for use in this 'TotalExpenseByVendorSpendCategory' class.
		
		List<WebElement> currentHoverList; // this holds current hover info.

		// create a string list (expectedSpendCategoryLegends) of expected legend names in expense spend control.
		BuildLegendsList(); 

		// store vendor names currently shown in expense control. these will be the same vendor names shown in the spend control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest();
		
		ShowText("Disabling Legends Multiple Vendors");
		
		// move to the spend category control.
		WebElement temp = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>div:nth-of-type(2)")); 
		new Actions(driver).moveToElement(temp).perform();
		// Thread.sleep(500);
		
		// ////////////////////////////////////////////////////////////////////////////////////////////
		//  
		// ////////////////////////////////////////////////////////////////////////////////////////////

		// get the web elements for the legends so the legend text names can be clicked on and the names of the legends can be stored.
		totalExpenseLegendsElementList = driver.findElements(By.cssSelector("#" + chartId + ExpenseHelper.partialCssForLegendsText)); 
		
		// create at string list that can be used for tracking expense trend legends that should be showing in the hovers.
		List<String> expectedLegends = new ArrayList<String>();

		// put the text name of the legends into a string list. these that will be shown in each hover.
		expectedLegends = GetListOfTextValuesFromWebElementList(totalExpenseLegendsElementList);
		
		List<String> currentExpectedLegendsList = new ArrayList<String>();
		
		
		// loop through the legends clicking them one at a time and verify hovers for all vendors.
		for(int x = 0; x < totalExpenseLegendsElementList.size(); x++)
		{
			currentExpectedLegendsList.add(expectedLegends.get(x));
			
			totalExpenseLegendsElementList.get(x).click(); // select to add a legend.
			
			// this clicks through the hover list for each vendor/country.
			for(int y = 1; y <= ExpenseHelper.maxNumberOfLegends + 1; y++)
			{
				TotalExpenseByVendorSpendCategory.MoveMouseToSpendCategoryMultipleVendors(y); // select vendor/country y

				currentHoverList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo)); // get tool tip info.
				
				VerifyToolTipInfoTwo(currentExpectedLegendsList, currentHoverList); // verify tool tip info to expected.

				Assert.assertEquals(currentHoverList.get(0).getText(),totalExpenseExpectedLegendsList.get(y - 1), 
			            "Verify of vendor/country name in 'TotalExpenseByVendorSpendCategory.VerifyAddingCategoriesMultipleVendors' failed."); // verify vendor/country name.
				
				currentHoverList.clear(); // clear tool tip info.
			}
			Thread.sleep(1000);
		}
	}	
	
	
	
	public static void Setupdata()  
	{
		chartId = UsageHelper.getChartId(1);
		
		// get legend names shown in UI.
		legendsList = driver.findElements(By.xpath("//div[@id='" + chartId + "']/*/*[@class='highcharts-legend']/*/*/*"));
		// for(WebElement ele : legendsList){System.out.println(ele.getText());} // DEBUG

		// this gets the number of items (legends) shown in the UI.  
		numberOfLegendsInBarChart = legendsList.size();

		// build the list of expected spend category legends. these are the legends that are expected to be present. 
		expectedSpendCategoryLegends.add("Voice");
		expectedSpendCategoryLegends.add("Data");
		expectedSpendCategoryLegends.add("Messages");
		expectedSpendCategoryLegends.add("Roaming");
		expectedSpendCategoryLegends.add("Equipment");
		expectedSpendCategoryLegends.add("Taxes");
		expectedSpendCategoryLegends.add("Other");
		expectedSpendCategoryLegends.add("Account");
		
		// this gets the expected number of legends.
		numberOfLegendsInLegendList = expectedSpendCategoryLegends.size();
		
		// this gets the number of available bar sections.    
		numberOfLegendsInBarChart = driver.findElements(By.xpath("//div[@id='" + chartId + "']" + ExpenseHelper.partialXpathForBarChartInTotalSpendCategoryCategories)).size()/2;		

		// verify there are no unselected elements in the bar charts.
		if(eleList != null)
		{
			eleList.clear();
		}
		
		eleList = driver.findElements(By.xpath("//div[@id='" + chartId + "']" + ExpenseHelper.partialXpathForBarChartInTotalSpendCategoryCategories));
		
		for(WebElement ele : eleList)
		{
			if(ele.getAttribute("visibility") != null)
			{
				Assert.fail(errMessage);
			}
		}
	}
	
	public static void VerifyLegendsTitleAndbarGraphCount(ViewType vType) 
	{
		errMessage = "Failed checks in TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndPieCount.";
		
		ClearAllContainers();
		
		Setupdata();
		
		// verify title
		if(vType == ViewType.country)
		{
			Assert.assertEquals(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]")).getText(), titleCountry, errMessage);			
		}
		else
		{
			Assert.assertEquals(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]")).getText(), titleExpense, errMessage);		
		}

		
		// verify correct names for legends.
		// used array for actual, had problems with web element list. 
		for(int x = 0; x < expectedSpendCategoryLegends.size(); x++)
		{
			Assert.assertEquals(legendsList.get(x).getText(), expectedSpendCategoryLegends.get(x));
		}
		
		// verify number of sections in the bar graphs equals number of legends. 
		Assert.assertTrue(numberOfLegendsInBarChart == numberOfLegendsInLegendList, errMessage);
		
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// this gets the the sections in the horizontal bar graph. 
		// List<WebElement> eleList =  driver.findElements(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[5]/*/*/.."));
		
		// UN-SELECTING LEGENDS AND VERIFYING THEY ARE SELECTED.
		// In web element loop below,the attribute "visibility" will be null if the legend is selected.
		// It will be equal to "hidden" if the legend is not selected it is not selected. 

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// loop through the sections. 
		/*
		for(WebElement ele : eleList)
		{
			//System.out.println(ele.getAttribute("width"));
			//System.out.println(ele.getAttribute("height"));
			System.out.println(ele.getAttribute("visibility"));			
		}
		*/
	}	
	
	// verify vendors listed match the vendors listed in the total expense control.
	public static void VerifyVendors() throws Exception
	{

		// bob had to add  - 5/23/17
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		ExpenseHelper.WaitForControlLegend(controlType.countOfServiceNumbers);		
		
		// get vendors in total expense control.
		vendorsListEexpected = ExpenseHelper.GetTotalExpenseLegends();
		
		// get the actual vendor list in the control.
		List<WebElement> tempList = driver.findElements(By.xpath("//div[@id='" + chartId + "']" + ExpenseHelper.partialXpathForLegendsInTotalSpendCategory));
		
		// add actual vendor list to list of strings. 
		for(WebElement ele : tempList)
		{
			vendorsListActual.add(ele.getText());
		}
		
		Assert.assertEquals(vendorsListActual, vendorsListEexpected);
	}
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(1);
	}
	
	public static void VerifyVendorsCountries() throws Exception
	{
		// clear containers if needed.
		ClearAllContainers();
		
		Setupdata();

		// get the list vendors or countries listed by the bar graph.
		eleList = driver.findElements(By.xpath("(.//*[@id='highcharts-2']/*/*)[11]/*/*"));
		
		for(WebElement ele : eleList) // put vendor names into a list so can use 'contains'. 
		{
			vendorsList.add(ele.getText());
		}
		
		// verify 'other' selection present or not.
		if(eleList.size() < 5)
		{
			Assert.assertFalse(vendorsList.contains(otherString));
		}
		else
		{
			Assert.assertTrue(vendorsList.contains(otherString));
		}
	}
	
	public static void MoveMouseToSpendCategory(/*String chartId,*/ /*int indexHighchart*/) throws Exception
	{
		Robot robot = new Robot();
		
		// String referencePoint = "#" + chartId +  ">svg>g:nth-of-type(3)>text"; 
		// String referencePoint = "#" + chartId +  ">svg>.highcharts-series-group>g:nth-of-type(7)"; // orig
		String referencePoint = "#" + chartId +  ">svg>g.highcharts-legend>g>g>g:nth-of-type(4)"; 
		// >svg>g.highcharts-legend>g>g>g:nth-of-type(4) 
		
		
		// 'bar' WebElement will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(referencePoint));
				
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x = barCoordinates.getX();
		int y = GeneralHelper.getYCoordinate(chartId);
		
		int y_offset = (int) GeneralHelper.getScrollPosition();
		y += y_offset + 100; //  
		
		// below was -250, changed to -50   6/9/17  bob version 1.2.16
		robot.mouseMove(x - 50, y); // bob added  these lines after. 
		Thread.sleep(500);
		
		robot.mouseMove(x, y);
		// System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		try 
		{
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
		} 
		catch (Exception e) 
		{
			System.out.println("Tooltip NOT present in DOM.");
			e.printStackTrace();
		}
	}

	// the yClickPoint Css selector is what decides the y position by using an index.  
	public static void MoveMouseToSpendCategoryMultipleVendors(int index) throws Exception 
	{
		Robot robot = new Robot();
		
		String yClickPoint = "";
		yClickPoint = "#" + chartId +  ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + index  + ")";  

		// web element for Y  
		WebElement Y = driver.findElement(By.cssSelector(yClickPoint));

		Point elementLoc = Y.getLocation();
		
		int xLoc = elementLoc.getX();
		int yLoc = elementLoc.getY();

		if(index  == 1) // first time called, need a mouse move.
		{
			robot.mouseMove(xLoc + 175, yLoc + 198);				
		}
		
		Thread.sleep(500);

		robot.mouseMove(xLoc + 275, yLoc + 198);
		
		// Thread.sleep(500);
		
		try 
		{
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
		} 
		catch (Exception e) 
		{
			System.out.println("Tooltip NOT present in DOM.");
			e.printStackTrace();
		}
	}
	
	
	// matrix
	// NOTE - this currently selects just the only graph showing. This will change in future - 6/8/17 
	public static void ClickOneSpendCategoryMatrixPortal(String chartId) throws Exception
	{
		// NOTE: css bar and css line use the same css selector.
		
		// X - one of the month indicators.
		String cssBar = "#" + chartId + ">svg>g.highcharts-axis-labels>text:nth-of-type(7)";	
		
		// Y - one of the month indicators. 
		String cssLine = "#" + chartId + ">svg>g.highcharts-axis-labels>text:nth-of-type(7)";

		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = bar.getLocation();
		Point lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		
		int x = barCoordinates.getX();
		int y = lineCoordinates.getY() + 50;

		// doing this because 
		robot.mouseMove(x -30, y);
		Thread.sleep(500);

		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		// DebugTimeout(9999, "9999");
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}	
	
	// this is used for expense trend control tests.
	public static void BuildLegendsList()
	{
		expectedSpendCategoryLegends.clear();
		
		// build the list of expected spend category legends. these are the legends that are expected to be present. 
		expectedSpendCategoryLegends.add("Voice");
		expectedSpendCategoryLegends.add("Data");
		expectedSpendCategoryLegends.add("Messages");
		expectedSpendCategoryLegends.add("Roaming");
		expectedSpendCategoryLegends.add("Equipment");
		expectedSpendCategoryLegends.add("Taxes");
		expectedSpendCategoryLegends.add("Other");
		expectedSpendCategoryLegends.add("Account");
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static void ClearVerifyToolTipInfoLists()
	{
		if(actuallList != null)
		{
			actuallList.clear();
		}

		if(expectedlList != null)
		{
			expectedlList.clear();
		}
	}
	
	public static void ClearAllContainers()
	{
		if(eleList != null)
		{
			eleList.clear();
		}
		
		if(expectedSpendCategoryLegends  != null)
		{
			expectedSpendCategoryLegends.clear();			
		}
		
		if(actualSpendCategoryLegends  != null)
		{
			actualSpendCategoryLegends.clear();			
		}
		
		if(vendorsList  != null)
		{
			vendorsList.clear();			
		}
		
		legendsList = null;
	}

	// the first time this is called an expected list is created that has all of the rows in the hover.
	// the expected list is used to test the following calls to this method. the following calls 
	// will have less rows. each call with less rows will use a part of the expected list for verification.  
	public static void VerifyToolTipInfo(List<WebElement> list, int eventNumber) throws Exception 
	{

		int x = 0;
		boolean foundFirstItem = false;
		boolean foundSecondItem = false;
		
		// when running country test and then vendor test together, clearing arrays fixed failure in vendor test.
		ClearVerifyToolTipInfoLists();
		
		if(eventNumber == 0) // build expected list.
		{
			BuildListOfAllSpendCatergoryHoverItems(list);
		}
		else
		{
			for(WebElement ele : list)
			{
				if(x == 0) // this should always be the vendor name that was selected previously in the point of view. 
				{
					if(vType == ViewType.vendor) // if in vendor mode, the vendor that was selected in the POV should be what is shown at the top of the pop-up.  
					{
						tempString = TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0); // temporary string holds the expected  
					}
					else // if in country mode, the vendor that was selected in the POV should NOT be what is shown at the top of the pop-up. The country that the has the selected vendor in it is shown. 
					{
						tempString = ExpenseHelper.GetCountryForVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
					}

					Assert.assertEquals(ele.getText(), tempString, 
							            "Vendor name appears wrong in TotalExpenseByVendorSpendCategoryVisual.ToolTipInfo.");
					x++;
				}
				if(ele.getText().contains(":"))
				{
					tempCategory = ele.getText();
					foundFirstItem = true;
				}
				
				if(ele.getText().contains("$"))
				{
					tempCost = ele.getText();
					foundSecondItem = true;
				}
				
				if(foundFirstItem && foundSecondItem)
				{
					foundFirstItem = false;
					foundSecondItem = false;
					actuallList.add(tempCategory + tempCost);
				}
			}
			
			ShowText("Actual List");
			
			
			for(int y = 0, z = eventNumber; z < expectedlList.size(); y++, z++)
			{
				ShowText("Show " + actuallList.get(y)); // DEBUG
				ShowText("Show " + expectedlList.get(z)); // DEBUG
				Assert.assertEquals(actuallList.get(y), expectedlList.get(z));	
			}

			actuallList.clear();
		}
	}

	// * get a list of expected legend names (legends that are enabled) and a list of items in the tool tip.
	// * store the legend names into a list and store the legend names found in the tool tip list into a list
	// * make sure both list match.
	public static void VerifyToolTipInfoTwo(List<String> expectedLegendNames, List<WebElement> actualToolTipInfoList) throws Exception 
	{
		List<String> localListActual = new ArrayList<String>();
		
		// store legend names from tool tip info.
		for(WebElement ele : actualToolTipInfoList)
		{
			if(ele.getText().split(":").length == 3)
			{
				localListActual.add(ele.getText().split(":")[1].trim());
			}
		}
		
		Assert.assertEquals(localListActual, expectedLegendNames, "Legend names in the tooltip are not the same as the actual legend names.");
	}

	// this will do click on expense trend control
	public static void PerformClick(List<WebElement> expenseControlSlicesElemntsList, int x) throws Exception
	{
		if(loginType.equals(loginType.ReferenceApp))
		{
			// these two clicks make the hover visible.
			expenseControlSlicesElemntsList.get(x).click();
			expenseControlSlicesElemntsList.get(x).click();
		}
		else if (loginType.equals(LoginType.MatrixPortal))
		{
			Thread.sleep(1000); // need this in matrix.
			ClickOneSpendCategoryMatrixPortal(chartId);
		}
		else
		{
			Thread.sleep(500);
			MoveMouseToSpendCategory(); 
		}
	}
	
	public static void BuildListOfAllSpendCatergoryHoverItems(List<WebElement> list) throws Exception
	{
		int x = 0;
		boolean foundFirstItem = false;
		boolean foundSecondItem = false;

		for(WebElement ele : list)
		{
			if(x == 0) 
			{
				if(vType == ViewType.vendor) // if in vendor mode, the vendor that was selected in the POV should be what is shown at the top of the pop-up.  
				{
					tempString = TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0); // temporary string holds the expected  
				}
				else // if in country mode, the vendor that was selected in the POV should NOT be what is shown at the top of the pop-up. The country that the has the selected vendor in it is shown. 
				{
					tempString = ExpenseHelper.GetCountryForVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
				}

				Assert.assertEquals(ele.getText(), tempString, 
						            "Vendor name appears wrong in TotalExpenseByVendorSpendCategoryVisual.ToolTipInfo.");
				x++;
			}

			if(ele.getText().contains(":"))
			{
				tempCategory = ele.getText();
				foundFirstItem = true;
			}
			
			if(ele.getText().contains("$"))
			{
				tempCost = ele.getText();
				foundSecondItem = true;
			}
			
			if(foundFirstItem && foundSecondItem)
			{
				foundFirstItem = false;
				foundSecondItem = false;
				expectedlList.add(tempCategory + tempCost);
			}
		}
	}

	// get text values for list of web elements into text list and return.
	public static List<String> GetListOfTextValuesFromWebElementList(List<WebElement> eleList)
	{
		List<String> retList = new ArrayList<String>();
		
		for(WebElement ele : eleList)
		{
			retList.add(ele.getText());
		}
		
		return retList;
	}
	
	
	
}



