package tools;

import com.sun.tools.doclets.Taglet;
import com.sun.javadoc.*;
import java.util.Map;


public class PostTaglet implements Taglet {

    @SuppressWarnings("unchecked")
	public static void register(Map tagletMap) {
		PostTaglet tag = new PostTaglet();
		Taglet t = (Taglet) tagletMap.get(tag.getName());
		if (t != null) {
			tagletMap.remove(tag.getName());
		}
		tagletMap.put(tag.getName(), tag);
	}
    
    public PostTaglet() {
    	return;
    }

	@Override
	public String getName() {
		return "post";
	}

	@Override
	public boolean inConstructor() {
		return true;
	}

	@Override
	public boolean inField() {
		return false;
	}

	@Override
	public boolean inMethod() {
		return true;
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
		return false;
	}

	@Override
	public boolean isInlineTag() {
		return true;
	}

	@Override
	public String toString(Tag tag) {
		return "<DT><B>POST:</B></DT><DD>" + tag.text() + "</DD>";
	}

	@Override
	public String toString(Tag[] arg0) {
		// Should never get here
		return null;
	}

}

