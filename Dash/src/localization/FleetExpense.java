package localization;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Dash.BaseClass;
import org.testng.Assert;

public class FleetExpense extends BaseClass 
{

	// public static String kpiOneTitle = "";	
	public static List<String> tempList = new ArrayList<String>();
	public static String startsWith = "[es]";
	
	public static void selectLanguage(String language) throws Exception 
	{
		  String xpath = "//div[contains(text(),'Language')]/following-sibling::div/select";
		  WaitForElementPresent(By.xpath(xpath), ShortTimeout);
		  new Select(driver.findElement(By.xpath(xpath))).selectByValue(language);
	 }
	
	public static void KpiTitleTitle()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-kpi__title'])[1]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[2]");
		tempList.add("(//h3[@class='tdb-kpi__title'])[3]");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileRolling()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileThreeMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileSixMonth()
	{
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		VerifyTextXpath(tempList);
	}
	
	public static void TwoMainTitles()
	{
		tempList.clear();
		tempList.add("(//h2[@class='tdb-h2'])[1]");
		tempList.add("(//h2[@class='tdb-h2'])[2]");
		VerifyTextXpath(tempList);
	}

	
	public static void SubTitles()
	{
		tempList.clear();
		tempList.add("(//h3[@class='tdb-h3'])[1]");
		tempList.add("(//h3[@class='tdb-h3'])[2]");
		tempList.add("(//h3[@class='tdb-h3'])[3]"); // 2 occurs
		tempList.add("(//h3[@class='tdb-h3'])[4]"); // 2 occurs
		tempList.add("(//h3[@class='tdb-h3'])[5]");
		
		int[] intArr = new int[] {1, 1, 2, 2, 1}; // this is for the expected number of 'startsWith'. 
		
		VerifyTextXpathWithCount(tempList, intArr);
	}
	
	public static void TopSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.xpath("(//div[@class='tdb-card'])[2]/div[1]/div"));
		ShowWebElementListText(eleList);
	}
	
	
	public static void BottomSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(3)"));
		ShowWebElementListText(eleList);
	}
	
	
	// verify language text titles and 'Display' text.
	public static void VerifySomeTextInSettingsPanel() throws Exception
	{
		WaitForElementVisible(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)"), TenTimeout);
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)")).getText(), "Language:", "");
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(3)>div:nth-of-type(1)")).getText(), "Currency:", "");
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(4)")).getText(), "Display", "");
	}	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												Helpers
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyTextXpath(List<String> listIn)
	{
		for(String str : listIn)
		{
			ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
		}
	}
	
	public static void VerifyTextXpathWithCount(List<String> listIn, int[] arrIn)
	{
		int x = 0;
		
		for(String str : listIn)
		{
			ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
			
			GetNumberOf(driver.findElement(By.xpath(str)).getText());

			Assert.assertTrue(GetNumberOf(driver.findElement(By.xpath(str)).getText()) == arrIn[x], "Falied to find correct number of 'startsWith' string.");
			x++;
		}
	}
	
	// http://stackoverflow.com/questions/24288809/stringutils-countmatches-words-starting-with-a-string
	public static int GetNumberOf(String strIn) // common methods.
	{
		 Matcher matcher = Pattern.compile("\\[" + "es" + "\\]").matcher(strIn);
		
		int countr = 0;
		while (matcher.find()) 
		{
		    countr++;
		}	
		
		return countr;			
	}
	

}
