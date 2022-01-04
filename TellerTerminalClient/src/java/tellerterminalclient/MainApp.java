
package tellerterminalclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import enumeration.DepositAccountType;
import exception.AtmCardExistException;
import exception.AtmCardLinkingException;
import exception.AtmCardNotFoundException;
import exception.CustomerExistException;
import exception.CustomerNotFoundException;
import exception.DepositAccountExistException;
import exception.DepositAccountNotFoundException;
import exception.GeneralException;
import exception.InvalidLoginCredentialException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp{
    
    private DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private AtmCardSessionBeanRemote atmCardSessionBeanRemote;

    public MainApp() {
    }
    
    MainApp(DepositAccountSessionBeanRemote depositAccountSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, AtmCardSessionBeanRemote atmCardSessionBeanRemote) {
        this.depositAccountSessionBeanRemote = depositAccountSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;
    }

    void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to RCBS - Teller Terminal ***\n");
            System.out.println("1: Login");
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
                        doLogin(); // Reserved for future use
                        System.out.println("Login successful!\n");
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
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
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password = "";
        
        System.out.println("*** Teller Terminal :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            // To invoke session bean method to perform login
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** RCBS - Teller Terminal ***\n");
            System.out.println("You are logged in\n");
            System.out.println("1: Create Customer");
            System.out.println("2: Open Deposit Account");
            System.out.println("3: Issue ATM Card");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateCustomer();
                }
                else if(response == 2)
                {
                    doOpenDepositAccount();
                }
                else if(response == 3)
                {
                    doIssueAtmCard();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    private void doCreateCustomer()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Customer newCustomer = new Customer();

            System.out.println("*** RCBS - Teller Terminal :: Create Customer ***\n\n");
            System.out.print("Enter First Name> ");
            newCustomer.setFirstName(scanner.nextLine().trim());
            System.out.print("Enter Last Name> ");
            newCustomer.setLastName(scanner.nextLine().trim());
            System.out.print("Enter Identification Number> ");
            newCustomer.setIdentificationNumber(scanner.nextLine().trim());
            System.out.print("Enter Contact Number> ");
            newCustomer.setContactNumber(scanner.nextLine().trim());
            System.out.print("Enter Address Line 1> ");
            newCustomer.setAddressLine1(scanner.nextLine().trim());
            System.out.print("Enter Address Line 2> ");
            newCustomer.setAddressLine2(scanner.nextLine().trim());
            System.out.print("Enter Postal Code> ");
            newCustomer.setPostalCode(scanner.nextLine().trim());

            newCustomer = customerSessionBeanRemote.createNewCustomer(newCustomer);
            System.out.println("New customer created successfully!: " + newCustomer.getCustomerId() + "\n");
        }
        catch(CustomerExistException | GeneralException ex)
        {
            System.out.println("An error has occurred while creating the new customer: " + ex.getMessage() + "!\n");
        }
    }
    
    private void doOpenDepositAccount()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("*** RCBS - Teller Terminal :: Open Deposit Account ***\n\n");
            System.out.print("Enter Customer ID> ");
            Long customerId = scanner.nextLong();
            Customer customer = customerSessionBeanRemote.retrieveCustomerByCustomerId(customerId, false, true);
            System.out.println("Open deposit account for " + customer.getFirstName() + " " + customer.getLastName() + " (Identification Number: " + customer.getIdentificationNumber() + ")\n");
            DepositAccount newDepositAccount = new DepositAccount();
            
            while(true)
            {
                System.out.print("Select Account Type (1: Savings, 2: Current)> ");
                Integer depositAccountTypeInt = scanner.nextInt();

                if(depositAccountTypeInt >= 1 && depositAccountTypeInt <= 2)
                {
                    if(depositAccountTypeInt == 1)
                    {
                        newDepositAccount.setAccountType(DepositAccountType.SAVINGS);
                        break;
                    }
                    else
                    {
                        System.out.println("Sorry, this account type is currently not available. Please try again!\n");
                    }
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            scanner.nextLine();
            System.out.print("Enter Account Number> ");
            newDepositAccount.setAccountNumber(scanner.nextLine().trim());
            newDepositAccount.setEnabled(true);
            
            System.out.print("Enter Initial Cash Deposit Amount> $");
            newDepositAccount.setAvailableBalance(scanner.nextBigDecimal());
            newDepositAccount.setLedgerBalance(newDepositAccount.getAvailableBalance());
            
            newDepositAccount = depositAccountSessionBeanRemote.createNewDepositAccount(newDepositAccount, customerId);
            System.out.println("New deposit account created successfully!: " + newDepositAccount.getDepositAccountId() + "\n");
            
            if(customer.getAtmCard() != null)
            {
                System.out.println("The customer has an existing ATM card: " + customer.getAtmCard().getAtmCardId() + "\n");
                System.out.print("Link New Deposit Account to ATM Card? (Enter 'Y' to Link)> ");
                String input = scanner.nextLine().trim();

                if(input.equals("Y"))
                {
                    atmCardSessionBeanRemote.linkDepositAccountToAtmCard(customer.getAtmCard().getAtmCardId(), newDepositAccount.getDepositAccountId());
                    System.out.println("New deposit account linked to ATM card successfully!\n");
                }
                else
                {
                    System.out.println("New deposit account NOT linked to ATM card!\n");
                }
            }
            else
            {
                System.out.println("The customer does NOT have an existing ATM card.\n");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "!\n");
        }
        catch(DepositAccountExistException | GeneralException ex)
        {
            System.out.println("An error has occurred while creating the new deposit account: " + ex.getMessage() + "!\n");
        }
        catch(AtmCardLinkingException ex)
        {
            System.out.println("An error has occurred while linking the new deposit account to the customer's existing ATM card: " + ex.getMessage() + "!\n");
        }
    }
    
    private void doIssueAtmCard()
    {
        try
        {
            Boolean issueAtmCard = true;
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("*** RCBS - Teller Terminal :: Issue ATM Card ***\n\n");
            System.out.print("Enter Customer ID> ");
            Long customerId = scanner.nextLong();
            Customer customer = customerSessionBeanRemote.retrieveCustomerByCustomerId(customerId, true, true);
            System.out.println("Issuing ATM card for " + customer.getFirstName() + " " + customer.getLastName() + " (Identification Number: " + customer.getIdentificationNumber() + ")\n");
            scanner.nextLine();
            
            if(customer.getAtmCard() != null)
            {
                System.out.println("The customer has an existing ATM card! To issue a replacement card, the existing card must be unlinked from all deposit accounts and removed.");
                System.out.print("Remove Existing ATM Card? (Enter 'Y' to Remove)> ");
                String input = scanner.nextLine().trim();

                if(input.equals("Y"))
                {
                    atmCardSessionBeanRemote.removeAtmCard(customer.getAtmCard().getAtmCardId());
                    customer.setAtmCard(null); // Ensure our local copy of customer entity instance is synchronised
                    System.out.println("Existing ATM card removed successfully!\n");
                }
                else
                {
                    issueAtmCard = false;
                    System.out.println("Existing ATM card NOT removed!\n");
                }
            }
            
            if(issueAtmCard)
            {
                AtmCard newAtmCard = new AtmCard();
                System.out.print("Enter Card Number> ");
                newAtmCard.setCardNumber(scanner.nextLine().trim());
                System.out.print("Enter Name on Card> ");
                newAtmCard.setNameOnCard(scanner.nextLine().trim());
                System.out.print("Enter PIN> ");
                String pin = scanner.nextLine().trim();
                System.out.print("Enter PIN again> ");
                String reenterPin = scanner.nextLine().trim();
                
                if(pin.equals(reenterPin))
                {
                    List<Long> depositAccountIds = new ArrayList<>();
                    System.out.println("Linking existing deposit accounts to new ATM card> ");
                    
                    for(DepositAccount depositAccount:customer.getDepositAccounts())
                    {
                        System.out.print("Link account " + depositAccount.getAccountNumber() + "? (Enter 'Y' to Link)> ");
                        
                        if(scanner.nextLine().trim().equals("Y"))
                        {
                            depositAccountIds.add(depositAccount.getDepositAccountId());
                        }
                    }
                    
                    if(!depositAccountIds.isEmpty())
                    {
                        newAtmCard.setPin(pin);
                        newAtmCard.setEnabled(true);
                        newAtmCard = atmCardSessionBeanRemote.createNewAtmCard(newAtmCard, customerId, depositAccountIds);
                        System.out.println("New ATM card created successfully!: " + newAtmCard.getAtmCardId() + "\n");
                    }
                    else
                    {
                        System.out.println("An ATM card must be linked to at least one deposit account, unable to create new ATM card!\n");
                    }
                }
                else
                {
                    System.out.println("PIN mismatched, unable to create new ATM card!\n");
                }
            }
        }
        catch(CustomerNotFoundException | DepositAccountNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "!\n");
        }
        catch(AtmCardNotFoundException ex)
        {
            System.out.println("An error has occurred while removing customer's existing ATM card: " + ex.getMessage() + "!\n");
        }
        catch(AtmCardExistException | AtmCardLinkingException | GeneralException ex)
        {
            System.out.println("An error has occurred while creating the new ATM card: " + ex.getMessage() + "!\n");
        }
    }
}
