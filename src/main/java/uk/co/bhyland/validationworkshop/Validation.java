package uk.co.bhyland.validationworkshop;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/*
Implement the following to make the tests pass.
Feel free to change anything about the implementation, so long as the tests (or something with the same intent) still pass.
*/

public abstract class Validation<Failure, T> {

    private Validation() {}

    public static <Failure, T> Validation<Failure, T> success(T value) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, T> Validation<Failure, T> failure(Failure value, Failure... values) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> U fold(Function<T, U> ifSuccess, Function<List<Failure>, U> ifFailure) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean isSuccess() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean isFailure() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public T getOrElse(Supplier<T> defaultValue) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public Validation<Failure, T> orElse(Supplier<Validation<Failure, T>> defaultValue) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<Failure, U> map(Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<Failure, U> flatMap(Function<T, Validation<Failure, U>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<Failure, U> apply(final Validation<Failure, Function<T, U>> functionValidation) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, T> Validation<Failure, List<T>> sequence(List<Validation<Failure, T>> validations) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, T, U> Validation<Failure, List<U>> traverse(List<Validation<Failure, T>> validations, Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, T, U> Function<Validation<Failure, T>, Validation<Failure, U>> lift(Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, A, B, Result> Validation<Failure, Result> map2(Validation<Failure, A> va, Validation<Failure, B> vb,
                                                                           Function<A, Function<B, Result>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, A, B, C, Result> Validation<Failure, Result> map3(Validation<Failure, A> va, Validation<Failure, B> vb, Validation<Failure, C> vc,
                                                                              Function<A, Function<B, Function<C, Result>>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
    Do *not* implement the following, or anything isomorphic.

    public T get() {
        throw new UnsupportedOperationException("no cheating");
    }

    public List<T> getFailures() {
        throw new UnsupportedOperationException("no cheating");
    }
    */
}
