package uk.co.bhyland.validationworkshop;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The Validation class represents a disjoint union (aka sum type) of either
 *      a (non-empty) list of error values of type E, or
 *      a success value of type T
 *
 * The internal implementation should not be observable to client code except via the Validation public api.
 */
public abstract class Validation<E, T> {

    /*
    Implement the following to make the tests pass.
    Feel free to change anything about the implementation, so long as the tests still pass and the intent of the class remains.
    */

    private Validation() {}

    /**
     * Produce a Validation representing the success value of type T.
     */
    public static <E, T> Validation<E, T> success(T value) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Produce a Validation representing the list of error values of type E.
     */
    @SafeVarargs
    public static <E, T> Validation<E, T> failure(E value, E... values) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Convenience to test what this Validation represents while ignoring its contained value(s).
     */
    public boolean isSuccess() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Convenience to test what this Validation represents while ignoring its contained value(s).
     */
    public boolean isFailure() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return the success value of this Validation, if available; otherwise, return the supplied default value.
     */
    public T getOrElse(Supplier<T> defaultValue) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return a Validation containing the success value of this Validation, if available; otherwise, return the supplied default value.
     */
    public Validation<E, T> orElse(Supplier<Validation<E, T>> defaultValue) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return a Validation containing the success value of this Validation, if available, transformed by the given function;
     * otherwise, return a Validation containing the error values.
     */
    public <U> Validation<E, U> map(Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Run exactly one of the two provided functions, depending on what this Validation represents.
     * Return the result of the selected function.
     */
    public <U> U fold(Function<T, U> ifSuccess, Function<List<E>, U> ifFailure) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Convenience to perform an effect using the success value if this Validation represents a success.
     * Not tested: implementing this is optional.
     */
    public void ifSuccess(Consumer<T> ifSuccess) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Convenience to perform an effect using the failure values if this Validation represents a failure.
     * Not tested: implementing this is optional.
     */
    public void ifFailure(Consumer<List<E>> ifFailure) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return the success value of this Validation, if available, transformed to a Validation by the given function;
     * otherwise, return a Validation containing the error values.
     */
    public <U> Validation<E, U> flatMap(Function<T, Validation<E, U>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return a Validation containing the success value of this Validation transformed by the given function,
     * if both the value and the function are available;
     * otherwise, return a Validation containing the all the available error values.
     */
    public <U> Validation<E, U> apply(final Validation<E, Function<T, U>> functionValidation) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Specialisation of List.map() that transforms the given list of inputs into a list of Validations by applying the given function.
     * Unfortunately, List.map() doesn't actually exist, although Stream.map() does.
     */
    public static <E, A, B> List<Validation<E, B>> mapInputs(List<A> inputs, Function<A, Validation<E, B>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Specialisation of List.flatMap() that transforms the given list of Validations into a new list of Validations by applying the given function.
     * Unfortunately, List.flatMap() doesn't actually exist, although Stream.flatMap() does.
     */
    public static <E, A, B> List<Validation<E, B>> flatMapInputs(List<Validation<E, A>> inputs, Function<A, Validation<E, B>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Transform the given list of Validations into a Validation of a List.
     * If any of the given Validations represented error values, then the resulting Validation contains all available errors;
     * otherwise, it contains a List of all available success values.
     */
    public static <E, T> Validation<E, List<T>> sequence(List<Validation<E, T>> validations) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Transform the given list of Validations into a Validation of a List.
     * If any of the given Validations represented error values, then the resulting Validation contains all available errors;
     * otherwise, it contains a List of all available success values, each transformed by the given function.
     */
    public static <E, T, U> Validation<E, List<U>> traverse(List<Validation<E, T>> validations, Function<T, U> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return a Validation containing the success values of the given Validations transformed by the given curried function, if available;
     * otherwise, return a Validation containing the error values.
     */
    public static <E, A, B, R> Validation<E, R> map2(Validation<E, A> va, Validation<E, B> vb,
                                                     Function<A, Function<B, R>> f) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Return a Validation containing the success values of the given Validations transformed by the given curried function, if available;
     * otherwise, return a Validation containing the error values.
     */
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
