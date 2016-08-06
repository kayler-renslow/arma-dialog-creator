/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main.lang;

/**
 @author Kayler
 Lang class for any custom FX controls
 Created on 07/15/2016. */
public interface FXControlLang {
	interface InputField {
		String SUBMIT_BTN_TOOLTIP = "Submit";
		
		/** Lang strings for InputFieldDataChecker instances */
		interface DataCheckers {
			String NO_VALUE = "No value entered.";

			interface ArmaString {
				String TYPE_NAME = "String";
				String MISSING_QUOTE = "Missing quote.";
			}

			interface Double {
				String NOT_A_NUMBER = "Not a number.";
				String TYPE_NAME = "Floating Point Number";
			}

			interface Identifier {
				String NOT_IDENTIFIER = "Not an identifier. (Start with: letter, _, $. Then can have: letter, _, $, number)";
				String TYPE_NAME = "Identifier";
			}

			interface Integer {
				String NOT_INTEGER = "Not an integer.";
				String TYPE_NAME = "Integer";
			}

			interface Expression {
				String TYPE_NAME = "Expression";
				String UNKNOWN_ERROR = "Unknown Error";
			}
			
			interface StringChecker {
				String TYPE_NAME = "String";
			}
		}
	}
}

