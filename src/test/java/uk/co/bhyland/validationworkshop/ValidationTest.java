package uk.co.bhyland.validationworkshop;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static uk.co.bhyland.validationworkshop.Failure.*;
import static uk.co.bhyland.validationworkshop.TestUtils.failingFunction;
import static uk.co.bhyland.validationworkshop.TestUtils.isFailureOf;
import static uk.co.bhyland.validationworkshop.TestUtils.isSuccessOf;
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
    public void shouldGetSuccessValueFromSuccess() {
        final Validation<Failure, String> v1 = success("yay");
        final String value = v1.getOrElse(TestUtils::fail);

        assertThat(value, is("yay"));
    }

    @Test
    public void shouldGetDefaultValueFromFailure() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final String value = v1.getOrElse(() -> "boo");

        assertThat(value, is("boo"));
    }

    @Test
    public void shouldGetValidationSuccessValueFromSuccess() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, String> v2 = v1.orElse(TestUtils::fail);

        assertThat(v1, is(v2));
    }

    @Test
    public void shouldGetDefaultValidationValueFromFailure() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, String> v2 = v1.orElse(() -> success("boo"));

        assertThat(v2, isSuccessOf("boo"));
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

    /* harder */
    @Test
    public void shouldApplyFunctionInContextOfValidationToSuccess() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.apply(Validation.<Failure, Function<String, Integer>>success(String::length));
        final Validation<Failure, Integer> v3 = v1.apply(failure(OH_DEAR));

        assertThat(v2, isSuccessOf(3));
        assertThat(v3, isFailureOf(OH_DEAR));
    }

    /* harder */
    @Test
    public void shouldApplyFunctionInContextOfValidationToFailure() {
        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.apply(success(failingFunction()));
        final Validation<Failure, Integer> v3 = v1.apply(failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));

        assertThat(v2, is(v1));
        assertThat(v3, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    /* harder */
    @Test
    public void shouldSequenceListOfSuccessValidations() {
        final List<Validation<Failure, String>> validations = Arrays.asList(success("a"), success("b"), success("c"));
        final Validation<Failure, List<String>> sequenced = Validation.sequence(validations);

        assertThat(sequenced, isSuccessOf(Arrays.asList("a", "b", "c")));
    }

    /* harder */
    @Test
    public void shouldSequenceListWithFailureValidations() {
        final List<Validation<Failure, String>> validations = Arrays.asList(failure(OH_DEAR), success("b"), failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));
        final Validation<Failure, List<String>> sequenced = Validation.sequence(validations);

        assertThat(sequenced, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    /* harder */
    @Test
    public void shouldTraverseListOfSuccessValidations() {
        final List<Validation<Failure, String>> validations = Arrays.asList(success("a"), success("bb"), success("ccc"));
        final Validation<Failure, List<Integer>> traversed = Validation.traverse(validations, String::length);

        assertThat(traversed, isSuccessOf(Arrays.asList(1, 2, 3)));
    }

    /* harder */
    @Test
    public void shouldTraverseListWithFailureValidations() {
        final List<Validation<Failure, String>> validations = Arrays.asList(failure(OH_DEAR), success("b"), failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));
        final Validation<Failure, List<String>> traversed = Validation.traverse(validations, failingFunction());

        assertThat(traversed, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    /* harder */
    @Test
    public void shouldTransformValidationsWithMap2() {
        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = success(3);
        final Validation<Failure, String> v3 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, Integer> v4 = failure(NOT_MY_FAULT_GUV);

        final Validation<Failure, String> formatted1 = Validation.map2(v1, v2, a -> b -> String.format("%s %d", a, b));
        final Validation<Failure, String> formatted2 = Validation.map2(v3, v4, failingFunction());
        final Validation<Failure, String> formatted3 = Validation.map2(v2, v3, a -> b -> String.format("%s %s", a, b));

        assertThat(formatted1, isSuccessOf("yay 3"));
        assertThat(formatted2, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
        assertThat(formatted3, isFailureOf(OH_DEAR, WHAT_A_PITY));
    }

    /* harder */
    @Test
    public void shouldTransformValidationsWithMap3() {
        final Validation<Failure, String> v1 = success("woo");
        final Validation<Failure, Integer> v2 = success(3);
        final Validation<Failure, String> v3 = success("yay");
        final Validation<Failure, String> v4 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, Integer> v5 = failure(NOT_MY_FAULT_GUV);

        final Validation<Failure, String> formatted1 = Validation.map3(v1, v2, v3, a -> b -> c -> String.format("%s %d %s", a, b, c));
        final Validation<Failure, String> formatted2 = Validation.map3(v4, v5, v1, failingFunction());
        final Validation<Failure, String> formatted3 = Validation.map3(v3, v4, v5, a -> b -> c -> String.format("%s %s %d", a, b, c));

        assertThat(formatted1, isSuccessOf("woo 3 yay"));
        assertThat(formatted2, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
        assertThat(formatted3, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    /*

    Extra credit:

    0.
    Is there anything in this exercise that can be replaced by features from the Stream api (or anything introduced in Java 8)?
    Should it be?

    1.
    We haven't implemented equality for Validation. Would this be a useful or sensible addition? Why?

    2.
    Surely there must some things about the design of what we have so far that could be improved.
    Can you identify anything? If so, can you change the design, keep the current tests working, and add any additional tests we need?

    3.
    We've tested a powerful but pretty small api.
    Can you think of any functionality you would like to use that can be derived from what we've seen so far?
    If so, suggest, test and implement!

    4.
    You may have found ways of implementing parts of the api in terms of other parts. What is the minimum api surface necessary to
    implement Validation thus far? Are there any patterns or further abstractions you can see? Could any of this minimal functionality be further generalised?
    If not, why not?

    5.
    We have implemented a map function for different arities, which probably seems quite clunky.
    Does anything prevent us from implementing a generalised map function which works for any number of input Validations?
    If so, should we go ahead and implement mapN functions for larger N? How large?

    */
}
