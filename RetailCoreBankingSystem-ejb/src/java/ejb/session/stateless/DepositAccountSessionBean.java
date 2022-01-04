/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import exception.CustomerExistException;
import exception.CustomerNotFoundException;
import exception.DepositAccountExistException;
import exception.DepositAccountNotFoundException;
import exception.GeneralException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

/**
 *
 * @author User
 */
@Stateless
public class DepositAccountSessionBean implements DepositAccountSessionBeanRemote, DepositAccountSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public DepositAccount createNewDepositAccount(DepositAccount newDepositAccount, Long customerId) throws CustomerNotFoundException, GeneralException, DepositAccountExistException{
        try{
            Customer customer = customerSessionBean.retrieveCustomerByCustomerId(customerId);
             em.persist(newDepositAccount);

             //set bi-directional relationship
             newDepositAccount.setCustomer(customer);
             customer.getDepositAccounts().add(newDepositAccount);

             em.flush();
             return newDepositAccount;
        } catch(CustomerNotFoundException ex){
            throw new CustomerNotFoundException("Unable to create new deposit account as the customer record does not exist");
        } catch(PersistenceException ex){
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")){
                throw new DepositAccountExistException("DepositAccount with same account number already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
         
    }
    
    @Override
    public DepositAccount retrieveDepositAccountByDepositAccountId(Long depositAccountId) throws DepositAccountNotFoundException{
        DepositAccount depositAccount = em.find(DepositAccount.class, depositAccountId);
        if(depositAccount!=null){
            return depositAccount;
        } else{
            throw new DepositAccountNotFoundException("Deposit Account ID " + depositAccountId + " does not exist");
        }
    }
    
    @Override
    public DepositAccount retrieveDepositAccountByDepositAccountId(Long depositAccountId, Boolean fetchCustomer, Boolean fetchAtmCard, Boolean fetchTransactions)
            throws DepositAccountNotFoundException{
        DepositAccount depositAccount = retrieveDepositAccountByDepositAccountId(depositAccountId);
        if(fetchCustomer){
            depositAccount.getCustomer();
        }
        if(fetchAtmCard){
            depositAccount.getAtmCard();
        }
        if(fetchTransactions){
            depositAccount.getTransactions().size();
        }
        
        return depositAccount;
    }

    
}
