package helperObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
		
		WaitForElementVisible(By.xpath("//h3[text()='Total Expense']"), MainTimeout);		
		WaitForElementVisible(By.xpath("//h3[text()='Optimizable Expense']"), MainTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Roaming Expense']"), MainTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Cost per Service Number']"), MainTimeout);
		
	}

	
	// Select the "VIEW TOP TEN" button
	public static void selectTopTenView() throws Exception {

		WaitForElementClickable(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(2)"), MediumTimeout, "VIEW TOP TEN button not clickable");
		WebElement viewTopTenToggle = driver.findElement(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(2)"));
		viewTopTenToggle.click();
		WaitForElementVisible(By.xpath("//h3[text()='Top 10 Service Numbers by Expense Amount - ']"), MediumTimeout);	
		waitForTopTenChartToLoad();
		
	}
	
	
	public static void selectCategory(int category){
				
		WebElement categoryToSelect = driver.findElement(By.cssSelector("div.tdb-boxSelector__option:nth-child(" + category + ")"));
		categoryToSelect.click();
		
	}
		
	
	public static void waitForTopTenChartToLoad() throws Exception {
		
		String cssSelector = "#" + UsageHelper.getChartId(HierarchyHelper.topTenChart) + ">svg>g>g>rect.highcharts-point:nth-child(1)";
		WaitForElementPresent(By.cssSelector(cssSelector), MediumTimeout);
		
	}
	
	
		
	// Get the list of Dependent Units listed on PoV
	public static List<WebElement> getDependentUnitsPoV() {
			
		return driver.findElements(By.cssSelector("li.tdb-pov__item"));
			
	}
	
	
	// Click on Dependent Unit on PoV
	public static void drillDownOnDependentUnitPoV(int numDepUnit) throws Exception {
			
		WaitForElementPresent(By.cssSelector("li.tdb-pov__item:nth-child(" + numDepUnit + ")"), MainTimeout);
		driver.findElement(By.cssSelector("li.tdb-pov__item:nth-child(" + numDepUnit + ")")).click();

	}
	
	
	// Get the list of hierarchies listed on dropdown
	public static List<WebElement> getHierarchiesFromDropdown() {
		
		return driver.findElements(By.cssSelector("app-hierarchy-selector>div>select>option"));
		
	}
	
	// Select a hierarchy on dropdown  
	public static void selectHierarchyFromDropdown(int numHierarchy) throws Exception {
		
		driver.findElement(By.cssSelector("app-hierarchy-selector>div>select>option:nth-child(" + numHierarchy + ")")).click();
		WaitForElementPresent(By.cssSelector("li.tdb-pov__item:nth-child(1)"), MainTimeout);

	}
	
	
	// It returns a list with the months listed in dropdown month selector in the format MMM yyyy. E.g.: May 2016
	public static List<String> getMonthsListedInDropdown() {
		
		List<WebElement> months = driver.findElements(By.cssSelector(".tdb-pov__monthPicker>div>select>option"));
		List<String> monthsListedInDropdown = new ArrayList<>();
		
		for (WebElement month: months) {
			
			monthsListedInDropdown.add(month.getText());
			
		}
		
		return monthsListedInDropdown;
		
	}

	
	// Returns a list of breadcrumbs
	public static List<WebElement> getBreadcrumbs() {
		
		return driver.findElements(By.cssSelector(".breadcrumbs>span>a"));
	}


	// Clicks on the selected breadcrumb to go up on hierarchy
	public static void clickOnBreadcrumb(int breadcrumbNum) throws Exception {

		driver.findElement(By.cssSelector(".breadcrumbs>span>a:nth-of-type(" + breadcrumbNum + ")")).click();
		WaitForElementPresent(By.cssSelector("li.tdb-pov__item:nth-child(1)"), MainTimeout);
		
	}
	
	
	public static void drillDownOnTreeMap() throws AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(treeMapChart);
		WebElement tile = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-series-group>g>g>rect:nth-child(1)"));
		Point p = GeneralHelper.getAbsoluteLocation(tile);
		
		int x_offset = tile.getSize().getHeight() / 2;
		int y_offset = tile.getSize().getWidth() / 2;
		
		int x = p.getX() + x_offset;
		int y = p.getY() + y_offset;
		
		Robot robot = new Robot();
		robot.mouseMove(x, y);
				
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		Thread.sleep(2000);
	}

	
}

