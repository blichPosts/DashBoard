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

public class ExpenseValuesHelper extends BaseClass 
{
	public static String filePath = "C:\\LichPublic\\_NewDash\\"; // this is where the input files for each vendor are stored.
	
	// 
	public static List<String> rowList;
	
	public static int numOfColumns = 0;
	public static int fieldWidth =  30;
	public static int rowsOfValues= 0;
	public static int testRow =  0;

	public static List<String> titlesList;
	
	public static List<List<String>> listOfRows = new ArrayList<List<String>>();
	
	public static String newMonth = "";
	public static String errMessage = "";
	
	public static double actual = 0;
	public static double expected = 0;
	
	// mouse clicks.
	public static String cssBar = "";
	public static String cssLine = "";
	public static Point barCoordinates;
	public static Point lineCoordinates;
	
	// 
	public static String chartId = "";
	
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
	
	// this selects the month being tested. the month being tested is the integer passed in.
	public static void SelectMonth(int month) throws Exception
	{
		// this gets an integer pair for month/year from the expected data 'invoice_month'.
		String [] tempArray = GetMonthYear(ExpenseValuesHelper.listOfRows.get(month).get(ExpenseValuesHelper.titlesList.indexOf("invoice_month")));

		// this uses the integer pair from above to selects the month.  
		CommonTestStepActions.selectMonthYearPulldown(CommonTestStepActions.convertMonthNumberToName(tempArray[0], tempArray[1]));
		
		newMonth = CommonTestStepActions.convertMonthNumberToName(tempArray[0], tempArray[1]);
		WaitForElementVisible(By.xpath("(//h2[@class='tdb-h2'])[1][text()='" + newMonth + "']"), MediumTimeout);
		
		// this tells the expected data, listOfRows container, what row will have the expected values.  
		testRow = month;
	}
	
	public static void SetupChartIdForExpense()
	{
		chartId = UsageHelper.getChartId(0);
	}
	
	public static void SetupChartIdForExpenseSpendCategory()
	{
		chartId = UsageHelper.getChartId(1);
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
        
        bufferedReader.close();
        fileReader.close();
	}

	public static void VerifyExpenseControl()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(3)>text")); // get hover list.
		// for(WebElement ele : eleList){ShowText(ele.getText());}  // DEBUG

		// get actual and expected.
		actual = GetDoubleValueActual(eleList.get(0).getText()); // from UI.
		expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("total_charge_ex"))); // from expected container. 
		
		Assert.assertEquals(actual, expected, "Failed value check in ExpenseValuesHelper.VerifyExpenseControl");
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
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////// Helpers ///////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("voice_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 1: // data
				{
					actual = GetDoubleValueActual(items.get(1));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("data_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 2: // message
				{
					actual = GetDoubleValueActual(items.get(2));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("messaging_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 3: // roaming
				{
					actual = GetDoubleValueActual(items.get(3));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("roaming_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 4: //equipment
				{
					actual = GetDoubleValueActual(items.get(4));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("equipment_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 5: // taxes
				{
					actual = GetDoubleValueActual(items.get(5));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("tax_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;
				}
				case 6: // other
				{
					actual = GetDoubleValueActual(items.get(6));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("other_charges_ex"))); // from expected container.
					Assert.assertEquals(actual, expected, errMessage);
					break;

				}
				case 7: // account
				{
					actual = GetDoubleValueActual(items.get(7));
					expected = GetDoubleValueForValueExpected(listOfRows.get(testRow).get(titlesList.indexOf("total_account_level_charges_e"))); // from expected container.
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
	
	// this takes info from a hover and returns the value in it as a double.
	public static double GetDoubleValueActual(String strVal)
	{
		strVal = strVal.split("\\$")[1].replace(" ","");
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
