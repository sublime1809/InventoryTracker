/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

/**
 *
 * @author owner
 */
public enum BulletType {
    dash("dash"),
    circle("circle");
    
    private String type;
    
    private BulletType(String _type) {
        type = _type;
    }
    
    @Override
    public String toString() {
        return type;
    }
}
