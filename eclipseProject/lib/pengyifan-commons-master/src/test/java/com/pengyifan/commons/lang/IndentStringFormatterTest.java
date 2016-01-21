package com.pengyifan.commons.lang;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IndentStringFormatterTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  private final static String PARAGRAPH =
      "12 3456789 "
          + "12345 789"
          + "123";

  private final static int INDENT = 2;
  private final static int WIDTH = 10;
  private final static String EXPECTED_INDENT_NOSPACE =
      "  12 34567\n"
          + "89 12345 7\n"
          + "89123";
  private final static String EXPECTED_INDENT_SPACE =
      "  12\n"
          + "3456789\n"
          + "12345\n"
          + "789123";
  private final static String EXPECTED_NOINDENT_SPACE =
      "12 3456789\n"
          + "12345\n"
          + "789123";
  private final static String EXPECTED_HANGINDENT_SPACE =
      "12 3456789\n"
          + "  12345\n"
          + "  789123";
  private final static String EXPECTED_HANGINDENT_NOSPACE =
      "12 3456789\n"
          + "   12345 7\n"
          + "  89123";

  private IndentStringFormatter baseBuilder;

  @Before
  public void setUp() {
    baseBuilder = IndentStringFormatter.newFormat()
        .withWidth(WIDTH)
        .withIndent(INDENT);
  }

  @Test
  public void test_hangIndentConsiderSpace() {
    String actual = baseBuilder
        .withHangIndent(true)
        .format(PARAGRAPH);
    assertEquals(EXPECTED_HANGINDENT_SPACE, actual);
  }

  @Test
  public void test_hangIndentNotConsiderSpace() {
    String actual = baseBuilder
        .withHangIndent(true)
        .withSplitAtWhiteSpaces(false)
        .format(PARAGRAPH);
    assertEquals(EXPECTED_HANGINDENT_NOSPACE, actual);
  }

  @Test
  public void test_noHangIndent() {
    String actual = baseBuilder
        .withIndent(0)
        .format(PARAGRAPH);
    assertEquals(EXPECTED_NOINDENT_SPACE, actual);
  }

  @Test
  public void test_negativeIndent() {
    exception.expect(IllegalArgumentException.class);
    baseBuilder
        .withIndent(-1)
        .format(PARAGRAPH);
  }
  
  @Test
  public void test_negativeWidth() {
    exception.expect(IllegalArgumentException.class);
    baseBuilder
        .withWidth(-1)
        .format(PARAGRAPH);
  }
  
  @Test
  public void test_widthSmallerThanIndent() {
    exception.expect(IllegalArgumentException.class);
    baseBuilder
        .withIndent(12)
        .format(PARAGRAPH);
  }

  @Test
  public void test_indentConsiderSpace() {
    String actual = baseBuilder
        .withSplitAtWhiteSpaces(true)
        .format(PARAGRAPH);
    assertEquals(EXPECTED_INDENT_SPACE, actual);
  }

  @Test
  public void test_indentNotConsiderSpace() {
    String actual = baseBuilder
        .withSplitAtWhiteSpaces(false)
        .format(PARAGRAPH);
    assertEquals(EXPECTED_INDENT_NOSPACE, actual);
  }

}
