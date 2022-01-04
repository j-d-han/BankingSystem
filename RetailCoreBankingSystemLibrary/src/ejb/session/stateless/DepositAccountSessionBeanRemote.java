/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DepositAccount;
import exception.CustomerNotFoundException;
import exception.DepositAccountExistException;
import exception.DepositAccountNotFoundException;
import exception.GeneralException;
import javax.ejb.Remote;

/**
 *
 * @author User
 */
@Remote
public interface DepositAccountSessionBeanRemote {
    public DepositAccount createNewDepositAccount(DepositAccount newDepositAccount, Long customerId) throws CustomerNotFoundException, GeneralException, DepositAccountExistException;

    public DepositAccount retrieveDepositAccountByDepositAccountId(Long depositAccountId) throws DepositAccountNotFoundException;

    public DepositAccount retrieveDepositAccountByDepositAccountId(Long depositAccountId, Boolean fetchCustomer, Boolean fetchAtmCard, Boolean fetchTransactions) throws DepositAccountNotFoundException;
}
