package TestSuite1;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
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
		List<WebElement> ctryList = driver.findElements(By.cssSelector(".tdb-povGroup__Label--subhead"));
		
		// get all vendors
		List<WebElement> vndrList = driver.findElements(By.cssSelector(".md-checkbox-label"));	
		
		
		int y = 0;
		int numberOfVendorsInCountry = 0;

		// got through the countries
		for(int x = 1; x <= ctryList.size(); x++)
		{
			// create country object and add to list.
			Country tmpCountry = new Country(ctryList.get(x-1).getText()); 
			countryList.add(tmpCountry);
			
			// add the vendors to the country just added to the list by getting  the number of vendors for the country and add that number of vendors from the vendor list.
			numberOfVendorsInCountry = driver.findElements(By.xpath("(//div/div[@class='tdb-povGroup__Label--subhead'])[" + x + "]/following-sibling ::div/div")).size();
			
			Assert.assertTrue(numberOfVendorsInCountry > 0, "Found a country with no vendors.");
			
			for(int z = 0; z < numberOfVendorsInCountry; z++)
			{
				tmpCountry.AddToVendorList(vndrList.get(y).getText());
				y++;
			}
		}
		
		// ShowCountryVendorList(); 
		
		// verify the countries and the vendors for each country are in alphabetical order.
		VerifyCountryAndVendorListSorted();
		
		DebugTimeout(9999, "DONE");
		
		
		// for(int x = 0; x < webEleList.size(); x++){DebugTimeout(0, webEleList.get(x).getText());}
		
		
		// countries - vendors -- find how many check boxes under each
		
		// this is num under country
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div
		
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div/descendant ::input
		// select checkbox -- //input[@id='input-md-checkbox-10']
		
		// this tells how many check boxes under a country.  
		// (//div/div[@class='tdb-povGroup__Label--subhead'])[8]/following-sibling ::div/div
		
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
