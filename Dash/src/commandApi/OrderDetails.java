package commandApi;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

import Dash.BaseClass;

public class OrderDetails extends BaseClass 
{

	// this assumes the order details page is open. the only way to get to this page is to select an order link.
	// this gets the three date/time entries from the order details. 
	public static List GetOrderHistoryDates() throws Exception
	{
		List<String> listDates = new ArrayList<String>();		
		String dateTime = "";
		//String dateTimeParsed = "";		
		
		WaitForElementPresent(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[1])"), MainTimeout);
		dateTime = driver.findElement(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[1])")).getText();
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
		listDates.add(RetrieveTimeStamp(dateTime));
	    dateTime = driver.findElement(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[2])")).getText();	    
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
	    listDates.add(RetrieveTimeStamp(dateTime));
	    dateTime = driver.findElement(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[3])")).getText();	    
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
	    listDates.add(RetrieveTimeStamp(dateTime));
	    return listDates;
	}
	
	// this assumes the order details page is open. the only way to get to this page is to select an order link.
	// this gets the three date/time entries from the order details and returns them in a list. 
	public static List GetOrderHistoryDatesAndReturn() throws Exception
	{
		DebugTimeout(0, "Start Order History");
		DebugTimeout(1, ""); // frame error
		List<String> listDates = new ArrayList<String>();		
		String dateTime = "";
		String frameIndex = "4";

		//tr/td[@class='template-pageTitle']		
		WaitForElementPresent(By.xpath("//tr/td[@class='template-pageTitle']"), MainTimeout);		
		WaitForElementPresent(By.xpath("//span[text()='Order History']"), MainTimeout);		
		
		//span[text()='Order History']
		
		//List<WebElement> listWeb = driver.findElements(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[1])"));
		//DebugTimeout(0,  String.valueOf(listWeb.size()));
		
		if(driver.findElements(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[1])")).size() == 0)			
		{
			frameIndex = "5";
		}	
		
		WaitForElementPresent(By.xpath("(((//table[@id=''])[" + frameIndex + "]/tbody/tr/td/div)[1])"), MainTimeout);
		
		dateTime = driver.findElement(By.xpath("(((//table[@id=''])[" + frameIndex + "]/tbody/tr/td/div)[1])")).getText();
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
		listDates.add(RetrieveTimeStamp(dateTime));
	    
		dateTime = driver.findElement(By.xpath("(((//table[@id=''])[" + frameIndex + "]/tbody/tr/td/div)[2])")).getText();	    
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
	    listDates.add(RetrieveTimeStamp(dateTime));

	    dateTime = driver.findElement(By.xpath("(((//table[@id=''])[" + frameIndex + "]/tbody/tr/td/div)[3])")).getText();	    
		//dateTimeParsed = RetrieveTimeStamp(dateTime);
	    listDates.add(RetrieveTimeStamp(dateTime));
		
		WaitForElementClickable(By.xpath("//input[@name='modifyWorkingSetBtn']"), MainTimeout, "element click failed in GetOrderHistoryDatesAndReturn()");
		driver.findElement(By.xpath("//input[@name='modifyWorkingSetBtn']")).click();
		DebugTimeout(0, "End Order History");
		return listDates;	    
	}	

	
	// assume order details page is open
	public static void LeaveOrderDetailsPage()
	{
		WaitForElementClickable(By.xpath("(((//table[@id=''])[4]/tbody/tr/td/div)[1])"), MainTimeout, "element click failed in GetOrderHistoryDatesAndReturn()");
		
	}
	
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

}
