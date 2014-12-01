package uk.co.bhyland.validationworkshop.tests;

import org.junit.Test;
import uk.co.bhyland.validationworkshop.Failure;
import uk.co.bhyland.validationworkshop.Validation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.bhyland.validationworkshop.Failure.*;
import static uk.co.bhyland.validationworkshop.TestUtils.*;
import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class AdvancedValidationTest {

    /*
    For the tests in this suite, most people will find the implementation a bit harder.
    Don't get discouraged, it's more of a plateau than a continual increase in difficulty.
    */

    /*
    Feel free to change anything that follows, but try to stay true to the spirit of the tests.
    */

    @Test
    public void shouldApplyFunctionContainedInValidationToSuccessValidation() {

        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = v1.apply(success((Function<String, Integer>)String::length));
        final Validation<Failure, Integer> v3 = v1.apply(failure(OH_DEAR));

        assertThat(v2, isSuccessOf(3));
        assertThat(v3, isFailureOf(OH_DEAR));

        // NB: the cast above is an example of inadequate type inference.
        // In a few lambda edge cases it is necessary to convince the compiler we are doing the right thing,
        // It is safe - note that it does not result in an unchecked warning.
    }

    @Test
    public void shouldApplyFunctionContainedInValidationToFailureValidation() {

        final Validation<Failure, String> v1 = failure(OH_DEAR);
        final Validation<Failure, Integer> v2 = v1.apply(success(failTheTestIfCalledFunction()));
        final Validation<Failure, Integer> v3 = v1.apply(failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));

        assertThat(v2, isFailureOf(OH_DEAR));
        assertThat(v3, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    @Test
    public void shouldSequenceListOfSuccessValidations() {

        final List<Validation<Failure, String>> validations = Arrays.asList(success("a"), success("b"), success("c"));
        final Validation<Failure, List<String>> sequenced = Validation.sequence(validations);

        assertThat(sequenced, isSuccessOf(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void shouldSequenceListWithFailureValidations() {

        final List<Validation<Failure, String>> validations = Arrays.asList(failure(OH_DEAR), success("b"), failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));
        final Validation<Failure, List<String>> sequenced = Validation.sequence(validations);

        assertThat(sequenced, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    @Test
    public void shouldTraverseListOfSuccessValidations() {

        final List<Validation<Failure, String>> validations = Arrays.asList(success("a"), success("bb"), success("ccc"));
        final Validation<Failure, List<Integer>> traversed = Validation.traverse(validations, String::length);

        assertThat(traversed, isSuccessOf(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void shouldTraverseListWithFailureValidations() {

        final List<Validation<Failure, String>> validations = Arrays.asList(failure(OH_DEAR), success("b"), failure(WHAT_A_PITY, NOT_MY_FAULT_GUV));
        final Validation<Failure, List<String>> traversed = Validation.traverse(validations, failTheTestIfCalledFunction());

        assertThat(traversed, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }

    @Test
    public void shouldTransformValidationsWithMap2() {

        final Validation<Failure, String> v1 = success("yay");
        final Validation<Failure, Integer> v2 = success(3);
        final Validation<Failure, String> v3 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, Integer> v4 = failure(NOT_MY_FAULT_GUV);

        final Validation<Failure, String> formatted1 = Validation.map2(v1, v2, a -> b -> String.format("%s %d", a, b));
        final Validation<Failure, String> formatted2 = Validation.map2(v3, v4, failTheTestIfCalledFunction());
        final Validation<Failure, String> formatted3 = Validation.map2(v2, v3, a -> b -> String.format("%s %s", a, b));

        assertThat(formatted1, isSuccessOf("yay 3"));
        assertThat(formatted2, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
        assertThat(formatted3, isFailureOf(OH_DEAR, WHAT_A_PITY));
    }

    @Test
    public void shouldTransformValidationsWithMap3() {

        final Validation<Failure, String> v1 = success("woo");
        final Validation<Failure, Integer> v2 = success(3);
        final Validation<Failure, String> v3 = success("yay");
        final Validation<Failure, String> v4 = failure(OH_DEAR, WHAT_A_PITY);
        final Validation<Failure, Integer> v5 = failure(NOT_MY_FAULT_GUV);

        final Validation<Failure, String> formatted1 = Validation.map3(v1, v2, v3, a -> b -> c -> String.format("%s %d %s", a, b, c));
        final Validation<Failure, String> formatted2 = Validation.map3(v4, v5, v1, failTheTestIfCalledFunction());
        final Validation<Failure, String> formatted3 = Validation.map3(v3, v4, v5, a -> b -> c -> String.format("%s %s %d", a, b, c));

        assertThat(formatted1, isSuccessOf("woo 3 yay"));
        assertThat(formatted2, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
        assertThat(formatted3, isFailureOf(OH_DEAR, WHAT_A_PITY, NOT_MY_FAULT_GUV));
    }
}