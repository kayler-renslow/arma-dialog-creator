/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionEvaluationException;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 Expression value that is used for {@link com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter} and is storable in {@link com.kaylerrenslow.armaDialogCreator.control.ControlProperty}
 @author Kayler
 @since 07/15/2016. */
public class Expression extends SerializableValue {
	
	/**
	 {@link ValueConverter} instance for Expression values. The {@link DataContext} parameter in the {@link ValueConverter#convert(DataContext, String...)}
	 method must contain a non-null entry with key {@link DataKeys#ENV}, or {@link IllegalArgumentException} will be thrown from {@link ValueConverter#convert(DataContext, String...)}.
	 */
	public static final ValueConverter<Expression> CONVERTER = new ValueConverter<Expression>() {
		
		@Override
		public Expression convert(DataContext context, @NotNull String... values) throws Exception {
			Env env = DataKeys.ENV.get(context);
			if (env == null) {
				throw new IllegalArgumentException("context key is missing:" + DataKeys.ENV);
			}
			return new Expression(values[0], env);
		}
	};
	
	private final Env env;
	
	public Expression(String exp, Env env) throws ExpressionEvaluationException {
		super(exp);
		this.env = env;
		setExpression(exp);
	}
	
	public void setExpression(String exp) throws ExpressionEvaluationException {
		valuesAsArray[0] = exp;
	}
	
	public String getExpression() {
		return valuesAsArray[0];
	}
	
	public Value getValue() {
		return ExpressionInterpreter.getInstance().evaluate(valuesAsArray[0], env);
	}
	
	public Env getEnv() {
		return env;
	}
	
	/** Returns {@link #getValue()} and casts it to {@link Value.NumVal} and returns {@link Value.NumVal#v()}*/
	public double getNumVal() {
		Value.NumVal value = (Value.NumVal) getValue();
		return value.v();
	}
	
	@Override
	public SerializableValue deepCopy() {
		return new Expression(valuesAsArray[0], env);
	}
	
	@Override
	public String toString() {
		return valuesAsArray[0];
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o instanceof Expression){
			Expression other = (Expression) o;
			return this.valuesAsArray[0].equals(other.valuesAsArray[0]);
		}
		return false;
	}
}
