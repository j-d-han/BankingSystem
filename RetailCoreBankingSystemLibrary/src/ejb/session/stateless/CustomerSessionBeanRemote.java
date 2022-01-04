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
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface CustomerSessionBeanRemote {
    public Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, GeneralException;

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public Customer retrieveCustomerByCustomerId(Long customerId, Boolean fetchDepositAccounts, Boolean fetchAtmCard) throws CustomerNotFoundException;
}
