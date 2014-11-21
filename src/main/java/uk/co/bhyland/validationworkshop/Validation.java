package uk.co.bhyland.validationworkshop;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Validation<Failure, T> {

    /*
    Implement the following to make the tests pass.
    Do not change these signatures relating to Validation instantiation.
    */

    private Validation() {}

    public static <Failure, T> Validation<Failure, T> success(T value) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public static <Failure, T> Validation<Failure, T> failure(Failure value, Failure... values) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
    Implement the following to make the tests pass.
    Feel free to change anything about the implementation, so long as the tests (or something with the same intent) still pass.
    */

    public <U> U fold(Function<T, U> ifSuccess, Function<List<Failure>, U> ifFailure) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean isSuccess() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public boolean isFailure() {
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

    /*
    Do *not* implement the following, or anything isomorphic.
    */
    public T get() {
        throw new UnsupportedOperationException("no cheating");
    }

    public List<T> getFailures() {
        throw new UnsupportedOperationException("no cheating");
    }
}
