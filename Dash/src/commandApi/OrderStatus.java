package commandApi;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

import Dash.BaseClass;

public class OrderStatus extends BaseClass
{

	// public static Writer SortedOrderList; 
	public static Writer OrderInfoOutList; 
	
	public static String OrderInfoOutListFile = "C:\\SoapUi_Projects\\_API_2017_SFE_15041\\Hacking_XX1\\SeleniumOrderListForReadyApi.txt";
	public static String OrderInfoOutListFileAllCommandInfo = "C:\\SoapUi_Projects\\_API_2017_SFE_15041\\Hacking_XX1\\SeleniumOrderListForReadyApiAllCommandInfo.txt";
	public static String SortedOrderListOutFile = "C:\\SoapUi_Projects\\AutoTestsSoapTestsUpgrade\\SeleniumSortedOrderListOut.txt";
	public static String PathToReadyApiTests = "C:\\SoapUi_Projects\\AutoTestsSoapTestsUpgrade\\";
	
	public static String userOneLastName = "Pace";
	public static String userOneFirstName = "AnaLaura";
	// public static String sortingEnumType = "";
	
	public static Writer TestFile; 
	public static String TestFileOut = PathToReadyApiTests +  "_DeleteMe.txt";   	
	
	public static List<OrderObject> orderObjList = new ArrayList<OrderObject>();
	
	
	/*
	public enum TableColumns
	{
		UserDept,
		EmailLogin,
		UserId,
		OrderIdExternOrderId,
		FullFiller,
		OrderDateOrderType,
		ServiceNumber,
		StatusDateStatus,
		Requestor
	}	
*/


	// from the login page, go to the order status page.
	public static void GoToOrderStatus() throws Exception
	{
	    WaitForElementClickable(By.id("menuMainProcure"),MainTimeout, "Failed wait in GoToOrderStatus");	
	    Thread.sleep(1000);
	    driver.findElement(By.id("menuMainProcure")).click();
		
	    WaitForElementPresent(By.id("menuMainProcure_Order_Status"),MainTimeout);	    
	    driver.findElement(By.id("menuMainProcure_Order_Status")).click();
	    
	    // switch to the correct frame and wait for name text box.
	    DebugTimeout(0, "Ready for frame switch");
	    driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
	    WaitForElementPresent(By.xpath("(.//*[@id='pad_rt_5']/input[1])[1]"), MainTimeout);
	    WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);	    
	    DebugTimeout(0, "frame switch done");
	    WaitForElementClickable(By.xpath("//input[@value='Search']"),MainTimeout,"failed wait before selecting search button.");	    
	}


	public static void StartDefaultSearch() throws Exception
	{
	    // saw error again in here 12/10/15  
	    // unknown error: Element is not clickable at point (132, 711). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
		WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);

		// intermittent error here when try to click search: unknown error: Element is not clickable at point (1205, 499). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
	    WaitForElementClickable(By.xpath("//input[@value='Search']"),MainTimeout,"failed wait before selecting search button.");
	    driver.findElement(By.xpath("//input[@value='Search']")).click();	    
	    
	    WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
	    WaitForElementClickable(By.xpath("(//div[text()='start'])[2]"),MediumTimeout,"failed wait before selecting search button.");
	}
	
	

	// this assumes oder status page is opened and a search has been done for a user's orders.
	// this will find a list of orders and output some info.
	public static void SetupListOfOrders(int numOrders) throws Exception
	{
	    String[] tempArray;
	    String strTemp;
		List<String> listDates = new ArrayList<String>();	    
		String[] dateLabels = {"InitNote", "SubmitEvent", "FulfillEvent"};		
	    
	    OrderInfoOutList = new FileWriter(OrderInfoOutListFile); // create file writer.
	    OrderInfoOutList.write("Orders List:\n"); // output header.
	    
		
	    for(int x = 3, z = 1; x < /*5*/  13 ; x += 2, z++) 
		{
		    strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(5)")).getText();
		    ShowText(strTemp);
		    //tempArray = strTemp.split("\n"); // don't need this array anymore.
		    
		    // this was 
		    OrderInfoOutList.write("OrderId:" + strTemp + "\n"); 		    

		    // external order Id is no longer shown in orders list.
		    OrderInfoOutList.write("ExternalOrderId:" + "null" + "\n");		    

		    strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(7)")).getText();
		    
		    ShowText(strTemp.split("\n")[0]);
		    ShowText(strTemp.split("\n")[1]);
		    
		    //strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[7]")).getText();
		    //tempArray = strTemp.split("\n");
		    OrderInfoOutList.write("OrderDate:" + strTemp.split("\n")[0] + "\n"); 		    
		    OrderInfoOutList.write("OrderType:" + strTemp.split("\n")[1] + "\n");		    
		    
		    //strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[9]")).getText();
		    //tempArray = strTemp.split("\n");		    
		    strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(7)")).getText();
		    
		    OrderInfoOutList.write("StatusDate:" + strTemp.split("\n")[0] + "\n"); 		    
		    OrderInfoOutList.write("Status:" + strTemp.split("\n")[1] + "\n");		    

		    driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(2)")).click();		    
		    
		    listDates = OrderDetails.GetOrderHistoryDatesAndReturn();
		    
		    for(int y = 0; y < listDates.size(); y++)
		    {
		    	OrderInfoOutList.write("Date" + dateLabels[y]  + ":" + listDates.get(y) + "\r\n");
		    	ShowText("Date" + dateLabels[y]  + ":" + listDates.get(y));
		    }		    		    	
		}
		
		OrderInfoOutList.close();
	}


	public static void SetupListOfOrdersAllCommandData(int numOrders) throws Exception
	{
	    //String[] tempArray;
	    String strTemp;
		//List<String> listDates = new ArrayList<String>();	    
		//String[] dateLabels = {"InitNote", "SubmitEvent", "FulfillEvent"};		
	    
	    OrderInfoOutList = new FileWriter(OrderInfoOutListFileAllCommandInfo); // create file writer.
	    OrderObject.OutputHeaderAndDate(OrderInfoOutList);
	    
	    for(int x = 3, z = 1; x < 50 /* 13*/ ; x += 2, z++) 
		{
	    	OrderObject obj = new OrderObject();
	    	
	    	// user and department.
	    	strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(2)")).getText();

		    obj.user = strTemp.split("\n")[0];
		    obj.department = strTemp.split("\n")[1];
		    
		    obj.email = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(3)>span:nth-of-type(1)")).getText();
		    obj.login = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(3)>span:nth-of-type(2)")).getText();
		    obj.userId = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(4)")).getText(); 
		    obj.orderId = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(5)")).getText();		    
		    obj.fulFiller = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(6)")).getText();		    

		    strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(7)")).getText();
		    obj.orderDate = strTemp.split("\n")[0];
		    obj.orderType = strTemp.split("\n")[1];		    

		    obj.serviceNumber = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(8)")).getText();
		    
		    strTemp = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(9)")).getText();
		    obj.statusDate = strTemp.split("\n")[0];
		    obj.status = strTemp.split("\n")[1];		  
		    
		    obj.requestedBy = driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(10)>a")).getText();
		    
			
		    // BELOW NEW
		    //driver.findElement(By.xpath("(.//*[@id='forceShow'])[3]")).click();
		    //driver.findElement(By.cssSelector(".tngoBox.gridArea>table:nth-of-type(3)>tbody>tr:nth-of-type(" + x + ")>td:nth-of-type(2)")).click();		    
		    // (//b[text()='Doright, Dudley'])[1]
		   

		    /*
		    listDates = OrderDetails.GetOrderHistoryDatesAndReturn();
		    
		    for(int y = 0; y < listDates.size(); y++)
		    {
		    	OrderInfoOutList.write("Date" + dateLabels[y]  + ":" + listDates.get(y) + "\r\n");
		    	ShowText("Date" + dateLabels[y]  + ":" + listDates.get(y));
		    }		    		    	
			*/
			
			//DateInitNote:Aug 29, 2016 11:52:44 PM CDT -0500 UTC
			//DateSubmitEvent:Aug 29, 2016 11:52:44 PM CDT -0500 UTC
			//DateFulfillEvent:Aug 29, 2016 11:52:46 PM CDT -0500 UTC
			
		    orderObjList.add(obj);
		    
		}

	    for(OrderObject obj: orderObjList)
	    {
	    	obj.OutputObject(OrderInfoOutList);
	    }
	    
	    OrderInfoOutList.close();
	}
	
	// jnupp this will select a user that has orders.
	public static void SearchUserByLastName() throws Exception
	{
	    DebugTimeout(0,"starting SearchUserByLastName()");
	    // saw error again in here 12/10/15  
	    // unknown error: Element is not clickable at point (132, 711). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
		WaitForElementPresent(By.cssSelector(".form[name='lastName']"), ShortTimeout);

		
	    driver.findElement(By.cssSelector(".form[name='lastName']")).sendKeys(userOneLastName);	    
	    driver.findElement(By.cssSelector(".form[name='firstName']")).sendKeys(userOneFirstName);

	    // intermittent error here when try to click search: unknown error: Element is not clickable at point (1205, 499). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
	    WaitForElementClickable(By.xpath("//input[@value='Search']"),MainTimeout,"failed wait before selecting search button.");
	    driver.findElement(By.xpath("//input[@value='Search']")).click();	    
	    WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
	    DebugTimeout(0,"search method done");
	}
	
	
	
	
}
