package com.justintullgren.spice;

public interface Function<Ret, Arg> {
	Ret apply(Arg arg);
}
