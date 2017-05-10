package localization;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;

public class Hierarchy extends BaseClass 
{

	// public static String kpiOneTitle = "";	
	public static List<String> tempList = new ArrayList<String>();
	public static String chartId = "";
	public static String startsWith = "[es]";
	public static List<WebElement> eleList;	
	
	public static void selectLanguage(String language) throws Exception 
	{
		  String xpath = "//div[contains(text(),'Language')]/following-sibling::div/select";
		  WaitForElementPresent(By.xpath(xpath), ShortTimeout);
		  new Select(driver.findElement(By.xpath(xpath))).selectByValue(language);
	}	
	
	public static void KpiTitleTitleAndMainTitle()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-kpi__title'])[1]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[2]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[3]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[4]");
		VerifyTextXpath(tempList);
		
		// this is title above the KPIs.
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-kpi__header>span")).getText().startsWith(startsWith));
	}

	public static void KpiTileRolling()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[2]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[3]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[4]/h4");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileThreeMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[7]");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileSixMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[8]");
		VerifyTextXpath(tempList);
	}

	public static void KpiTileAllocation()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[1]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[2]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[3]");
		tempList.add("(//div[@class='tdb-kpi__statistic--secondary'])[4]");
		VerifyTextXpath(tempList);
	}
	
	public static void OddsAndEnds() throws Exception
	{
		// 'view by vendor link'
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith));
		
		// countries
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));
		
		// none text
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText().startsWith(startsWith));

		// month selector
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));

		// hierarchy text by pulldown -- NOTE!!! -when get tags see what comes back - are there tags in the pulldown. 
		//Assert.assertTrue(driver.findElement(By.csXXXXXXXXXXXXXXXXsSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));
		//ShowText(driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText());
		//String []  strArray = driver.findElement(By.xpath("//div[@class='tdb-space--top']")).getText().split("\n");
		//ShowArray(strArray);
		
		// dependent units.
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));

		// max displayed
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-text--xsmall.tdb-text--lighter")).getText().startsWith(startsWith));
		

		//ShowText(driver.findElement(By.cssSelector(".tdb-text--xsmall.tdb-text--lighter")).getText());
		
		// .tdb-text--xsmall.tdb-text--lighter
		// //div[@class='tdb-space--top']
		
	}	
	
	public static void TileMapText()
	{
	
		ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(1)")).getText());
		ShowText(driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div>div>h2:nth-of-type(2)")).getText());
		
	
	
	
	}
	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												Helpers 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyTextXpath(List<String> listIn)
	{
		for(String str : listIn)
		{
			ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
		}
	}
	
	public static void RunExpenseLocalizationTagTests() throws Exception
	{
		// KpiTitleTitleAndMainTitle();
		//KpiTileRolling();
		//KpiTileThreeMonth();
		//KpiTileSixMonth();
		//KpiTileAllocation();
		//OddsAndEnds();
		TileMapText();
		// miss step.
	}	
	
	
}
