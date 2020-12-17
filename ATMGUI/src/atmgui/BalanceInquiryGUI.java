
package atmgui;

import javax.swing.JTextArea;


public class BalanceInquiryGUI extends TransactionGUI
{
    public BalanceInquiryGUI(int userAccountNumber, BankDatatbaseGUI atmBankDatabase,
        JTextArea atmTextArea)
    {
        super(userAccountNumber, atmBankDatabase, atmTextArea);
    }
    
    @Override
    public JTextArea execute()
    {
        JTextArea textArea = getTextArea();
        
        // get reference to bank database and screen
        BankDatatbaseGUI bankDatabase = getBankDatabase();
        
        // get the available balance for the account involved
        double availableBalance =
            bankDatabase.getAvailableBalance(getAccountNumber());
        
        // get the total balance for the account involved 
        double totalBalance =
            bankDatabase.getTotalBalance(getAccountNumber());
        
        // display the balance information on the screen
        textArea.setText(String.format("\nBalance Information:\n"
                + " - Available balance: $%,.2f"
                + "\n - Total balance:   $%,.2f\n"
                + "\nClick Reload to return to Main Menu",
                availableBalance, totalBalance ));
        
        return textArea;
    }
}

