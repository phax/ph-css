#ph-css

Java CSS 2 and CSS 3 parser and builder. This version supercedes phloc-css.
The Maven plugin to compress CSS files at build time is located in project [ph-csscompress-maven-plugin](https://github.com/phax/ph-csscompress-maven-plugin).

ph-css has no logic for applying CSS onto HTML elements. This page shows some basic code examples that can be used to use the library. All snippets are free for any use. 

## News and noteworthy

  * v3.8.2 - small bugfix release
  * v3.8.1 - improvements for expression parsing and single line comment handling 
  * v3.8.0 - initial version in com.helger group and package 

#Maven usage
Add the following to your pom.xml to use this artifact:
```
<dependency>
  <groupId>com.helger</groupId>
  <artifactId>ph-css</artifactId>
  <version>3.8.2</version>
</dependency>
```

To build ph-css from source, Maven 3.0.4 is required. Any Maven version below does **NOT** work! 

#Main classes
As ph-css is mainly concerned about the grammatical structure of CSS, the main classes are for reading and writing CSS. Additionally it offers the possibility to traverse the elements in a CSS and make modifications on them.

##Coding paradigms used

The following list gives a short overview of special programming techniques that are used inside phloc-css

  * All interfaces are named starting with a capital 'I' (like in `ICSSVisitor`)
  * All member variables are private and use the Hungarian notation (like `m_aList`)
  * All methods returning collections (lists, sets, maps etc.) are returning copies of the content. This helps ensuring thread-safety (where applicable) but also means that modifying returned collections has no impact on the content of the "owning" object. In more or less all cases, there are "add", "remove" and "clear" methods available to modify the content of an object directly. All the methods returning copies of collections should be annotated with `@ReturnsMutableCopy`. In contrast if the inner collection is returned directly (for whatever reason) it should be annotated with `@ReturnsMutableObject`. If an unmodifiable collection is returned, the corresponding annotation is `@ReturnsImmutableObject` (e.g. for `Collections.unmodifiableList` etc.)
  * For all non primitive parameter the annotations `@Nonnull` or `@Nullable` are used, indicating whether a parameter can be `null` or not. Additionally for Strings and collections the annotation `@Nonempty` may be present, indicating that empty values are also not allowed.
  
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
  * Reading only a list of style information (as e.g. present in an HTML style element can be achieved using `com.helger.css.reader.CSSReaderDeclarationList`. The result in this case will be an instance of `com.helger.css.decl.CSSDeclarationList`. 

Both reading classes support the reading from either a `java.io.File`, a `java.io.Reader`, a `com.helger.commons.io.IInputStreamProvider` or a `String`. The reason why `java.io.InputStream` is not supported directly is because internally the stream is passed twice - first to determine a potentially available charset and second to read the content with the correctly determined charset. That's why an `IInputStreamProvider` must be used, that creates 2 unique input streams! 

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

---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
