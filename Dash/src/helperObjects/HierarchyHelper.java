package helperObjects;



import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
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
	
	
	
	public static List<HierarchyTrendData> selectHierarchyViewAndGetData() throws Exception {
		
		WaitForElementClickable(By.cssSelector(".tdb-pov>div>a.tdb-button"), MediumTimeout, "VIEW BY HIERARCHY button not clickable");
		WebElement viewByHierarchyToggle = driver.findElement(By.cssSelector(".tdb-pov>div>a.tdb-button"));
		viewByHierarchyToggle.click();
		
		List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getJsonDataTrend(false);
		
		waitForKPIsToLoad();
		
		return valuesFromFile;
		
	}


	
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


	
	
	
	
	//        .tdb-pov__itemList>li.tdb-pov__item:nth-child(1)
	
	
	public static void drillDownOnPoV() throws AWTException, InterruptedException {
		
		List<WebElement> dependentUnitsPoV = driver.findElements(By.cssSelector(" .tdb-pov__itemList>li.tdb-pov__item"));
		
		dependentUnitsPoV.get(0).click();
		
		Thread.sleep(2000);
		
		
	}
	
	
	
	public static void drillDownOnHierarchy() throws AWTException, InterruptedException {
		
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


	public static void selectHierarchyFromDropdown(int numHierarchy) {
		
		driver.findElement(By.cssSelector("app-hierarchy-selector>div>select>option:nth-child(" + numHierarchy + ")")).click();

	}
	
	
}
