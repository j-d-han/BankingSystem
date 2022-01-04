/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.DepositAccount;
import exception.AtmCardExistException;
import exception.AtmCardLinkingException;
import exception.AtmCardNotFoundException;
import exception.AtmCardPinChangeException;
import exception.CustomerNotFoundException;
import exception.DepositAccountNotFoundException;
import exception.GeneralException;
import exception.InvalidAtmCardCredentialException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface AtmCardSessionBeanLocal {

    public AtmCard createNewAtmCard(AtmCard newAtmCard, Long customerId, List<Long> depositAccountIds) throws CustomerNotFoundException, DepositAccountNotFoundException, AtmCardLinkingException, AtmCardExistException, GeneralException;

    public AtmCard retrieveAtmCardByAtmCardId(Long atmCardId) throws AtmCardNotFoundException;

    public void linkDepositAccountToAtmCard(Long atmCardId, Long depositAccountId) throws AtmCardLinkingException;

    public void removeAtmCard(Long atmCardId) throws AtmCardNotFoundException;

    public List<DepositAccount> retreiveLinkedDepositAccountsByAtmCardId(Long atmCardId) throws AtmCardNotFoundException;

    public Long authenticateAtmCard(String atmCardNumber, String pin) throws InvalidAtmCardCredentialException;

    public void changePin(Long atmCardId, String currentPin, String newPin) throws AtmCardNotFoundException, AtmCardPinChangeException;
    
}
