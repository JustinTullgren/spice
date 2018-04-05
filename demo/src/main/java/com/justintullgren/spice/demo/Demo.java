package com.justintullgren.spice.demo;


public class Demo {
	public static void main(String[] args) {
		MaybeDemo.get()
				.map(String::toUpperCase)
				.map(s -> {
					String[] parts = s.split("");
					StringBuilder sb = new StringBuilder();
					for (int i = parts.length - 1; i >= 0 ; i--) {
						sb.append(parts[i]);
					}
					return sb.toString();
				})
				.fold(
						() -> System.out.println("Null value!!!"),
						s -> System.out.println("The maybe result is: " + s)
				);


		TryCatchDemo.get()
				.map(s -> { throw new RuntimeException("borked"); })
				.fold(err -> System.out.println("The trycatch error is: " + err.getMessage()),
						s -> System.out.println("The trycatch value is: " + s));



	}
}
