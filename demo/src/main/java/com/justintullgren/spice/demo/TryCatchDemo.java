/*
 * Copyright (c) 2018. Justin Tullgren
 */

package com.justintullgren.spice.demo;

import com.justintullgren.spice.TryCatch;

public class TryCatchDemo {
	/**
	 * @return {@link TryCatch} with a value of "Demo"
	 */
	public static TryCatch<String> get() {
		return TryCatch.from("Demo");
	}
}
