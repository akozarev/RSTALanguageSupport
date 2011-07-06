/*
 * 07/05/2011
 *
 * Copyright (C) 2011 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This code is licensed under the LGPL.  See the "license.txt" file included
 * with this project.
 */
package org.fife.rsta.ac.jsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fife.rsta.ac.html.AttributeCompletion;
import org.fife.rsta.ac.html.HtmlCompletionProvider;
import org.fife.rsta.ac.jsp.TldAttribute.TldAttributeParam;
import org.fife.ui.autocomplete.MarkupTagCompletion;


/**
 * Completion provider for JSP.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JspCompletionProvider extends HtmlCompletionProvider {

	/**
	 * Mapping of prefixes to TLD's.
	 */
	private Map prefixToTld;


	public JspCompletionProvider() {

		prefixToTld = new HashMap();

		String fileName = File.separatorChar=='/' ?
				"/users/robert/struts-2.2.3/lib/struts2-core-2.2.3.jar" :
				"c:/dev/struts/struts-2.2.3/lib/struts2-core-2.2.3.jar";
		File file = new File(fileName);
		if (!file.exists()) {
			file = new File("C:/temp/struts2-core-2.2.1.jar");
		}

		try {
			prefixToTld.put("s", new TldFile(this, file));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		setAutoActivationRules(false, "<:");

	}


	/**
	 * Overridden to handle JSP tags on top of standard HTML tags.
	 */
	protected List getAttributeCompletionsForTag(String tagName) {

		List list = super.getAttributeCompletionsForTag(tagName);

		if (list==null) {

			int colon = tagName.indexOf(':');
			if (colon>-1) {

				String prefix = tagName.substring(0, colon);
				tagName = tagName.substring(colon+1);

				TldFile tldFile = (TldFile)prefixToTld.get(prefix);
				if (tldFile!=null) {
					List attrs = tldFile.getAttributesForTag(tagName);
					if (attrs!=null && attrs.size()>-1) {
						list = new ArrayList();
						for (int i=0; i<attrs.size(); i++) {
							TldAttributeParam param = (TldAttributeParam)attrs.get(i);
							list.add(new AttributeCompletion(this, param));
						}
					}
				}

			}

		}

		return list;

	}


	protected List getTagCompletions() {

		List completions = new ArrayList(super.getTagCompletions());

		for (Iterator i=prefixToTld.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry)i.next();
			String prefix = (String)entry.getKey();
			TldFile tld = (TldFile)entry.getValue();
			for (int j=0; j<tld.getElementCount(); j++) {
				TldElement elem = tld.getElement(j);
				MarkupTagCompletion mtc = new MarkupTagCompletion(this,
						prefix + ":" + elem.getName());
				mtc.setDescription(elem.getDescription());
				completions.add(mtc);
			}
		}

		Collections.sort(completions);
		return completions;

	}


	protected boolean isValidChar(char ch) {
		return super.isValidChar(ch) || ch==':';
	}


}