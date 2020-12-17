
package atmgui;

public class CashDispenserGUI 
{
    // the default initial number of bills in the cash dispenser
    private final static int INITIAL_COUNT = 500;
    private int count; // number of $20 bills remaining
    
    public CashDispenserGUI()
    {
        count = INITIAL_COUNT;
    }
    
    public void dispenseCash(int amount)
    {
        int billsRequired = amount / 20; // number of $20 bills required
        count -= billsRequired; // update the count of bills
    }
    
    // indicates whether cash dispenser can dispense desired amount
    public boolean isSufficientCashAvailable(int amount)
    {
        int billsRequired = amount / 20; // number of $20 bills required
        
        if (count >= billsRequired)
            return true;
        else 
            return false;
    }
}
