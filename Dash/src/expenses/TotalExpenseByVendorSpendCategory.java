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
	
	public static String titleExpense = "Total Expense by Vendor and Spend Category";
	public static String titleCountry = "Total Expense by Country and Spend Category";
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

		
		//if(expectedSpendCategoryLegends != null)
		//{
		//	expectedSpendCategoryLegends.clear();
		//}

		
		// setup list of legends that are selected. legends are added in the order that they are selected.  
		//expectedSpendCategoryLegends.clear();
		
		//expectedSpendCategoryLegends.add("Voice");
		//expectedSpendCategoryLegends.add("Data");
		//expectedSpendCategoryLegends.add("Messages");
		//expectedSpendCategoryLegends.add("Roaming");
		//expectedSpendCategoryLegends.add("Equipment");
		//expectedSpendCategoryLegends.add("Taxes");
		//expectedSpendCategoryLegends.add("Other");
		//expectedSpendCategoryLegends.add("Account");	
	}
	
	
	// PROTOTYPE
	public static void VerifyRemovingCategories(ViewType viewType) throws Exception 
	{
		// DebugTimeout(1, "Start");
		Thread.sleep(1000);
		
		vType = viewType; // setup the view type for use in this 'TotalExpenseByVendorSpendCategory' class.
		
		List<WebElement> tempList; // this holds hover info.
 				
		// get web element  list of legends in expense spend category. this is for clicking legends. 
		totalExpenseLegendsElementList =  ExpenseHelper.GetTotalExpenseCatergoryLegends();  
		
		if(expenseControlSlicesElemntsList != null)
		{
			expenseControlSlicesElemntsList.clear();
		}
		
		// this waits for the vendor(s) in total expense spend control.
		ExpenseHelper.WaitForControlLegend(controlType.expenseTrending);
		
		Thread.sleep(500);
		
		// store the web elements for the slices into a list. 
		expenseControlSlicesElemntsList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForSliceSelections));  
		
		for(int x = 0; x < ExpenseHelper.numOfLegendsInExpenseSpendCategory; x++)
		{
			DebugTimeout(0, "starting click");
			Thread.sleep(2000);
			
			if(loginType.equals(loginType.ReferenceApp))
			{
				// these two clicks make the hover visible.
				expenseControlSlicesElemntsList.get(x).click();
				expenseControlSlicesElemntsList.get(x).click();
			}
			else
			{
				// move to the spend category control.
				WebElement expenseTrendingSection = driver.findElement(By.cssSelector(".tdb-currentCharts-EXPENSE>div:nth-of-type(2)"));
				new Actions(driver).moveToElement(expenseTrendingSection).perform();
				
				Thread.sleep(1000);
				
				MoveMouseToSpendCategory();
			}
			
			Thread.sleep(2000);

			// store the info found in the hover.  
			tempList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			VerifyToolTipInfo(tempList, x);
			
			// ShowInt(tempList.size()); // DEBUG
			
			Thread.sleep(500);

			//DebugTimeout(1, "Click legend");
			totalExpenseLegendsElementList.get(x).click(); // click legend.
		}
		
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.totalExpenseSpendCatergory); // verify there are no bar graphs in expense spend category. 
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
	public static void VerifyVendors()
	{
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
		
		// String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
		// String cssBar = "#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + indexHighchart + ")";
		
		String referencePoint = "#" + chartId +  ">svg>g:nth-of-type(3)>text"; 
		
		// 'bar' WebElement will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(referencePoint));
				
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x = barCoordinates.getX();
		int y = GeneralHelper.getYCoordinate(chartId);
		
		int y_offset = (int) GeneralHelper.getScrollPosition();
		y += y_offset + 60; 
		
		robot.mouseMove(x - 10, y); // bob addeed  these lines after. 
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
			
			for(int y = 0, z = eventNumber; z < expectedlList.size(); y++, z++)
			{

				ShowText(actuallList.get(y)); // DEBUG
				ShowText(expectedlList.get(z)); // DEBUG
				Assert.assertEquals(actuallList.get(y), expectedlList.get(z));	
			}

			actuallList.clear();
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

}



