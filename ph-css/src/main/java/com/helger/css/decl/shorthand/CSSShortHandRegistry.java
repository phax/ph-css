/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.css.decl.shorthand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.collection.ext.ICommonsSet;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.css.ECSSUnit;
import com.helger.css.property.CCSSProperties;
import com.helger.css.property.ECSSProperty;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.utils.ECSSColor;

/**
 * A static registry for all CSS short hand declarations (like
 * <code>border</code> or <code>margin</code>).
 *
 * @author Philip Helger
 * @since 3.7.4
 */
@ThreadSafe
public final class CSSShortHandRegistry
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static final ICommonsMap <ECSSProperty, CSSShortHandDescriptor> s_aMap = new CommonsHashMap <> ();

  static
  {
    // Register default short hands
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BACKGROUND,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_COLOR,
                                                                                              CCSSValue.TRANSPARENT),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_IMAGE,
                                                                                              CCSSValue.NONE),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_REPEAT,
                                                                                              CCSSValue.REPEAT),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_POSITION,
                                                                                              "top left"),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_ATTACHMENT,
                                                                                              CCSSValue.SCROLL),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_CLIP,
                                                                                              CCSSValue.BORDER_BOX),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_ORIGIN,
                                                                                              CCSSValue.PADDING_BOX),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BACKGROUND_SIZE,
                                                                                              "auto auto")));
    // Not supported by Firefox 28
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.FONT,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.FONT_STYLE,
                                                                                              CCSSValue.NORMAL),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.FONT_VARIANT,
                                                                                              CCSSValue.NORMAL),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.FONT_WEIGHT,
                                                                                              CCSSValue.NORMAL),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.FONT_SIZE,
                                                                                              CCSSValue.INHERIT),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.LINE_HEIGHT,
                                                                                              CCSSValue.NORMAL),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.FONT_FAMILY,
                                                                                              CCSSValue.INHERIT)));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER_TOP,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER_RIGHT,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER_BOTTOM,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER_LEFT,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.BORDER_WIDTH,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_WIDTH,
                                                                                              ECSSUnit.px (3))));
    registerShortHandDescriptor (new CSSShortHandDescriptorWithAlignment (ECSSProperty.BORDER_STYLE,
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_STYLE,
                                                                                                           CCSSValue.SOLID),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_STYLE,
                                                                                                           CCSSValue.SOLID),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_STYLE,
                                                                                                           CCSSValue.SOLID),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_STYLE,
                                                                                                           CCSSValue.SOLID)));
    registerShortHandDescriptor (new CSSShortHandDescriptorWithAlignment (ECSSProperty.BORDER_COLOR,
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_TOP_COLOR,
                                                                                                           ECSSColor.BLACK.getName ()),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_RIGHT_COLOR,
                                                                                                           ECSSColor.BLACK.getName ()),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_BOTTOM_COLOR,
                                                                                                           ECSSColor.BLACK.getName ()),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.BORDER_LEFT_COLOR,
                                                                                                           ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptorWithAlignment (ECSSProperty.MARGIN,
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.MARGIN_TOP,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.MARGIN_RIGHT,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.MARGIN_BOTTOM,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.MARGIN_LEFT,
                                                                                                           CCSSValue.AUTO)));
    registerShortHandDescriptor (new CSSShortHandDescriptorWithAlignment (ECSSProperty.PADDING,
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.PADDING_TOP,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.PADDING_RIGHT,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.PADDING_BOTTOM,
                                                                                                           CCSSValue.AUTO),
                                                                          new CSSPropertyWithDefaultValue (CCSSProperties.PADDING_LEFT,
                                                                                                           CCSSValue.AUTO)));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.OUTLINE,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.OUTLINE_WIDTH,
                                                                                              ECSSUnit.px (3)),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.OUTLINE_STYLE,
                                                                                              CCSSValue.SOLID),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.OUTLINE_COLOR,
                                                                                              ECSSColor.BLACK.getName ())));
    registerShortHandDescriptor (new CSSShortHandDescriptor (ECSSProperty.LIST_STYLE,
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.LIST_STYLE_TYPE,
                                                                                              CCSSValue.DISC),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.LIST_STYLE_POSITION,
                                                                                              CCSSValue.OUTSIDE),
                                                             new CSSPropertyWithDefaultValue (CCSSProperties.LIST_STYLE_IMAGE,
                                                                                              CCSSValue.NONE)));
  }

  private CSSShortHandRegistry ()
  {}

  public static void registerShortHandDescriptor (@Nonnull final CSSShortHandDescriptor aDescriptor)
  {
    ValueEnforcer.notNull (aDescriptor, "Descriptor");

    final ECSSProperty eProperty = aDescriptor.getProperty ();
    s_aRWLock.writeLocked ( () -> {
      if (s_aMap.containsKey (eProperty))
        throw new IllegalStateException ("A short hand for property '" +
                                         eProperty.getName () +
                                         "' is already registered!");
      s_aMap.put (eProperty, aDescriptor);
    });
  }

  @Nonnull
  @ReturnsMutableCopy
  public static ICommonsSet <ECSSProperty> getAllShortHandProperties ()
  {
    return s_aRWLock.readLocked ( () -> s_aMap.copyOfKeySet ());
  }

  public static boolean isShortHandProperty (@Nullable final ECSSProperty eProperty)
  {
    if (eProperty == null)
      return false;

    return s_aRWLock.readLocked ( () -> s_aMap.containsKey (eProperty));
  }

  @Nullable
  public static CSSShortHandDescriptor getShortHandDescriptor (@Nullable final ECSSProperty eProperty)
  {
    if (eProperty == null)
      return null;

    return s_aRWLock.readLocked ( () -> s_aMap.get (eProperty));
  }
}
