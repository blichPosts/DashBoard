package helperObjects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

// this is a dependent unit
public class Child implements Comparator<Child> /*, Comparable<Child>*/
{
	// members
	public String childName;
	// public int cost;
	public double cost;
   
	public String [] tempArray;
	public String tempName = "";
	// public int tempCost = 0;
	public double tempCost = 0;
	static int childRowNumber = 0; //  this keeps track 

	// file output. // jnupp
    public static BufferedWriter output = null;
    public static String lineFeed = "\r\n";
    public static File file;

	public Child(){}
			
	//public Child(String chName, int cst) 
	//{
	//	childName = chName;
	//	cost = cst;
	//}

	public Child(String chName, double cst) 
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
	//public int compare(Child d, Child d1) 
	//{
	//	return d1.cost - d.cost;
	//}
   
	/*// TRY ONE
	public int compare(Child d, Child d1) 
	{
		// return (int) (d1.cost - (d.cost)); // descending. // original  with int
		
		if(d.cost > d1.cost)
		{
			return -1;			
		}
		else
		{
			return 1;
		}
		
		
		// return (int) (d.cost - (d1.cost)); // ascending.
	}
	*/
	
	// http://stackoverflow.com/questions/4242023/comparator-with-double-type
	public int compare(Child d, Child d1) 
	{
	    double delta = d1.cost - d.cost;
	    if(delta > 0.00001) return 1;
	    if(delta < -0.00001) return -1;
	    return 0;	
	
	}
	
	
	
	
	/*
	// Overriding the compare method to sort the age -- DOUBLE ------------------ <<<<<<<<<<<<<<<<<<<<<<<< =======================  NEW ??? 
	public int compare(Child d, Child d1) 
	{
		if(d1.cost != d.cost)
		{
			return (int) (d1.cost - (d.cost)); // descending.			
		}
		else
		{
			return (d.childName).compareTo(d1.childName);
		}
	}
	*/
	
	
	public void Show()
	{
		// System.out.println("----------------");
		System.out.println(childName + " " + cost);
	}	
   
	public static void SetupOutputFile() throws IOException // jnupp
	{
		file = new File("C:\\LichPublic\\JavaOutFile.txt");
        output = new BufferedWriter(new FileWriter(file));
	}	
	
	public void ShowOutFile() throws IOException // jnupp
	{
		output.write(childName + " " + cost + lineFeed);
	}	
	
	public static void CloseFile() throws IOException // jnupp
	{
        output.close();
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
