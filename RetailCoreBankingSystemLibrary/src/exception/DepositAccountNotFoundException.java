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
public class DepositAccountNotFoundException extends Exception{

    /**
     * Creates a new instance of <code>DepositAccountNotFoundException</code>
     * without detail message.
     */
    public DepositAccountNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DepositAccountNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DepositAccountNotFoundException(String msg) {
        super(msg);
    }
}
