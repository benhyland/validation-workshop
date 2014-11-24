package uk.co.bhyland.validationworkshop;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class TestUtils {

    private TestUtils() {}

    public static <T> T fail(Object... args) {
        throw new AssertionError("unexpected call to fail with args " + Arrays.deepToString(args));
    }

    public static <E, T> Matcher<Validation<E, T>> isSuccessOf(T value) {
        return new TypeSafeMatcher<Validation<E, T>>() {
            @Override
            protected boolean matchesSafely(Validation<E, T> candidate) {
                return candidate.fold(t -> t.equals(value), es -> false);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Validation success containing " + value);
            }
        };
    }

    public static <E, T> Matcher<Validation<E, T>> isFailureOf(E value, E... values) {
        final List<E> expected = new ArrayList<>(values.length + 1);
        expected.add(value);
        expected.addAll(Arrays.asList(values));

        return new TypeSafeMatcher<Validation<E, T>>() {
            @Override
            protected boolean matchesSafely(Validation<E, T> candidate) {
                return candidate.fold(t -> false, es -> es.equals(expected));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Validation failure containing " + Arrays.deepToString(expected.toArray()));
            }
        };
    }

    public static <T, U> Function<T, U> failingFunction() {
        return TestUtils::fail;
    }
}
