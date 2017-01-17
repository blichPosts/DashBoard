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
import org.w3c.dom.Element;

import Dash.BaseClass;

public class ExpenseValuesHelper extends BaseClass 
{
	public static String filePath = "C:\\LichPublic\\_NewDash\\"; // this is where the input files for each vendor are stored.
	
	// 
	public static List<String> rowList;
	public static int numOfColumns = 0;
	public static int fieldWidth =  30;
	public static List<String> titlesList;
	public static List<List<String>> listOfRows = new ArrayList<List<String>>();
	public static int rowsOfValues= 0;
	
	
	// mouse clicks.
	public static String cssBar = "";
	public static String cssLine = "";
	public static Point barCoordinates;
	public static Point lineCoordinates;
	
	// 
	public static String chartId = "";
	
	// this vendor has been selected and the 'listOfRows' has been created for the vendor.
	public static void VerifyOneVendor() throws Exception
	{
		
		// get the 'expense' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(1)"));
		new Actions(driver).moveToElement(expenseTrending).perform();

		ClickExpenseControl();
		
		Thread.sleep(1000);
		VerifyExpenseControl();
		
		DebugTimeout(9999, "9999");
		
	}
	
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(0);
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

	// ///////////////////////////////////////////// Helpers ///////////////////////////////////////////////////////////////////////////////
	
	public static void ClickExpenseControl() throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(2)>rect";
		chartId = UsageHelper.getChartId(1);
		cssLine = "#" + chartId + ">svg>g:nth-of-type(8)";
		SetupChartId();
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		//int x = barCoordinates.getX() + 30; 
		//int y = lineCoordinates.getY() + 250;
		
		// 1/11/16 - moves cursor arrow up.
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 200;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public static void VerifyExpenseControl()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g:nth-of-type(3)>text"));
		for(WebElement ele : eleList)
		{
			ShowText(ele.getText());
		}
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
