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
		CommonTestStepActions.GoToExpensePage();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();

		// 
		ExpenseValuesHelper.ReadFileAndBuildLists("AT&T Mobility.txt");

		ShowText(ExpenseValuesHelper.listOfRows.get(0).get(ExpenseValuesHelper.titlesList.indexOf("invoice_month")));
		
		ShowText(CommonTestStepActions.convertMonthNumberToName("9", "2016")); 
		
		DebugTimeout(9999, "9999");
		
		// this month is known to have all the vendors possible showing. 
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		// 
		CommonTestStepActions.SelectSingleVendor("AT&T Mobility");
		
		// 
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		
		
		ExpenseValuesHelper.SetupChartId();
		
		ExpenseValuesHelper.VerifyOneVendor();
		
		//ShowText((ExpenseValuesHelper.listOfRows.get(0).get(ExpenseValuesHelper.titlesList.indexOf("ordinal_month"))));
		//ShowText(ExpenseValuesHelper.listOfRows.get(0).get(ExpenseValuesHelper.titlesList.indexOf("roaming_data_charges_ex")));
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		driver.close();
		driver.quit();
	}

	
	
	
	
}
