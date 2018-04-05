/*
 * Copyright (c) 2018. Justin Tullgren
 */

package com.justintullgren.spice.demo;

import com.justintullgren.spice.Maybe;

public class MaybeDemo {
	/**
	 * @return {@link Maybe} with a value of "Demo"
	 */
	public static Maybe<String> get() {
		return Maybe.from("Demo");
	}
}
