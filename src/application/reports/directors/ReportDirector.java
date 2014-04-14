/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.reports.builders.ReportBuilder;

/**
 *
 * @author owner
 */
public abstract class ReportDirector {
    
    private ReportBuilder builder;
    
    /**
     * This will populate the report will all it's necessary information 
     * based on the data in the system
     */
    public abstract void buildReport();
    
    /**
     * This will call buildReport if it wasn't already built and print the file
     * @return String location of the file
     */
    public abstract String printReport();
    
    /**
     * This will set the type of builder to be used to print
     * @param _builder - builder to be utilized to build report (HTML or PDF currently)
     */
    public void setBuilder(ReportBuilder _builder) {
        this.builder = _builder;
    }
    
    /**
     * This will return the type of builder if it needs to be utilized
     * in the child classes
     * @return builder that is being used by the director
     */
    protected ReportBuilder getBuilder() {
        return this.builder;
    }
}
