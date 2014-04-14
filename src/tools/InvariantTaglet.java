package tools;

import com.sun.tools.doclets.Taglet;
import com.sun.javadoc.*;
import java.util.Map;


public class InvariantTaglet implements Taglet {

    @SuppressWarnings("unchecked")
	public static void register(Map tagletMap) {
		InvariantTaglet tag = new InvariantTaglet();
		Taglet t = (Taglet) tagletMap.get(tag.getName());
		if (t != null) {
			tagletMap.remove(tag.getName());
		}
		tagletMap.put(tag.getName(), tag);
	}
    
    public InvariantTaglet() {
    	return;
    }

	@Override
	public String getName() {
		return "invariant";
	}

	@Override
	public boolean inConstructor() {
		return false;
	}

	@Override
	public boolean inField() {
		return false;
	}

	@Override
	public boolean inMethod() {
		return false;
	}

	@Override
	public boolean inOverview() {
		return false;
	}

	@Override
	public boolean inPackage() {
		return false;
	}

	@Override
	public boolean inType() {
		return true;
	}

	@Override
	public boolean isInlineTag() {
		return true;
	}

	@Override
	public String toString(Tag tag) {
		return "<DT><B>INVARIANT:</B></DT><DD>" + tag.text() + "</DD>";
	}

	@Override
	public String toString(Tag[] arg0) {
		// Should never get here
		return null;
	}

}

