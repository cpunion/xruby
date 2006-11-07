/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.compiler.codedom;

public interface CodeVisitor {
	public void visitBinaryOperator(String operator);
	public Object visitAndBinaryOperatorLeft();
	public void visitAndBinaryOperatorRight(Object label);
	public Object visitOrBinaryOperatorLeft();
	public void visitOrBinaryOperatorRight(Object label);
	public void visitUnaryOperator(String operator);
	public void visitInclusiveRangeOperator();
	public void visitExclusiveRangeOperator();
	public void visitMethodCall(String methodName, boolean hasReceiver, String[] assignedCommons, String blockName);
	public void visitConstant(String name);
	public void visitCurrentNamespaceConstant(String name);
	public void visitTopLevelConstant(String name);

	public void visitGlobalVariableAssignmentOperator(String var, boolean rhs_is_method_call);
	public void visitInstanceVariableAssignmentOperator(String var, boolean rhs_is_method_call);
	public void visitClassVariableAssignmentOperator(String var, boolean rhs_is_method_call);
	public void visitLocalVariableAssignmentOperator(String var, boolean rhs_is_method_call, boolean is_multiple_assignment);
	public void visitCurrentNamespaceConstantAssignmentOperator(String var, boolean rhs_is_method_call, boolean is_multiple_assignment);
	public void visitTopLevelConstantAssignmentOperator(String var, boolean rhs_is_method_call, boolean is_multiple_assignment);
	public void visitConstantAssignmentOperator(String var, boolean rhs_is_method_call, boolean is_multiple_assignment);

	public void visitNoParameter();
	public void visitNoBlock(boolean is_in_super);
	public void visitBlockArgument();

	public void visitNoSuperClass();
	
	public void visitFloatExpression(double value);
	public void visitIntegerExpression(int value);
	public void visitLocalVariableExpression(String value); 
	public void visitTrueExpression();
	public void visitFalseExpression();
	public void visitNilExpression();
	public void visitSelfExpression();
	public void visitStringExpression(String value);
	public void visitRegexpExpression(String value);
	public void visitCommandOutputExpression(String value);
	
	public void visitStringExpressionWithExpressionSubstitutionBegin();
	public void visitStringExpressionWithExpressionSubstitution(String value);
	public void visitStringExpressionWithExpressionSubstitution();
	public void visitStringExpressionWithExpressionSubstitutionEnd();
	public void visitRegexpExpressionWithExpressionSubstitutionEnd();
	public void visitCommandOutputExpressionWithExpressionSubstitutionEnd();
	
	public void visitMethodDefination(String methodName, int num_of_args, boolean has_asterisk_parameter, int num_of_default_args, boolean is_singleton_method);
	public void visitMethodDefinationParameter(String name);
	public void visitMethodDefinationAsteriskParameter(String name);
	public void visitMethodDefinationBlockParameter(String name);
	public void visitMethodDefinationEnd(boolean last_statement_has_return_value);
	public Object visitMethodDefinationDefaultParameterBegin(int index);
	public void visitMethodDefinationDefaultParameterEnd(Object next_label);
	
	public void visitClassDefination1(String className);
	public void visitClassDefination2(String className);
	public void visitClassDefination3();
	public void visitClassDefination4();
	public void visitClassDefinationEnd(boolean last_statement_has_return_value);
	
	public void visitModuleDefination(String moduleName);
	public void visitModuleDefinationEnd(boolean last_statement_has_return_value);
	
	public void visitTerminal();
	public void visitEof(boolean last_statement_has_return_value);
	
	public Object visitAfterIfCondition();
	public Object visitAfterIfBody(Object next_label, Object end_label);
	
	public Object visitAfterCaseCondition();
	public Object visitAfterWhenCondition(Object case_value);
	public Object visitAfterWhenBody(Object next_label, Object end_label);

	public Object visitAfterUnlessCondition();
	public Object visitAfterUnlessBody(Object next_label, Object end_label);
	
	public Object visitPrepareRescueBegin();
	public Object visitPrepareRescueEnd(Object start);
	public Object visitRescueVariable(String name, Object exception_var);
	public Object visitAfterRescueBody(Object next_label, Object end_label);
	public void visitRescueEnd(Object exception_var, Object end_label);

	public void visitArrayBegin(int size, boolean notSingleAsterisk);
	public void visitArrayEnd();
	public void visitArrayElementBegin();
	public void visitArrayElementEnd(boolean asterisk, boolean is_method_call);
	
	public void visitYieldBegin();
	public void visitYieldEnd();

	public void visitSuperBegin();
	public void visitSuperEnd();

	public void visitSymbolExpression(String value);
	public void visitGlobalVariableExpression(String value);
	public void visitClassVariableExpression(String value);
	public void visitInstanceVariableExpression(String value);
	
	public void visitReturn();
	public void visitBreakBegin();
	public void visitBreakEnd();
	public void visitNextBegin();
	public void visitNextEnd();
	public void visitRedo();
	
	public void visitAliasGlobalVariable(String newName, String oldName);
	public void visitAliasMethod(String newName, String oldName);
	public void visitUndef(String name);

	public void visitHashBegin();
	public void visitHashEnd();
	public void visitHashElementBegin();
	public void visitHashElementEnd();
	
	public void visitWhileConditionBegin();
	public void visitWhileConditionEnd(boolean is_until);
	public void visitWhileBodyEnd();
	
	public String visitBlock(int num_of_args, boolean has_asterisk_parameter, int num_of_default_args);
	public String[] visitBlockEnd(String name, boolean last_statement_has_return_value);
	
	public void visitMrhs(int var, int index, boolean asterisk);
	public int visitMultipleAssignmentBegin(boolean single_lhs, boolean single_rhs);
	public void visitMultipleAssignmentEnd();
	
	public int visitNestedVariableBegin(boolean single_lhs);
	public void visitNestedVariableEnd();
}
