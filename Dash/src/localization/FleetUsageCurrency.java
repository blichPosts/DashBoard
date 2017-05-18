package localization;

import org.testng.Assert;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Dash.BaseClass;
import helperObjects.Currencies;
import helperObjects.Currency;

public class FleetUsageCurrency extends BaseClass {

	
	public static void verifyCurrencyKpis(String currencySelected){
		
		Currency currency = Currencies.currenciesMap.get(currencySelected);
		
		String expectedSymbol = currency.getCurrencySymbol();
		String expectedCode = currency.getCurrencyCode();
		String expectedName = currency.getCurrencyName();
		
		// Verify symbol and currency's name below KPIs 
		
		String currencyCode = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(3).getText();
		String currencyName = driver.findElements(By.cssSelector(".tdb-kpi__footerItem>span")).get(4).getText();
		
		Assert.assertEquals(currencyCode, expectedCode);
		Assert.assertEquals(currencyName, expectedName);
		
		
		// Verify symbol on KPI's main value ('Total Expense' and 'Cost per Service Number' KPIs)
		
		String symbolTotalExpenseKpi = driver.findElement(By.xpath("//h3[text()='Total Expense']/following-sibling::div/div[@class='tdb-kpi__statistic']")).getText();
		String symbolCostServNumberKpi = driver.findElement(By.xpath("//h3[text()='Cost per Service Number']/following-sibling::div/div[@class='tdb-kpi__statistic']")).getText();
		
		Assert.assertTrue(symbolTotalExpenseKpi.startsWith(expectedSymbol));
		Assert.assertTrue(symbolCostServNumberKpi.startsWith(expectedSymbol));
	
		
		// Verify symbol on rolling averages values
		
		List<WebElement> rollingAvgs = driver.findElements(By.xpath("//div[@class='tdb-inlineBlock']/following-sibling::div"));
		
		Assert.assertTrue(rollingAvgs.get(0).getText().startsWith(expectedSymbol));
		Assert.assertTrue(rollingAvgs.get(1).getText().startsWith(expectedSymbol));
		Assert.assertTrue(rollingAvgs.get(4).getText().startsWith(expectedSymbol));
		Assert.assertTrue(rollingAvgs.get(5).getText().startsWith(expectedSymbol));
		
	}

	
	// Selects the specified currency on Settings
	public static void selectCurrency(String currency) throws Exception {
		
		String xpath = "//div[contains(text(),'Currency')]/following-sibling::div/select";
		WaitForElementPresent(By.xpath(xpath), ShortTimeout);
		
		Select languageSelector = new Select(driver.findElement(By.xpath(xpath)));
		languageSelector.selectByValue(currency);
		
	} 


	
	
}
