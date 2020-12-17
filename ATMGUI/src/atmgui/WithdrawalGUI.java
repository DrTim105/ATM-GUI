
package atmgui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class WithdrawalGUI extends TransactionGUI
{
    // attributes
    private int amount;
    private String line = "";
    int userChoice = 0;
    private CashDispenserGUI cashDispenser;
    private boolean cashDispensed = false;
    
    // constant corresponding to menu option to cancel
    private final static int CANCELED = 6;
    
    public WithdrawalGUI(int userAccountNumber,
        BankDatatbaseGUI atmBankDatabase,
        CashDispenserGUI atmCashDispenser, JTextArea atmTextArea)
    {
        super (userAccountNumber, atmBankDatabase, atmTextArea);
        cashDispenser = atmCashDispenser;
        
        textArea.addKeyListener(new KeyHandler());
    }
    
    // private inner class to handle key event processing
    private class KeyHandler extends KeyAdapter
    {
        public void keyPressed(KeyEvent event)
        {
            // process input
            if (KeyEvent.getKeyText(event.getKeyCode()).equals("Enter") != true)
                line = KeyEvent.getKeyText(event.getKeyCode());
            else
            {
                    getMenuOfAmounts();
            }
        }
    }
    
    // perform transaction
    @Override
    public JTextArea execute()
    {        
        JTextArea textArea = getTextArea();
        
        // display Withdrawal Menu
        textArea.setText(String.format("Withdrawal Menu:\n"
                    + "1 - $20\n"
                    + "2 - $40\n"
                    + "3 - $60\n"
                    + "4 - $100\n"
                    + "5 - $200\n"
                    + "6 - Cancel transaction\n"
                    + "\nChoose a withdrawal amount: "));
        
        return textArea;
    }
                
    // display a menu of withdrawal amounts and the option to cancel;
    // return the chosen amount or 0 if the user chooses to cancel
    private void executeTransaction()
    {
        JTextArea textArea = getTextArea();
        double availableBalance;
        BankDatatbaseGUI bankDatabase = getBankDatabase();

        //obtain a chosen withdrawal amount or canceled
        amount = userChoice;

        // check whether user chose a withdrawal amount or canceled
        if (amount != CANCELED)
        {
            // get available balance of account involved
            availableBalance =
                bankDatabase.getAvailableBalance(getAccountNumber());

            // check whether the user has enough money in the account
            if (amount <= availableBalance)
            {
                // check whether the cash dispenser has enough money
                if ( cashDispenser.isSufficientCashAvailable( amount ) )
                {
                 // update the account involved to reflect the withdrawal
                 bankDatabase.debit( getAccountNumber(), amount );

                 cashDispenser.dispenseCash( amount ); // dispense cash
                 cashDispensed = true; // cash was dispensed
                 indicator = true;
                 
                 // instruct user to take cash
                 textArea.setText( "\nYour cash has been" +
                 " dispensed. Please take your cash now." +
                "\n\nClick Reload to return to Main Menu" );
                 } // end if
                 else // cash dispenser does not have enough cash
                {
                    JOptionPane.showMessageDialog(textArea,
                    "\nInsufficient cash available in the ATM." +
                    "\n\nPlease choose a smaller amount." );
                    execute();
                }
            } // end if
            else // not enough money available in user's account
            {
                JOptionPane.showMessageDialog(textArea,
                "\nInsufficient funds in your account." +
                "\n\nPlease choose a smaller amount." );
                execute();
            } // end else
        } // end if
        else // user chose cancel menu option
        {
            JOptionPane.showMessageDialog(textArea,
                "\nCanceling transaction...");
            textArea.setText("\nTransaction canceled!"
             + "\n\nClick Reload to return to Main Menu");
        } // end else
    } 
    
    private void getMenuOfAmounts()
    {
        JTextArea textArea = getTextArea();
        
        userChoice = 0;
        int input = Integer.parseInt(line);
        
        // array of amounts to correspond to menu numbers
        int[] amounts = { 0, 20, 40, 60, 100, 200 };
        
        // determine how to proceed based on the input value
        switch (input)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                userChoice = amounts[input];
                executeTransaction();
                break;
            case CANCELED:
                userChoice = CANCELED;
                executeTransaction();
                break;
            default:
                JOptionPane.showMessageDialog(textArea,
                    "\nInvalid selection. Try again.");
                execute();
        }
    }
}
