
package automatedtellermachineclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import entity.DepositAccount;
import exception.AtmCardNotFoundException;
import exception.AtmCardPinChangeException;
import exception.InvalidAtmCardCredentialException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

public class MainApp{

    private AtmCardSessionBeanRemote atmCardSessionBeanRemote;
    
    private Long atmCardId;

    public MainApp() {
    }
    
    MainApp(AtmCardSessionBeanRemote atmCardSessionBeanRemote) {
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;
    }

    void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to RCBS - Automated Teller Machine (ATM) ***\n");
            System.out.println("1: Insert ATM Card");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                    try
                    {
                        doInsertAtmCard();
                        System.out.println("ATM card authentication successful!\n");
                        
                        menuMain();
                    }
                    catch(InvalidAtmCardCredentialException ex) 
                    {
                        System.out.println("Unable to authenticate ATM card: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doInsertAtmCard() throws InvalidAtmCardCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String atmCardNumber;
        String pin = "";
        
        System.out.println("*** ATM :: Insert ATM Card ***\n");
        System.out.print("Enter ATM Card Number> ");
        atmCardNumber = scanner.nextLine().trim();
        System.out.print("Enter PIN> ");
        pin = scanner.nextLine().trim();                
        
        if(atmCardNumber.length() > 0 && pin.length() > 0)
        {
            atmCardId = atmCardSessionBeanRemote.authenticateAtmCard(atmCardNumber, pin);
        }
        else
        {
            throw new InvalidAtmCardCredentialException("Missing ATM card credential!");
        }
    }
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** RCBS - Automated Teller Machine (ATM) ***\n");
            System.out.println("You are logged in\n");
            System.out.println("1: Enquire Available Balance");
            System.out.println("2: Change PIN");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doEnquireAvailableBalance();
                }
                else if(response == 2)
                {
                    doChangePin();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }

    private void doEnquireAvailableBalance()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("*** RCBS - ATM :: Enquire Available Balance ***\n\n");
            List<DepositAccount> depositAccounts = atmCardSessionBeanRemote.retreiveLinkedDepositAccountsByAtmCardId(atmCardId);
            System.out.printf("%3s%15s%18s\n", "S/N", "Account Type", "Account Number");
            
            int sn = 0;
            
            for(DepositAccount depositAccount:depositAccounts)
            {
                ++sn;
                System.out.printf("%3s%15s%18s\n", sn, depositAccount.getAccountType().toString(), depositAccount.getAccountNumber());
            }
            
            System.out.println("------------------------");
            System.out.print("Account to Enquire > ");
            int response = scanner.nextInt();
            
            if(response >= 1 && response <= sn)
            {
                System.out.println("Available balance is " + NumberFormat.getCurrencyInstance().format(depositAccounts.get(response-1).getAvailableBalance()));
            }
            else
            {
                System.out.println("Invalid option!\n");
            }
            
        }
        catch(AtmCardNotFoundException ex)
        {
            System.out.println("Invalid ATM card!\n");
        }
    }
    
    private void doChangePin()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("*** RCBS - ATM :: Change PIN ***\n\n");

            System.out.print("Enter current PIN> ");
            String currentPin = scanner.nextLine().trim();
            System.out.print("Enter new PIN> ");
            String newPin = scanner.nextLine().trim();
            System.out.print("Enter new PIN again> ");
            String reenterNewPin = scanner.nextLine().trim();

            if(newPin.equals(reenterNewPin))
            {
                atmCardSessionBeanRemote.changePin(atmCardId, currentPin, newPin);
                System.out.println("New PIN changed successfully!\n");
            }
            else
            {
                System.out.println("New PIN mismatched!\n");
            }
        }
        catch(AtmCardNotFoundException ex)
        {
            System.out.println("Invalid ATM card!\n");
        }
        catch(AtmCardPinChangeException ex)
        {
            System.out.println(ex.getMessage() + "!\n");
        }
    }
}
