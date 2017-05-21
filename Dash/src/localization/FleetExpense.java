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
import expenseHierarchy.HierarchyNumbersDependents;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

import org.testng.Assert;

public class FleetExpense extends BaseClass 
{

	// public static String kpiOneTitle = "";	
	public static List<String> tempList = new ArrayList<String>();
	public static String chartId = "";
	public static String startsWith = "";
	public static String insideLanguageTag = "";
	public static List<WebElement> eleList;
	public static boolean rollingAverageVisible = false;
	public static boolean sixMonthVisible = false;
	public static boolean threeMonthVisible = false;
	public static boolean exceptionCaught = false;
	public static String expectedCurrency = "";
	public static String[] currentmonthYearFormat;
	

	
	
	public static void ExpenseTrendingLegendsDateTimeFormat()
	{
		for(int x = 2; x < 5; x++)
		{
			
			System.out.println("Run month/date date//month format for chart ID# " + x + " ----------------------");
			
			chartId = UsageHelper.getChartId(x);
			
			List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan")); 
			
			// ShowWebElementListText(eleList);
			
			for(WebElement ele : eleList)
			{
				if(startsWith.equals("[MDE]")) // month/year
				{
					VerifyMonthYear(ele.getText());
				}
				
				if(startsWith.equals("[ja]")) // year/month
				{
					VerifyYearMonth(ele.getText());
				}
			}
			
			ShowInt(eleList.size());
			
		}
	}

	
	public static void SetupLanguageTag(String languageTag) throws Exception 
	{
		startsWith = languageTag;
	}
	
	public static void SetupInsideTag(String insideTag) throws Exception 
	{
		insideLanguageTag = insideTag;
	}
	
	
	public static void SetCurrency(String currencyString) throws Exception 
	{
		expectedCurrency = currencyString;
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
		if(!rollingAverageVisible)
		{
			return;
		}
		// ShowText("Run Rolling");
		tempList.clear();
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[2]/h4");
		tempList.add("(//div[@class='tdb-kpi__averagesTitle'])[3]/h4");
		VerifyTextXpath(tempList);
	}
	
	public static void KpiTileThreeMonth()
	{
		if(!rollingAverageVisible)
		{
			return;
		}
		if(!threeMonthVisible)
		{
			return;
		}

		if(sixMonthVisible)
		{
			// ShowText("Run six month");
			tempList.clear();
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[5]");
			VerifyTextXpath(tempList);
		}
		else
		{
			// ShowText("Run three month");
			tempList.clear();
			tempList.add("(//div[@class='tdb-inlineBlock'])[1]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
			tempList.add("(//div[@class='tdb-inlineBlock'])[3]");
			VerifyTextXpath(tempList);
		}
	}
	
	public static void KpiTileSixMonth()
	{
		if(!rollingAverageVisible)
		{
			return;
		}
		if(!sixMonthVisible)
		{
			return;
		}
		ShowText("Run six month");
		tempList.clear();
		tempList.add("(//div[@class='tdb-inlineBlock'])[2]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[4]");
		tempList.add("(//div[@class='tdb-inlineBlock'])[6]");
		VerifyTextXpath(tempList);
	}
	
	public static void TwoMainTitles()
	{
		tempList.clear();
		//tempList.add("");
		tempList.add("(//h2[@class='tdb-h2'])[2]");
		VerifyTextXpath(tempList);
	
		if(startsWith.equals("[ja]"))
		{
			String []  strData = driver.findElement(By.xpath("//h2[@class='tdb-h2']")).getText().split(" ");
			Assert.assertTrue(strData[1].startsWith(startsWith));
		}
		else
		{
			////h2[@class='tdb-h2'])[2]
			Assert.assertTrue(driver.findElement(By.xpath("(//h2[@class='tdb-h2'])[1]")).getText().startsWith(startsWith));
		}
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
		// ShowWebElementListText(eleList);
		
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void BottomSelectors()
	{
		List<WebElement> eleList = driver.findElements(By.cssSelector(".tdb-card>div:nth-of-type(3)"));
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void VendorSpendLegends() throws Exception
	{
		chartId = UsageHelper.getChartId(1);
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
		// Pause("");
	}
	
	public static void AllLegends()
	{
		for (int x = 2; x < 5; x++)
		{
			chartId = UsageHelper.getChartId(x);
			ClearEleList();
			eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
			for(WebElement ele : eleList)
			{
				if(ele.getText().contains("Other"))
				{
					ShowText(ele.getText());
					if(startsWith.equals("[MDE]"))
					{
						Assert.assertTrue(ele.getText().startsWith("[de]"));
					}
					if(startsWith.equals("[ja]"))
					{
						Assert.assertTrue(ele.getText().startsWith(startsWith));
					}
				}
				else
				{
					Assert.assertTrue(!ele.getText().contains("[ja]"));
					Assert.assertTrue(!ele.getText().contains("[de]"));
				}
			}
		}
		
		//ShowWebElementListText(eleList);
		// VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void AllTrendLegends()
	{
		chartId = UsageHelper.getChartId(2); // expense ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));
		
		// ShowWebElementListText(eleList);  
		VerifyListOfElementsStartsWith(eleList);
		
		chartId = UsageHelper.getChartId(3); // cost per service numbers ----------------------------------------------------------------------------
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		//ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
		
		ShowText("Expense side tag " + driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText());
		
		
		chartId = UsageHelper.getChartId(4); // count of service numbers ---------------------------------------------------------------------------- 
		ClearEleList();
		eleList = driver.findElements(By.cssSelector("#" + chartId +  ">svg>g.highcharts-legend>g>g>g"));
		
		// text on side of trend
		Assert.assertTrue(driver.findElement(By.cssSelector("#" + chartId +  ">svg>g.highcharts-axis.highcharts-yaxis")).getText().startsWith(startsWith));		
		
		// ShowWebElementListText(eleList);
		VerifyListOfElementsStartsWith(eleList);
	}
	
	public static void OddsAndEnds() throws Exception
	{
		
		// 'view by hierarchy link' - this is already converted.
		// ShowText("-- " + driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText());
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).getText().startsWith(startsWith.toUpperCase()));

		
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-pov__heading")).getText());		
		// countries
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__heading")).getText().startsWith(startsWith));


		// none text
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText().startsWith(startsWith.toUpperCase()));
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).getText());
		
		// month selector
		Assert.assertTrue(driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText().startsWith(startsWith));
		//ShowText("-- " + driver.findElement(By.cssSelector(".tdb-pov__monthLabel")).getText());

		// below gets the text for vendor and country view strings.
		String [] strArray = driver.findElement(By.cssSelector(".md-tab-header")).getText().split("\n");
		
		for(String str : strArray)
		{
			Assert.assertTrue(str.startsWith(startsWith.toUpperCase()));
			// ShowText(str);
		}
	}
	


	
	// verify language text titles and 'Display' text.
	public static void VerifySomeTextInSettingsPanel() throws Exception
	{
		DebugTimeout(5, "Wait 5 in error spot");
		
		//WaitForElementVisible(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)"), TenTimeout);
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(2)>div:nth-of-type(1)")).getText(), "Language:", "");
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(3)>div:nth-of-type(1)")).getText(), "Currency:", "");
		//Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-slideout__body>div:nth-of-type(4)")).getText(), "Display", "");
	}	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												Helpers 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyYearMonth(String yearMonth)
	{
		// year/month is separated by "-". 
		String[] yearMonthArray = new String[]{yearMonth.split("-")[0],yearMonth.split("-")[1]}; 
		
		// year
		Assert.assertTrue(yearMonthArray[0].length() == 4); // size should be 4
		Assert.assertTrue(yearMonthArray[0].equals("2016") ||  yearMonthArray[0].equals("2017")); // should be 2016 or 2017 
		Assert.assertTrue(Integer.valueOf(yearMonthArray[0]) == 2016 ||  Integer.valueOf(yearMonthArray[0]) == 2017); // should be 2016 or 201t as integer also.
		
		
		// month
		Assert.assertTrue(yearMonthArray[1].length() == 2); // size should be 2
		Assert.assertTrue(Integer.valueOf(yearMonthArray[1]) < 13); // should be able to convert to an integer and 12 is the max month number.

		
	}

	public static void VerifyMonthYear(String Monthyear)
	{
		// /month/year is separated by "-". 
		String[] monthYearArray = new String[]{Monthyear.split("-")[0],Monthyear.split("-")[1]}; 

		// month
		Assert.assertTrue(monthYearArray[0].length() == 2); // size should be 2
		Assert.assertTrue(Integer.valueOf(monthYearArray[0]) < 13); // should be able to convert to an integer and 12 is the max month number.

		// year
		Assert.assertTrue(monthYearArray[1].length() == 4); // size should be 4
		Assert.assertTrue(monthYearArray[1].equals("2016") ||  monthYearArray[1].equals("2017")); // should be 2016 or 2017 
		Assert.assertTrue(Integer.valueOf(monthYearArray[1]) == 2016 ||  Integer.valueOf(monthYearArray[1]) == 2017); // should be 2016 or 201t as integer also.
	}

	
	
	
	public static void VerifyTextXpath(List<String> listIn)
	{
		for(String str : listIn)
		{
			// ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
		}
	}
	
	// send in the current month in the pull down list.
	// /////////////////////////////////////////////////////////////////////////////////////
	// this verifies the month selector month/date format is correct per the language.
	// it makes another call to verify the month/date format in the top chart.
	// /////////////////////////////////////////////////////////////////////////////////////
	public static void GetAndVerifyCurrentMonthYear(String textInMonthPulldown) throws Exception
	{ 
		String temp = "";
		String[] tempArr;
		exceptionCaught = false;		

		// ///////////////////////////////////////////////////////////////
		// get month/year or year/month stored away
		// ///////////////////////////////////////////////////////////////
		if(textInMonthPulldown .contains("["))
		{
			if(startsWith.equals("[MDE]"))
			{
				temp = textInMonthPulldown.split("]")[1];
				currentmonthYearFormat = new String[]{temp.split(" ")[0],temp.split(" ")[1]};
				ShowText("array zero " + currentmonthYearFormat[0]);
				ShowText("array one " + currentmonthYearFormat[1]);
				
			}
			if(startsWith.equals("[ja]"))
			{
				//tempArr = new String[]{temp.split(startsWith)[0],temp.split(startsWith)[1]};
				currentmonthYearFormat = new String[]{textInMonthPulldown.split("\\" + startsWith)[0].trim(),textInMonthPulldown.split("\\" + startsWith)[1].trim()};
				ShowText("array zero " + currentmonthYearFormat[0]);
				ShowText("array one " + currentmonthYearFormat[1]);
				
				//ShowText("zer " + fullFormat.split("\\" + startsWith)[0].trim());
				//ShowText("one " + fullFormat.split("\\" + startsWith)[1].trim());
				
			}
			
			//Pause("");
		}
		
		if(startsWith.equals("[ja]"))
		{
			// Japan has "yyyy mmm" format

			// verify element 0 (year) is an integer
			int tempInt= Integer.parseInt(currentmonthYearFormat[0]);


			// verify element 1 (month) is a string.
			try
			{
				tempInt = Integer.parseInt(currentmonthYearFormat[1]);
			}
			catch(Exception ex) // expected fail when try to convert string to integer.
			{
				if(!ex.getMessage().contains("For input string:"))
				{
					Assert.fail("It looks like the second item in 'currentMonthYearFormat' array is wrong type.");
				}
				exceptionCaught = true;
			}
			
			if(!exceptionCaught)
			{
				Assert.fail("It looks like the second item in 'currentmonthYearFormat' array is wrong type. Failed flag test.");			
			}
		}
		
		if(startsWith.equals("[MDE]"))
		{
			// German has "mmm yyyy" format

			// verify element 1 (year) is an integer
			int tempInt= Integer.parseInt(currentmonthYearFormat[1]);

			// verify element 0 (month) is a string.
			try
			{
				tempInt = Integer.parseInt(currentmonthYearFormat[0]);
			}
			catch(Exception ex) // expected fail when try to convert string to integer.
			{
				exceptionCaught = true;
				if(!ex.getMessage().contains("For input string:"))
				{
					Assert.fail("It looks like the second item in 'currentmonthYearFormat' array is wrong type.");
				}
			}

			if(!exceptionCaught)
			{
				Assert.fail("It looks like the second item in 'currentmonthYearFormat' array is wrong type. Failed flag test.");			
			}
		}


		
		VerifyMonthYearFormatInTitle();
	}
	
	public static void VerifyMonthYearFormatInTitle() throws Exception
	{
		String temp = "";
		String [] arr = {};
		
		// see if this is a localization tag test.
		if(driver.findElement(By.xpath("(//div[@class='tdb-EXPENSE__NORMAL-VIEW']/div/div/h2)[1]")).getText().contains("]"))
		{
			// get month/date - date/month from title. 	// German has "mmm yyyy" format
			if(startsWith.equals("[MDE]"))
			{
				temp = driver.findElement(By.xpath("(//div[@class='tdb-EXPENSE__NORMAL-VIEW']/div/div/h2)[1]")).getText().split("]")[1];
				arr = new String[]{temp.split(" ")[0],temp.split(" ")[1]};	
				ShowText(arr[0]);
				ShowText(arr[1]);
			}
			
			if(startsWith.equals("ja"))
			{
				temp = driver.findElement(By.xpath("(//div[@class='tdb-EXPENSE__NORMAL-VIEW']/div/div/h2)[1]")).getText();

				temp.split("\\" + startsWith)[0].trim();
				temp.split("\\" + startsWith)[1].trim();
				
				arr = new String[]{temp.split(" ")[0],temp.split(" ")[1]};	
				ShowText(arr[0]);
				ShowText(arr[1]);
			}
			
			if(startsWith.equals("MDE"))
			{
				// German has "mmm yyyy" format

				// verify element 1 (year) is an integer
				int tempInt= Integer.parseInt(arr[1]);

				// verify element 0 (month) is a string.
				try
				{
					tempInt = Integer.parseInt(arr[0]);
				}
				catch(Exception ex) // expected fail when try to convert string to integer.
				{
					if(!ex.getMessage().contains("For input string:"))
					{
						Assert.fail("It looks like the second item in 'currentmonthYearFormat' array is wrong type.");
					}
				}				
			}	
			
			if(startsWith.equals("ja"))
			{
				// Japan has "yyyy mmm" format

				// verify element 1 (year) is an integer
				int tempInt= Integer.parseInt(arr[0]);

				// verify element 0 (month) is a string.
				try
				{
					tempInt = Integer.parseInt(arr[1]);
				}
				catch(Exception ex) // expected fail when try to convert string to integer.
				{
					if(!ex.getMessage().contains("For input string:"))
					{
						Assert.fail("It looks like the second item in 'currentmonthYearFormat' array is wrong type.");
					}
				}				
			}	
			
		}
	}
	
	public static void VerifyTextXpathWithCount(List<String> listIn, int[] arrIn)
	{
		int x = 0;
		
		for(String str : listIn)
		{
			// ShowText(driver.findElement(By.xpath(str)).getText());
			Assert.assertTrue(driver.findElement(By.xpath(str)).getText().startsWith(startsWith));
			
			GetNumberOf(driver.findElement(By.xpath(str)).getText());

			Assert.assertTrue(GetNumberOf(driver.findElement(By.xpath(str)).getText()) == arrIn[x], "Falied to find correct number of 'startsWith' string.");
			x++;
		}
	}
	
	// this verifies list of web elements text sent in start with variable 'startsWith'.
	public static void VerifyListOfElementsStartsWith(List<WebElement> listIn)
	{
		for(WebElement ele : listIn)
		{
			Assert.assertTrue(ele.getText().startsWith(startsWith));
		}
	
	}
	
	// http://stackoverflow.com/questions/24288809/stringutils-countmatches-words-starting-with-a-string
	public static int GetNumberOf(String strIn) // common methods.
	{
		 Matcher matcher = Pattern.compile("\\[" + insideLanguageTag + "\\]").matcher(strIn);
		
		int countr = 0;
		while (matcher.find()) 
		{
		    countr++;
		}	
		
		return countr;			
	}
	
	public static void ClearEleList() 
	{
		if(eleList != null)
		{
			eleList.clear();
		}
	}
	
	
	public static void LoopThroughMonths() throws Exception
	{
		CommonTestStepActions.initializeMonthSelector();
		
		// ShowInt(CommonTestStepActions.webListPulldown.size());
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			ShowText("Selected Month is " + ele.getText() + " ****************************************************");

			HierarchyHelper.WaitForProgressBarInactive(ExtremeTimeout);
			
			Thread.sleep(3000);

			GetAndVerifyCurrentMonthYear(ele.getText());
			
			AllLegends(); // this verifies the legends have no language tag except 'other'.
			
			InitVisibilityTileMapAverages(); // find what is showing in the KPI tiles and set booleans.
			
			if(Integer.parseInt(driver.findElement(By.xpath("(//div[@class='tdb-kpi__footerItem'])[1]/span[1]")).getText()) > 0)
			{
				ShowText("Month " + ele.getText() + " - running test with data. ");
				ExpenseTrendingLegendsDateTimeFormat(); // good
				RunExpenseLocalizationTagTests();		
			}
			else
			{
				ShowText("Month " + ele.getText() + " - no data test being run. ");
				RunExpenseLocalizationNoDataTagTests();
			}
			
			ShowText("Month Complete.");
			// Pause("Check Flags.");
		}
		
	}
	
	public static void CurrencySection() throws Exception
	{
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[1]/span[2]")).getText());
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[1]")).getText());
		//ShowText(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[3]")).getText());		
		
		// accounts loaded
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[1]/span[2]")).getText().startsWith(startsWith));
		
		// currency text.
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[1]")).getText().startsWith(startsWith));

		// actual currency.
		Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='tdb-flexContainer']/div)[1]/div[2]/span[3]")).getText().startsWith(startsWith));
	}
	
	public static void Currency() throws Exception
	{
		//(//span[@class='tdb-kpi__footerItem--value'])[2]
		
		Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='tdb-kpi__footerItem--value'])[2]")).getText(), expectedCurrency,"");
	}
	
	public static void InitVisibilityTileMapAverages() throws Exception
	{
		rollingAverageVisible = false;
		threeMonthVisible = false;
		sixMonthVisible = false;
		
		rollingAverageVisible = WaitForElementPresentNoThrow(By.xpath("(//div[@class='tdb-kpi__averagesTitle'])[1]/h4"), MiniTimeout);
		
		if(!rollingAverageVisible)
		{
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 6)
		{
			threeMonthVisible = true;
			sixMonthVisible = true;
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
			return;
		}
		
		if(driver.findElements(By.xpath("(//div[@class='tdb-inlineBlock'])")).size() == 3)
		{
			threeMonthVisible = true;
			//System.out.println(rollingAverageVisible);
			//System.out.println(threeMonthVisible);
			//System.out.println(sixMonthVisible);
			return;
		}
	}
	
	public static void VerifyCurrency()
	{

		
		
	}
	
	public static void RunExpenseLocalizationTagTests() throws Exception
	{
		// TITLE !!!!! ACCOUNTS LOADED ----- Currency: USD (United States Dollar) 
		FleetExpense.KpiTitleTitle();
		FleetExpense.KpiTileRolling();
		FleetExpense.KpiTileThreeMonth();
		FleetExpense.KpiTileSixMonth();
		FleetExpense.TwoMainTitles(); // bladdxx
		FleetExpense.SubTitles();
		// Above are complete.
		
		FleetExpense.TopSelectors();
		FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();
		FleetExpense.VendorSpendLegends();
		//FleetExpense.AllTrendLegends();
		FleetExpense.OddsAndEnds();
		Currency();
		CurrencySection();
	}
	
	// only test what's showing when no data is available.
	public static void RunExpenseLocalizationNoDataTagTests() throws Exception
	{
		// TITLE !!!!! ACCOUNTS LOADED ----- Currency: USD (United States Dollar) 
		FleetExpense.KpiTitleTitle();
		FleetExpense.KpiTileRolling();
		FleetExpense.KpiTileThreeMonth();
		FleetExpense.KpiTileSixMonth();
		FleetExpense.TwoMainTitles(); //  bladdxx
		FleetExpense.SubTitles();
		Currency();
		CurrencySection();
		
		FleetExpense.TopSelectors();
		FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();  // --------------- comment
		FleetExpense.VendorSpendLegends();
		//FleetExpense.AllTrendLegends(); // ------------ comment
		FleetExpense.OddsAndEnds(); // -------------- comment
	}
	
}
