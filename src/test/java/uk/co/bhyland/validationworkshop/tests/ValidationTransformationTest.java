package uk.co.bhyland.validationworkshop.tests;

import org.junit.Test;
import uk.co.bhyland.validationworkshop.Failure;
import uk.co.bhyland.validationworkshop.Validation;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.bhyland.validationworkshop.Failure.OH_DEAR;
import static uk.co.bhyland.validationworkshop.Failure.WHAT_A_PITY;
import static uk.co.bhyland.validationworkshop.TestUtils.*;
import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class ValidationTransformationTest {

    /*
    Feel free to change anything that follows, but try to stay true to the spirit of the tests.
    */

    @Test
    public void shouldTransformSuccessWithFold() {

        final Validation<Failure, String> v1 = success("yay");
        final Integer l = v1.fold(failTheTestIfCalledFunction(), String::length);

        assertThat(l, is(3));
    }

    @Test
    public void shouldTransformFailureWithFold() {

        final Validation<Failure, String> v1 = failure(OH_DEAR, WHAT_A_PITY);
        final Integer l = v1.fold(List::size, failTheTestIfCalledFunction());

        assertThat(l, is(2));
    }

    @Test
    public void shouldTransformSuccessWithMap() {

        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.map(String::length);

        assertThat(v2, isSuccessOf(3));
    }

    @Test
    public void shouldNotTransformFailureWithMap() {

        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.map(failTheTestIfCalledFunction());

        assertThat(v2, isFailureOf(OH_DEAR));
    }

    @Test
    public void shouldTransformSuccessWithFlatMap() {

        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.flatMap(t -> success(t.length()));
        final Validation<Failure, Integer> v3 = v1.flatMap(t -> failure(OH_DEAR));

        assertThat(v2, isSuccessOf(3));
        assertThat(v3, isFailureOf(OH_DEAR));
    }

    @Test
    public void shouldNotTransformFailureWithFlatMap() {

        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.flatMap(failTheTestIfCalledFunction());

        assertThat(v2, isFailureOf(OH_DEAR));
    }

    @Test
    public void shouldTransformListWithMap() {

        final List<String> inputs = Arrays.asList("a", "bb", "ccc");
        final List<Validation<Failure, Integer>> vs = Validation.mapInputs(inputs, (String s) -> {
            if (s.length() > 1) {
                return success(s.length());
            } else {
                return failure(OH_DEAR);
            }
        });

        assertThat(vs.size(), is(3));
        assertThat(vs.get(0), isFailureOf(OH_DEAR));
        assertThat(vs.get(1), isSuccessOf(2));
        assertThat(vs.get(2), isSuccessOf(3));
    }

    @Test
    public void shouldTransformListWithFlatMap() {

        final List<Validation<Failure, String>> inputs = Arrays.asList(success("a"), failure(OH_DEAR), success("ccc"));
        final List<Validation<Failure, Integer>> vs = Validation.flatMapInputs(inputs, (String s) -> {
            if (s.length() > 1) {
                return success(s.length());
            } else {
                return failure(WHAT_A_PITY);
            }
        });

        assertThat(vs.size(), is(3));
        assertThat(vs.get(0), isFailureOf(WHAT_A_PITY));
        assertThat(vs.get(1), isFailureOf(OH_DEAR));
        assertThat(vs.get(2), isSuccessOf(3));
    }
}
