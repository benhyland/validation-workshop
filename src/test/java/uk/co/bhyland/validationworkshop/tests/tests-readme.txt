Implement the missing pieces of Validation.java to make the tests pass.


It is probably best to address the tests in the order they appear in each suite and to take the suites in the following order:

    BasicValidationTest
    TransformationValidationTest
    AdvancedValidationTest


Once the tests are passing (congratulations!) you may wish to consider the following questions as a starting point for discussion:

    0.
    Is there anything in this exercise that can be replaced by features from the Stream api (or anything introduced in Java 8)?
    Should it be?

    1.
    We haven't implemented equality for Validation. Would this be a useful or sensible addition? Why?

    2.
    Surely there must some things about the design of what we have so far that could be improved.
    Can you identify anything? If so, can you change the design, keep the current tests working, and add any additional tests we need?

    3.
    We've implemented and done some basic testing of a powerful but pretty small api.
    Can you think of any functionality you would like to use that can be derived from what we've seen so far?

    4.
    You may have found ways of implementing parts of the api in terms of other parts. What is the minimum api surface necessary to
    implement Validation thus far? Are there any patterns or further abstractions you can see? Could any of this minimal functionality be further generalised?
    If not, why not?

    5.
    We have implemented a map function for different arities, which probably seems quite clunky.
    Does anything prevent us from implementing a generalised map function which works for any number of input Validations?
    If so, should we go ahead and implement mapN functions for larger N? How large?
