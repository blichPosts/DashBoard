package testSuiteNumericValues;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseValuesHelper.SpendCategory;
import helperObjects.ExpenseValuesHelper;

public class SingleValuesAllControls extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void SingleValuesAllControlsTest() throws Exception
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
		
		CommonTestStepActions.SelectSingleVendor("AT&T Mobility"); // select the vendor

		// /////////////////////////////////////////////////////////////////////////////////
		// total expense control test. loop through each month for the selected vendor.
		// /////////////////////////////////////////////////////////////////////////////////
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
				ExpenseValuesHelper.SetupChartIdForExpense(); // need to set correct chart id. method above changes it.
			}
			ExpenseValuesHelper.VerifyOneVendorTotalExpense(); // verify total expense value for current month selection. 
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////		
		// total expense Spend Category control test. loop through each month for the selected vendor.
		// /////////////////////////////////////////////////////////////////////////////////////////////
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);
				ExpenseValuesHelper.SetupChartIdForExpenseSpendCategory(); // need to set correct chart id. method above changes it. 
			}
			
			ExpenseValuesHelper.VerifyOneVendorTotalExpenseSpendCategory(); // verify total expense value for current month selection. 
		}


		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		// this is for the expense trend. the months don't need to be cycled through because this test verifies the 
		// values for each month by checking the value for each month/bar-graph.  
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ExpenseValuesHelper.SelectMonth(0);  
		ExpenseHelper.WaitForControlLegend(controlType.expenseTrending);
		ExpenseValuesHelper.SetupChartIdForExpenseTrending(); // need to set correct chart id. method above changes it. 
		ExpenseValuesHelper.VerifyOneVendorExpenseTrending(); // verify total expense value for current month selection.
		
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		// this is for count of service numbers trend. the months don't need to be cycled through because this test verifies the 
		// values for each month by checking the value for each month/bar-graph.  
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ExpenseValuesHelper.SelectMonth(0);  
		ExpenseHelper.WaitForControlLegend(controlType.countOfServiceNumbers);
		ExpenseValuesHelper.SetupChartIdForCountOfServiceNumbers(); // need to set correct chart id. method above changes it. 
		ExpenseValuesHelper.VerifyOneVendorCountOfServiceNumbers(); // verify total expense value for current month selection.
		
		// NOTE: the two tests above verify the values in expected equal the actual values. in the test below, the expected values 
		// that have been verified above, are used to verify the 'cost per service' number below.    

		
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		// this is for the cost per service number trend. the months don't need to be cycled through because this test verifies the 
		// values for each month by checking the value for each month/bar-graph.  
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		ExpenseValuesHelper.SelectMonth(0);  
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		ExpenseValuesHelper.SetupChartIdForCostPerServiceNumber(); // need to set correct chart id. method above changes it. 
		ExpenseValuesHelper.VerifyOneVendorCostPerServiceNumber(); // verify total expense value for current month selection.
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		driver.close();
		driver.quit();
	}
}
