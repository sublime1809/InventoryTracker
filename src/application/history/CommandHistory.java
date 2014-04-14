/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.history;

import common.exceptions.CommandFailedException;
import java.util.LinkedList;

/**
 *
 * @author Trevor
 */
public class CommandHistory
{
	private LinkedList<Command> doneCommands;
	private LinkedList<Command> undoneCommands;

	public CommandHistory()
	{
		doneCommands = new LinkedList<Command>();
		undoneCommands = new LinkedList<Command>();
	}
	
	public void addNew(Command command)
	{
		doneCommands.add(command);
		undoneCommands.clear();
	}
	
	public boolean canUndo()
	{
		return doneCommands.size() > 0;
	}
	
	public Command getNextCommandToUndo()
	{
		undoneCommands.add(doneCommands.removeLast());
		return  undoneCommands.getLast();
	}
	
	public boolean canRedo()
	{
		return undoneCommands.size() > 0;
	}
	
	public Command getNextCommandToRedo()
	{
		doneCommands.add(undoneCommands.removeLast());
		return doneCommands.getLast();
	}
	
	
//	
//	public void doNew(Command command) throws CommandFailedException
//	{
//		command.execute();
//		doneCommands.add(command);
//		undoneCommands.clear();
//	}
	
	
//	
//	public void undo() throws CommandFailedException
//	{
//		undoneCommands.add(doneCommands.removeLast());
//		undoneCommands.getLast().undo();
//	}
//	
//	public void redo() throws CommandFailedException
//	{
//		doneCommands.add(undoneCommands.removeLast());
//		doneCommands.getLast().execute();
//	}
}
