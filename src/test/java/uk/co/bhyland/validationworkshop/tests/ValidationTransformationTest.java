package uk.co.bhyland.validationworkshop.tests;

import org.junit.Test;
import uk.co.bhyland.validationworkshop.Failure;
import uk.co.bhyland.validationworkshop.Validation;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.bhyland.validationworkshop.Failure.OH_DEAR;
import static uk.co.bhyland.validationworkshop.Failure.WHAT_A_PITY;
import static uk.co.bhyland.validationworkshop.TestUtils.failingFunction;
import static uk.co.bhyland.validationworkshop.TestUtils.isFailureOf;
import static uk.co.bhyland.validationworkshop.TestUtils.isSuccessOf;
import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class ValidationTransformationTest {

    /*
    Feel free to change anything that follows, but try to stay true to the spirit of the tests.
    */

    @Test
    public void shouldTransformSuccessWithMap() {

        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.map(String::length);

        assertThat(v2, isSuccessOf(3));
    }

    @Test
    public void shouldNotTransformFailureWithMap() {

        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.map(failingFunction());

        assertThat(v1, is(v2));
    }

    @Test
    public void shouldTransformSuccessWithFold() {

        final Validation<Failure, String> v1 = success("yay");
        final Integer l = v1.fold(String::length, failingFunction());

        assertThat(l, is(3));
    }

    @Test
    public void shouldTransformFailureWithFold() {

        final Validation<Failure, String> v1 = failure(OH_DEAR, WHAT_A_PITY);
        final Integer l = v1.fold(failingFunction(), List::size);

        assertThat(l, is(2));
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
        final Validation<Failure, Integer> v2 = v1.flatMap(failingFunction());

        assertThat(v1, is(v2));
    }
}
