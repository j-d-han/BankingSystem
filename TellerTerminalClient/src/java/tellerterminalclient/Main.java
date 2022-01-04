/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author User
 */
public class Main {

    @EJB
    private static DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static AtmCardSessionBeanRemote atmCardSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(depositAccountSessionBeanRemote, customerSessionBeanRemote, atmCardSessionBeanRemote);
        mainApp.runApp();
    }
    
}
