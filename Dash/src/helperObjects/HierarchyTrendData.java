package helperObjects;

public class HierarchyTrendData {

	private String id;
	private String name;
	private String ordinalYear;
	private String ordinalMonth;
	private String numberOfInvoices;
	private String numberOfLines;
	private String numberOfAccounts;
	private String numberOfInvoicesRollup;
    private String numberOfLinesRollup;
    private String numberOfAccountsRollup;
	private String currencyCode;
	private String totalExpense;
	private String optimizableExpense;
	private String roamingExpense;
	private String totalExpenseRollup;
	private String optimizableExpenseRollup;
	private String roamingExpenseRollup;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrdinalYear() {
		return ordinalYear;
	}
	public void setOrdinalYear(String ordinalYear) {
		this.ordinalYear = ordinalYear;
	}
	public String getOrdinalMonth() {
		return ordinalMonth;
	}
	public void setOrdinalMonth(String ordinalMonth) {
		this.ordinalMonth = ordinalMonth;
	}
	public String getNumberOfInvoices() {
		return numberOfInvoices;
	}
	public void setNumberOfInvoices(String numberOfInvoices) {
		this.numberOfInvoices = numberOfInvoices;
	}
	public String getNumberOfLines() {
		return numberOfLines;
	}
	public void setNumberOfLines(String numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
	public String getNumberOfAccounts() {
		return numberOfAccounts;
	}
	public void setNumberOfAccounts(String numberOfAccounts) {
		this.numberOfAccounts = numberOfAccounts;
	}
	public String getNumberOfInvoicesRollup() {
		return numberOfInvoicesRollup;
	}
	public void setNumberOfInvoicesRollup(String numberOfInvoicesRollup) {
		this.numberOfInvoicesRollup = numberOfInvoicesRollup;
	}
	public String getNumberOfLinesRollup() {
		return numberOfLinesRollup;
	}
	public void setNumberOfLinesRollup(String numberOfLinesRollup) {
		this.numberOfLinesRollup = numberOfLinesRollup;
	}
	public String getNumberOfAccountsRollup() {
		return numberOfAccountsRollup;
	}
	public void setNumberOfAccountsRollup(String numberOfAccountsRollup) {
		this.numberOfAccountsRollup = numberOfAccountsRollup;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTotalExpense() {
		return totalExpense;
	}
	public void setTotalExpense(String totalExpense) {
		this.totalExpense = totalExpense;
	}
	public String getOptimizableExpense() {
		return optimizableExpense;
	}
	public void setOptimizableExpense(String optimizableExpense) {
		this.optimizableExpense = optimizableExpense;
	}
	public String getRoamingExpense() {
		return roamingExpense;
	}
	public void setRoamingExpense(String roamingExpense) {
		this.roamingExpense = roamingExpense;
	}
	public String getTotalExpenseRollup() {
		return totalExpenseRollup;
	}
	public void setTotalExpenseRollup(String totalExpenseRollup) {
		this.totalExpenseRollup = totalExpenseRollup;
	}
	public String getOptimizableExpenseRollup() {
		return optimizableExpenseRollup;
	}
	public void setOptimizableExpenseRollup(String optimizableExpenseRollup) {
		this.optimizableExpenseRollup = optimizableExpenseRollup;
	}
	public String getRoamingExpenseRollup() {
		return roamingExpenseRollup;
	}
	public void setRoamingExpenseRollup(String roamingExpenseRollup) {
		this.roamingExpenseRollup = roamingExpenseRollup;
	} 
	
// Other data that comes in JSON but is not needed/used for now:	
// date
// ordinal_year_month
// total_expense_rollup_ex_3_month_avg
// total_expense_rollup_ex_6_month_avg
// optimizable_expense_rollup_ex_3_month_avg
// optimizable_expense_rollup_ex_6_month_avg
// roaming_expense_rollup_ex_3_month_avg
// roaming_expense_rollup_ex_6_month_avg
// cost_per_line_ex
// cost_per_line_rollup_ex
// cost_per_line_rollup_ex_3_month_avg
// cost_per_line_rollup_ex_6_month_avg
	
	
}
