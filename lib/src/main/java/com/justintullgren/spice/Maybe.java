package com.justintullgren.spice;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type that represents the possibility of a value.
 * @param <T> the type to elevate.
 */
public abstract class Maybe<T> {
	protected final T value;

	Maybe(@Nullable final T value) {
		this.value = value;
	}

	/**
	 * @param mapper transform function.
	 * @param <O> the type to transform to.
	 * @return {@link Maybe} with the mapper result or a maybe of nothing.
	 */
	public abstract <O> Maybe<O> map(@NotNull Function<O, T> mapper);

	/**
	 * @param nullCallback will be called if any mapper function returned null.
	 * @param successCallback will be called if a value is present.
	 */
	public abstract void fold(@Nullable Unit nullCallback, @Nullable Action<T> successCallback);

	public TryCatch<T> toTryCatch() {
		return TryCatch.from(value);
	}

	public Either<T> toEither() {
		return Either.from(value);
	}

	/**
	 * @param source value to elevate.
	 * @param <I> the type to elevate.
	 * @return {@link Maybe} of the specified source.
	 */
	public static <I> Maybe<I> from(@Nullable I source) {
		if (source == null) {
			return new Left<>();
		} else {
			return new Right<>(source);
		}
	}

	private static class Left<A> extends Maybe<A> {
		protected Left() {
			super(null);
		}

		@Override
		public <O> Maybe<O> map(@NotNull final Function<O, A> mapper) {
			return new Left<>();
		}

		@Override
		public void fold(@Nullable final Unit nullCallback, @Nullable final Action<A> successCallback) {
			if(nullCallback != null) {
				nullCallback.apply();
			}
		}
	}

	private static class Right<A> extends Maybe<A> {
		protected Right(@NotNull final A value) {
			super(value);
		}

		@Override
		public <O> Maybe<O> map(@NotNull final Function<O, A> mapper) {
			if(value == null) {
				return new Left<>();
			}
			O result = mapper.apply(value);
			return (result == null) ? new Left<O>() : new Right<>(result);
		}

		@Override
		public void fold(@Nullable final Unit nullCallback, @Nullable final Action<A> successCallback) {
			if(successCallback != null) {
				successCallback.apply(value);
			}
		}
	}
}
