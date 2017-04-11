package helperObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
		WaitForElementVisible(By.xpath("//h3[contains(text(), 'Optimizable Expense')]"), MainTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Roaming Expense']"), MainTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Cost per Service Number']"), MainTimeout);
		
	}

	
	// Select the "VIEW TOP TEN" button
	public static void selectTopTenView(int toggleNum) throws Exception {

		WaitForElementClickable(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(" + toggleNum + ")"), MediumTimeout, "VIEW TOP TEN button not clickable");
		WebElement viewTopTenToggle = driver.findElement(By.cssSelector("div.tdb-dashboardToggle__option:nth-child(" + toggleNum + ")"));
		viewTopTenToggle.click();
		WaitForElementVisible(By.xpath("//h3[text()='Top 10 Service Numbers by Expense Amount - ']"), MediumTimeout);	
		//waitForTopTenChartToLoad();
		
	}
	

	// *** NOT WORKING FINE, REPLACE BY THE TWO METHODS BELOW .. SEE IF IT NEEDS TO BE ADDED BACK FOR OTHER TEST
	
//	public static void selectCategory(int chartNum, int category){
		
//		String xpath = "//div[@class='tdb-card'][" + Integer.toString(chartNum+1) + "]/div/div[" + Integer.toString(category) + "]"; 
		
//		String xpath = "//div[@class='tdb-card'][2]/div/div[1]";
//		
//		System.out.println("xpath: " + xpath);
//		driver.findElement(By.xpath(xpath)).click();
		
//		driver.findElement(By.xpath("//div[@class='tdb-card'][2]/div/div[1]")).click();
		
//		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-card>div>div.tdb-inlineBlock:nth-child(" + category + ")"));
//		WebElement categoryToSelect = driver.findElement(By.xpath("//div[@class='tdb-card'][" + (chartNum+1) + "]/div/div[" + category + "]"));
//		categoryToSelect.click();
		
//	}
		
	
	
	public static void selectCategory(int section, int category){
		
		String sectionToSelect = "";
		if(section == 0) sectionToSelect = "odd"; 
		if(section == 1) sectionToSelect = "even";
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(" + sectionToSelect + ")>div:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}
	

	public static void selectCategoryTopTen(int section, int category){
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-boxSelector.tdb-align--right>div:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}
	
	
	
	// Wait until the tile map is displayed, or until the "..no dependents units.." message is displayed 
	public static void waitForTileMapToBeDisplayed() throws Exception {
		
		try {
			
			String chartCss = "#" + UsageHelper.getChartId(0) + ">svg>g.highcharts-series-group";
			WaitForElementPresent(By.cssSelector(chartCss), MediumTimeout);
			
		} catch (NoSuchElementException e) {
			
			try {
				
				String messageCss = ".tdb-charts__contentMessage";
				WaitForElementPresent(By.cssSelector(messageCss), ShortTimeout);
				
			} catch (NoSuchElementException e2) {
				
				ShowText("Tile Map chart not displayed. 'No dependent units' message not displayed either.");
				
			}
		}
		
	}
	
	
	public static void waitForChartToLoad(int chartId) throws Exception {
		
		String cssSelector = "#" + UsageHelper.getChartId(chartId) + ">svg>g>g.highcharts-series";    // >svg>g>g>rect.highcharts-point:nth-child(1)";
		WaitForElementPresentNoThrow(By.cssSelector(cssSelector), MainTimeout);
		
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
	
	
	// Get the Dependent Unit's name from PoV
	public static String getDependentUnitNamePoV(int numDepUnit) throws Exception {
			
		WaitForElementPresent(By.cssSelector("li.tdb-pov__item:nth-child(" + numDepUnit + ")>a"), MainTimeout);
		return driver.findElement(By.cssSelector("li.tdb-pov__item:nth-child(" + numDepUnit + ")>a")).getText().trim();

	}
	
	
	// Get the list of hierarchies listed on dropdown
	public static List<WebElement> getHierarchiesFromDropdown() {
		
		return driver.findElements(By.cssSelector("app-hierarchy-selector>div>select>option"));
		
	}
	
	
	public static List<String> getHierarchiesValues() {
		
		List<String> hierarchyIds = new ArrayList<>();
		List<WebElement> hierarchyValues = driver.findElements(By.cssSelector("app-hierarchy-selector>div>select>option"));
		
		for (WebElement h: hierarchyValues) {
			
			hierarchyIds.add(h.getAttribute("value"));
		}
		
		return hierarchyIds;
		
	}
	
	
	// Select a hierarchy on dropdown  
	public static void selectHierarchyFromDropdown(int numHierarchy) throws Exception {
		
		driver.findElement(By.cssSelector("app-hierarchy-selector>div>select>option:nth-child(" + numHierarchy + ")")).click();
		
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


	public static String getHierarchySelected() {
		
		return new Select(driver.findElement(By.cssSelector("app-hierarchy-selector>div>select"))).getFirstSelectedOption().getText();
		
	}


	public static String getDependentUnitOnTitle() {
		
		return driver.findElement(By.cssSelector(".tdb-kpi__header>span>span")).getText().trim();
		
	}

	
	// Get the latest added breadcrumb
	public static String getLastAddedBreadcrumb() throws Exception {

		WaitForElementPresent(By.cssSelector(".breadcrumbs"), MainTimeout);
		return driver.findElement(By.cssSelector(".breadcrumbs>span:last-child")).getText().replace("/", "").trim();

	}

	
}

