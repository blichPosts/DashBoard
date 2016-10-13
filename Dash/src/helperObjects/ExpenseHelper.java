package helperObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;

public class ExpenseHelper extends BaseClass
{
	public static String tmpStr = "";
	public static String errMessage = "";	
	
	
	public static void VerifyTotalExpenseCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyTotalExpenseCost";
		
		//The top says 'Total Expense'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > h3")).getText(), ExpenseKpiNames[0], errMessage);

		VerifyExpenseTotalCost(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText(), errMessage);
	}
	
	public static void VerifyCountOfServiceNumbersAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCountOfServiceNumbersAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText(), ExpenseKpiNames[1], errMessage);

		VerifyExpenseTotalCost(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText(), errMessage);
	}	
	
	public static void VerifyThreeComponents()
	{
		errMessage = "Failure in ExpenseHelper.VerifyThreeComponents";
		
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi")).size(), 3, errMessage);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi > h3")); // get the names of the three KPI components. 

		Assert.assertTrue(webList.size() == 3, errMessage);
		
		// verify the three components.
		for(int x = 0; x < ExpenseKpiNames.length; x++)
		{
			Assert.assertEquals(ExpenseKpiNames[x], webList.get(x).getText(), errMessage);
		}
	}
	
	public static void TotalExpenseTrendVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.TotalExpenseTrendVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.cssSelector(".tdb-kpi__trend > span:nth-of-type(2)")).getText(), errMessage);
	}
	
	public static void VerifyRollingAverageTotalExpense()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverage";
		
		VerifyRollingAverageTotalExpense(errMessage);
	}
	
	// //////////////////////////////////////////////////////////////////////	
	// 								helpers
	// //////////////////////////////////////////////////////////////////////
	
	public static void VerifyRollingAverageTotalExpense(String errMess) 
	{
		String strArray [] = driver.findElement(By.xpath("(//h4[@class='tdb-h4'])[1]/../..")).getText().split("\n");
		
		// bladdxx
		//Assert.assertEquals(strArray[0], rollingAverages, errMess); // rolling averages text

		//Assert.assertEquals(strArray[1].split("s")[0] + "s", rollingMonthsThree, ""); // 3 months text 
		//Assert.assertEquals(strArray[2].split("s")[0] + "s", rollingMonthsSix, "");	//  months text

		VerifyInteger(strArray[1].split("months")[1].replace("K",""), errMess);	// numeric value	
		VerifyInteger(strArray[2].split("months")[1].replace("K",""), errMess);	// numeric value
		
		/*
		0 Rolling Averages
		1 3 months511K
		2 6 months516K
		DONE
		 */
	}
	
	public static void VerifyInteger(String tmpStr, String errMessage)
	{
		try
		{
			int num = Integer.parseInt(tmpStr);
		} 
		catch (NumberFormatException e) 
		{
			Assert.fail(errMessage + e.getMessage());
		}
	}
	
	public static void VerifyExpenseTotalCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.contains("$"), errMessage); // verify contains '$'.
		Assert.assertTrue(tmpStrIn.contains("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}
	
	public static void VerifyCountServiceNumbersCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.contains("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace(".", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyTrendDetails(String tmpStrIn, String errMess)
	{
		// this is up arrow. - can't check
		// tmpStr = driver.findElement(By.cssSelector(".tdb-kpi__trend > span:nth-of-type(1)")).getText();
		// Assert.assertTrue(tmpStr.contains("?"), ""); 
		
		Assert.assertTrue(tmpStrIn.contains("%"), ""); // verify contains "%".		
		tmpStrIn = tmpStrIn.replace("%", "").replace(".", ""); // get number with percent.
		VerifyInteger(tmpStrIn, errMessage); // verify all integers after removing $, % , and '.'.
	}
	

};


