/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author User
 */
public class DepositAccountExistException extends Exception{

    /**
     * Creates a new instance of <code>DepositAccountExistException</code>
     * without detail message.
     */
    public DepositAccountExistException() {
    }

    /**
     * Constructs an instance of <code>DepositAccountExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DepositAccountExistException(String msg) {
        super(msg);
    }
}
