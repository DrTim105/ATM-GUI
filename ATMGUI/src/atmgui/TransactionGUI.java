    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atmgui;

import javax.swing.JTextArea;

public abstract class TransactionGUI
{
    private int accountNumber;
    private BankDatatbaseGUI bankDatabase;
    protected final JTextArea textArea;
    protected boolean indicator;
    
    public TransactionGUI(int userAccountNumber,
        BankDatatbaseGUI atmBankDatabase, JTextArea atmTextArea)
    {
        accountNumber = userAccountNumber;
        bankDatabase = atmBankDatabase;
        atmTextArea = new JTextArea(5, 10);
        textArea = atmTextArea;
    }
    
    public int getAccountNumber()
    {
        return accountNumber;
    }
    
    public BankDatatbaseGUI getBankDatabase()
    {
        return bankDatabase;
    }
    
    public JTextArea getTextArea()
    {
        return textArea;
    }
    public abstract JTextArea execute();
}
