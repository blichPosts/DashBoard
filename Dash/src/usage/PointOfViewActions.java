package usage;

import org.testng.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Dash.BaseClass;


public class PointOfViewActions extends BaseClass{

	
	public static void verifyMonthLabel() throws InterruptedException {
			
		WebElement leftArrow = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>div:nth-of-type(1)"));
		
		// month and year from Month selector
		String monthYearSelector;		
		String monthLabelGeneral;
		String monthLabelComparisonBox;
		
		
		for(int i = 0; i < 13; i++){
			
			monthYearSelector = new Select(driver.findElement(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"))).getFirstSelectedOption().getText();
			monthLabelGeneral = driver.findElement(By.cssSelector(".tdb-currentContextMonth>h1")).getText();
			monthLabelComparisonBox = driver.findElement(By.xpath("//div[@class='tdb-currentCharts-USAGE']/preceding-sibling::h2[@class='tdb-h2']")).getText();
			
			Assert.assertTrue(monthLabelGeneral.startsWith(monthYearSelector));
			Assert.assertEquals(monthLabelComparisonBox, monthYearSelector);
			
			leftArrow.click();
			Thread.sleep(1000);
			
		}
		
	}
	
	

}
 