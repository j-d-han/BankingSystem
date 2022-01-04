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
public class AtmCardExistException extends Exception{

    /**
     * Creates a new instance of <code>AtmCardExistException</code> without
     * detail message.
     */
    public AtmCardExistException() {
    }

    /**
     * Constructs an instance of <code>AtmCardExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AtmCardExistException(String msg) {
        super(msg);
    }
}
