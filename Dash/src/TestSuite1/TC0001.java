package TestSuite1;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.Country;


public class TC0001 extends BaseClass
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void tc0001()throws Exception
	{
		WaitForExpensePageLoad();
		SelectExpenseTab();
		
		// get all countries
		List<WebElement> webEleList = driver.findElements(By.cssSelector(".tdb-povGroup__Label--subhead"));
		
		for(int x = 1; x <= webEleList.size(); x++)
		{
			//System.out.println(webEleList.get(x-1).getText());
			//System.out.println(driver.findElements(By.xpath("(//div/div[@class='tdb-povGroup__Label--subhead'])[" + x + "]/following-sibling ::div/div")).size());
			Country tmpCountry = new Country(webEleList.get(x-1).getText());
			countryList.add(tmpCountry);
		}
		
		ShowCountryVendorList();
		
		
		// for(int x = 0; x < webEleList.size(); x++){DebugTimeout(0, webEleList.get(x).getText());}
		
		
		// countries - vendors -- find how many check boxes under each
		
		// this is num under country
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div
		
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div/descendant ::input
		// select checkbox -- //input[@id='input-md-checkbox-10']
		
		// this tells how many check boxes under a country.  
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div
		
		
		DebugTimeout(9999, "DONE");
		
		//select
		//GoToOrderStatus();
		// DebugTimeout(9999, "Freeze");

		
		//loginAsAdmin();
		//GoToOrderStatus();
		// DebugTimeout(9999, "Freeze");

	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}
}
