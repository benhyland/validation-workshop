package uk.co.bhyland.validationworkshop;

import java.util.Arrays;

public final class TestUtils {

    private TestUtils() {}

    public static <T> T fail(Object... args) {
        throw new AssertionError("unexpected call to fail with args " + Arrays.deepToString(args));
    }
}
