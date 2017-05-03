package localization;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class ClassOne extends BaseClass 
{

	// open setting panel in the top rigth corner.
	public static void OpenSeetingsPanel()
	{
		WaitForElementClickable(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]"), MediumTimeout, "");
		driver.findElement(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]")).click();
	}
	
	public static void VerifyTextInSettingsPanel()
	{
		// List<WebElement> settingsList = driver.findElements(By.cssSelector(".tdb-slideout__body>div"));
		
		
		// .tdb-slideout__body>div:nth-of-type(1) -- Localization
		// .tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)  - Language: tect
		
		// .tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(2) -- list of language pulldown.
		
		// .tdb-slideout__body>div:nth-of-type(3)>div:nth-of-type(1) -- Currency: text
		
		
		
		
		// ShowWebElementListText(settingsList);
		//for(int x = 0; x < settingsList.size(); x++)
		//{
		//	ShowInt(x);
		//	ShowText(settingsList.get(x).getText());
		//}
		
		
		
		// .tdb-slideout__body>div>div:nth-of-type(1)
	}
	
	
}
