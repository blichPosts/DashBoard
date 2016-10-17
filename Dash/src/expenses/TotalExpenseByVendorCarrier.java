package expenses;

import org.openqa.selenium.By;

import Dash.BaseClass;

public class TotalExpenseByVendorCarrier extends BaseClass
{
	public static String [] strArray; 
	
	public static void getAllLegends()
	{
		strArray = driver.findElement(By.xpath(".//*[@id='highcharts-0']/*/*[@class='highcharts-legend']")).getText().split("\n");
		
		ShowArray(strArray);
		
		
		// .//*[@id='highcharts-0']/*/*[@class='highcharts-legend']
	}
	
	
	
}
