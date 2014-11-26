package uk.co.bhyland.validationworkshop.tests;

import org.junit.Test;
import uk.co.bhyland.validationworkshop.Failure;
import uk.co.bhyland.validationworkshop.TestUtils;
import uk.co.bhyland.validationworkshop.Validation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static uk.co.bhyland.validationworkshop.Failure.*;
import static uk.co.bhyland.validationworkshop.TestUtils.isSuccessOf;
import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class BasicValidationTest {

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
}
