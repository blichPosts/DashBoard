package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;

public class PointOfView extends BaseClass
{
	
	public static List<String> expectedLegendsList = new ArrayList<String>();
	public static List<String> listOfVendorsForSelection = new ArrayList<String>();
	public static List<WebElement> tempWebElementList;
	public static String chartId = "";
	public static String tempUrl = "";
	
	public static void StoreAllLegendsInTotalExpense()
	{
		expectedLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
	}
	
	public static void BuildListOfVendorsForVendorSelection() throws InterruptedException
	{
		tempWebElementList = driver.findElements(By.cssSelector(".md-checkbox-label"));
		for(WebElement ele : tempWebElementList)
		{
			listOfVendorsForSelection.add(ele.getText());
		}
	}
	
	public static void ClickVendor() throws InterruptedException
	{
		DebugTimeout(3, "three");
		
		driver.findElement(By.xpath("(//div[@class='md-checkbox-inner-container'])[3]")).click();		
		
		DebugTimeout(9999, "Freeze");
	}

	// this verifies that each control is not visible by looking for the first legend in each control being not visible.
	public static void VerifyControlsNotPresent() throws Exception
	{
		chartId =  UsageHelper.getChartId(0); // total expenses 
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
		chartId =  UsageHelper.getChartId(2);
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));

		chartId =  UsageHelper.getChartId(3);
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
		chartId =  UsageHelper.getChartId(4);
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));

		
		
		// tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";
		// System.out.println(tempUrl);
		// WaitForElementVisible(By.xpath(tempUrl), MediumTimeout);
		// Boolean ret =  WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout);
		// System.out.println(ret);
		// (.//*[@id='highcharts-faf3ftr-0']//*/*[@class='highcharts-legend']/*/*/*)[1]/*
		// 		webElementListLegands = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls));
	}
	
	
	
	
	// ///////////////////////////////////////////// HELPER ////////////////////////////////////////////
	
	public static void ClearContainers()
	{
		if(expectedLegendsList != null)
		{
			expectedLegendsList.removeAll(expectedLegendsList);
		}
		
		if(listOfVendorsForSelection  != null)
		{
			listOfVendorsForSelection .removeAll(expectedLegendsList);
		}
	
		if(tempWebElementList != null)
		{
			tempWebElementList.removeAll(expectedLegendsList);
		}
	}
}
