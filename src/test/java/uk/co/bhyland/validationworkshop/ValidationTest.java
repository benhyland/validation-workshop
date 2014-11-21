package uk.co.bhyland.validationworkshop;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static uk.co.bhyland.validationworkshop.Failure.*;
import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class ValidationTest {

    /*
    Feel free to change anything that follows, but try to stay true to the spirit of the tests.
    */

    @Test
    public void shouldProduceSuccessFromSuccessFactory() {
        final Validation<Failure, String> v = success("yay");

        assertTrue(v.isSuccess());
        assertFalse(v.isFailure());
    }

    @Test
    public void shouldProduceFailureFromFailureFactory() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);

        assertFalse(v1.isSuccess());
        assertTrue(v1.isFailure());

        final Validation<Failure, String> v2 = failure(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV);

        assertFalse(v2.isSuccess());
        assertTrue(v2.isFailure());
    }

    @Test
    public void shouldImplementEqualityForSuccesses() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, String> v2 = success("yay");
        final Validation<Failure, String> v3 = success("boo");
        final Validation<Failure, String> v4 = failure(OH_DEAR);

        assertTrue(v1.equals(v2));
        assertFalse(v1.equals(v3));
        assertFalse(v1.equals(v4));
        assertFalse(v1 == v2);
    }

    @Test
    public void shouldImplementEqualityForFailures() {
        final Validation<Failure, String> v1 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, String> v2 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, String> v3 = failure(OH_DEAR);
        final Validation<Failure, String> v4 = success("boo");

        assertTrue(v1.equals(v2));
        assertFalse(v1.equals(v3));
        assertFalse(v1.equals(v4));
        assertFalse(v1 == v2);
    }

    @Test
    public void shouldTransformSuccessWithFold() {
        final Validation<Failure, String> v1 = success("yay");
        final Integer l = v1.fold(String::length, TestUtils::fail);

        assertThat(l, is(3));
    }

    @Test
    public void shouldTransformFailureWithFold() {
        final Validation<Failure, String> v1 = failure(OH_DEAR, WHAT_A_PITY);
        final Integer l = v1.fold(TestUtils::fail, List::size);

        assertThat(l, is(2));
    }

    @Test
    public void shouldTransformSuccessWithMap() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.map(String::length);

        assertThat(v2, is(success(3)));
    }

    @Test
    public void shouldNotTransformFailureWithMap() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.map(String::length);

        assertThat(v2, is(failure(OH_DEAR)));
    }

    @Test
    public void shouldTransformSuccessWithFlatMap() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.flatMap(t -> success(t.length()));
        final Validation<Failure, Integer> v3 = v1.flatMap(t -> failure(OH_DEAR));

        assertThat(v2, is(success(3)));
        assertThat(v3, is(failure(OH_DEAR)));
    }

    @Test
    public void shouldNotTransformFailureWithFlatMap() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.flatMap(t -> success(t.length()));
        final Validation<Failure, Integer> v3 = v1.flatMap(t -> failure(WHAT_A_PITY));

        assertThat(v2, is(failure(OH_DEAR)));
        assertThat(v3, is(failure(OH_DEAR)));
    }

    @Test
    public void shouldApplyFunctionInContextOfValidationToSuccess() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.apply(success(String::length));
        final Validation<Failure, Integer> v3 = v1.apply(failure(OH_DEAR));

        assertThat(v2, is(success(3)));
        assertThat(v3, is(failure(OH_DEAR)));
    }


    @Test
    public void shouldApplyFunctionInContextOfValidationToFailure() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.apply(success(String::length));
        final Validation<Failure, Integer> v3 = v1.apply(failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));

        assertThat(v2, is(failure(OH_DEAR)));
        assertThat(v3, is(failure(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV)));
    }

    /*

    Extra credit:

    1.
    Surely there must some things about the design of what we have so far that could be improved.
    Can you identify anything? If so, can you change the design, keep the current tests working, and add any additional tests we need?

    2.
    We've tested a powerful but pretty small api.
    Can you think of any functionality you would like to use that can be derived from what we've seen so far?
    If so, suggest, test and implement them!

    */
}
