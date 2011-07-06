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

import org.fife.ui.autocomplete.MarkupTagCompletion;


/**
 * An element defined in a TLD.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class TldElement extends MarkupTagCompletion {


	public TldElement(JspCompletionProvider provider, String name, String desc){
		super(provider, name);
		setDescription(desc);
	}


}