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
public class InvalidAtmCardCredentialException extends Exception{

    /**
     * Creates a new instance of <code>InvalidAtmCardCredentialException</code>
     * without detail message.
     */
    public InvalidAtmCardCredentialException() {
    }

    /**
     * Constructs an instance of <code>InvalidAtmCardCredentialException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAtmCardCredentialException(String msg) {
        super(msg);
    }
}
