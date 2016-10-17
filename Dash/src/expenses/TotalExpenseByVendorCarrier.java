package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.sun.jna.StringArray;

import Dash.BaseClass;

public class TotalExpenseByVendorCarrier extends BaseClass
{
	public static String [] legendsArray;
	public static List<String> legendsList = new ArrayList<>();
	public static int numPieParts = 0;
	public static int maxNumLegends = 6;	
	public static String otherString = "Other";
	
	public static void setAllLegendsAndPieCount()
	{
		// store all legend names.
		legendsArray= driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[8]")).getText().split("\n");

		// store number of sections in the pie.
		numPieParts = driver.findElements(By.xpath(".//*[@id='highcharts-0']/*/*/*[@class='highcharts-series highcharts-series-0 highcharts-tracker']/*")).size();

		// put legend array into list.
		for (String str : legendsArray)
		{
			legendsList.add(str);
		}

	}
	
	public static void VerifyLegendsAndPieCount()
	{
		setAllLegendsAndPieCount();
		System.out.println(numPieParts);
		
		if(numPieParts <= 5)
		{
			
		}
		else
		{
			Assert.assertTrue(legendsList.contains(otherString), "");
			Assert.assertTrue(numPieParts == maxNumLegends, "");
		}
		
		
	}
	
}
