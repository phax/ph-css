#ph-css

[![Build Status](https://travis-ci.org/phax/ph-css.svg?branch=master)](https://travis-ci.org/phax/ph-css)
﻿
[![Join the chat at https://gitter.im/phax/ph-css](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/phax/ph-css?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


Java CSS 2 and CSS 3 parser and builder. This version supersedes phloc-css.
The Maven plugin to compress CSS files at build time is located in sub-project ph-csscompress-maven-plugin and described further down.

ph-css has no logic for applying CSS onto HTML elements. This page shows some basic code examples that can be used to use the library. All snippets are free for any use.

ph-css and ph-csscompress-maven-plugin are both licensed under the **Apache 2.0 license**.

## News and noteworthy

  * v5.0.3 - 2017-01-09
    * Binds to ph-commons 8.6.0
    * Added custom callback for illegal characters
  * v5.0.2 - 2016-10-21
    * Made tab size configurable (issue #29)
    * Improved media expressions (issue #30)
    * Allowing to disable consistency checks in class `CSSValue`
    * Made CSS interpretation warnings customizable with class `ICSSInterpretErrorHandler` (issue #33)
  * v5.0.1 - 2016-08-17
    * Using "modern java template" for JavaCC parser - results in quicker execution
    * Enhancement issue #27
    * Integrated `ph-csscompress-maven-plugin` into this repository
    * Bug fix wrong OutputStream (issue #28) 
  * v5.0.0 - 2016-06-12
    * Using JDK8 as the basis
    * removed explicit grammar for CSS 2.1 (issue #20)
    * Added browser compliant error handler
  * v4.1.6 - 2016-10-21
    * Made CSS interpretation warnings customizable with class `ICSSInterpretErrorHandler` (issue #33)
  * v4.1.5 - 2016-09-16
    * Improved media expressions (issue #30)
    * Integrated ph-csscompress-maven-plugin into this project (compatibility to 5.x)
  * v4.1.4 - Improved browser compliant parsing (issue #26)
  * v4.1.3 - Improved browser compliant parsing (issue #24)
  * v4.1.2 - Improved browser compliant parsing (issue #21)
  * v4.1.1 - Skipping style rules with invalid selectors in browser compliant mode; allowing "--" identifier prefix; improved unknown rule parsing (issues #17, #18, #19)
  * v4.1.0 - Improved calc parsing; extended support for page rules with page margin blocks
  * v4.0.1 - Added customizable newline characters
  * v4.0.0 - Updated to ph-commons 6.0.0 and added "browser compliant parsing mode"
  * v3.9.2 - Updated to ph-commons 5.7.1 and fix for some minor issues
  * v3.9.1 - Updated to ph-commons 5.6.0 and fix for some shorthand handling
  * v3.9.0 - API improvements and support for vendor specific "-calc" added
  * v3.8.2 - small bugfix release
  * v3.8.1 - improvements for expression parsing and single line comment handling 
  * v3.8.0 - initial version in com.helger group and package 

ph-css will be part of [Apache JMeter 3](https://github.com/apache/jmeter) :)

#Maven usage
Add the following to your pom.xml to use this artifact:
```
<dependency>
  <groupId>com.helger</groupId>
  <artifactId>ph-css</artifactId>
  <version>5.0.3</version>
</dependency>
```

To build ph-css from source, Maven 3.0.4 is required. Any Maven version below does **NOT** work! 

#Documentation
As ph-css is mainly concerned about the grammatical structure of CSS, the main classes are for reading and writing CSS. Additionally it offers the possibility to traverse the elements in a CSS and make modifications on them.

##Coding paradigms used

Please look at my personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodeingStyleguide.md) for the naming conventions used in this project.
  
##Basic Classes
A complete stylesheet is represented as an instance of `com.helger.css.decl.CascadingStyleSheet`. There is no difference between CSS 2.1 and CSS 3.0 instances. The class `com.helger.css.decl.CascadingStyleSheet` contains all top-level rules that may be present in a CSS:

  * Import rules (`@import`) - `com.helger.css.decl.CSSImportRule`
  * Namespace rules (`@namespace`) - `com.helger.css.decl.CSSNamespaceRule`
  * Style rules (e.g. `div{color:red;}`) - `com.helger.css.decl.CSSStyleRule`
  * Page rules (`@page`) - `com.helger.css.decl.CSSPageRule`
  * Media rules (`@media`) - `com.helger.css.decl.CSSMediaRule`
  * Font face rules (`@font-face`) - `com.helger.css.decl.CSSFontFaceRule`
  * Keyframes rules (`@keyframes`) - `com.helger.css.decl.CSSKeyframesRule`
  * Viewport rules (`@viewport`) - `com.helger.css.decl.CSSViewportRule`
  * Supports rules (`@supports`) - `com.helger.css.decl.CSSSupportsRule`
  * Any other unknown rules (`@foo`) - `com.helger.css.decl.CSSUnknownRule`

##CSS reading
ph-css contains two different possibilities to read CSS data:

  * Reading a complete CSS file can be achieved using `com.helger.css.reader.CSSReader`. The result in this case will be an instance of `com.helger.css.decl.CascadingStyleSheet`.
  * Reading only a list of style information (as e.g. present in an HTML `style` element) can be achieved using `com.helger.css.reader.CSSReaderDeclarationList`. The result in this case will be an instance of `com.helger.css.decl.CSSDeclarationList`. 

Both reading classes support the reading from either a `java.io.File`, a `java.io.Reader`, a `com.helger.commons.io.IInputStreamProvider` or a `String`. The reason why `java.io.InputStream` is not supported directly is because internally the stream is passed twice - first to determine a potentially available charset and second to read the content with the correctly determined charset. That's why an `IInputStreamProvider` must be used, that creates 2 unique input streams!

**Note:** reading from a `String` is possible in two different ways: one that requires a charset and one that doesn't. The version with the charset treats the `String` as if it was created from a byte array and tries to determine the charset like any other byte array based version. The version without a charset assumes that the `String` was already created with the correct charset, and any `@charset` rule contained in the CSS is ignored.

Since v3.8.2 the class `com.helger.css.reader.CSSReaderSettings` is present and encapsulates the CSS version, the fallback charset, the recoverable error handler (see below) and the unrecoverable error handler (also see below) in one settings object. This settings object can be used for multiple invocations of `CSSReader` and `CSSReaderDeclarationList`.

###Recoverable Errors
ph-css differentiates between recoverable errors and unrecoverable errors. An example for a recoverable error is e.g. an `@import` rule in the wrong place or a missing closing bracket within a style declaration. For recoverable errors a special handler interface `com.helger.css.reader.errorhandler.ICSSParseErrorHandler` is present. You can pass an implementation of this error handler to the CSS reader (see above). The following implementations are present by default (all residing in package `com.helger.css.reader.errorhandler`):

  * `DoNothingCSSParseErrorHandler` - silently ignoring all recoverable errors
  * `LoggingCSSParseErrorHandler` - logging all recoverable errors to an SLF4J logger
  * `ThrowingCSSParseErrorHandler` - throws a `com.helger.css.parser.ParseException` in case of a recoverable error which is afterwards handled by the unrecoverable error handler (see below). This can be used to enforce handling only 100% valid CSS files. This is the default setting, if no error handler is specified during reading.
  * `CollectingCSSParseErrorHandler` - collects all recoverable errors into a list of `com.helger.css.reader.errorhandler.CSSParseError` instances for later evaluation. 

Some error handlers can be nested so that a combination of a logging handler and a collecting handler can easily be achieved like:

```java
new CollectingCSSParseErrorHandler (new LoggingCSSParseErrorHandler ())
```

`DoNothingCSSParseErrorHandler` and `ThrowingCSSParseErrorHandler` cannot be nested because it makes no sense.

Both `CSSReader` and `CSSReaderDeclarationList` have the possibility to define a default recoverable error handler using the method `setDefaultParseErrorHandler(ICSSParseErrorHandler)`. If a reading method is invoked without an explicit `ICSSParseErrorHandler` than this default error handler is used. 

###Unrecoverable Errors

In case of an unrecoverable error, the underlying parser engine of JavaCC throws a `com.helger.css.parser.ParseException`. This exception contains all the necessary information on where the error occurred. In case of such an unrecoverable error, the result of the reading will always be `null` and the exception is not automatically propagated to the caller. To explicitly get notified when such a parse error occurs, the handler interface `com.helger.css.handler.ICSSParseExceptionHandler` is available. The available implementations are (all residing in package `com.helger.css.handler`):

  * `DoNothingCSSParseExceptionHandler` - silently ignore all unrecoverable errors
  * `LoggingCSSParseExceptionHandler` - log all unrecoverable errors to an SLF4J logger 

As there is at most one unrecoverable error per parse there is no collecting implementation of an `ICSSParseExceptionHandler` available. If it is desired to propagate the Exception to the caller you need to implement your own `ICSSParseExceptionHandler` subclass that throws an unchecked exception (one derived from `RuntimeException`). Example:

```java
  final ICSSParseExceptionHandler aThrowingExceptionHandler = new ICSSParseExceptionHandler () {
    public void onException (final ParseException ex) {
      throw new IllegalStateException ("Failed to parse CSS", ex);
    }
  };
```

Both `CSSReader` and `CSSReaderDeclarationList` have the possibility to define a default unrecoverable error handler using the method `setDefaultParseExceptionHandler(ICSSParseExceptionHandler)`. If a reading method is invoked without an explicit `ICSSParseExceptionHandler` than this default exception handler is used. 

##CSS iteration/visiting
Once a CSS file was successfully read, it can easily be iterated using the class `com.helger.css.decl.visit.CSSVisitor`. It requires a valid instance of `com.helger.css.decl.CascadingStyleSheet` as well as an implementation of `com.helger.css.decl.visit.ICSSVisitor`. The `CascadingStyleSheet` can be acquired either by reading from a file/stream or by creating a new one from scratch. For the `ICSSVisitor` it is recommended to use the class `com.helger.css.decl.visit.DefaultCSSVisitor` as the base class - this class contains empty implementations of all methods defined in the `ICSSVisitor` interface. To visit all declarations (e.g. `color:red;`) it is sufficient to simply override the method `public void onDeclaration (@Nonnull final CSSDeclaration aDeclaration)`. For details please have a look at the JavaDocs of `ICSSVisitor`. To start the visiting call `CSSVisitor.visitCSS (CascadingStyleSheet, ICSSVisitor)`.

A special visitor is present for URLs. URLs can occur on several places in CSS files, especially in the `@import` rules and within declarations (like in `background-image: url(../images/bg.gif)`). Therefore a special interface `com.helger.css.decl.visit.ICSSUrlVisitor` together with the empty default implementation `com.helger.css.decl.visit.DefaultCSSUrlVisitor` is provided. So to visit all URLs within a CSS call `CSSVisitor.visitCSSUrl(CascadingStyleSheet, ICSSUrlVisitor)`.

For modifying URLs (e.g. to adopt paths to a different environment) a special base class `com.helger.css.decl.visit.AbstractModifyingCSSUrlVisitor` is available. It offers the abstract method `protected abstract String getModifiedURI (@Nonnull String sURI)` to modify a URL and write the result back into the original `CascadingStyleSheet`. An example of how this can be used, can be found in the test method `com.helger.css.decl.visit.CSSVisitorDeclarationListTest.testModifyingCSSUrlVisitor ()`.

**Note**: it is safe to modify a CSS while iterating it, but only changes affecting children of the current node may be considered during the same iteration run.

##CSS writing
CSS writing is performed with the class `com.helger.css.writer.CSSWriter`. The most basic settings can be passed either directly to the constructor or using an instance of `com.helger.css.writer.CSSWriterSettings` which offers a quite find grained control of the output process. To write the content of a `CascadingStyleSheet` or any `ICSSWriteable` to an arbitrary `java.io.Writer`, the method `writeCSS` is what you need. If you want the CSS serialized to a `String` the shortcut method `getCSSAsString` is available. For the remaining configuration methods please check the JavaDoc.

By default all CSS code is pretty-printed. To create a minified version of the CSS code call `setOptimizedOutput (true)` and `setRemoveUnnecessaryCode (true)` on your `CSSWriterSettings` object.

##Data URL handling
Data URLs are URLs that directly contain the content inline. A regular use case is referencing small images directly inside a CSS. During CSS parsing no special handling for data URLs is added. Instead they are stored in a `String` like any other URL.

To special handle data URLs the class `com.helger.css.utils.CSSDataURLHelper` offers the possibility to check if a URL is a data URL via `public static boolean isDataURL (String)`. If this method returns `true` the method `public static CSSDataURL parseDataURL (String)` can be used to extract all the information contained in the data URL. This method returns `null` if the passed URL is not a data URL.  

##Shorthand property handling
A *CSS shorthand property* is a property that consists of multiple values. Classical examples are `margin` or `border`. ph-css contains support for selected shorthand properties. All shorthand related classes can be found in package `com.helger.css.decl.shorthand`. The supported shorthand properties are:

  * `background`
  * `font`
  * `border`
  * `border-top`
  * `border-right`
  * `border-bottom`
  * `border-left`
  * `border-width`
  * `border-style`
  * `border-color`
  * `margin`
  * `padding`
  * `outline`
  * `list-style`

All of these shorthand properties are registered in class `CSSShortHandRegistry` and you can manually register your own shorthand descriptors. The `CSSShortHandRegistry` allows you to split a single `CSSDeclaration` like `border:1px dashed` into the corresponding "sub-declarations":

```java
  // Parse a dummy declaration
  final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border:1px dashed", ECSSVersion.CSS30).getDeclarationAtIndex (0);

  // Get the Shorthand descriptor for "border"    
  final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER);

  // And now split it into pieces
  final List <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
```

In the above example, `aSplittedDecls` will contain 3 elements with the following content:

  * `border-width:1px`
  * `border-style:dashed`
  * `border-color:black` 

Even though no color value was provided, the default value `black` is returned. For all "sub-declarations", sensible default values are defined.

##CSS utilities
ph-css contains a multitude of small utility class covering different aspects of CSS
  * `com.helger.css.utils.CSSColorHelper` contains methods to read and write the different types of CSS color values (rgb, rgba, hsl, hsla and hex value)
  * `com.helger.css.utils.ECSSColor` contains the basic CSS colors as an enumeration
  * `com.helger.css.ECSSUnit` contains all the default CSS units (like. `px` or `em`)
  * `com.helger.css.utils.CSSNumberHelper` contains methods for handling the combination of numeric values and units.
  * `com.helger.css.utils.CSSRectHelper` contains methods for handling CSS `rect` values.
  * `com.helger.css.tools.MediaQueryTools` provides shortcut methods for wrapping a complete `CascadingStyleSheet` in one or more media queries

#Code Examples
  * [Reading a CSS 3.0 file](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiReadCSS.java)
  * [Writing a CSS 3.0 file](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiWriteCSS.java)
  * [Creating a @font-face rule from scratch](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiCreateFontFaceRule.java)
    * The code creates a CSS @font-face rule that looks like this:
    ```css    
    @font-face {
      font-family: "Your typeface";
      src: url("path/basename.eot");
      src: local("local font name"),
           url("path/basename.woff") format("woff"),
           url("path/basename.otf") format("opentype"),
           url("path/basename.svg#filename") format("svg");
    }
    ```
  * [Read the CSS content of a HTML style attribute](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiReadFromHtml.java)
    * Reads the CSS content of `sStyle` as CSS 3.0 and creates a `CSSDeclarationList` from it
  * [Visiting all declarations contained in an HTML style attribute](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiVisitFromHtml.java)
    * Similar to the above example, but visiting all declarations and printing them on stdout. Two different approaches are shown: first all declarations are retrieved via the native API, and second a custom visitor is used to determine all declarations. The result of this method looks like this:
    ```    
    color: red (not important)
    background: fixed (important)
    ```
  * [Visit all URLs contained in a CSS](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiVisitUrls.java)
    * Read a CSS from a String and than extracts all contained URLs. The output looks like this:
    ```
    Import: foobar.css - source location reaches from [1/1] up to [1/21]
    background - references: a.gif - source location reaches from [2/22] up to [2/31]
    background-image - references: /my/folder/b.gif - source location reaches from [3/25] up to [3/47]
    ``` 
  * [Visit all URLs (incl.data URLs) contained in a CSS](https://github.com/phax/ph-css/blob/master/ph-css/src/test/java/com/helger/css/supplementary/wiki/WikiVisitDataUrls.java)
    * Read a CSS from a String and than extracts all contained URLs with special focus on data URLs. The output looks like this:
    ```
    Import: /folder/foobar.css
    background - references data URL with 158 bytes of content
    background-image - references regular URL: /my/folder/b.gif
    ```

#Known shortcomings
The following list gives an overview of known shortcomings in ph-css

  * Escaped characters (like \26) are not interpreted correctly.

#ph-csscompress-maven-plugin

A Maven plugin to compress CSS files at build time using ph-css.

It requires Java 8 and Maven 3 to run.

## Maven configuration
```xml
      <plugin>
        <groupId>com.helger.maven</groupId>
        <artifactId>ph-csscompress-maven-plugin</artifactId>
        <version>5.0.3</version>
        <executions>
          <execution>
            <goals>
              <goal>csscompress</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <forceCompress>false</forceCompress>
          <removeUnnecessaryCode>true</removeUnnecessaryCode>
          <quoteURLs>true</quoteURLs>
          <verbose>true</verbose>
          <sourceDirectory>${basedir}/src/main/resources</sourceDirectory>
        </configuration>
      </plugin>
```

Configuration items are:

  * `File` **sourceDirectory**  
     The directory where the CSS files reside. It must be an existing directory.  
     Defaults to `${basedir}/src/main/resources`
  * `boolean` **recursive**  
     Should all directories be scanned recursively for CSS files to compress? 
     Defaults to `true`
  * `boolean` **removeUnnecessaryCode**  
     Should unnecessary code be removed (e.g. rules without declarations)? 
     Defaults to `false`
  * `boolean` **quoteURLs**  
     Should URLs always be quoted? If false they are only quoted when absolutely necessary. 
     Defaults to `false`
  * `boolean` **writeNamespaceRules**  
     Should `@namespace` rules be written? 
     Defaults to `true`
  * `boolean` **writeFontFaceRules**  
     Should `@font-face` rules be written? 
     Defaults to `true`
  * `boolean` **writeKeyframesRules**  
     Should `@keyframes` rules be written? 
     Defaults to `true`
  * `boolean` **writeMediaRules**  
     Should `@media` rules be written? 
     Defaults to `true`
  * `boolean` **writePageRules**  
     Should `@page` rules be written? 
     Defaults to `true`
  * `boolean` **writeViewportRules**  
     Should `@viewport` rules be written? 
     Defaults to `true`
  * `boolean` **writeSupportsRules**  
     Should `@supports` rules be written? 
     Defaults to `true`
  * `boolean` **writeUnknownRules**  
     Should unknown `@` rules be written? 
     Defaults to `true`
  * `boolean` **forceCompress**  
     Should the CSS files be compressed, even if the timestamp of the compressed file is newer than the timestamp of the original CSS file? 
     Defaults to `false`
  * `boolean` **verbose**  
     If true some more output is emitted. 
     Defaults to `false`
  * `boolean` **browserCompliantMode** (since 1.4.0)
     If true the "browser compliant mode" for parsing is selected.
     Defaults to `false`.   
  * `String` **sourceEncoding**  
     The encoding of the source CSS files to be used for reading the CSS file in case neither a @charset rule nor a BOM is present.
     Defaults to `UTF-8`
  * `String` **targetFileExtension**  
     The filename extension that should be used for the minified/compressed CSS file. 
     Defaults to `.min.css`
  * `String` **targetEncoding** (since 1.4.0)
     The encoding of the target CSS files to be used for writing the CSS file.
     Defaults to `UTF-8`.  

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodeingStyleguide.md) |
On Twitter: <a href="https://twitter.com/philiphelger">@philiphelger</a>
