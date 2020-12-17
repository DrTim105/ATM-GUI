
package atmgui;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class ATMGUI extends JFrame implements ActionListener
{
    private boolean userAuthenticated; // whether user is authenticated
    private int currentAccountNumber; // current user's acct number
    private CashDispenserGUI cashDispenser;
    private DepositSlotGUI depositSlot;
    private BankDatatbaseGUI bankDatabase;
    
    // constants corresponding to main menu options
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int DEPOSIT = 3;
    private static final int EXIT = 4;
    
    // GUI variables
    private Box box;
    private final JTextArea textArea;
    private final JPasswordField passwordField;
    private final JTextField textField;
    private final JLabel textLabel;
    private final JLabel passwordLabel;
    private final JPanel textPanel;
    private final JPanel buttonPanel;
    private final JPanel slotPanel;
    private final JButton[] buttons;
    private final GridLayout textLayout;
    private final GridLayout buttonLayout;
    private final GridLayout slotLayout;
    private String line = "";
    private int step = 0;
    private int input1;
    private int input2;
    private int indicator = 0;
    private boolean reload = false;
    
    // no-argument constructor
    public ATMGUI()
    {
        super("ATM GUI");
        
        userAuthenticated = false;
        currentAccountNumber = 0;
        cashDispenser = new CashDispenserGUI(); // create cash dispenser
        depositSlot = new DepositSlotGUI(); // create deposit slot
        bankDatabase = new BankDatatbaseGUI(); // create acct info database
        
        
        // GUI initialization
        setLayout(new BorderLayout());
        box = Box.createVerticalBox();
        textArea = new JTextArea(5, 10);
        buttonPanel = new JPanel();
        slotPanel = new JPanel();
        textPanel = new JPanel();
        buttonLayout = new GridLayout(4, 3, 5, 5);
        slotLayout = new GridLayout(2, 1, 10, 10);
        textLayout = new GridLayout(2,2,5,5);
        buttons = new JButton[14];
        textField = new JTextField(5);
        passwordField = new JPasswordField(5);
        textLabel = new JLabel("Please enter your account number");
        passwordLabel = new JLabel("Enter your pin");
        
        
        
        // GUI construction
        textPanel.setLayout(textLayout);
        buttonPanel.setLayout(buttonLayout);
        slotPanel.setLayout(slotLayout);
        textArea.setText("Welcome!");
        
        for (int count = 0; count < 9; count++)
        {
            buttons[count] = new JButton("" + (count + 1));
            buttonPanel.add(buttons[count]); // add button to panel
        }
        buttonPanel.add(buttons[9] = new JButton("" + 0));
        buttonPanel.add(buttons[10] = new JButton("Enter"));
        buttonPanel.add(buttons[11] = new JButton("Reload"));
        
        buttons[12] = new JButton("Take Cash Here");
        buttons[13] = new JButton("Insert Deposit Envelope Here");
        slotPanel.add(buttons[12]);
        slotPanel.add(buttons[13]);
        
        textPanel.add(textLabel);
        textPanel.add(textField);
        textPanel.add(passwordLabel);
        textPanel.add(passwordField);
        box.add(textPanel);
        box.add(new JScrollPane(textArea));
        
        add(box, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);
        add(slotPanel, BorderLayout.EAST);
        
        
        // GUI Event Handling
        textArea.addKeyListener(new KeyHandler());
        passwordField.addActionListener(this);
        textField.addActionListener(this);
        buttons[11].addActionListener(this);
    }
    
    // process action events
    @Override
    public void actionPerformed(ActionEvent event)
    {
        // user pressed Enter in textField
        if (event.getSource() == textField)
        {
            input1 = Integer.parseInt(event.getActionCommand());
            JOptionPane.showMessageDialog(ATMGUI.this, "Account Number entered!"); 
            textField.setEditable(false);
            indicator++;
        }
        // user pressed Enter in passwordField
        else if (event.getSource() == passwordField)
        {
            input2 = Integer.parseInt(event.getActionCommand());
            JOptionPane.showMessageDialog(ATMGUI.this, "Pin entered!"); 
            passwordField.setEditable(false);
            indicator++;
        }   
        // user clicked on Reload Button
        else if (event.getSource() == buttons[11])
        {
            if (step == 1)
            {
                box.removeAll();
                box.add(textArea);
                validate();
                textArea.addKeyListener(new KeyHandler());
                displayMainMenu();
                reload = true;
            }
                
        }
        
        // determine when to proceed to authenticate user
        if (indicator == 2)
            authenticateUser();
    }
    
    // private inner class for key event handling
    private class KeyHandler extends KeyAdapter
    {
        public void keyPressed(KeyEvent event)
        {
            // process input 
            if (KeyEvent.getKeyText(event.getKeyCode()).equals("Enter") != true)
                line = KeyEvent.getKeyText(event.getKeyCode());
            else
            {
                if (step == 1)
                {
                    displayMainMenu();
                }
                if (step == 2 || reload == true)
                {
                    performTransactions();
                }       
            }   
        }
    }
    
    // attempts to authenticate user against database
    private void authenticateUser()
    {
        int accountNumber = input1;
        int pin = input2;

        // set userAuthenticated to boolean value returned by database
        userAuthenticated =
            bankDatabase.authenticateUser(accountNumber, pin);
        
        // check whether authentication succeeded
        if (userAuthenticated)
        {
            currentAccountNumber = accountNumber;
            step++;
            indicator = 0;
            displayMainMenu();
        }
        else
        {
            textField.setEditable(true);
            passwordField.setEditable(true);
            indicator = 0;
            JOptionPane.showMessageDialog(ATMGUI.this,
                "Invalid account number or PIN. Please try again.");
        }    
    }
    
    // display the main menu and return an input selection
    private void displayMainMenu()
    {
        // display Main Menu and get input
        textArea.setText(String.format("Main menu: \n"
            + "1 - View my balance\n"
            + "2 - Withdraw cash\n"
            + "3 - Deposit funds\n"
            + "4 - Exit\n"
            + "Enter a choice:  "));
        
        box.remove(textPanel);
        validate();
        step++;
    }
    
    
    private void performTransactions()
    {
        // local variable to store transaction currently being processed
        TransactionGUI currentTransaction =  null;

        int mainMenuSelection = Integer.parseInt(line);
        
        // decide how to proceeed based on user menu selection
        switch (mainMenuSelection)
        {
            // user chose to perform one of three transaction types
            case BALANCE_INQUIRY:
            case WITHDRAWAL:
            case DEPOSIT:

                // intialize as new object of chosen type
                currentTransaction =
                    createTransaction(mainMenuSelection);

                box.removeAll();
                // add new transaction as a textArea
                box.add(new JScrollPane(currentTransaction.execute()));
                step = 1;
                validate();   
                break;
            case EXIT: // user chose to terminate session
                JOptionPane.showMessageDialog(ATMGUI.this,
                    "\nExiting the system...");
                JOptionPane.showMessageDialog(ATMGUI.this,
                    "\nThank you! Goodbye!");
                
                userAuthenticated = false;
                currentAccountNumber = 0;
                System.exit(0); // close window and program
                break;
            default : // user did not enter an integer from 1 - 4
                JOptionPane.showMessageDialog(ATMGUI.this, String.format(
                "\nYou did not enter a valid selection. Try again.%d\n",
                    mainMenuSelection));
                
                step = 1;
                displayMainMenu();  
                break;
        }
    }
    
    private TransactionGUI createTransaction(int type)
    {
        TransactionGUI temp = null; // temporary Transaction variable
        
        // determine which type of Transaction to create
        switch(type)
        {
            case BALANCE_INQUIRY:
                temp = new BalanceInquiryGUI(
                    currentAccountNumber, bankDatabase, textArea);
                break;
            case WITHDRAWAL:
                temp = new WithdrawalGUI(currentAccountNumber,
                    bankDatabase, cashDispenser, textArea);
                break;
            case DEPOSIT:
                temp = new DepositGUI(currentAccountNumber,
                    bankDatabase,depositSlot, textArea);
                break;
        }
        
        return temp;
    }
}
