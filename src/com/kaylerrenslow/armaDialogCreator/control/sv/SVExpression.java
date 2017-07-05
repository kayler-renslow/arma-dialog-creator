package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionEvaluationException;
import com.kaylerrenslow.armaDialogCreator.expression.ExpressionInterpreter;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 SVExpression value that is used for {@link ExpressionInterpreter} and is
 storable in {@link ControlProperty}. Only {@link Value.NumVal} is allowed, thus {@link #getValue()} will only return {@link Value.NumVal}.

 @author Kayler
 @since 07/15/2016. */
public class SVExpression extends SerializableValue implements SVNumericValue {

	private static final ResourceBundle bundle = Lang.getBundle("ExpressionBundle");

	private static final ExpressionInterpreter SHARED_INTERPRETER = ExpressionInterpreter.newInstance();

	/**
	 {@link ValueConverter} instance for {@link SVExpression} values. The {@link DataContext} parameter in the {@link ValueConverter#convert(DataContext, String...)}
	 method must contain a non-null entry with key {@link DataKeys#ENV}, or {@link IllegalArgumentException} will be thrown from {@link ValueConverter#convert(DataContext, String...)}.
	 */
	public static final ValueConverter<SVExpression> CONVERTER = new ValueConverter<SVExpression>() {

		@Override
		public SVExpression convert(DataContext context, @NotNull String... values) throws Exception {
			Env env = DataKeys.ENV.get(context);
			if (env == null) {
				throw new IllegalArgumentException("context key is missing:" + DataKeys.ENV);
			}
			return new SVExpression(values[0], env);
		}
	};

	private final Env env;
	private String exp;

	public SVExpression(@NotNull String exp, @NotNull Env env) throws ExpressionEvaluationException {
		this.env = env;
		setExpression(exp);
	}

	public void setExpression(@NotNull String exp) throws ExpressionEvaluationException {
		this.exp = exp;
		//check if valid
		getValue();
	}

	@NotNull
	public String getExpression() {
		return exp;
	}

	@NotNull
	public Value getValue() {
		Value v = null; //do not cache because the environment may change;
		try {
			v = SHARED_INTERPRETER.evaluate(exp, env).get(4, TimeUnit.SECONDS);
		} catch (InterruptedException ignore) {

		}
		if (v instanceof Value.NumVal) {
			return v;
		}
		throw new ExpressionEvaluationException(null,
				String.format(bundle.getString("unexpected_value_expected_f"), exp, bundle.getString("number"))
		);
	}

	@NotNull
	public Env getEnv() {
		return env;
	}

	/** Returns {@link #getValue()} and casts it to {@link Value.NumVal} and returns {@link Value.NumVal#v()} */
	public double getNumVal() {
		Value.NumVal value = (Value.NumVal) getValue();
		return value.v();
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return new SVExpression(exp, env);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Float;
	}

	@Override
	public String toString() {
		return exp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVExpression) {
			SVExpression other = (SVExpression) o;
			return this.exp.equals(other.exp);
		}
		return false;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{exp};
	}

	@Override
	public int toInt() {
		return (int) getNumVal();
	}

	@Override
	public double toDouble() {
		return getNumVal();
	}
}
