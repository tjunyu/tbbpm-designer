package com.taobao.tbbpm.designer.bpmContents.editors;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * 
 * @author junyu.wy
 * 
 */
public class XMLWhitespaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
