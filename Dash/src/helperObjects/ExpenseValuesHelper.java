package helperObjects;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.CountOfServiceNumbersTrend;
import expenses.TotalExpensesTrend;
import testSuiteExpenseActions.TotalExpenseSingleVendor;

public class ExpenseValuesHelper extends BaseClass 
{
	public static String filePath = "C:\\LichPublic\\_NewDash\\"; // this is where the input files for each vendor are stored.
	
	// 
	public static List<String> rowList;
	
	public static int numOfColumns = 0;
	public static int fieldWidth =  30;
	public static int rowsOfValues= 0;
	public static int rowWithActualValues =  0;
	public static int rowsOfValuesOriginal = 0; // this is the rows of values when the vendor file is first read in
	public static int SpendCategoriesCounter = 0;	
	
	public static List<String> titlesList;
	public static String tempString = "";
	public static String tempStringTwo = "";
	public static List<List<String>> listOfRows = new ArrayList<List<String>>();
	public static List<String> dataList = new ArrayList<String>();
	public static List<WebElement>  webEleListBarGraphHoverValues;
	public static String newMonth = "";
	public static String errMessage = "";
	public static String errMessageBarCharts = "Fail value testing in ExpenseValuesHelper. ";
	public static String expenseTrendValue = "";
	public static String actualValueCostPerServiceNumber  = "";
	public static String countOfServiceNumbers  = "";
	
	public static double actual = 0;
	public static double expected = 0;

	public static boolean catergoryNameDone = false;
	public static boolean catergoryCostDone = false;
	
	// mouse clicks.
	public static String cssBar = "";
	public static String cssLine = "";
	public static Point barCoordinates;
	public static Point lineCoordinates;
	
	// 
	public static String chartId = "";
	
	// these are the trend selections above the 'Expense Trending' bar graph.
	public static enum SpendCategory
	{
		All,
		Voice,
		Data,
		Messages,
		Roaming,
		Equipment,
		Taxes,
		Other,
		Account
	}
	
	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendorTotalExpense() throws Exception
	{
		// get the 'expense' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();

		ClickExpenseControl();
		Thread.sleep(500);
		VerifyExpenseControl();
	}

	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendorTotalExpenseSpendCategory() throws Exception
	{
		// get the 'expense spend category' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();

		ClickExpenseSpendCategoryControl();
		Thread.sleep(500);
		VerifyExpenseSpendCategoryControl();
	}	
	
	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendorExpenseTrending() throws Exception
	{
		// get the 'expense trending' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();
		
		TotalExpensesTrend.SetupChartId(); // calling into TotalExpensesTrend for clicks. need to setup its chartId.
		
		// rowsOfValuesOriginal - this is the number of rows that have values in the input file.
		// rowWithActualValues - this can vary, depending on where the expected data is in relation to the invoice month.
		//                       this gets changed each time 'SelectMonthExpectedValueOnly(int)' gets called.   
		// this goes through each bar graph, selects it, gets the hover info and compares it with the expected.
		// it starts selecting the bar graph with the highest month value and continues down to the lowest month value. 
		for(int x = rowsOfValuesOriginal, y = 0; x > 0; x--, y++)
		{
			TotalExpensesTrend.clickBarIndex(x); // click bar graph.
			
			// get web list that holds the DOM section that holds the hover values just selected.
			webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			actual = GetDoubleValueActual(webEleListBarGraphHoverValues.get(3).getText()); // get the numeric value
			
			SelectMonthExpectedValueOnly(y); // this sets the 'rowWithActualValues' variable that used below in the call to get the expected data.
			
			// this limits how many loops are done when the data in the invoice month contains data from the previous month.
			if(rowWithActualValues > rowsOfValues)
			{
				break;
			}
			
			expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("total_charge_ex"))); // get expected from expected container.
			// ShowText(" actual: expected: -------"); System.out.println(actual);System.out.println(expected); // DEBUG
			
			Assert.assertEquals(actual, expected, errMessage + "VerifyOneVendorExpenseTrending");
			webEleListBarGraphHoverValues.clear();
		}
	}	
	
	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendorCostPerServiceNumber() throws Exception
	{
		double expenseValue = 0;
		double countOfServiceNumbersValue = 0;
		// double expected = 0;
		
		// get the 'cost per service number' control visible by moving to it. 
		WebElement costPerServiceNumber = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(2)"));
		new Actions(driver).moveToElement(costPerServiceNumber).perform();
		
		CostPerServiceNumberTrend.SetupChartId(); // calling into CostPerServiceNumberTrend for clicks. need to setup its chartId.
		
		Thread.sleep(250);
		
		// rowsOfValuesOriginal - this is the number of rows that have values in the input file.
		// rowWithActualValues - this can vary, depending on where the expected data is in relation to the invoice month.
		//                       this gets changed each time 'SelectMonthExpectedValueOnly(int)' gets called.   
		// this goes through each bar graph, selects it, gets the hover info and compares it with the expected.
		// it starts selecting the bar graph with the highest month value and continues down to the lowest month value. 
		for(int x = rowsOfValuesOriginal, y = 0; x > 0; x--, y++)
		{
			CostPerServiceNumberTrend.clickBarIndex(x); // click bar graph.
			//ShowInt(x);
			//DebugTimeout(9999, "9999");
			
			// get web list that holds the DOM section that holds the hover values just selected.
			webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			actual = GetDoubleValueActual(webEleListBarGraphHoverValues.get(3).getText()); // get the numeric value
			
			SelectMonthExpectedValueOnly(y); // this sets the 'rowWithActualValues' variable that used below in the call to get the expected data.
			
			// this limits how many loops are done when the data in the invoice month contains data from the previous month.
			if(rowWithActualValues > rowsOfValues)
			{
				break;
			}
			
			// for this control, the expected is 'expense trend' divided by 'count of service numbers'.
			expenseValue = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("total_charge_ex"))); // get expected from expected container.
			countOfServiceNumbersValue = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("no_of_lines"))); // get expected from expected container.
			expected = Math.round(expenseValue/countOfServiceNumbersValue);
			
			// ShowText(" actual: expected: -------"); System.out.println(actual);System.out.println(expected); // DEBUG
			Assert.assertEquals(actual, expected, errMessageBarCharts +  "VerifyOneVendorCostPerServiceNumber");
			
			webEleListBarGraphHoverValues.clear();
		}
	}	
	
	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendorCountOfServiceNumbers() throws Exception 
	{
		// get the 'expense trending' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(3)"));
		new Actions(driver).moveToElement(expenseTrending).perform();
		
		CountOfServiceNumbersTrend.SetupChartId(); // calling into CountOfServiceNumbersTrend for clicks. need to setup its chartId.
		
		// rowsOfValuesOriginal - this is the number of rows that have values in the input file.
		// rowWithActualValues - this can vary, depending on where the expected data is in relation to the invoice month.
		//                       this gets changed each time 'SelectMonthExpectedValueOnly(int)' gets called.   
		// this goes through each bar graph, selects it, gets the hover info and compares it with the expected.
		// it starts selecting the bar graph with the highest month value and continues down to the lowest month value. 
		for(int x = rowsOfValuesOriginal, y = 0; x > 0; x--, y++)
		{
			CountOfServiceNumbersTrend.clickBarIndex(x); // click bar graph.
			
			// get web list that holds the DOM section that holds the hover values just selected.
			webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
			
			actual = GetDoubleValueActualNoDollarSign(webEleListBarGraphHoverValues.get(3).getText()); // get the numeric value
			
			SelectMonthExpectedValueOnly(y); // this sets the 'rowWithActualValues' variable that used below in the call to get the expected data.
			
			// this limits how many loops are done when the data in the invoice month contains data from the previous month.
			if(rowWithActualValues > rowsOfValues)
			{
				break;
			}
			
			expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("no_of_lines"))); // get expected from expected container.
			
			// ShowText(" actual: expected: -------"); System.out.println(actual);System.out.println(expected); // DEBUG
			Assert.assertEquals(actual, expected, errMessageBarCharts +  "VerifyOneVendorCountOfServiceNumbers");
			
			webEleListBarGraphHoverValues.clear();
		}
	}		
	
	// select spend category button depending on enum sent in.
	public static void SelectSpendCategory(SpendCategory catergory)
	{
		switch(catergory)
		{
			case All:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(1)")).click();
				break;
			}
			case Voice:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(2)")).click();				
				break;
			}
			case Data:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(3)")).click();
				break;
			}
			case Messages:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(4)")).click();
				break;
			}
			case Roaming:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(5)")).click();
				break;
			}
			case Equipment:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(6)")).click();
				break;
			}
			case Taxes:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(7)")).click();
				break;
			}
			case Other:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(8)")).click();
				break;
			}
			case Account:
			{
				driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(9)")).click();
				break;
			}
		}
	}
	
	
	// this selects the month being tested. the month being tested is the integer passed in.it also sets up the 
	// row to use when getting test values. 
	public static void SelectMonth(int month) throws Exception
	{
		// this gets an integer pair for month/year from the expected data 'invoice_month'.
		String [] tempArray = GetMonthYear(ExpenseValuesHelper.listOfRows.get(month).get(ExpenseValuesHelper.titlesList.indexOf("invoice_month")));

		// ShowText(tempArray[0]);ShowText(tempArray[1]); // DEBUG		
		
		// this uses the integer pair from above to selects the month.  
		CommonTestStepActions.selectMonthYearPulldown(CommonTestStepActions.convertMonthNumberToName(tempArray[0], tempArray[1]));
		
		// this is wait for month selection to complete.
		newMonth = CommonTestStepActions.convertMonthNumberToName(tempArray[0], tempArray[1]);
		WaitForElementVisible(By.xpath("(//h2[@class='tdb-h2'])[1][text()='" + newMonth + "']"), MediumTimeout);
		
		// this tells the expected data, listOfRows container, what row will have the expected values.  
		rowWithActualValues = month;
		
		// SETUP OFFSET
		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// this tells the expected data, expected values container, what row will have the expected values.
		// this example is for the case where the invoice month row holds the expected values for the
		// previous month.
		// rowWithActualValues = month + 1;
		// ///////////////////////////////////////////////////////////////////////////////////////////////
	}
	
	// this selects the month being tested. the month being tested is the integer passed in.
	public static void SelectMonthExpectedValueOnly(int month) throws Exception
	{
		// this tells the expected data, listOfRows container, what row will have the expected values.  
		rowWithActualValues = month;
		
		// SETUP OFFSET
		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// this tells the expected data, expected values container, what row will have the expected values.
		// this example is for the case where the invoice month row holds the expected values for the
		// previous month.
		// rowWithActualValues = month + 1;
	}
	
	
	public static void SetupChartIdForExpense()
	{
		chartId = UsageHelper.getChartId(0);
	}
	
	public static void SetupChartIdForExpenseSpendCategory()
	{
		chartId = UsageHelper.getChartId(1);
	}
	
	public static void SetupChartIdForExpenseTrending()
	{
		chartId = UsageHelper.getChartId(2);
	}
	
	public static void SetupChartIdForCostPerServiceNumber()
	{
		chartId = UsageHelper.getChartId(3);
	}
	
	public static void SetupChartIdForCountOfServiceNumbers()
	{
		chartId = UsageHelper.getChartId(4);
	}
	
	public static void ReadFileAndBuildLists(String fileName) throws Exception
	{
		String [] strArray;
		boolean TitlesFound = false;

		fileName = filePath +  fileName;		
		
		// FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader(fileName);	
        
        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        // This will reference one line at a time
        String line = "";
        
        while((line = bufferedReader.readLine()) != null) 
        {
        	strArray = line.split(" ");
        	
        	// get column names and number of columns. 
        	if(strArray.length > 10 && !TitlesFound)
        	{
        		numOfColumns = line.length()/fieldWidth; // get number of columns with each column having a width of fieldWidth.

        		titlesList = GetListOfItemsInRow(line); // load all column titles into titles list and set boolean saying title has been found.
            	TitlesFound = true;
        	}
        	else
        	{
            	if(TitlesFound) // if title has been found, are now in row section that has values to be used for test.
            	{
            		rowList =  GetListOfItemsInRow(line); // get the information in current row read in into list
            		
            		listOfRows.add(rowList); // add the current row list onto list of rows.
            	}
        	}
        	rowsOfValues++; // keep track of how many rows were read in.
        }

        rowsOfValues -= 2; // the number of rows with actual test values is all the rows minus the first two rows in the file (vendor and column titles).  
        rowsOfValuesOriginal = rowsOfValues; // this is needed for the bar graph controls that have all months shown.
        
        bufferedReader.close();
        fileReader.close();
	}

	public static void VerifyExpenseControl()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(3)>text")); // get hover list. 

		// get actual and expected.
		actual = GetDoubleValueActual(eleList.get(0).getText()); // from UI.
		expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("total_charge_ex"))); // from expected container. 
		// ShowText("-----------------");System.out.println(actual);System.out.println(expected); // DEBUG
		Assert.assertEquals(actual, expected, errMessageBarCharts +   "ExpenseValuesHelper.VerifyExpenseControl");
	}
	
	public static void VerifyExpenseSpendCategoryControl()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(9)>text>tspan")); // get hover list.
		List<String> dataList = new ArrayList<String>();
				
		String tempCategory = "";
		String tempCost = "";
		boolean foundFirstItem = false;
		boolean foundSecondItem = false;
		
		for(WebElement ele : eleList)
		{
			// ShowText(ele.getText());
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
				dataList.add(tempCategory + tempCost);
			}
		}  
		VerifyExpenseSpendCategoryValues(dataList);
		
		// get actual and expected.
		//actual = GetDoubleValueForExpenseValueActual(eleList.get(0).getText()); // from UI.
		//expected = GetDoubleValueForExpenseValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("total_charge_ex"))); // from expected lists. 
		
		//Assert.assertEquals(actual, expected, "Failed value check in ExpenseValuesHelper.VerifyExpenseControl");
	}
	
	// this sets up the index that is used to keep track of what bar graph is being tested. the index is decremented by the method below
	public static void SetupSpendCategoriesCounter()
	{
		SpendCategoriesCounter = rowsOfValuesOriginal;
	}
	
	//
	// For one month:
	//
	// * for the current selected month, get hover the values from the spend category control (these values have already been verified) and use 
	//   these expected values to test the values in an expense trending bar graph. 
	//
	// * each trend selection (except 'all') is selected (one at a time) and the bar graph for the current selected month has it's hover 
	//   value verified by using the expected value found in the spend category control. 
	// 
	//  * when the hover value for a bar graph is verified, the bar graph next to it is selected first. this is done because the click 
	//    method being used has problems clicking the same bar graph multiple times.
	public static void VerifyExpenseTrendSpendCategoriesForSelectedMonth() throws Exception 
	{
		// set chart Id in this class to the spend category control because this class will accessing the spend category control.
		SetupChartIdForExpenseSpendCategory(); 

		// get the 'expense spend category' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();
		Thread.sleep(250);
		
		ClickExpenseSpendCategoryControl();
		Thread.sleep(250);
		
		// this stores spend category hover entries onto the 'dataList list' list. that has name and corresponding value for each category. 
		StoreSpendCatergoryNamesValuesForCurrentMonth();    
		
		// set chart Id in this class to the expense trending control because this class will accessing the expense trend control.
		SetupChartIdForExpenseTrending();  
		
		// get the 'expense trending' control clickable by moving to it. 
		expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();
		Thread.sleep(250);
		
		// go through spend categories and select each, except the 'all' selection, one at a time 
		for(SpendCategory spend : SpendCategory.values())
		{
			if(spend.name()!= "All")
			{
				ExpenseValuesHelper.SelectSpendCategory(spend); // select spend category.
				Thread.sleep(250);

				// the if statements below click the bar graph next to the bar graph being tested. the method description above explains why this is done.  
				if(SpendCategoriesCounter != rowsOfValuesOriginal) 
				{
					clickBarIndexExpenseTrend(SpendCategoriesCounter + 1);  
					Thread.sleep(250);
				}
				else
				{
					clickBarIndexExpenseTrend(SpendCategoriesCounter - 1);  
					Thread.sleep(250);
				}
				
				clickBarIndexExpenseTrend(SpendCategoriesCounter); // click the bar graph for the selected pulldown month.  
				Thread.sleep(250);
				
				// verify
				Assert.assertEquals(GetActualValue(), GetExpectedValue(spend), 
						            "Failed verify of values in ExpenseValueHelpser.VerifySpendCategoriesForSelectedMonth"); 
				Thread.sleep(250);
			}			
		}

		// FIGURE OUT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// this limits how many loops are done when the data in the invoice month contains data from the previous month.
		//if(rowWithActualValues > rowsOfValues)
		//{return;}
		
		dataList.clear(); // clear expected data container for the next call to this method.
		
		// set this index to the next month in the expense trending graph. this will be used in the next call to this method.
		SpendCategoriesCounter--;  
	}
	
	//
	// For one month: // DO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//
	// * for the current selected month, get hover the values from the spend category control (these values have already been verified) and use 
	//   these expected values to test the values in an expense trending bar graph. 
	//
	// * each trend selection (except 'all') is selected (one at a time) and the bar graph for the current selected month has it's hover 
	//   value verified by using the expected value found in the spend category control. 
	// 
	//  * when the hover value for a bar graph is verified, the bar graph next to it is selected first. this is done because the click 
	//    method being used has problems clicking the same bar graph multiple times.
	public static void VerifyCostPerServiceSpendCategoriesForSelectedMonth() throws Exception // bladdxx  
	{
		WebElement costPerServiceNumber;
		
		// get the 'cost per service number' control clickable by moving to it. 
		costPerServiceNumber = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(3)"));
		new Actions(driver).moveToElement(costPerServiceNumber).perform();
		Thread.sleep(250);
		
		TotalExpensesTrend.SetupChartId(); // need this class chartId setup to call its click its bar charts. 
		
		// go through spend categories and select each, except the 'all' selection, one at a time 
		for(SpendCategory spend : SpendCategory.values())
		{
			if(spend.name()!= "All")
			{
				ExpenseValuesHelper.SelectSpendCategory(spend); // select spend category.
				Thread.sleep(250);

				TotalExpensesTrend.clickBarIndex(SpendCategoriesCounter);
				Thread.sleep(250);
				
				SetupChartIdForExpenseTrending(); // 
				
				webEleListBarGraphHoverValues =  driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(9)>text>tspan"));
				expenseTrendValue = webEleListBarGraphHoverValues.get(3).getText();
		
				SetupChartIdForCostPerServiceNumber(); // 

				CostPerServiceNumberclickBarIndex(SpendCategoriesCounter);
				webEleListBarGraphHoverValues =  driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(9)>text>tspan"));
				actualValueCostPerServiceNumber = webEleListBarGraphHoverValues.get(3).getText();
				
				countOfServiceNumbers = listOfRows.get(rowWithActualValues).get(titlesList.indexOf("no_of_lines"));
				
				VerifyCostPerServiceNumberForSpendCategory(GetDoubleValueActual(expenseTrendValue), 
						                                   GetDoubleValueActualNoDollarSign(countOfServiceNumbers), 
						                                   GetDoubleValueActual(actualValueCostPerServiceNumber));
			}			
		}

		// FIGURE OUT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// this limits how many loops are done when the data in the invoice month contains data from the previous month.
		//if(rowWithActualValues > rowsOfValues)
		//{return;}
		
		dataList.clear(); // clear expected data container for the next call to this method.
		
		// set this index to the next month in the expense trending graph. this will be used in the next call to this method.
		SpendCategoriesCounter--;  
	}
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////// Helpers ///////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void VerifyCostPerServiceNumberForSpendCategory(double expenseTrendVal, double countOfServiceNumbers, double actualValueCostPerServiceNumber)
	{
		double expectedCostPerServiceNumber = Math.round(expenseTrendVal/countOfServiceNumbers);
		
		//System.out.println(expected);
		//System.out.println(actualValueCostPerServiceNumber);
		
		Assert.assertEquals(actualValueCostPerServiceNumber, expectedCostPerServiceNumber);

	}
	
	public static String GetActualValue()
	{
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		// ShowInt(webEleListBarGraphHoverValues.size()); // DEBUG.
		return webEleListBarGraphHoverValues.get(3).getText();
	}
	
	// this is done in this class instead of calling into the 'expense trend' class because when going through the trends 
	// the bar graphs are all over the place. this local copy of the  'expense trend' method can be tweaked.
	public static void clickBarIndexExpenseTrend(int barIndex) throws Exception
	{
		cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + barIndex + ")";
		// cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(2)"; // orig
		cssLine = "#" + chartId + ">svg>.highcharts-axis>text";		
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 200;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void CostPerServiceNumberclickBarIndex(int barIndex) throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(8)>text:nth-of-type(" + barIndex + ")";
		// cssLine = "#" + chartId + ">svg>g:nth-of-type(7)>text:nth-of-type(1)";
		cssLine = "#" + chartId + ">svg>g:nth-of-type(8)>text:nth-of-type(1)";
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		
		// original 
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 150;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void StoreSpendCatergoryNamesValuesForCurrentMonth()
	{
		tempString = "";
		tempStringTwo = "";

		catergoryNameDone = false;
		catergoryCostDone = false;
		
		if(webEleListBarGraphHoverValues != null)
		{
			webEleListBarGraphHoverValues.clear();
		}
		
		if(dataList != null)
		{
			dataList.clear();
		}		
		
		// get web list that has the DOM section that holds the hover values just selected by a click.
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		
		for(WebElement ele : webEleListBarGraphHoverValues)
		{
			if(ele.getText().contains(":")) // this is category name
			{
				tempString = ele.getText();
				catergoryNameDone = true;
			}

			if(ele.getText().contains("$")) // this is dollar value
			{
				tempStringTwo = ele.getText();
				catergoryCostDone = true;
			}
			
			if(catergoryNameDone && catergoryCostDone)
			{
				dataList.add(tempString + tempStringTwo);
				catergoryCostDone = false;
				catergoryNameDone = false;				
			}
		}

		// ShowListOfStrings(dataList); // DEBUG
		
		webEleListBarGraphHoverValues.clear();
	}
	
	public static String GetExpectedValue(SpendCategory spendCategory)
	{
		for(String str : dataList)
		{
			if(str.contains(spendCategory.name()))
			{
				// ShowText(str); // DEBUG
				return str.replace(":", "").replace(spendCategory.name(), "");
			}
		}
		
		Assert.fail("Error in ExpenseValueHelper.GetExpectedValue. Did not find an expected value.");
		return"";
	}
	
	
	public static String GetCostFromDataListForSpendCategory(SpendCategory spendCategory)
	{
		for(String str: dataList)
		{
			if(str.contains(spendCategory.name()))
			{
				return str;
			}
		}

		Assert.fail("Error in ExpenseValuesHelper.GetCostFromDataListForSpendCategory. A spend category was not found.");
		
		return "";

	}
	
	
	public static void StoreExpenseSpendCategoryValues()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(9)>text>tspan")); // get hover list.
		dataList = new ArrayList<String>();
				
		String tempCategory = "";
		String tempCost = "";
		boolean foundFirstItem = false;
		boolean foundSecondItem = false;
		
		for(WebElement ele : eleList)
		{
			// ShowText(ele.getText());
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
				dataList.add(tempCategory + tempCost);
			}
		}
	}
	
	// this gets a list of the items in the expense spend category hover and verifies the values.
	// it goes through the list of hover values and test each value depending on what spend category the value is.
	public static void VerifyExpenseSpendCategoryValues(List<String> items)
	{
		errMessage = "Fail in testing values for Total Expense Spend Categories in ExpenseValuesHelper.VerifyExpenseSpendCategoryValues.";
		for(int x = 0; x < items.size(); x++)
		{
			switch(x)
			{
				case 0: // voice
				{
					actual = GetDoubleValueActual(items.get(0));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("voice_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 1: // data
				{
					actual = GetDoubleValueActual(items.get(1));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("data_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 2: // message
				{
					actual = GetDoubleValueActual(items.get(2));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("messaging_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 3: // roaming
				{
					actual = GetDoubleValueActual(items.get(3));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("roaming_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 4: //equipment
				{
					actual = GetDoubleValueActual(items.get(4));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("equipment_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 5: // taxes
				{
					actual = GetDoubleValueActual(items.get(5));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("tax_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 6: // other
				{
					actual = GetDoubleValueActual(items.get(6));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("other_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 7: // account
				{
					actual = GetDoubleValueActual(items.get(7));
					expected = GetDoubleValueForValueExpected(listOfRows.get(rowWithActualValues).get(titlesList.indexOf("total_account_level_charges_e"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				default:
				{
					Assert.fail("Error in ExpenseValuesHelper.VerifyExpenseSpendCategoryValues. Incorrect case selection.");
				}
			}
		}
	}
	
	public static String[] GetMonthYear(String invoiceMonth)
	{
		return new String[]  {invoiceMonth.split("/")[0],invoiceMonth.split("/")[2]};
	}
	
	// this takes number info from a hover and returns the value in it as a double.
	public static double GetDoubleValueActual(String strVal)
	{
		strVal = strVal.split("\\$")[1].replace(" ","");
		return Double.parseDouble(strVal);
	}	
	
	// this takes number info from a hover and returns the value in it as a double.
	public static double GetDoubleValueActualNoDollarSign(String strVal)
	{
		strVal = strVal.replace(" ","");
		return Double.parseDouble(strVal);
	}	
	
	// this takes a string with expected value and returns the value as a double. 
	public static double GetDoubleValueForValueExpected(String strVal)
	{
		return Math.round(Double.parseDouble(strVal));
	}	
	
	
	public static void ClickExpenseControl() throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(2)>rect"; // vertical coordinate. 
		chartId = UsageHelper.getChartId(1); // change chartId to expense spend category for horizontal coordinate. 
		cssLine = "#" + chartId + ">svg>g:nth-of-type(8)"; // horizontal coordinate.
		SetupChartIdForExpense(); // set chart Id back.
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 200;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void ClickExpenseSpendCategoryControl() throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(7)>text:nth-of-type(4)"; // vertical coordinate. 
		cssLine = "#" + chartId + ">svg>g:nth-of-type(8)>text"; // horizontal coordinate.
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 200;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	// this takes a row and breaks it apart into sections of fieldWidth characters, trims each section, and adds each section to the list to be returned.
	public static List<String> GetListOfItemsInRow(String oneLine) throws Exception 
	{
		List<String> strList = new ArrayList<String>();

		for (int x = 0, y = 29, loopCntr = 0; loopCntr < numOfColumns; x += fieldWidth, y += fieldWidth, loopCntr++)
		{
			strList.add(oneLine.substring(x, y).trim());
		}

		return strList;
	}
	
	
	
}
