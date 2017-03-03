package expenseHierarchy;

import org.openqa.selenium.By;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;

public class HierarchyTopTenActions extends BaseClass {


	public static void verifyTopTenLabelCategory(int chartId, int category) throws Exception {
		
		HierarchyHelper.selectCategoryTopTen(chartId, category);
		
		// Wait for the data to be updated on chart
		HierarchyHelper.waitForTopTenChartToLoad();
		Thread.sleep(2000);
		
		String labelFound = driver.findElement(By.cssSelector("h3.tdb-h3>span")).getText().trim();
		String labelExpected = "";
		
		switch (category) {
		
			case HierarchyHelper.categoryTotal:
				labelExpected = "Total Expenses";
				break;
		
			case HierarchyHelper.categoryOptimizable:
				labelExpected = "Optimizable Expenses";
				break;
		
			case HierarchyHelper.categoryRoaming:
				labelExpected = "Roaming Expenses";
				break;
			
		}
		
		System.out.println("labelFound: " + labelFound + ", labelExpected: " + labelExpected);
		Assert.assertEquals(labelFound, labelExpected);
		
	}

	
	public static void verifyMonthYearOnTitle() {
		
		String monthYearSelected = CommonTestStepActions.GetPulldownTextSelected();
		String monthYearTitle = driver.findElement(By.cssSelector("h1")).getText().split("~")[0].trim();
		
		System.out.println("monthYearSelected: " + monthYearSelected + ", monthYearTitle: " + monthYearTitle);
		Assert.assertEquals(monthYearSelected, monthYearTitle);
		
	}


	public static void verifyHierarchyNameOnTitle() {

		String hierarchySelected = HierarchyHelper.getHierarchySelected();
		String hierarchyTitle = driver.findElement(By.cssSelector("h1")).getText().split("~ Hierarchy:")[1].trim();
		
//		System.out.println("hierarchySelected: " + hierarchySelected + ", hierarchyTitle: " + hierarchyTitle);
		Assert.assertEquals(hierarchySelected, hierarchyTitle);
		
	}


	public static void verifyDependentUnitOnTitleAndBreadcrumbs(int numDepUnit) throws Exception {
		
		String depUnitText = HierarchyHelper.getDependentUnitNamePoV(numDepUnit);
		String depUnitInTitlePrevious = HierarchyHelper.getDependentUnitOnTitle();	
		
		HierarchyHelper.drillDownOnDependentUnitPoV(numDepUnit);
		Thread.sleep(2000);
		
		String depUnitInTitle = HierarchyHelper.getDependentUnitOnTitle();
		String depUnitBreadcrumb = HierarchyHelper.getLastAddedBreadcrumb();
				
		System.out.println("depUnitText: " + depUnitText + ", depUnitInTitle: " + depUnitInTitle);
		Assert.assertEquals(depUnitText, depUnitInTitle);
		
		
		System.out.println("depUnitInTitlePrevious: " + depUnitInTitlePrevious + ", depUnitBreadcrumb: " + depUnitBreadcrumb);
		Assert.assertEquals(depUnitInTitlePrevious, depUnitBreadcrumb);
		
	}
	
	
	
}
