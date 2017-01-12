package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class TotalExpenseByVendorSpendCategoryActionsCountry extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByVendorSpendCategoryActionsCountryTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// need to
		ExpenseHelper.SetChartId(1);
		
		// store vendor names currently shown in expense control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest(); 

		CommonTestStepActions.SelectCountryView();
		
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		
		CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
		
		// need to 
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		ExpenseHelper.SetupCountryAndVendorData();
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(ViewType.country);
		
		// verify control is empty.
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.totalExpenseSpendCatergory); // verify there are no bar graphs in expense spend category.
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
