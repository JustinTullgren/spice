package com.justintullgren.spice;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Attempt a process and auto catch errors.
 * @param <T> the type to elevate.
 */
public abstract class TryCatch<T> {
	protected final T value;
	TryCatch(T value) {
		this.value = value;
	}

	/**
	 * @param value from producer.
	 * @param <A> the type to elevate.
	 * @return {@link TryCatch} with {@link NullPointerException} if value is null or a {@link TryCatch} elevating the value.
	 */
	public static <A> TryCatch<A> from(A value) {
		if(value == null) {
			return new Left<>(new Throwable(new NullPointerException("Initial value is null.")));
		} else {
			return new Right<>(value);
		}
	}

	/**
	 * @param producer which might throw an error when retrieving the value.
	 * @param <A> the type to elevate.
	 * @return {@link TryCatch} that elevates the result of the producer.
	 */
	public static <A> TryCatch<A> from(SingleFunction<A> producer) {
		try {
			return new Right<>(producer.apply());
		} catch (Exception e) {
			return new Left<>(new Throwable(e));
		}
	}

	/**
	 * @param mapper transform function.
	 * @param <O> the type to transform to.
	 * @return {@link TryCatch} with the mapper result.
	 */
	public abstract <O> TryCatch<O> map(@NotNull Function<O, T> mapper);

	/**
	 * @param errorCallback will be called if an exception occurred while mapping.
	 * @param successCallback will be called if no exception occurred while mapping.
	 */
	public abstract void fold(@Nullable Action<Throwable> errorCallback, @Nullable Action<T> successCallback);

	public Maybe<T> toMaybe() {
		return Maybe.from(value);
	}

	public Either<T> toEither() {
		return Either.from(value);
	}

	private static class Left<A> extends TryCatch<A> {
		private final Throwable error;
		Left(final Throwable throwable) {
			super(null);
			this.error = throwable;
		}

		@Override
		public <O> TryCatch<O> map(@NotNull final Function<O, A> mapper) {
			return new Left<>(error);
		}

		@Override
		public void fold(@Nullable final Action<Throwable> errorCallback, @Nullable final Action<A> successCallback) {
			if(errorCallback != null) {
				errorCallback.apply(error);
			}
		}
	}

	private static class Right<A> extends TryCatch<A> {
		Right(final A value) {
			super(value);
		}

		@Override
		public <O> TryCatch<O> map(@NotNull final Function<O, A> mapper) {
			try {
				return new Right<>(mapper.apply(value));
			} catch (Exception e) {
				return new Left<>(new Throwable(e));
			}
		}

		@Override
		public void fold(@Nullable final Action<Throwable> error, @Nullable final Action<A> successCallback) {
			if(successCallback != null) {
				successCallback.apply(value);
			}
		}
	}
}
