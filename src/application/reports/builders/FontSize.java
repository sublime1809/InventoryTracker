/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

/**
 *
 * @author owner
 */
public enum FontSize {
    
    large(20),
    medium(14),
    average(12),
    small(10);
    
    private int size;
    
    private FontSize(int _size) {
        this.size = _size;
    }
    
    public int getSize() {
        return this.size;
    }
}
