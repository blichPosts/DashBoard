package helperObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

import Dash.BaseClass;

public class UsageCalculationHelper extends BaseClass{

	
	// Used to convert *Data* units
	// E.g.: Given a value for Data of 20236954 bytes or Kb??? , the shortened and rounded value 
	// that fits the format to be displayed on the KPI tile will be "19"
	public static String convertDataUnits(double amount) {
		
		String amountConverted = "";
		//String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		//System.out.println("Original amount: " + amount);
			
		if (amount < 1000.0){
			tmpAmount = amount;
		}
		else if (amount > 1000.0 && amount < 1000000.0){
			tmpAmount = amount / 1024;
			//unit = "K";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / Math.pow(1024, 2);
			//unit = "M";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / Math.pow(1024, 3);
			//unit = "G";
		}
		else if (amount > 1000000000000.0){
			tmpAmount = amount / Math.pow(1024, 4);
			//unit = "T";
		}
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue());  // + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue());  // + unit;
				
			}
			
		}else{
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue);  // + unit;
						
		}
		
		//System.out.println("Value rounded for KPI: " + amountConverted);
		
		return amountConverted;
		
	}


	// Used to convert *Data* units from KB to GB. The amount returned will be an integer; no decimal point.
	// It's used in tests that verify the data usage displayed on tooltips of charts 
	// E.g.: Given a value for Data of 20236954 KB , the shortened and rounded value 
	// that fits the format to be displayed on the tooltip will be 19 (expressed in GB)
	public static String convertDataUnitToGbNoDecimalPoint(double amount) {
		
		double tmpAmount = amount / Math.pow(1024, 2);
		String amountConverted = roundNoDecimalDigits(tmpAmount);
		//System.out.println("Original amount: " + amount + ", Converted from KB to GB: " + tmpAmount + ", Value rounded: " + amountConverted);
		
		return amountConverted;
		
	}
	
		
	// Round value, no decimal digits
	public static String roundNoDecimalDigits(double notRoundedValue){
		
		long roundedValue = Math.round(notRoundedValue);
		String amountConverted = Long.toString(roundedValue);
		//System.out.println("Not Rounded Value: " + notRoundedValue + ", Value rounded: " + amountConverted);
		
		return amountConverted;
		
	}
	
	
	// Used to convert *Voice* and *Messages* values to different units
	// E.g.: Given a value for Voice of 15236 minutes, the shortened and rounded value 
	// that fits the format to be displayed on the KPI tile will be 15K 
	public static String convertUnits(double amount){
		
		String amountConverted = "";
		String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		//System.out.println("Original amount: " + amount);
			
//		if (amount < 1000.0){
//			tmpAmount = amount;
//		}
//		else if (amount > 1000.0 && amount < 1000000.0){
		if (amount < 950.0){
			tmpAmount = amount;
		}
		else if (amount > 950.0 && amount < 1000000.0){
			tmpAmount = amount / 1000;
			unit = "K";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / 1000000;
			unit = "M";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / 1000000000;
			unit = "G";
		}
		else if (amount > 1000000000000.0){
			tmpAmount = amount / 1000000000000.0;
			unit = "T";
		}
		
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue()) + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue()) + unit;
				
			}
			
		}else{
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue) + unit;
						
		}
				
		//System.out.println("Value rounded for KPI: " + amountConverted);
		
		return amountConverted;
		
	}

	
	
	// It adds the corresponding unit of measure, KB, MB, GB, TB...
	// E.g.: Given a value for Data of 20236954 Kb , the shortened and rounded value 
	// that fits the format to be displayed on the KPI tile will be  
	public static String convertDataUnitsAndAddPrefix(double amount) {
		
		String amountConverted = "";
		String unit = "";
		double tmpAmount = 0.0;
		String amountConvertedTmp = ""; 
		
		//System.out.println("Original amount: " + amount);
			
		if (amount == 0){
			tmpAmount = amount;
			unit = "B";
		}
		else if (amount < 1000.0){
			tmpAmount = amount;
			unit = "KB";
		}
		else if (amount > 1000.0 && amount < 1000000.0){
			tmpAmount = amount / 1024;
			unit = "MB";
		}
		else if (amount > 1000000.0 && amount < 1000000000.0){
			tmpAmount = amount / Math.pow(1024, 2);
			unit = "GB";
		}
		else if (amount > 1000000000.0 && amount < 1000000000000.0){
			tmpAmount = amount / Math.pow(1024, 3);
			unit = "TB";
		}
		//else if (amount > 1000000000000.0){
		//	tmpAmount = amount / Math.pow(1024, 4);
		//	unit = "TB";
		//}
		
		//System.out.println("Temp amount: " + tmpAmount);
		
		if(tmpAmount < 10.0){
			
			BigDecimal rounded = new BigDecimal(tmpAmount);
			rounded = rounded.setScale(1, RoundingMode.HALF_UP);
			amountConvertedTmp = Double.toString(rounded.doubleValue());
			if(amountConvertedTmp.endsWith(".0")){
				
				amountConverted = Integer.toString(rounded.toBigIntegerExact().intValue()) + unit;
				
			}else{
				
				amountConverted = Double.toString(rounded.doubleValue()) + unit;
				
			}
			
		}else{
			
			long roundedValue = Math.round(tmpAmount);
			amountConverted = Long.toString(roundedValue) + unit;
						
		}
		
		//System.out.println("Value rounded for Data KPI: " + amountConverted);
		
		return amountConverted;
		
	}


		
	// Given a value with a prefix representing the units, get ONLY the numeric value
	// E.g.: Original value is: 36K min, numeric value obtained is: 36
	// This is used for the main KPI tile value
	public static String getNumericValue(String stringValue) {
		
		String unitK = "K";
		String unitM = "M";
		String unitG = "G";
		String unitT = "T";
		
		String valueNoUnits = "";
		
		//System.out.println("Value String: " + stringValue);
		
		if (stringValue.contains(unitK)){
			
			String[] kpiValueParts = stringValue.split(unitK);
			valueNoUnits = kpiValueParts[0];
							
		}else if (stringValue.contains(unitM)){
			
			String[] kpiValueParts = stringValue.split(unitM);
			valueNoUnits = kpiValueParts[0];
			
		}else if (stringValue.contains(unitG)){
			
			String[] kpiValueParts = stringValue.split(unitG);
			valueNoUnits = kpiValueParts[0];
		
		}else if (stringValue.contains(unitT)){
			
			String[] kpiValueParts = stringValue.split(unitT);
			valueNoUnits = kpiValueParts[0];
		
		}else{
			valueNoUnits = stringValue;
			
		}
		
		if(valueNoUnits.endsWith(".0")){
			String[] tmpValue = valueNoUnits.split(".0"); 
			valueNoUnits = tmpValue[0];
		}
		
		//System.out.println("Value with no units: " + valueNoUnits);
		
		return valueNoUnits.trim();
		
	}
	
	
	// Given a numeric value with a prefix representing the units and the units represented (either 'min' or 'msg'), 
	// get the numeric value with the prefix and get rid off 'min' or 'msg'
	// E.g.: Original value is: 36K min, value obtained is: 36K
	// This is used for the rolling averages
	public static String getNumericValueWithPrefixes(String stringValue) {
		
		String minutes = "min";
		String messages = "msg";
		
		String valueNoUnits = "";
		
		//System.out.println("Original Value: " + stringValue);
		
		if (stringValue.contains(minutes)){
			
			String[] valueParts = stringValue.split(minutes);
			valueNoUnits = valueParts[0];
							
		}else if (stringValue.contains(messages)){
			
			String[] valueParts = stringValue.split(messages);
			valueNoUnits = valueParts[0];
			
		}else{
			valueNoUnits = stringValue;
			
		}
		
		if(valueNoUnits.endsWith(".0")){
			String[] tmpValue = valueNoUnits.split(".0"); 
			valueNoUnits = tmpValue[0];
		}
		
		//System.out.println("Value with no units: " + valueNoUnits);
		
		return valueNoUnits.trim();
		
	}

	
	// Adds the '$' symbol to the expenses values 
	public static String convertUnitsExpense(double value) {
		
		String valueConverted = convertUnits(Math.abs(value));
		
		// If the original value was less than zero, add a leading dash to the value to be returned
		if (value < 0) {
			valueConverted = "-" + valueConverted;
		}
		
		return "$" + valueConverted;
		
	}
	
	
}
