package com.justintullgren.spice;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type that represents the disjointing paths of a producer. The semantics of {@link Either} differs
 * from {@link Maybe} in that {@link Either} always calls back with a value.
 * @param <T> the type to elevate.
 */
public abstract class Either<T> {
	protected final T value;
	Either(@Nullable final T value) {
		this.value = value;
	}

	/**
	 * @param mapper transform function.
	 * @param <O> the type to transform to.
	 * @return {@link Maybe} with the mapper result or a maybe of nothing.
	 */
	public abstract <O> Either<O> map(@NotNull Function<O, T> mapper);

	/**
	 * @param defaultValue to use if a null value occurs during transformation.
	 * @param successCallback will be called every time, either with the successful result or the default value.
	 */
	public abstract void fold(T defaultValue, @Nullable Action<T> successCallback);

	public Maybe<T> toMaybe() {
		return Maybe.from(value);
	}

	public TryCatch<T> toTryCatch() {
		return TryCatch.from(value);
	}

	/**
	 * @param source value to elevate.
	 * @param <A> the type to elevate.
	 * @return {@link Either}
	 */
	public static <A> Either<A> from(@Nullable A source) {
		if(source == null) {
			return new Left<>();
		} else {
			return new Right<>(source);
		}
	}

	private static class Left<A> extends Either<A> {
		Left() {
			super(null);
		}

		@Override
		public <O> Either<O> map(@NotNull final Function<O, A> mapper) {
			return new Left<>();
		}

		@Override
		public void fold(final A defaultValue, @Nullable final Action<A> successCallback) {
			if(defaultValue == null) {
				throw new IllegalArgumentException("Default value must be provided.");
			}

			if(successCallback != null) {
				successCallback.apply(defaultValue);
			}
		}
	}

	private static class Right<A> extends Either<A> {
		Right(final A value) {
			super(value);
		}

		@Override
		public <O> Either<O> map(@NotNull final Function<O, A> mapper) {
			if(value == null) {
				return new Left<>();
			}

			O result = mapper.apply(value);
			return (result == null) ? new Left<O>() : new Right<>(result);
		}

		@Override
		public void fold(final A defaultValue, @Nullable final Action<A> successCallback) {
			if(successCallback != null) {
				successCallback.apply(value);
			}
		}
	}
}
