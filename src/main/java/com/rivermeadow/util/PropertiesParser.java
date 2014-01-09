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

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
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
  private final String PATTERN = "\\$\\{[^\\$\\{\\}]+\\}";
  private final Pattern pattern = Pattern.compile(PATTERN);
  private final Properties props;

  // for circular reference check
  private final ThreadLocal<Set<String>> parsedKey = new ThreadLocal<Set<String>>();
  
  public PropertiesParser(Properties props) {
    this.props = props;
  }
  
  public void parse() {
    parsedKey.set(new HashSet<String>());
    for (String key : props.stringPropertyNames()) {
      String value = props.getProperty(key);
      if (hasSubstKey(value)) {
        value = applyPattern(value);
        props.setProperty(key, value);
      }
    }
    parsedKey.remove();
  }
  
  /**
   * Executes string substitution for the {@code value}
   *
   * @param value key
   * @return the fully substituted value, if possible
   */
  protected String applyPattern(String value) {
    Matcher m = pattern.matcher(value);
    StringBuilder sb = new StringBuilder();

    int curPos = 0;
    while (m.find()) {
	    sb.append(value.substring(curPos, m.start()))
            .append(extractKeyValue(m.group()));
        curPos = m.end();
    }

    sb.append(value.substring(curPos));
    return sb.toString();
  }
  
  /** Replaces the subst key with its value, if available in {@code props} */
  // TODO: improve javadoc
  private String extractKeyValue(String key) {
    if (key.length() > 3) {
        Set<String> parsed = parsedKey.get();
        if (parsed.contains(key)) {
            // circular reference, break;
            return key;
        }
        parsed.add(key);
        String name = key.substring(2, key.length() - 1);
        String property = props.getProperty(name);
        if (hasSubstKey(property)) {
            property = applyPattern(property);
            props.setProperty(name, property);
        }
        return property;
    } else {
        return key;
    }
  }
  
  private boolean hasSubstKey(String value) {
    return pattern.matcher(value).find();
  }
}
