package helperObjects;

import java.util.Comparator;

// this is a dependent unit
public class Child implements Comparator<Child>, Comparable<Child>
{
	// members
	public String childName;
	public int cost;
   
	public String [] tempArray;
	public String tempName = "";
	public int tempCost = 0;
	static int childRowNumber = 0; //  this keeps track 
   
	public Child(){}
			
	public Child(String chName, int cst) 
	{
		childName = chName;
		cost = cst;
	}

	// Overriding the compareTo method
	public int compareTo(Child d) 
	{
		return (this.childName).compareTo(d.childName);
	}

	// Overriding the compare method to sort the age 
	public int compare(Child d, Child d1) 
	{
		return d1.cost - d.cost;
	}
   
	public void Show()
	{
		// System.out.println("----------------");
		System.out.println(childName + " " + cost);
	}	
   
	public String GetNameAndCost()
	{
		// split the name into an array.
		tempArray = childName.split(" ");
   
		// Debug below -- show name
		// System.out.println("-----------------------");
		// for(String str : tempArray){System.out.println(str);} 
   
		childRowNumber ++; // this assigns row number of child info
		
		// setup user name 
		tempName =   tempArray[1] + " " + tempArray[2].split(":")[0];
   
		// get the cost 
		tempCost = cost;
   
		return childRowNumber +  " " + tempName + " " + tempCost; // send back name and cost
   }
}
