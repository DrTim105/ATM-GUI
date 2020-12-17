package atmgui;

import javax.swing.JFrame;

public class ATMGUICaseStudy {

    // main method creates and runs the ATM

    public static void main(String[] args) {
        ATMGUI theATM = new ATMGUI();
        theATM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theATM.setSize(450, 400);
        theATM.setVisible(true);
    }
}
