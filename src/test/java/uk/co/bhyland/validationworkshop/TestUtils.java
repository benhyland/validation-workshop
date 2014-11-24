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

    public static <Failure, T> Matcher<Validation<Failure, T>> isSuccessOf(T value) {
        return new TypeSafeMatcher<Validation<Failure, T>>() {
            @Override
            protected boolean matchesSafely(Validation<Failure, T> candidate) {
                return candidate.isSuccess() && candidate.getOrElse(TestUtils::fail).equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches Validations which are successes containing " + value);
            }
        };
    }

    public static <Failure, T> Matcher<Validation<Failure, T>> isFailureOf(Failure value, Failure... values) {
        return new TypeSafeMatcher<Validation<Failure, T>>() {
            @Override
            protected boolean matchesSafely(Validation<Failure, T> candidate) {
                return candidate.isSuccess() && candidate.getOrElse(TestUtils::fail).equals(value);
            }

            @Override
            public void describeTo(Description description) {
                final List<Failure> failures = new ArrayList<Failure>(values.length + 1);
                failures.add(value);
                failures.addAll(Arrays.asList(values));
                description.appendText("matches Validations which are failures containing " + Arrays.deepToString(failures.toArray()));
            }
        };
    }

    public static <T, U> Function<T, U> failingFunction() {
        return new Function<T, U>() {
            @Override
            public U apply(T t) {
                return fail(t);
            }
        };
    }
}
