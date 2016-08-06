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
 @author Kayler
 Expression value that is used for {@link com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter} and is storable in {@link com.kaylerrenslow.armaDialogCreator.control.ControlProperty}
 Created on 07/15/2016. */
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
}
