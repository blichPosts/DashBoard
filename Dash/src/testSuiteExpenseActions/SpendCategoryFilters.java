package testSuiteExpenseActions;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

// history

// Bob - changed 

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.SpendCategories;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper.expenseFilters;
import helperObjects.ExpenseValuesHelper.SpendCategory;

public class SpendCategoryFilters extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void SpendCategoryFiltersTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// ///////////////////////////
		// 	      vendor view
		// ///////////////////////////
		
		SpendCategories.SetViewMode(ExpensesViewMode.vendor);
		
		
		// this sets up names of cost filters.
		SpendCategories.SetupExpectedCostFilters();
		
		ExpenseHelper.SetExpenseFilter(expenseFilters.Expense); // this indicates which expense filter is being tested. 
		
		// verify selecting expense trending selectors different ---- FINISH 
		SpendCategories.VerifySpendCateoryFilter(); 

		// REMOVED in 17.1
		//SpendCategories.SetExpenseFilter(expenseFilters.CountOfServiceNumbers); // this indicates which expense filter is being tested.
		
		// REMOVED in 17.1
		// this filter was removed.
		// verify selecting count of trending 
		//ExpenseHelper.VerifySpendCateoryFilter();

		ExpenseHelper.SetExpenseFilter(expenseFilters.CostPerServiceNumber); // this indicates which expense filter is being tested.
		
		// verify selecting cost per service trending 
		SpendCategories.VerifySpendCateoryFilter();
		
		// ///////////////////////////
		// 	      country view
		// ///////////////////////////
		
		SpendCategories.SetViewMode(ExpensesViewMode.country);
		
		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryPageWait();
		
		// move to country view.
		CommonTestStepActions.SelectCountryView();

		// this sets up names of cost filters.
		SpendCategories.SetupExpectedCostFilters();
		
		ExpenseHelper.SetExpenseFilter(expenseFilters.Expense); // this indicates which expense filter is being tested. 
		
		// verify selecting expense trending selectors different ---- FINISH 
		SpendCategories.VerifySpendCateoryFilter(); 

		// REMOVED in 17.1
		//SpendCategories.SetExpenseFilter(expenseFilters.CountOfServiceNumbers); // this indicates which expense filter is being tested.
		
		// REMOVED in 17.1
		// this filter was removed.
		// verify selecting count of trending 
		//ExpenseHelper.VerifySpendCateoryFilter();

		ExpenseHelper.SetExpenseFilter(expenseFilters.CostPerServiceNumber); // this indicates which expense filter is being tested.
		
		// verify selecting cost per service trending 
		SpendCategories.VerifySpendCateoryFilter();
		
		Pause("Passeed");
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	
}
