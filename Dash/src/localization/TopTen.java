package localization;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.HierarchyHelper;
import helperObjects.UsageHelper;

public class TopTen extends BaseClass {

	
	public static void verifyLocalizationFleetTopTenWithTags(String languageTag) {
		
		
		String baseXpath = "//div[@class='tdb-TOPUSERS']/div[@class='tdb-card']";
		
		
		for (int num = 1; num <= 3; num++) {
			
			// Verify chart's title
			
			String xpathTitleChart = baseXpath + "[" + num + "]/h3";
			
			String expectedTitleChart = driver.findElement(By.xpath(xpathTitleChart)).getText();
			
			ShowText("expectedTitleChart: " + expectedTitleChart);
			
			Assert.assertTrue(expectedTitleChart.startsWith(languageTag));
			
			// Verify category next to chart's title  
			
			String expectedCategorySelected = driver.findElement(By.xpath(xpathTitleChart + "/span")).getText();
			
			ShowText("expectedCategorySelected: " + expectedCategorySelected);
			
			Assert.assertTrue(expectedCategorySelected.startsWith(languageTag));
		
			// Verify axis label's text
			
			String expectedAxisLabelPartOne = driver.findElement(By.xpath(baseXpath + "[" + num + "]/div[@class='tdb-h3']")).getText().split(" ")[0];
			String expectedAxisLabelPartTwo = driver.findElement(By.xpath(baseXpath + "[" + num + "]/div[@class='tdb-h3']")).getText().split(" ")[1];
			
			ShowText("expectedAxisLabelPartOne: " + expectedAxisLabelPartOne);
			ShowText("expectedAxisLabelPartTwo: " + expectedAxisLabelPartTwo);
			
//			Assert.assertTrue(expectedAxisLabelPartOne.startsWith(languageTag));
//			Assert.assertTrue(expectedAxisLabelPartTwo.startsWith(languageTag));
			
			// Verify caption below chart
			
			String xpathCaption = baseXpath + "[" + num + "]/div[contains(@class,'tdb-caption')]";
			
			String expectedCaptionText = driver.findElement(By.xpath(xpathCaption)).getText();
			
			ShowText("expectedCaptionText: " + expectedCaptionText);
			
			Assert.assertTrue(expectedCaptionText.startsWith(languageTag));
			
		}
		
		
		// Verify "Zero use devices"
		
		String expectedTitleZeroUse = driver.findElements(By.cssSelector("h3")).get(3).getText(); 
		
		ShowText("expectedTitleZeroUse: " + expectedTitleZeroUse);
		
		Assert.assertTrue(expectedTitleZeroUse.startsWith(languageTag));
		
		String expectedTextZeroUseLabel = driver.findElement(By.xpath("//h3/following-sibling::div[contains(@class, 'tdb-align--center')]")).getText();
		
		ShowText("expectedTextZeroUseLabel: " + expectedTextZeroUseLabel);
		
		Assert.assertTrue(expectedTextZeroUseLabel.contains(languageTag + "Service Numbers"));
		
		String expectedTextZeroUseButton = driver.findElement(By.xpath("//div/button[@class='tdb-button--raised']")).getText();
		
		ShowText("expectedTextZeroUseButton: " + expectedTextZeroUseButton);
		
		Assert.assertTrue(expectedTextZeroUseButton.startsWith(languageTag.toUpperCase()));
		
		
		// Verify category selector labels
		
		for (int num = 1; num <= 4; num++) {
			
			String xpathSelectors = "//div[@class='tdb-card'][" + num + "]/div[1]/div";
			
			List<WebElement> selectors = driver.findElements(By.xpath(xpathSelectors));
			
			for (int numSel = 1; numSel <= selectors.size(); numSel++) {
				
				String expectedSelectorLabel = driver.findElement(By.xpath(xpathSelectors + "[" + numSel + "]")).getText();
				
				ShowText("expectedSelectorLabel: " + expectedSelectorLabel);
				
				Assert.assertTrue(expectedSelectorLabel.startsWith(languageTag));
								
			}
			
		}
		
	}


	public static void verifyLocalizationHierarchyTopTenWithTags(String languageTag) {
		
		
		String baseXpath = "//div[@class='tdb-TOPUSERS']/div[@class='tdb-card']";
			
	
		// Verify chart's title
		
		String xpathTitleChart = baseXpath + "[1]/h3";
		
		String expectedTitleChart = driver.findElement(By.xpath(xpathTitleChart)).getText();
		
		ShowText("expectedTitleChart: " + expectedTitleChart);
		
		Assert.assertTrue(expectedTitleChart.startsWith(languageTag));
		
		// Verify category next to chart's title  
		
		String expectedCategorySelected = driver.findElement(By.xpath(xpathTitleChart + "/span")).getText();
		
		ShowText("expectedCategorySelected: " + expectedCategorySelected);
		
		Assert.assertTrue(expectedCategorySelected.startsWith(languageTag));
	
		// Verify axis label's text
		
		String expectedAxisLabel = driver.findElement(By.xpath(baseXpath + "[1]/div[@class='tdb-h3']")).getText();
					
		ShowText("expectedAxisLabelPartOne: " + expectedAxisLabel);
					
		Assert.assertTrue(expectedAxisLabel.startsWith(languageTag));

		
		// Verify caption below chart
		
		String xpathCaption = baseXpath + "[1]/div[contains(@class,'tdb-caption')]";
		
		String expectedCaptionText = driver.findElement(By.xpath(xpathCaption)).getText();
		
		ShowText("expectedCaptionText: " + expectedCaptionText);
		
		Assert.assertTrue(expectedCaptionText.startsWith(languageTag));
			

			
		// Verify category selector labels
		
		for (int num = 1; num <= 3; num++) {
			
			String xpathSelectors = "//div[@class='tdb-card'][" + num + "]/div[1]/div";
			
			List<WebElement> selectors = driver.findElements(By.xpath(xpathSelectors));
			
			for (int numSel = 1; numSel <= selectors.size(); numSel++) {
				
				String expectedSelectorLabel = driver.findElement(By.xpath(xpathSelectors + "[" + numSel + "]")).getText();
				
				ShowText("expectedSelectorLabel: " + expectedSelectorLabel);
				
				Assert.assertTrue(expectedSelectorLabel.startsWith(languageTag));
								
			}
			
		}
		
		// Verify "Average" item on chart
		
		String cssXAxis = "#" + UsageHelper.getChartId(HierarchyHelper.topTenChart) + ">svg>g.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan";
		
		List<WebElement> xAxisItems = driver.findElements(By.cssSelector(cssXAxis));
		List<String> xAxisText = new ArrayList<>();
		
		for (WebElement item: xAxisItems) {
			
			xAxisText.add(item.getText());
			
		}
		
		Assert.assertTrue(xAxisText.contains(languageTag + "Average"));
		
	}

	
	
}
