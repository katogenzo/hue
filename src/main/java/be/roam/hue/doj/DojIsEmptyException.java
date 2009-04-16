/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.roam.hue.doj;

/**
 *
 * @author kevin
 */
public class DojIsEmptyException extends Exception {

    public DojIsEmptyException() {
        super("The Doj instance is empty");
    }

}
