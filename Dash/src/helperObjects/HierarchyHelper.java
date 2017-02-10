package helperObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class HierarchyHelper extends BaseClass {

	
	// Select the "VIEW BY HIERARCHY" button
	public static void selectHierarchyView() throws Exception {

		WaitForElementClickable(By.cssSelector(".tdb-pov>div>a.tdb-button"), MediumTimeout, "VIEW BY HIERARCHY button not clickable");
		WebElement viewByHierarchybutton = driver.findElement(By.cssSelector(".tdb-pov>div>a.tdb-button"));
		viewByHierarchybutton.click();
		waitForKPIsToLoad();
		
	}

	
	public static void waitForKPIsToLoad() throws Exception{
		
		WaitForElementVisible(By.xpath("//h3[text()='Total Expense']"), MediumTimeout);		
		WaitForElementVisible(By.xpath("//h3[text()='Optimizable Expense']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Roaming Expense']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//h3[text()='Cost per Service Number']"), MediumTimeout);
		
	}
	
	
}
