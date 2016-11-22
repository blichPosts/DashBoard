package helperObjects;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;

public class ExpenseHelper extends BaseClass
{
	public static String tmpStr = "";
	public static String errMessage = "";	
	public static String chartId = "";
	public static List<WebElement> webElementListLegands;	
	public static List<String> legendsListTotalExpense = new ArrayList<String>();	

	// these are for VerifyMonths method 
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<String> actualYearMonthList = new ArrayList<String>();
	
	
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
	
	public static void VerifyTotalExpenseCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyTotalExpenseCost";
		
		//The top says 'Total Expense'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > h3")).getText(), ExpenseKpiNames[0], errMessage);
		// css=.tdb-kpi:nth-of-type(1) > h3 // this type of indexing works.

		VerifyExpenseTotalCost(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText(), errMessage);
	}
	
	public static void VerifyCountOfServiceNumbersCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCountOfServiceNumbersAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText(), ExpenseKpiNames[1], errMessage);

		VerifyCountServiceNumbers(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > div> div:nth-of-type(1)")).getText(), errMessage);
	}	
	
	public static void VerifyCostPerServiceNumberCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCostPerServiceNumberCostAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > h3")).getText(), ExpenseKpiNames[2], errMessage);

		VerifyCostPerServiceNumber(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > div> div:nth-of-type(1)")).getText(), errMessage);
	}		
	
	public static void TotalExpensedVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.TotalExpensedVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[1]/span)[2]")).getText(), errMessage);
	}
	
	public static void CountServiceNumbersVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.CountServiceNumbersVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[2]/span)[2]")).getText(), errMessage);
	}	
	
	public static void CostPerServiceNumberVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.CostPerServiceNumberVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[3]/span)[2]")).getText(), errMessage);
	}		
	
	public static void VerifyRollingAverageTotalExpense()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverage";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[1]/../..");
	}
	
	public static void VerifyRollingAverageCountServiceNumbers()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverageCountServiceNumbers";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[2]/../.."); 
	}
	
	public static void VerifyRollingAverageCostPerServiceNumber()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverageCountServiceNumbers";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[3]/../.."); 
	}
	
	// this gets the legends in the expense control. this is here because all the legends in the other controls are supposed to
	// match the total expense control. this is called by each control when the control's legends are verified to match the 
	// total expense control's legends. 
	public static List<String> GetTotalExpenseLegends()
	{
		chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.

		// store all legend names into web element list.
		webElementListLegands = driver.findElements(By.xpath("//div[@id='" +  chartId + "']/*/*[@class='highcharts-legend']/*/*/*"));		
		// for(WebElement ele : webElementListLegands ){System.out.println(ele.getText());} // DEBUG
		
		if(legendsListTotalExpense != null)
		{
			legendsListTotalExpense.removeAll(legendsListTotalExpense);
		}
		
		// store legend names into legend list.
		for(WebElement ele : webElementListLegands )
		{
			legendsListTotalExpense.add(ele.getText());

		} 
		
		return legendsListTotalExpense;
	}
	
	public static void VerifyMonths(String chartId, String xpathSegment) throws ParseException 
	{
		ClearAllContainersForVerifyMonths();
		
		// get expected month(int)/years pulldown - format is MM-YYYY. 
		expectedYearMonthList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		// get the months shown in the control in the UI.
		// webEleListMonthYearActual = driver.findElements(By.xpath(".//*[@id='" + chartId + "']/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*"));
		webEleListMonthYearActual = driver.findElements(By.xpath(".//*[@id='" + chartId + "']"  + xpathSegment));
		
		// put actual web list values into a list of strings.
		for(WebElement ele : webEleListMonthYearActual)
		{
			actualYearMonthList.add(ele.getText());
			//System.out.println(ele.getText());			
		}

		// the actual list is in descending order. the expected list is in ascending order. using sort on actual list didn't correct order, so use x increment and y decrement.  
		for(int x = 0, y = expectedYearMonthList.size() - 1; x < expectedYearMonthList.size(); x++, y--)
		{
			Assert.assertEquals(actualYearMonthList.get(y), expectedYearMonthList.get(x), "Failed check of months in TotalExpensesTrend.VerifyMonths");
		}
	}	
	
	// //////////////////////////////////////////////////////////////////////	
	// 								helpers
	// //////////////////////////////////////////////////////////////////////
	
	public static void ClearAllContainersForVerifyMonths()
	{
		if(expectedYearMonthList != null)
		{
			expectedYearMonthList.removeAll(expectedYearMonthList);
		}
		
		if(webEleListMonthYearActual != null)
		{
			webEleListMonthYearActual.removeAll(expectedYearMonthList);
		}		
		
		if(actualYearMonthList != null)
		{
			actualYearMonthList.removeAll(expectedYearMonthList);
		}
	}
	
	
	
	public static void VerifyRollingAverageTotalExpense(String errMess, String locator)   
	{
		// String strArray [] = driver.findElement(By.xpath("(//h4[@class='tdb-h4'])[1]/../..")).getText().split("\n");
		
		String strArray [] = driver.findElement(By.xpath(locator)).getText().split("\n");
		
		Assert.assertEquals(strArray[0], rollingAverages, errMess); // rolling averages text
		Assert.assertEquals(strArray[1].split("s")[0] + "s", rollingMonthsThree, ""); // 3 months text 
		Assert.assertEquals(strArray[2].split("s")[0] + "s", rollingMonthsSix, "");	//  6 months text
	
		VerifyInteger(strArray[1].split("months")[1].replace("K","").replace(".","").replace("$", "").trim(), errMess);	// numeric value	
		VerifyInteger(strArray[2].split("months")[1].replace("K","").replace(".","").replace("$", "").trim(), errMess);	// numeric value
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
		Assert.assertTrue(tmpStrIn.startsWith("$"), errMessage); // verify contains '$'.
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}
	
	public static void VerifyCountServiceNumbers(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyCostPerServiceNumber(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.startsWith("$"), errMessage); // verify contains '$'.
		tmpStrIn = tmpStrIn.replace("$","").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyCountServiceNumbersCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace(".", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyTrendDetails(String tmpStrIn, String errMess)
	{
		// this is up arrow. - can't check
		// tmpStr = driver.findElement(By.cssSelector(".tdb-kpi__trend > span:nth-of-type(1)")).getText();
		// Assert.assertTrue(tmpStr.contains("?"), ""); 
		
		Assert.assertTrue(tmpStrIn.endsWith("%"), ""); // verify contains "%".		
		tmpStrIn = tmpStrIn.replace("%", "").replace(".", ""); // get number with percent.
		VerifyInteger(tmpStrIn, errMessage); // verify all integers after removing $, % , and '.'.
	}
};


