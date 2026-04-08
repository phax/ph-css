# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ph-css is a Java-based CSS 3 parser and builder library (v8.1.2-SNAPSHOT). It parses CSS into a Java object model, supports traversal/modification via the visitor pattern, and can serialize back to CSS. The companion module `ph-csscompress-maven-plugin` provides build-time CSS compression.

## Build Commands

```bash
# Full build (requires Java 17+, Maven 3.x)
mvn clean install

# Run all tests
mvn test

# Run a single test class
mvn -pl ph-css test -Dtest=CSSReaderFuncTest

# Run a single test method
mvn -pl ph-css test -Dtest=CSSReaderFuncTest#testReadBadButSucceeding

# Build only the main library
mvn -pl ph-css clean install

# Build only the Maven plugin
mvn -pl ph-csscompress-maven-plugin clean install

# Check license headers
mvn license:check
```

## Module Structure

- **`ph-css/`** - Core library: CSS parsing, object model, and serialization
- **`ph-csscompress-maven-plugin/`** - Maven Mojo for CSS compression at build time

## Architecture

### Parser (JavaCC-generated)

Grammar files in `ph-css/src/main/jjtree/`:
- `ParserCSS30.jjt` - Main CSS 3.0 grammar
- `ParserCSSCharsetDetector.jjt` - Charset detection

JavaCC generates parser sources into `target/generated-sources/jjtree` and `target/generated-sources/javacc`. Do not edit generated parser files directly; modify the `.jjt` grammars instead.

### Core Package Layout (`com.helger.css`)

| Package | Purpose |
|---------|---------|
| `decl` | CSS object model: `CascadingStyleSheet`, style/media/font-face/keyframes rules, declarations |
| `decl.visit` | Visitor pattern interfaces and implementations for CSS traversal |
| `decl.shorthand` | Shorthand CSS property expansion |
| `reader` | `CSSReader` (full stylesheets), `CSSReaderDeclarationList` (inline styles) |
| `reader.errorhandler` | Parse error handlers (logging, collecting, throwing) |
| `writer` | `CSSWriter` for serializing the object model back to CSS text |
| `parser` | JavaCC-generated parser classes (do not edit manually) |
| `property` | CSS property definitions and metadata (`ECSSProperty`, `CCSSProperties`) |
| `propertyvalue` | Typed CSS values (colors, functions, URIs, etc.) |
| `media` | Media query model and `ECSSMedium` enum |
| `tools` | Utilities like `MediaQueryTools` |
| `utils` | Helpers for colors, numbers, URLs, rectangles |
| `annotation` | Custom annotations (`@DeprecatedInCSS`) |
| `handler` | Exception handlers for the parser |

### Read/Write Flow

1. **Reading**: `CSSReader.readFromString/File/Stream()` -> JavaCC parser -> `CascadingStyleSheet` object model
2. **Manipulation**: Visitor pattern via `ICSSVisitor` / `DefaultCSSVisitor` on the object model
3. **Writing**: `CSSWriter.getCSSAsString(CascadingStyleSheet)` -> CSS text output, controlled by `CSSWriterSettings` (minified/formatted, version)

### Key Dependencies

- `ph-commons` (12.1.5) - Collection types (`ICommonsList`, `CommonsArrayList`), I/O utilities, type conversion
- `ph-javacc-maven-plugin` (5.0.1) - Parser generation from `.jjt` grammars
- JUnit 4 for tests

## Test Resources

CSS test files live in `ph-css/src/test/resources/testfiles/css30/`:
- `good/` - Valid CSS files (used to verify successful parsing)
- `bad/` - Invalid CSS files (expected to fail parsing)
- `bad_but_succeeding/` - Invalid CSS that the parser recovers from
- `bad_but_browsercompliant/` - Invalid CSS that browsers accept

## Coding Conventions

See the global rules in `~/.claude/rules/naming.md` for Hungarian notation, formatting, and naming. Key project-specific points:
- OSGi bundle packaging (`<packaging>bundle</packaging>`) via `maven-bundle-plugin`
- Apache 2.0 license header required on all Java files (enforced by `license-maven-plugin`)
- Forbidden APIs plugin blocks unsafe/deprecated JDK usage
