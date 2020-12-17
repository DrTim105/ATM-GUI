
package atmgui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class DepositGUI extends TransactionGUI
{
    private double amount;
    private String line = "";
    private DepositSlotGUI depositSlot;
    private final static int CANCELED = 0;

    
    public DepositGUI(int userAccountNumber,
        BankDatatbaseGUI atmBankDatabase, DepositSlotGUI atmDepositSlot,
        JTextArea atmTextArea)
    {
        super (userAccountNumber, atmBankDatabase, atmTextArea);
        depositSlot = atmDepositSlot;
        
        textArea.addKeyListener(new KeyHandler());
    }
    
    private class KeyHandler extends KeyAdapter
    {
        public void keyPressed(KeyEvent event)
        {
            if (KeyEvent.getKeyText(event.getKeyCode()).equals("Enter") != true)
                line += KeyEvent.getKeyText(event.getKeyCode());
            else
            {
                    getDepositAmount();
            }
        }
    }
    
    
    @Override
    public JTextArea execute()
    {
        JTextArea textArea = getTextArea();
        
        textArea.setText("\nPlease enter a deposit amount in " +
        "CENTS (or 0 to cancel): ");
        
        return textArea;
    }
    
    // perform transaction
    public void executeTransaction()
    {
        BankDatatbaseGUI bankDatabase = getBankDatabase();
        JTextArea textArea = getTextArea();
        
        // check whether user entered a deposit amount or canceled
        if (amount != CANCELED)
        {
            // request deposit envelope containing specified amount
            JOptionPane.showMessageDialog(textArea, String.format(
                "\nPlease insert a deposit envelope containing $%,.2f.",
                amount));
            
            // receive deposit envelope
            boolean envelopeReceived = depositSlot.isEnvelopeReceived();
            
            // check whether deposit envelope was received
            if (envelopeReceived)
            {
                textArea.setText( "\nYour envelope has been " +
                    "received.\nNOTE: The money just deposited will not " +
                    "be available \nuntil we verify the amount of any " +
                    "enclosed cash \nand your checks clear." +
                    "\n\nClick Reload to return to Main Menu" );
                
                // credit account to reflect the deposit
                bankDatabase.credit(getAccountNumber(), amount);
            }
            else
            {
                JOptionPane.showMessageDialog(textArea,
                    "\nYou did not insert an " +
                    "envelope, so the ATM has canceled your transaction." );
                textArea.setText("\nTransaction canceled!" +
                "\n\nClick Reload to return to Main Menu");
            }
        }
        else // user canceled instead of entering amount
        {
            JOptionPane.showMessageDialog(textArea,
                "\nCanceling transaction..." );
            textArea.setText("\nTransaction canceled!" +
                "\n\nClick Reload to return to Main Menu");
        }
    }
        
    // prompt user to enter a deposit amount in cents
    private void getDepositAmount()
    {
        int input = Integer.parseInt(line); // receive input of deposit amount
        
        // check whether the user canceled or entered a valid amount
        if (input == CANCELED)
            amount = CANCELED;
        else
        {
           amount = input / 100;
        }
        
        executeTransaction();
    }
}

