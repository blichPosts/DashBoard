package commandApi;

import Dash.BaseClass;
import helperObjects.GeneralHelper;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.javascript.host.html.RowContainer;

public class CommandHelper extends BaseClass 
{

	public static Writer EmployeeList;
	public static int offset = 0;
	
	
	
	// the first row in command is 3. each rwow after that is 5.7.9.13. etc.
	static int rowControl = 3;
	
	public static String userOneLastName = "Pace";
	public static String userOneFirstName = "AnaLaura";
	public static Writer OrderInfoOut; 
	public static String OrderInfoOutFile = "C:\\SoapUi_Projects\\_API_2017_SFE_15041\\Hacking_XX1\\SeleniumSingleOrderForReadyApi.txt";
	public static Writer OrderInfoOutList; 
	//public static String OrderInfoOutListFile = "C:\\SoapUi_Projects\\AutoTestSoapTestsPro\\SeleniumOrderListForReadyApi.txt";
	public static String OrderInfoOutListFile = "C:\\SoapUi_Projects\\_API_2017_SFE_15041\\Hacking_XX1\\SeleniumOrderListForReadyApi.txt";
	
	public static String EmployeeListOutFile = "C:\\SoapUi_Projects\\_API_2017_SFE_15041\\Hacking_XX1\\SeleniumEmployeeListForReadyApi.txt";

	// remove date from string that looks like this:
	// "Dudley Doright (Dec 09, 2015 11:59:48 AM CST -0600 UTC)"
	// This part is returned - "Dec 09, 2015 11:59:48 AM"
	public static String RetrieveTimeStamp(String strIn) throws InterruptedException
	{
		int intOne;
		String[] strOne;
		String strTwo = "";
		String strThree = "";
		strOne = strIn.split("\n");
		intOne = strOne[0].indexOf('(');
		strTwo = strOne[0].substring(intOne+1);
		strThree = strTwo.replace(")",""); 

		// format of strThree looks like this: Dec 09, 2015 11:59:48 AM CST -0600 UTC
		// now remove the CST part and trim leading/trailing spaces. 
		strOne = strThree.split("CST");
		strTwo = strOne[0];
		strThree = strTwo.trim();
		return strThree;
	}

	// this gets the three date/time entries from the order details. 
	public static List<String> GetOrderHistoryDates() throws Exception
	{
		List<String> listDates = new ArrayList<String>();		
		List<WebElement> eleList = new ArrayList<WebElement>();
		
		String dateTime = "";
		String tempHistoryStep = "";
		//String dateTimeParsed = "";		

		WaitForElementPresent(By.cssSelector(".box5Line-ew>div:nth-of-type(1)"), MediumTimeout);
		 		
		eleList =  driver.findElements(By.cssSelector(".box5Line-ew>div")); // see how many elements there are under '.box5Line-ew>div'.		
		
		// ShowInt(eleList.size());
		
		// store date/time onto list
		for(int x = eleList.size(); x > 0; x--)
		{
			dateTime = driver.findElement(By.cssSelector(".box5Line-ew>div:nth-of-type(" + x + ")")).getText();
			listDates.add(RetrieveTimeStamp(dateTime));
			ShowText(RetrieveTimeStamp(dateTime));
		}
		
		return listDates;
	}

	// this gets the three date/time entries from the order details. 
	public static List<String> GetOrderHistoryEvents() throws Exception
	{
		List<String> listEvents = new ArrayList<String>();		
		List<WebElement> eleList = new ArrayList<WebElement>();
		
		String tempHistoryStep = "";
		//String dateTimeParsed = "";		

		WaitForElementPresent(By.cssSelector(".box5Line-ew>div:nth-of-type(1)"), MediumTimeout);
		 		
		eleList =  driver.findElements(By.cssSelector(".box5Line-ew>div")); // see how many elements there are under '.box5Line-ew>div'.		
		
		int indexForOutputFile = 0;
		
		// store date/time onto list
		for(int x = eleList.size(); x > 0; x--)
		{
			tempHistoryStep = driver.findElement(By.cssSelector(".box5Line-ew>div:nth-of-type(" + x + ")>div:nth-of-type(1)")).getText();
			OrderInfoOut.write("OrderEvent_" + indexForOutputFile + ":" +  tempHistoryStep + "\n");
			indexForOutputFile++;
			if(indexForOutputFile == 3)
			{
				tempHistoryStep = driver.findElement(By.cssSelector(".box5Line-ew>div:nth-of-type(" + x + ")>div:nth-of-type(2)")).getText();
				OrderInfoOut.write("InitiatorNote:" +  tempHistoryStep.replace("Note: ","").trim() + "\n");
			}
		}
		
		return listEvents;
	}
	

	// this assumes oder  page is opened and a search has been done for a user's orders. // jnupp
	// this will find the first order and output some of its info.
	public static void GetAndOutputFirstOrderListed() throws Exception
	{
	    String[] tempArray;
	    String strTemp;
		List<String> listDates = new ArrayList<String>();	    

		// these labels get output to the output file to provide the identifier label that goes with each time/date that is output.
		String[] dateLabels = {"InitNote", "SubmitEvent", "FulfillEvent"};  
		
	    WaitForElementPresent(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(3)"), MediumTimeout);

		OrderInfoOut = new FileWriter(OrderInfoOutFile); // create file writer.
		OrderInfoOut.write("Order Details:\n"); // output header.

	    // get orderId/ExternalOrderId text and output.
	    //strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(3)>td:nth-of-type(5)")).getText();

		OrderInfoOut.write("ExternalOrderId:" + "N/A" + "\n"); // not found in G17.3

		// get orderDate/orderType text and output.
		strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(3)>td:nth-of-type(7)")).getText();
		tempArray = strTemp.split("\n");		
		OrderInfoOut.write("OrderDate:" + tempArray[0] + "\n");
		OrderInfoOut.write("OrderType:" + tempArray[1] + "\n");	    

		// //////////////////////////////////////////////////////////////////////////////	
		// select first order. this will take you to Order Details page.		
		// //////////////////////////////////////////////////////////////////////////////
		
		WaitForElementVisible(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(3)>td:nth-of-type(2)"), MainTimeout);
	    //DebugTimeout(0, "Select User Link in 'GetAndOutputFirstOrderListed'");
	    // // unknown error: Element is not clickable at point (132, 711). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
		DebugTimeout(2);
	    // WaitForElementNotClickable(By.id("PLEASE_WAIT_MODAL_WINDOW_FRAME"), MainTimeout); // this would always find the frame.
		
		driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + rowControl   +")>td:nth-of-type(2)")).click();		
	    DebugTimeout(0, "Select User Link in 'GetAndOutputFirstOrderListed' Done");
	    listDates = GetOrderHistoryDates();
	    
	    //DebugTimeout(0, Integer.valueOf(listDates.size()).toString());
	    for(int x = 0; x < listDates.size(); x++)
	    {
			OrderInfoOut.write("Date" + dateLabels[x]  + ":" + listDates.get(x) + "\n");	    	
	    }

	    // get order Id number
		strTemp = driver.findElement(By.xpath("//td[text()='Fulfillment Line Item']/following-sibling::td")).getText(); // this is found in order details	    
		OrderInfoOut.write("OrderId:" + strTemp + "\n");
		
		GetOrderHistoryEvents();
	    
	    // close the output file. 
		OrderInfoOut.close();
	}
	
	
	// go to list in employees page.
	public static void GoToEmployeesPageForEdit() throws Exception
	{
		// go to employees page.
	    WaitForElementClickable(By.id("menuMainOrganize"),MainTimeout,"");	
	    
	    Thread.sleep(1000);
	    
	    driver.findElement(By.id("menuMainOrganize")).click();
	    		
	    WaitForElementPresent(By.id("menuMainOrganize_Employees"),MainTimeout);	    
	    driver.findElement(By.id("menuMainOrganize_Employees")).click();

	    //WaitForElementPresent(By.xpath("//span[@class='banner_title']"), MainTimeout);	    
	    //WaitForElementPresent(By.xpath("//td[@id='logoBox']"), MainTimeout);
	    
	    // switch to the correct frame and wait for name text box.
	    DebugTimeout(0, "Ready for frame switch in employees page.");
	    driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
	    WaitForElementPresent(By.xpath("//input[@name='criteria.lastName']"), MainTimeout);
	    WaitForElementPresent(By.xpath("//a[@title='Add a New User']"), MainTimeout);	 
	    DebugTimeout(0, "frame switch done in employees page.");
	}	

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// * assuming employees page is open, get info for first ten user in user list.
	// * for each user, drill down into the user profile for shipping address info.
	// * add each user's info into a list of user objects.
	// * then output the the user list objects to a file. each user object's info is output into a 
	//   single line in the output file.
	// /////////////////////////////////////////////////////////////////////////////////////////////
	public static void LoadListOfEmployeeObjects() throws Exception
	{
		String tempString = "";
		EmployeeObject tempObject;

		// setup output file for writing out employee objects.
		EmployeeList = new FileWriter(EmployeeListOutFile); // create file writer.

		// wait for first column of users info.
		WaitForElementClickable(By.cssSelector(".tngoBox.gridArea:nth-of-type(2)>table:nth-of-type(2)>tbody>tr:nth-of-type(3)>td:nth-of-type(2)>a>b"), TenTimeout, "Failed Wait In 'LoadListOfEmployeeObjects()' method.");
		
		// http://programmers.stackexchange.com/questions/160763/multiple-same-object-instantiation
		List<EmployeeObject> listEmployeeObjects = new ArrayList<EmployeeObject>();
		
		String cssTopLocator = ".tngoBox.gridArea:nth-of-type(2)>table:nth-of-type(2)";
		
		int startRow = offset + 3;
		int stopRow = startRow + 10;
		
		for(int x = startRow; x < stopRow; x++) // go through some rows of employees.
		{
			tempObject = new EmployeeObject();
			
			tempObject.employeeLastFirstName = 	driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(2)>a>b")).getText();
			tempObject.companyEmployeeId = driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(2)>span")).getText();
			tempObject.employeeId = driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(2)>a")).getAttribute("href");
			
			tempObject.employeeDepartment = driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(4)")).getText(); // dept
			tempObject.employeeStatus = driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(6)")).getText().toUpperCase(); // status

			// go to user's profile
			driver.findElement(By.cssSelector(cssTopLocator + ">tbody>tr:nth-of-type(" + x + ")" + ">td:nth-of-type(2)>a>b")).click();

			UsersProfilePage.LoadEmployeeObjectAndReturn(tempObject); // looks like tempObject gets passed by reference.
			
			tempObject.OrganizeForFileOutput();

			listEmployeeObjects.add(tempObject);			
			
			// tempObject.Show();
		}

		EmployeeObject.OutputHeaderAndDate(EmployeeList);
		
		// go through list of object. output objects to console window and output objects to file. file output is in one-line format.
		for(EmployeeObject eObj: listEmployeeObjects)  
		{
			eObj.Show();
			eObj.OutputObject(EmployeeList);
		}		
		
		EmployeeList.close(); // close out file.		
		
		ShowText("OutputPassed");
	}

	// jnupp
	public static void GoToOrders() throws Exception
	{
		// get to the dash page
	
		WaitForElementClickable(By.cssSelector("#menuMainProcure"), MainTimeout, "Failed wait in GoToDashboard"); 
		
		Thread.sleep(2000); // bob added this. was getting click conflicts in getting dash in pull down. i have this fix this problem in other cases.  
		
		driver.findElement(By.cssSelector("#menuMainProcure")).click();

	    WaitForElementClickable(By.cssSelector("#menuMainProcure_Order_Status"), MainTimeout, "Failed wait in GoToDashboard");
		
		driver.findElement(By.cssSelector("#menuMainProcure_Order_Status")).click();
		
		// GeneralHelper.setUpiFrame();
		
		if(browserType.equals(BrowserType.FireFox))
		{
			Thread.sleep(2500);
		}
		
		SwitchToContentFrameAPI();
		
	}	

	
	// Switches to content frame // jnupp
	public static void SwitchToContentFrameAPI() throws InterruptedException {
		
		//if (loginType.equals(LoginType.CommandApi)) {
	    
	    	driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
		    
		    // this timeout is here because when at frame id "CONTENT" there is no DOM element to wait for.    
		    DebugTimeout(1, ""); 
		    
		    // no in API
		    // this will get to dashboard frame. At this point the dashboard test code will wait for the dash page to load. 
	    	// driver.switchTo().frame(driver.findElement(By.id("dashboard_iframe")));
	    	
	    //}
	    
		//if (loginType.equals(LoginType.MatrixPortal)) {
			
		//	driver.switchTo().frame(driver.findElement(By.id("iframe_MATRIX_ANALYTIC_DASHBOARDS")));
			
		//}
	    
	}
	
	
}
