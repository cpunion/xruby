/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.codedom;

public interface CodeVisitor {
	public void visitBinaryOperator(String operator);
	public void visitUnaryOperator(String operator);
	public void visitMethodCall(String methodName, boolean hasReceiver, boolean hasBlock);

	public void visitGlobalVariableAssignmentOperator(String var);
	public void visitInstanceVariableAssignmentOperator(String var);
	public void visitClassVariableAssignmentOperator(String var);
	public void visitLocalVariableAssignmentOperator(String var);
	public void visitLocalVariableMultipleAssignmentOperator(String var);

	public void visitParameters(int size);
	public void visitParameterBegin(int index);
	public void visitParameterEnd();
	
	public void visitFloatExpression(float value);
	public void visitIntegerExpression(int value);
	public void visitStringExpression(String value);
	public void visitVariableExpression(String value); 
	public void visitTrueExpression();
	public void visitFalseExpression();
	public void visitNilExpression();
	public void visitSuperExpression();
	public void visitSelfExpression();
	
	public void visitMethodDefination(String methodName, int num_of_args, boolean has_asterisk_parameter);
	public void visitMethodDefinationParameter(String name);
	public void visitMethodDefinationAsteriskParameter(String name);
	public void visitMethodDefinationEnd(boolean last_statement_has_return_value);
	
	public void visitClassDefination(String className, String superClassName);
	public void visitClassDefinationEnd(String className, boolean last_statement_has_return_value);
	
	public void visitModuleDefination(String className);
	public void visitModuleDefinationEnd(String moduleName, boolean last_statement_has_return_value);
	
	public void visitTerminal();
	public void visitEof(boolean last_statement_has_return_value);
	
	public Object visitAfterIfCondition();
	public Object visitAfterIfBody(Object next_label, Object end_label, boolean is_last);
	
	public Object visitAfterCaseCondition();
	public Object visitAfterWhenCondition(Object case_value);
	public Object visitAfterWhenBody(Object next_label, Object end_label, boolean is_last);

	public Object visitAfterUnlessCondition();
	public Object visitAfterUnlessBody(Object next_label, Object end_label, boolean is_last);
	
	public Object visitPrepareRescueBegin();
	public Object visitPrepareRescueEnd(Object start);
	public Object visitRescueVariable(String name, Object exception_var);
	public Object visitAfterRescueBody(Object next_label, Object end_label, boolean is_last);
	public void visitRescueEnd(Object exception_var);

	public void visitArrayBegin(int size);
	public void visitArrayEnd();
	public void visitArrayElementBegin();
	public void visitArrayElementEnd(boolean asterisk);
	
	public void visitYield();
	public void visitYieldEnd();

	public void visitSymbolExpression(String value);
	public void visitGlobalVariableExpression(String value);
	public void visitClassVariableExpression(String value);
	public void visitInstanceVariableExpression(String value);
	public void visitCommandOutputExpression(String value);
	public void visitReturn();
	public void visitAliasGlobalVariable(String newName, String oldName);
	public void visitAliasMethod(String newName, String oldName);
	public void visitUndef(String name);

	public void visitHashBegin();
	public void visitHashEnd();
	public void visitHashElementBegin();
	public void visitHashElementEnd();

	public Object visitWhileBody();
	public void visitWhileConditionBegin(Object label_pair);
	public void visitWhileConditionEnd(Object label_pair, boolean is_until);
	
	public void visitBlock(int num_of_args, boolean has_asterisk_parameter);
	public void visitBlockEnd(boolean last_statement_has_return_value);
}
