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

---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
