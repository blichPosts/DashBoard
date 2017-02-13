package helperObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class HierarchyHelper extends BaseClass {

	
	public final static int categoryTotal = 1;
	public final static int categoryOptimizable = 2;
	public final static int categoryRoaming = 3;

	public final static int treeMapChart = 0;
	public final static int expenseTrendingChart = 1;
	public final static int topTenChart = 0;
	
	public final static String directlyAllocated = "Direct Allocation";
	public final static String allocatedChildren = "Allocation to Dependent Units";
	
	
	// Select the "VIEW BY HIERARCHY" button
	public static void selectHierarchyView() throws Exception {

		WaitForElementClickable(By.cssSelector(".tdb-pov>div>a.tdb-button"), MediumTimeout, "VIEW BY HIERARCHY button not clickable");
		WebElement viewByHierarchyToggle = driver.findElement(By.cssSelector(".tdb-pov>div>a.tdb-button"));
		viewByHierarchyToggle.click();
		waitForKPIsToLoad();
		
	}

	
	public static void waitForKPIsToLoad() throws Exception{
		
		WaitForElementVisible(By.xpath("//h3[text()='Total Expense']"), MediumTimeout);		
		WaitForElementVisible(By.xpath("//h3[text()='Optimizable Expense']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Roaming Expense']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Cost per Service Number']"), MediumTimeout);
		
	}

	
	// Select the "VIEW TOP TEN" button
	public static void selectTopTenView() throws Exception {

		WaitForElementClickable(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(2)"), MediumTimeout, "VIEW TOP TEN button not clickable");
		WebElement viewTopTenToggle = driver.findElement(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(2)"));
		viewTopTenToggle.click();
		WaitForElementVisible(By.xpath("//h3[text()='Top 10 Service Numbers by Expense Amount - ']"), MediumTimeout);	
		
	}
	
	
	public static void selectCategory(int category){
				
		WebElement categoryToSelect = driver.findElement(By.cssSelector("div.tdb-boxSelector__option:nth-child(" + category + ")"));
		categoryToSelect.click();
		
	}
	
	
}
