/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import exception.CustomerExistException;
import exception.CustomerNotFoundException;
import exception.GeneralException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

/**
 *
 * @author User
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Customer createNewCustomer(Customer newCustomer)throws CustomerExistException, GeneralException{
        try{
            em.persist(newCustomer);
            em.flush();
            return newCustomer;
        } catch(PersistenceException ex){
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")){
                throw new CustomerExistException("Customer with same identification number already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId)throws CustomerNotFoundException{
        Customer customer = em.find(Customer.class, customerId);
        if(customer!=null){
            return customer;
        } else{
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist");
        }
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId, Boolean fetchDepositAccounts, Boolean fetchAtmCard)throws CustomerNotFoundException{
        Customer customer = retrieveCustomerByCustomerId(customerId);
        if(fetchDepositAccounts){
            customer.getDepositAccounts().size();
        }
        if(fetchAtmCard){
            customer.getAtmCard();
        }
        
        return customer;
    }

    
}
