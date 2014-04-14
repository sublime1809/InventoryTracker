/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.exceptions;

/**
 *
 * @author chrystal
 */
public class InvalidAddException extends Exception {
    /**
	 * Creates the exception with the given message
	 * @param msg
	 */
	public InvalidAddException(String msg) {
        super(msg);
    }
}
