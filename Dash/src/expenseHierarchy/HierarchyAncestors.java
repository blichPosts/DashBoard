package expenseHierarchy;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.AncestorsInfo;
import helperObjects.HierarchyHelper;

public class HierarchyAncestors extends BaseClass {

	public static void verifyAncestorsAndBreadcrumbs(AncestorsInfo ancestorsData) {
		
		// Get the current breadcrumbs
		List<WebElement> breadcrumbsTmp = HierarchyHelper.getBreadcrumbs();
		List<String> breadcrumbs = new ArrayList<String>();
		
		for (WebElement b: breadcrumbsTmp) {
			breadcrumbs.add(b.getText());
		}
		
		// Compare the ancestors listed on the ajax call to the breadcrumbs
		List<String> ancestorsList = ancestorsData.getAncestorsNames();
		
		for (int i = 0; i < ancestorsList.size(); i++) {
			
			String valueFound = breadcrumbs.get(i);
			String valueExpected = ancestorsList.get(i);
			
//			ShowText("valueFound: " + valueFound);
//			ShowText("valueExpected: " + valueExpected);
			
			Assert.assertEquals(valueFound, valueExpected);
			
		}
		
	}

}
