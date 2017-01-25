package testSuiteNumericValues;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseValuesHelper;
import helperObjects.ExpenseHelper.controlType;

public class SingleValuesAllSpendCategories extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void SingleValuesAllControlsSpendCategoriesTest() throws Exception
	{
		// clear all the controls.
		CommonTestStepActions.GoToExpensePage();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		ExpenseValuesHelper.ReadFileAndBuildLists("AT&T Mobility.txt"); // setup the list of lists to be used for testing. the list holds expected values.
		
		// SETUP OFFSET
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// This is for adjusting what row the expected data is in. This is an example of how to set the 'rowsOfValues' 
		// variable for the case where the  'invoice month' holds the data for the previous month.
		// ExpenseValuesHelper.rowsOfValues-= 1;
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		Thread.sleep(1000);
		CommonTestStepActions.SelectSingleVendor("AT&T Mobility"); // select the vendor
		
		// /////////////////////////////////////////////////////////////////////////////////
		// This tests all spend category selections in the expense trending.
		// /////////////////////////////////////////////////////////////////////////////////
		/*
		ExpenseValuesHelper.SetupSpendCategoriesCounter();
		
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);
			}
			ExpenseValuesHelper.VerifyExpenseTrendSpendCategoriesForSelectedMonth(); // verify 
		}		
		*/
		
		// /////////////////////////////////////////////////////////////////////////////////
		// 
		// /////////////////////////////////////////////////////////////////////////////////
		
		ExpenseValuesHelper.SetupSpendCategoriesCounter();
		
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);
			}
			ExpenseValuesHelper.VerifyCostPerServiceSpendCategoriesForSelectedMonth(); // verify 
			// DebugTimeout(9999, "9999");
		}		
	}

	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		driver.close();
		driver.quit();
	}
}
