package expenses;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.expenseFilters;
import helperObjects.UsageHelper;

public class SpendCategories extends BaseClass 
{

	public static List<String> expectedCostFilters = new ArrayList<>();
	public static expenseFilters currentExpenseFilter; // this is used to indicate which expense filter is being tested.
	public static ExpensesViewMode viewMode;
	public static String chartId = "";
	
	public static void SetupExpectedCostFilters()
	{
		expectedCostFilters.add("All Categories");
		expectedCostFilters.add("Voice Charges");
		expectedCostFilters.add("Data Charges");
		expectedCostFilters.add("Messaging Charges");
		expectedCostFilters.add("Roaming Charges");
		expectedCostFilters.add("Equipment Charges");
		expectedCostFilters.add("Taxes");
		expectedCostFilters.add("Other Charges");
		expectedCostFilters.add("Account Level Charges");
		
		// move to where filter tabs are.
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(2)>div:nth-of-type(3)"));
		new Actions(driver).moveToElement(expenseTrending).perform();
	}	

	
	public static void SetViewMode(ExpensesViewMode vMode)
	{
		viewMode = vMode;
	}
	
	
	// this uses the 'currentExpenseFilter' value to decide which set of cost filters will be used to make the clicks across all the filters. 
	// the 'ClickThroughFiltersAndVerify' method does the clicks and then calls the method that handles all the testing. 
	public static void VerifySpendCateoryFilter() throws Exception 
	{
		switch(ExpenseHelper.currentExpenseFilter)
		{
			case Expense:
			{
				ClickThroughFiltersAndVerify(ExpenseHelper.expenseTrendFilters);
				break;
			}
			case CostPerServiceNumber:
			{
				ClickThroughFiltersAndVerify(ExpenseHelper.costPerServiceNumberFilters);
				break;
			}
			case CountOfServiceNumbers:
			{
				//ClickThroughFiltersAndVerify(countofServiceNumberFilters); // this filter was removed.
				break;
			}
			default:
			{
				Assert.fail("Bad case sent to sent to ExpenseHelper.VerifySpendCateoryFilter.");
			}
		}
	}	
	
	// this clicks through the category selectors and does the testing described below.
	public static void ClickThroughFiltersAndVerify(String xPath) throws Exception
	{

		List<WebElement> webEleListBarGraphHoverValues = new ArrayList<WebElement>();
		
		
		// get a list of the web elements that are related to the xPath passed in.
		// the xPath is pointing to one of the category filters tabs.
		List<WebElement> listToClickThrough = driver.findElements(By.xpath(xPath));

		String actual = "";
		String expected  = "";
		
		Assert.assertTrue(listToClickThrough.size() != 0, "Found empty list in ClickThroughFiltersAndVerify.");

		// get to section where hover tests can be done.
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(2)"));  
		new Actions(driver).moveToElement(expenseTrending).perform();
		
		int x = 0;
		
		// go through the each filter (category) selection in xpath sent in and select them one at a time.
		for(WebElement ele : listToClickThrough)
		{
			ele.click();
			
			// these two waits verify the correct cost filter text is found in the tile for 'Expense Trending' and 'Cost Per Service Number'. 
			WaitForElementVisible(By.xpath("(//span[text()='" + expectedCostFilters.get(x) + "'])[1]"), MediumTimeout);  
			WaitForElementVisible(By.xpath("(//span[text()='" + expectedCostFilters.get(x) + "'])[2]"), MediumTimeout); 
			
			// ///////////////////////////////////////////////////////////////////////////////////
			// now verify complete titles for'Expense Trending' and 'Cost Per Service Number'. 
			// the titles will vary, depending which cost category is selected.
			// ///////////////////////////////////////////////////////////////////////////////////
			
			if(viewMode == ExpensesViewMode.vendor) // this is for vendor view.
			{
				// expense trending
				actual = driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText();
				expected = TotalExpensesTrend.vendorTitleShort + " - " + expectedCostFilters.get(x);
				Assert.assertEquals(actual, expected, "");
				
				VerifyHoverTitles(UsageHelper.getChartId(2), expected.replace(" ($)", ""));
				
				// cost per service number.
				actual = driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText();
				expected = CostPerServiceNumberTrend.vendorTitleShort + " - " + expectedCostFilters.get(x);
				Assert.assertEquals(actual, expected, "");
				
				VerifyHoverTitles(UsageHelper.getChartId(3), expected.replace(" ($)", ""));
				
				// verify text in Cost per Service Number by waiting for its text to be visible. 
				WaitForElementVisible(By.xpath("//span[text()='Cost per Service Numbers by Vendor']"), MediumTimeout);
				
				VerifyHoverTitles(UsageHelper.getChartId(4), "");
				
			}
			else // this is for expense view.
			{
				// expense trending
				actual = driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText();
				expected = TotalExpensesTrend.countryTitleShort + " - " + expectedCostFilters.get(x);
				Assert.assertEquals(actual, expected, "");
				
				// cost per service number.
				actual = driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText();
				expected = CostPerServiceNumberTrend.countryTitleShort + " - " + expectedCostFilters.get(x);
				Assert.assertEquals(actual, expected, "");
				
				// verify text in Cost per Service Number by waiting for its text to be visible. 
				WaitForElementVisible(By.xpath("//span[text()='Cost per Service Numbers by Country']"), MediumTimeout);
			}
			
			VerifyCorrectSelection(listToClickThrough, x); // verify the correct enable/disable states for control xPath sent in to this method.
			
			// this verifies the enabled/disabled states of other category selector that is not being clicked through.  
			VerifyOtherCategorySelector(x);
			x++;
		}
	}
	
	// this is sent a web list of selectors to look through. one of them is selected and the rest aren't.
	// the one that is selected is indicated by the integer sent in. the rest of the cost filters are not selected.
	// this verifies that the list element with the index 'x' is enabled and all the other list elements are not.  
	public static void VerifyCorrectSelection(List<WebElement> eleList,  int x)
	{
		String errMessage = "Failed testing of enabled/disabled cost filters in ExpenseHelper.VerifyCorrectSelection";
		
		Assert.assertTrue(eleList.size() != 0, "Method VerifyCorrectSelection has been paseed an empty web element list.");
		
		for(int y = 0; y < eleList.size(); y++)
		{
			if(y == x)
			{
				Assert.assertTrue(eleList.get(y).getAttribute("class").contains("option--selected"),errMessage); 
			}
			else
			{
				Assert.assertFalse(eleList.get(y).getAttribute("class").contains("option--selected"), errMessage);
			}
		}
	}
	
	// this verifies the remaining category selectors. This selectors that weren't clicked on. 
	public static void VerifyOtherCategorySelector(int x) 
	{
		List<WebElement> tempListOne;

		switch(ExpenseHelper.currentExpenseFilter)
		{
			case Expense: // expense trend is being clicked, verify other category selector.
			{
				tempListOne = driver.findElements(By.xpath(ExpenseHelper.costPerServiceNumberFilters));  
				VerifyCorrectSelection(tempListOne, x);
				break;
				
			}
			case CostPerServiceNumber: // cost per service number trend is being clicked, verify the other two category selectors.
			{
				tempListOne = driver.findElements(By.xpath(ExpenseHelper.expenseTrendFilters));
				VerifyCorrectSelection(tempListOne, x);
				break;
				
			}
			// CountOfServiceNumbers has no associated category selectors - REMOVED in 17.1.
			//case CountOfServiceNumbers:// count of service number trend is being clicked, verify other two category selectors. // REMOVED in 17.1.
			//{
			//	tempListOne = driver.findElements(By.xpath(expenseTrendFilters));
			//	VerifyCorrectSelection(tempListOne, x);
			//	tempListTwo  = driver.findElements(By.xpath(costPerServiceNumberFilters));
			//	VerifyCorrectSelection(tempListTwo, x);
			//	break;
			//}
			default:
			{
				Assert.fail("Bad case parameter in ExpenseHelper.VerifyRemainingCostSelectors.");
			}
		}
	}	
	
	
	// NOTE - this code is taken from "ExpenseHelper.MoveMouseToBarExpenseActions" method.
	// this hovers across the chatId trend graph and verifies each hover title is correct per 'expectedText' passed in.  
	public static void VerifyHoverTitles(String chartId, String expectedText) throws InterruptedException, AWTException
	{
		boolean firstMonth = true;

		String errMess = "Failed verification of title found in hover in method SpendCategories.VerifyHoverTitles";  
		
		for(int y = 1; y <= ExpenseHelper.maxNumberOfMonths; y++)
		{
			ExpenseHelper.MoveMouseToBarExpenseActions(chartId, y, firstMonth);
			
			String actual = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo)).get(1).getText();
			
			Assert.assertEquals(actual, expectedText, errMess);
			
			firstMonth = false; // <-- ana_add
		}		
	}
}
