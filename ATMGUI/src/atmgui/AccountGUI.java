
package atmgui;

public class AccountGUI 
{
    private int accountNumber; // account number
    private int pin; // PIN authentication
    private double availableBalance; // funds available for withdrawal
    private double totalBalance; // funds available + pending deposits
    
    public AccountGUI(int theAccountNumber, int thePIN,
        double theAvailableBalance, double theTotalBalance)
    {
        accountNumber = theAccountNumber;
        pin = thePIN;
        availableBalance = theAvailableBalance;
        totalBalance = theTotalBalance;
    }
    
    public boolean validatePIN(int userPIN)
    {
        if (userPIN == pin)
            return true;
        else
            return false;
    }
    
    // returns available balance
    public double getAvailableBalance()
    {
        return availableBalance;
    }
    
    // returns the total balance
    public double getTotalBalance()
    {
        return totalBalance;
    }
    
    // credits an amount to the account
    public void credit(double amount)
    {
        totalBalance += amount;
    }
    
    // debits an amount from the account
    public void debit(double amount)
    {
        availableBalance -= amount;
        totalBalance -= amount;
    }
    
    // returns account number
    public int getAccountNumber()
    {
        return accountNumber;
    }
}
