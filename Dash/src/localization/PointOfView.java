package localization;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;


public class PointOfView extends BaseClass{

	
	public static void verifyLocalizationOnPoVSection(String languageTag) throws Exception {
		
		String monthTitle = driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText();
		
		ShowText("monthTitle: " + monthTitle);
		
		Assert.assertTrue(monthTitle.startsWith(languageTag));
		
		List<WebElement> monthsInSelector = driver.findElements(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select>option"));
		
		for (WebElement m: monthsInSelector) {
			
			ShowText("month: " + m.getText());
			
			Assert.assertTrue(m.getText().contains(languageTag));
			
		}
		
		if (WaitForElementPresentNoThrow(By.cssSelector(".tdb-button.tdb-button--flat"), MiniTimeout)) {
			
			String labelViewByHierarchy = driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText();
		
			ShowText("labelViewByHierarchy: " + labelViewByHierarchy);
			
			Assert.assertTrue(labelViewByHierarchy.startsWith(languageTag.toUpperCase()));
			
		}
		
		String countriesLabel = driver.findElement(By.cssSelector(".tdb-pov__heading")).getText();
		
		ShowText("countriesLabel: " + countriesLabel);
		
		Assert.assertTrue(countriesLabel.startsWith(languageTag));
		
		String noneAllLabel = driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText();
		
		ShowText("noneAllLabel: " + noneAllLabel);
		
		Assert.assertTrue(noneAllLabel.startsWith(languageTag.toUpperCase()));
		
		
		
	}
	
}
