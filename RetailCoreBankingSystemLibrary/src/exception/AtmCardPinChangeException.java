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
public class AtmCardPinChangeException extends Exception{

    /**
     * Creates a new instance of <code>AtmCardPinChangeException</code> without
     * detail message.
     */
    public AtmCardPinChangeException() {
    }

    /**
     * Constructs an instance of <code>AtmCardPinChangeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AtmCardPinChangeException(String msg) {
        super(msg);
    }
}
