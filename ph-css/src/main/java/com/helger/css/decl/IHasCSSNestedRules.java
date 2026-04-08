package com.helger.css.decl;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.state.EChange;
import com.helger.collection.commons.ICommonsList;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Sanity interface for all objects having nested CSS rules.
 *
 * @param <IMPLTYPE>
 *        Implementation type
 * @author Philip Helger
 * @since 8.2.0
 */
public interface IHasCSSNestedRules<IMPLTYPE> {
  /**
   * Checks if this element has any nested rules.
   * @return <code>true</code> if at least one nested rule is present, <code>false</code> otherwise. Never
   * <code>null</code>.
   */
  boolean hasRules();

  /**
   * Gets the number of nested rules contained in this element.
   * @return The number of nested rules. Never negative.
   */
  @Nonnegative
  int getRuleCount();

  /**
   * Adds a rule to the list of nested rules. The rule is added at the end of the list.
   * @param aRule The rule to be added. Must not be <code>null</code>.
   * @return This element for chaining. Never <code>null</code>.
   */
  @NonNull IMPLTYPE addRule(@NonNull ICSSNestedRule aRule);

  /**
   * Adds a rule to the list of nested rules at the specified index.
   * @param nIndex The index at which the rule should be added. If equal to or greater than the current number of rules,
   * the rule will be added at the end of the list. Must not be negative.
   * @param aRule The rule to be added. Must not be <code>null</code>.
   * @return This element for chaining. Never <code>null</code>.
   */
  @NonNull IMPLTYPE addRule(@Nonnegative int nIndex, @NonNull ICSSNestedRule aRule);

  /**
   * Removes a rule from the list of nested rules, if present.
   * @param aRule The rule to be removed. Must not be <code>null</code>.
   * @return {@link EChange#CHANGED CHANGED} if the rule was removed, {@link EChange#UNCHANGED UNCHANGED} otherwise.
   * Never <code>null</code>.
   */
  @NonNull EChange removeRule(@NonNull ICSSNestedRule aRule);

  /**
   * Removes a rule from the list of nested rules at the specified index.
   * @param nRuleIndex The index of the rule to be removed. Values equal to or greater than the current number of rules
   * will be ignored. Must not be negative.
   * @return {@link EChange#CHANGED CHANGED} if the rule was removed, {@link EChange#UNCHANGED UNCHANGED} otherwise.
   * Never <code>null</code>.
   */
  @NonNull EChange removeRule(@Nonnegative int nRuleIndex);

  /**
   * Remove all nested rules.
   *
   * @return {@link EChange#CHANGED CHANGED} if any rule was removed, {@link EChange#UNCHANGED UNCHANGED} otherwise.
   * Never <code>null</code>.
   */
  @NonNull EChange removeAllRules();

  /**
   * Gets a nested rule at the specified index.
   * @param nRuleIndex The index of the rule to be retrieved. If equal to or greater than the current number of rules,
   * <code>null</code> will be returned. Must not be negative.
   * @return This rule for chaining. <code>null</code> if not found.
   */
  @Nullable ICSSNestedRule getRuleAtIndex(@Nonnegative int nRuleIndex);

  /**
   * Gets all nested rules contained in this element. Modifications to the returned list will not affect this style
   * rule, and vice versa.
   * @return A list of nested rules. Never <code>null</code>.
   */
  @NonNull
  @ReturnsMutableCopy
  ICommonsList<ICSSNestedRule> getAllRules();
}
