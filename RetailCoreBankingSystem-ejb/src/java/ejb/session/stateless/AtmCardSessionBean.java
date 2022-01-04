/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import exception.AtmCardExistException;
import exception.AtmCardLinkingException;
import exception.AtmCardNotFoundException;
import exception.AtmCardPinChangeException;
import exception.CustomerNotFoundException;
import exception.DepositAccountExistException;
import exception.DepositAccountNotFoundException;
import exception.GeneralException;
import exception.InvalidAtmCardCredentialException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author User
 */
@Stateless
public class AtmCardSessionBean implements AtmCardSessionBeanRemote, AtmCardSessionBeanLocal {

    @EJB
    private DepositAccountSessionBeanLocal depositAccountSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public AtmCard createNewAtmCard(AtmCard newAtmCard, Long customerId, List<Long> depositAccountIds) throws CustomerNotFoundException, 
            DepositAccountNotFoundException, AtmCardLinkingException, AtmCardExistException, GeneralException{
        try{
            em.persist(newAtmCard);
            Customer customer = customerSessionBean.retrieveCustomerByCustomerId(customerId);
            
            //set relationships
            customer.setAtmCard(newAtmCard);
            newAtmCard.setCustomer(customer);
            
            for(Long id: depositAccountIds){
                DepositAccount depositAccount = depositAccountSessionBean.retrieveDepositAccountByDepositAccountId(id);
                
                if(depositAccount.getCustomer().equals(customer)){
                    newAtmCard.getDepositAccounts().add(depositAccount);
                    depositAccount.setAtmCard(newAtmCard);
                } else {
                    throw new AtmCardLinkingException("ATM card holder is different from deposit account holder, unable to create new ATM card");
                }
            }
            
            em.flush();
            
            return newAtmCard;
        } catch(CustomerNotFoundException ex){
            throw new CustomerNotFoundException("Unable to create new Atm Card as the customer record does not exist");
        } catch(DepositAccountNotFoundException ex){
            throw new DepositAccountNotFoundException("Unable to create new Atm Card as the deposit record does not exist");
        } catch (AtmCardLinkingException ex) {
            throw new AtmCardLinkingException(ex.getMessage());
        } catch (PersistenceException ex){
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")){
                throw new AtmCardExistException("Atm Card with same card number already exist");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public AtmCard retrieveAtmCardByAtmCardId(Long atmCardId)throws AtmCardNotFoundException{
        AtmCard atmCard = em.find(AtmCard.class, atmCardId);
        if(atmCard!=null){
            return atmCard;
        } else{
            throw new AtmCardNotFoundException("Atm Card ID " + atmCardId + " does not exist");
        }
    }
    
    @Override
    public AtmCard retrieveAtmCardByAtmCardId(Long atmCardId, Boolean fetchCustomer, Boolean fetchDepositAccounts)throws AtmCardNotFoundException{
        AtmCard atmCard = retrieveAtmCardByAtmCardId(atmCardId);
        
        if(fetchCustomer){
            atmCard.getCustomer();
        }
        if(fetchDepositAccounts){
            atmCard.getDepositAccounts().size();
        }
        
        return atmCard;
    }
    
    @Override
    public void linkDepositAccountToAtmCard(Long atmCardId, Long depositAccountId)throws AtmCardLinkingException{
        try{
            AtmCard atmCard = retrieveAtmCardByAtmCardId(atmCardId);
            DepositAccount depositAccount = depositAccountSessionBean.retrieveDepositAccountByDepositAccountId(depositAccountId);
            
            if(atmCard.getCustomer().equals(depositAccount.getCustomer())){
                if(!atmCard.getDepositAccounts().contains(depositAccount)){
                    atmCard.getDepositAccounts().add(depositAccount);
                    depositAccount.setAtmCard(atmCard);
                } else{
                    throw new AtmCardLinkingException("The deposit account is already linked to the ATM card");
                }
            } else {
                throw new AtmCardLinkingException("ATM card holder is different from deposit account holder");
            }
        } catch(AtmCardNotFoundException | DepositAccountNotFoundException ex){
            throw new AtmCardLinkingException(ex.getMessage());
        }
        
    }
    
    @Override
    public void removeAtmCard(Long atmCardId)throws AtmCardNotFoundException{
            AtmCard atmCard = retrieveAtmCardByAtmCardId(atmCardId);            
            atmCard.getCustomer().setAtmCard(null);
            
            for(DepositAccount depositAccount:atmCard.getDepositAccounts()){
                depositAccount.setAtmCard(null);
            }
            
            atmCard.getDepositAccounts().clear();
            
            em.remove(atmCard);
    }
    
    
    @Override
    public List<DepositAccount> retreiveLinkedDepositAccountsByAtmCardId(Long atmCardId) throws AtmCardNotFoundException{
        AtmCard atmCard = retrieveAtmCardByAtmCardId(atmCardId);  
        List<DepositAccount> depositAccounts  = atmCard.getDepositAccounts();
        
        for(DepositAccount depositAccount: depositAccounts){
            em.detach(depositAccount);
            depositAccount.setCustomer(null);
            depositAccount.setAtmCard(null);
        }
        
        return depositAccounts;
    }
    
    @Override
    public Long authenticateAtmCard(String atmCardNumber, String pin) throws InvalidAtmCardCredentialException{
        Query query = em.createQuery("SELECT ac FROM AtmCard ac WHERE ac.cardNumber = :inCardNumber");
        query.setParameter("inCardNumber", atmCardNumber);
        
        try{
            AtmCard atmCard = (AtmCard)query.getSingleResult();
            
            if(atmCard.getPin().equals(pin)){
                return atmCard.getAtmCardId();
            } else{
                throw new InvalidAtmCardCredentialException("Invalid ATM card PIN");
            }
        } catch(NoResultException | NonUniqueResultException ex){
            throw new InvalidAtmCardCredentialException("Invalid ATM card number");
        } 
        
    }
    
    @Override
    public void changePin(Long atmCardId, String currentPin, String newPin) throws AtmCardNotFoundException, AtmCardPinChangeException{
        AtmCard atmCard = retrieveAtmCardByAtmCardId(atmCardId);
        if(atmCard.getPin().equals(currentPin)){
            atmCard.setPin(newPin);
        } else{
            throw new AtmCardPinChangeException("Current PIN is invalid");
        }
    }
}
