// Copyright (c) RiverMeadow, Inc. 2013
// All rights reserved.
//
// Coding Question for Finalist Candidates
// ---
//
// The code below was written by the sloppiest of your colleagues, before he left to join XYZ COMPANY.
// Your manager, though, thinks the guy was great and does not believe you when you say this is buggy,
// so you must first write unit tests to expose the bugs; then fix the code.  Then, ideally, add a couple
// more unit tests to make sure the code works, now and after the next refactoring.
//
// Please provide only the finished results (2x java classes: this one, fixed, and the unit tests) with
// comments/javadoc as appropriate.
//
// I appreciate this is a very simple exercise.  It should, however, be of value to you as a good indicator
// of your fit at RiverMeadow.   If it takes longer than 1-1.5 hour to complete, you will struggle here,
// even if you managed to somehow squeeze through the interview stage.
//
// Note:  the javadoc was written by Sloppy Joe too, so please don't assume that he covered ALL
// possible cases there.

package com.rivermeadow.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h3>PropertiesParser</h3>
 *
 * Resolves key substitution on a properties set; in other words, given:
 * <pre>FOO = foo
 * BAR = bar
 * baz = ${FOO} is ${BAR}
 * </pre>
 *
 * the following code will satisfy the assertion:
 * <pre> Properties props = Properties.load('my.properties');
 *   PropertiesParser parser = new PropertiesParser(props);
 *   parser.parse();
 *   assert("foo is bar".equals(props.getProperty("baz"));
 * </pre>
 */
public class PropertiesParser {
  
  /** A RegEx pattern for a key subst: ${key} */
  private final String PATTERN = "${.*}";
  private final Properties props;
  
  public PropertiesParser(Properties props) {
    this.props = props;
  }
  
  public void parse() {
    for (String key : props.stringPropertyNames()) {
      String value = props.getProperty(key);
      if (hasSubstKey(value))
        value = applyPattern(value);
    }
  }
  
  /**
   * Executes string substitution for the {@code value}
   *
   * @param value
   * @return the fully substituted value, if possible
   */
  protected String applyPattern(String value) {
    Pattern keyPattern = Pattern.compile(Pattern.quote(PATTERN));
    Matcher m = keyPattern.matcher(value);
    StringBuilder sb = new StringBuilder();
    
    if (m.find()) {
      int curPos = 0;
	sb.append(value.substring(curPos, m.start()))
        .append(extractKeyValue(m.group()));
      sb.append(value.substring(m.end()));
    }
    return sb.toString();
  }
  
  /** Replaces the subst key with its value, if available in {@code props} */
  // TODO: improve javadoc
  private String extractKeyValue(String key) {
	return "";
  }
  
  private boolean hasSubstKey(String value) {
	if (props.containsValue(value)) {
		return true;
	} else {
		return false;
	}
  }
}
