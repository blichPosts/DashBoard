package testSuiteExpenseActions;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import Dash.BaseClass.ViewType;
import expenses.TotalExpenseByVendorSpendCategory;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class TotalExpensesTrendVendorActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpensesTrendVendorActionsTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		TotalExpensesTrend.SetupChartId();
		
		TotalExpensesTrend.Setup();

		TotalExpensesTrend.VerifyRemovingLegends();
		DebugTimeout(9999, "Freeze");
		
		
		
		
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();

		// need this after verify above.
		TotalExpensesTrend.SetupChartId();
		
		// CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
		// TotalExpensesTrend.TestOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));

		
		// need this after verify above.
		TotalExpensesTrend.SetupChartId();

		
		
		TotalExpensesTrend.VerifyRemovingLegends();
		
		
		DebugTimeout(9999, "Freeze");
		
		// UsageTrendingTestValues from package usageTestValuesFromFile
		// line 427
		
		
		
		
		/*
		CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(1));

		DebugTimeout(3, "3");
		TotalExpensesTrend.Setup();
		DebugTimeout(3, "3");

		
		TotalExpensesTrend.TestOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));

		TotalExpensesTrend.webEleListLegends.get(0).click();

		TotalExpensesTrend.SetupChartId();
		DebugTimeout(2, "hope");		
		TotalExpensesTrend.TestOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
		*/
		
		/*
		for(WebElement ele : TotalExpensesTrend.webEleListLegends)
		{
			ShowText(ele.getText());
		}
		*/
		
		
		DebugTimeout(9999, "Freeze");
		
		
		//DebugTimeout(3, "pass 2");
		//TotalExpensesTrend.webEleListLegends.get(1).click();
		//TotalExpensesTrend.TestOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(1));
		
		
		
	
		/*
		// need to
		ExpenseHelper.SetChartId(1);
		
		// store vendor names currently shown in expense control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest(); 

		CommonTestStepActions.SelectCountryView();
		
		DebugTimeout(7 , "7");
		
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		
		CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
		
		// need to 
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		ExpenseHelper.SetupCountryAndVendorData();
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(ViewType.country);
		*/ 
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
