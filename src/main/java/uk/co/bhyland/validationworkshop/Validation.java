package uk.co.bhyland.validationworkshop;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/*
Implement the following to make the tests pass.
Feel free to change anything about the implementation, so long as the tests (or something with the same intent) still pass.
*/

public abstract class Validation<E, T> {

    private Validation() {}

    public static <E, T> Validation<E, T> success(T value) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, T> Validation<E, T> failure(E value, E... values) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> U fold(Function<T, U> ifSuccess, Function<List<E>, U> ifFailure) {
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

    public Validation<E, T> orElse(Supplier<Validation<E, T>> defaultValue) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<E, U> map(Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<E, U> flatMap(Function<T, Validation<E, U>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public <U> Validation<E, U> apply(final Validation<E, Function<T, U>> functionValidation) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, T> Validation<E, List<T>> sequence(List<Validation<E, T>> validations) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, T, U> Validation<E, List<U>> traverse(List<Validation<E, T>> validations, Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, T, U> Function<Validation<E, T>, Validation<E, U>> lift(Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, A, B, R> Validation<E, R> map2(Validation<E, A> va, Validation<E, B> vb,
                                                     Function<A, Function<B, R>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <E, A, B, C, R> Validation<E, R> map3(Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc,
                                                        Function<A, Function<B, Function<C, R>>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
    Do *not* implement the following, or anything isomorphic.

    public T get() {
        throw new UnsupportedOperationException("no cheating");
    }

    public List<E> getFailures() {
        throw new UnsupportedOperationException("no cheating");
    }
    */
}
