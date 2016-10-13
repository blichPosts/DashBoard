package Dash;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import helperObjects.Country;

public class BaseClass
{
	public static WebDriver driver;
	public static String baseUrl;
	//public static String CMD_baseUrl = "https://commcareqa.tangoe.com/manage/login";
	//public static String CMD_baseUrl = "http://dc1qacmdweb04:8080/manage/login/login.trq";
	//public static String CMD_baseUrl = "https://qa1cmd.tangoe.com/manage/login/login.trq";
	// public static String CMD_baseUrl = "https://qa3.traq.com/manage/login/login.trq";	
	public static String CMD_baseUrl = "http://dc1devmule1.prod.tangoe.com:3000/fleet/expense";
	// https://qa3.traq.com/manage/login/login.trq
	
	public static boolean testCaseStatus = false;
	
	// these are timeouts.
	public static int ExtremeTimeout = 120;
	public static int MainTimeout = 45;
	public static int MediumTimeout = 15;	
	public static int ShortTimeout = 5;
	public static int MiniTimeout = 3;	
	public static int TinyTimeout = 2;
	
	// command variables
	//public static String commandUserName = "bob.lichtenfels@tangoe.com XX1";
	//public static String commandPassword = "hop*ititmb9";	
	
	
	public static String commandUserName = "shirley.banai@philips.com";
	public static String commandPassword = "traq01";	
	public static String commandURL = "https://qa1cmd.tangoe.com/manage/login/login.trq";

	// names of the three KPI blocks in expenses page from left to right.
	public static String [] ExpenseKpiNames = {"Total Expense", "Count of Service Numbers", "Cost per Service Number"};
	
	// names for the three KPI blocks in Usage page, under Domestic
	public static String [] UsageKpiNames = {"Voice (min)", "Data (B)", "Messages", "Data (B)"};
	
	
	public static JFrame frame;	
	
	// these are for accessing current project folder.
	static File currentDirectory; 
	public static String projectPath = ""; 

	public static List<Country> countryList = new ArrayList<Country>();	 // this a list for holding the countries.
	
	public enum Action // needed for base class methods. 
	{
		Add,
		Remove
	}

	// ctor
	public BaseClass()  
	{
		System.out.println("BASE CLASS CONSTRUCTOR...");
		// projectPath = currentDirectory.getAbsolutePath();
	}
	
	public static void ShowArray(String [] strArray)
	{
		for(int x = 0; x < strArray.length; x++)
		{
			System.out.println(x + " " + strArray[x]);
		}
	}
	
	// this takes in a string that is a running total called 'runningTotal' and a string that is 
	// a value to add or subtract to/from the running total called 'valueToChange'.
	// 1) create doubles that hold the running total and the value to add.
	// 2) setup a decimal format.
	// 3) add running total and value to add.
	// 4) return string that has new running total
	public static String GetNewTotal(String runningTotal, String valueToChange, Action actionType)
	{
		// convert to double and create a decimal format.
		double tempTotalIn  = Double.valueOf(runningTotal).doubleValue();
        double tempValueForAction  = Double.valueOf(valueToChange).doubleValue(); 
	    DecimalFormat decFormat = new DecimalFormat("#.00");
        
        // add value to add sent in to running total and return.
	    if(actionType == Action.Add)
	    {
	    	tempTotalIn += tempValueForAction;
	    }
	    else
	    {	
	    	tempTotalIn -= tempValueForAction;	    
	    }

	    return decFormat.format(tempTotalIn);
	}		

	// returns one month behind current month and year. 
	static public String MonthYear()
	{
		Calendar c = Calendar.getInstance();		
		int month = c.get(Calendar.MONTH);		
		int year = c.get(Calendar.YEAR);		
		String monthString = new DateFormatSymbols().getMonths()[month - 1];
		
		if(monthString.equals("January"))
		{
			year = year - 1;
		}
		
		return monthString + " " + year;
	}	
	
	
	// this is method that takes a decimal cost (2344.45) and converts is to full money text ($2,344.45).  
	public static String CostMonthlyCalculatedConvertToFullText(String costMonthlyTotal)
	{
		
		StringBuilder finalValue = new StringBuilder(); // this is the string that gets created. it is returned. 
		// System.out.println(costMonthlyTotal); // DEBUG.
		
		int threeCntr = 1;
		
		// trim the monthly total into two parts with dollar part in element 0.  
		String [] tempArray = costMonthlyTotal.split("\\.");
		
		// loop through the dollar part from right to left. starting at end, build the string to be returned with inserted "," where needed.   
		for(int x = tempArray[0].length(); x > 0; x--)
		{
			finalValue.insert(0,tempArray[0].charAt(x - 1));
			
			if(threeCntr == 3 && x != 1) // insert "," if the x counter is not at he first number in the dollar part.
			{
				finalValue.insert(0,",");
				threeCntr = 0;
			}
			threeCntr++;	
		}

		finalValue.append("." + tempArray[1]); // add the cents part to end 
		finalValue.insert(0, "$"); // add dollar sign to front.
		
		return finalValue.toString();
	}		
	
	public static void setUpDriver() throws Exception
	{
		//driver = new FirefoxDriver();
		//System.setProperty("webdriver.ie.driver", path + "\\IEDriverServer.exe");
		
		currentDirectory = new File(".");
		projectPath = currentDirectory.getAbsolutePath();
		
		System.setProperty("webdriver.chrome.driver", projectPath + "\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		driver = new ChromeDriver(options);		

		baseUrl = CMD_baseUrl;
		driver.get(baseUrl);
		
		// maximize and configure timeouts
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public static void loginAsAdmin()throws Exception
	{
		// wait for login button.
		WaitForElementClickable(By.xpath("//input[@name='Login']"), MainTimeout, "Error waiting for Login Button element on login page.");
		
		driver.findElement(By.xpath("//input[@name='userName']")).clear();
	    driver.findElement(By.xpath("//input[@name='userName']")).sendKeys(commandUserName);	    
	    driver.findElement(By.xpath("//input[@name='password']")).clear();
	    driver.findElement(By.xpath("//input[@name='password']")).sendKeys(commandPassword);	    
	    driver.findElement(By.xpath("//input[@name='Login']")).click();	    
	}
	
	public static void SelectExpenseTab()throws Exception	
	{
		WaitForElementClickable(By.xpath("//a[text()='View Expense']"), MediumTimeout, "Failed Click in  BaseClass.WaitForExpensePageLoad");
		driver.findElement(By.xpath("//a[text()='View Expense']")).click(); // select 'view expenses'.
		// is selected true fails.
		//Assert.assertFalse(driver.findElement(By.xpath("//a[text()='View Usage']")).isSelected()); // verify not selected.
		//Assert.assertTrue(driver.findElement(By.xpath("//a[text()='View Expense']")).isSelected()); // verify selected.		
	}
	
	
	// Modified by Ana
	public static void SelectUsageTab()throws Exception	
	{
		WaitForElementClickable(By.xpath("html/body/app-root/app-fleet-dashboard-container/header/div[2]/div[2]/a[text()='View Usage']"), MediumTimeout, "Failed Click in  BaseClass.WaitForExpensePageLoad");
		driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/header/div[2]/div[2]/a[text()='View Usage']")).click();
	}
	
	
	public static void WaitForExpensePageLoad()throws Exception	
	{
		WaitForElementVisible(By.xpath("//span[text()='Countries']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Count of Service Numbers']"), MediumTimeout);		
		WaitForElementVisible(By.xpath("//div/h3[text()='Count of Service Numbers']"), MediumTimeout);		
		WaitForElementClickable(By.xpath("//select"), MediumTimeout, "Failed Click in  BaseClass.WaitForExpensePageLoad"); 	
	}	
	
	

	public static void WaitForExpensePageDetailedLoad()throws Exception	
	{
		WaitForElementVisible(By.xpath("//span[text()='Countries']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Count of Service Numbers']"), MediumTimeout);		
		WaitForElementVisible(By.xpath("//div/h3[text()='Count of Service Numbers']"), MediumTimeout);		
		WaitForElementClickable(By.xpath("//select"), MediumTimeout, "Failed Click in  BaseClass.WaitForExpensePageLoad"); 	
		
		// not sure if these two will always work. maybe the ID will change - beware. 
		WaitForElementPresent(By.xpath(".//*[@id='highcharts-8']/*/*"), MediumTimeout);
		WaitForElementVisible(By.xpath("(.//*[@id='highcharts-8']/*/*[@class='highcharts-axis'])[1]"), MediumTimeout);

	}		
	
	
	// Added by Ana 
	public static void WaitForViewUsageSelector() throws Exception{
		
		WaitForElementVisible(By.xpath("html/body/app-root/app-fleet-dashboard-container/header/div[2]/div[2]/a[text()='View Usage']"), MainTimeout);
		driver.findElement(By.xpath("html/body/app-root/app-fleet-dashboard-container/header/div[2]/div[2]/a[text()='View Usage']")).click();
		
		
	}
	
	// Added by Ana -- **** not sure if needed *****
	public static void WaitForUsagePageDetailedLoad() throws Exception {
		
		//WaitForElementVisible(By.xpath("//span[text()='Countries']"), MediumTimeout);
		WaitForElementVisible(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[1]/div/div[1]/h3[text()='Voice (min)']"), MediumTimeout);		
		WaitForElementVisible(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[1]/div/div[2]/h3[text()='Data (TB)']"), MediumTimeout);	
		WaitForElementVisible(By.xpath("html/body/app-root/app-fleet-dashboard-container/div[2]/main/app-usage-dashboard/div/app-usage-kpis/div[1]/div[1]/div/div[3]/h3[text()='Messages']"), MediumTimeout);
		
		
		
	}
	
	
	public static void ShowCountryVendorList()
	{
		for(Country ctry : countryList)
		{
			System.out.println("Country ----------- " + ctry.name);
			ctry.ShowVendorList();
		}
	}	

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE !!!!!!!!!!!!!!!!!!!!!!!! this is just a test for switching a frame in command.
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////
	// from the login page, go to the order status page.
	public static void GoToOrderStatus() throws Exception
	{
	    WaitForElementClickable(By.id("menuMainProcure"),MainTimeout, "Failed wait in GoToOrderStatus");	    
	    DebugTimeout(2, "Two second wait in 'GoToOrderStatus' - frame clash.");
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
	
	
	
	public static void ShowPopup(String message)		
	{
		JOptionPane.showMessageDialog(frame, message);
	}

	// /////////////////////////////////////////////////////////////////
	// The next two methods are need for date/time testing
	// /////////////////////////////////////////////////////////////////
	public static String BuildDateString()
	{
		LocalDateTime ldt = LocalDateTime.now();
        // System.out.println(ldt.getDayOfMonth() + " " + GetMonth() + " " + ldt.getYear());
		return ldt.getDayOfMonth() + " " + GetMonth() + " " + ldt.getYear();
	}
	
	// This returns the month in a string/calendar format with first char as Caps.
	public static String GetMonth()
	{
	   LocalDateTime ldt = LocalDateTime.now();
	   ldt.getMonth();
	   StringBuilder strBuilder = new StringBuilder();
	   String strMonth = ldt.getMonth().toString();
		   
	   for(int z = 0; z < strMonth.length(); z++)
	   {
		   if(z == 0)
		   {
			   strBuilder.append(strMonth.charAt(z));			   
		   }
		   else
		   {
			   strBuilder.append( strMonth.toLowerCase().charAt(z));			   
		   }
		}
		return strBuilder.toString();
	}
	
	
	// this takes a by object, a time duration in seconds, and an anticipated size to be found.
	// the by object is used in "driver.findelements(by)". the "driver.findelements(by)" 
	public static boolean WaitForElements(By by, long waitDuration, int anticipatedSize) throws Exception
	{
		long waitTime = waitDuration;
		
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis()-startTime) < waitTime *1000)
		{
			if(driver.findElements(by).size() == anticipatedSize)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void DebugTimeout(int TimeOut,  String... userInput) throws InterruptedException
	{
		if(userInput.length > 0)
		{
			System.out.println(userInput[0]);
		}
		Thread.sleep(TimeOut * 1000);
	}	
		
		public static void WaitForElementClickable(By by, int waitTime, String message)
		{
		    try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, waitTime);
		    	wait.until(ExpectedConditions.elementToBeClickable(by));
		    }
		    catch (WebDriverException e)
		    {
		        throw new WebDriverException(message);
		    }
		}	
	    
		public static boolean WaitForElementNotClickableNoThrow(By by, int waitTime, String message)
		{
		    try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, waitTime);
		    	ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)).apply(driver);
		    }
		    catch (WebDriverException e)
		    {
		    	return false;
		    }
		    return true;
		}	
		
		public static boolean WaitForElementVisible(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
		        //System.out.println(e.toString());
		        throw new Exception(e.toString());
	        }	    
		    return true;
		}
		
		public static boolean WaitForElementVisibleNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
	        	return false;
	        }	    
		    return true;
		}		
		
		
		public static boolean WaitForElementNotVisibleNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
	        	return false;
	        }	    
		    return true;
		}				
		
		public static boolean WaitForElementPresent(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.presenceOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
		        //System.out.println(e.toString());
		        throw new Exception(e.toString());
	        }	    
		    return true;
		}		

		public static boolean WaitForElementNotPresentNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.presenceOfElementLocated(by));
			    ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(by)).apply(driver);			    
		    }
	        catch (Exception e)
	        {
		        return false;
	        	//System.out.println(e.toString());
		        //throw new Exception(e.toString());
	        }	    
		    return true;
		}		
}
