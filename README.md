validation-workshop
===================

You'll need a Java 8 development environment.
The main code is under `src/main/java`, while tests and examples are under `src/test/java`.
The tests also depend on the jars in `lib`.

Implement the missing pieces of Validation.java to make the tests pass.

It is probably best to address the tests in the order they appear in each suite and to take the suites in the following order:

- BasicValidationTest
- ValidationTransformationTest
- AdvancedValidationTest

Pay close attention to the api type signatures and comments, especially if you are unsure what you need to do.
The motivation for some of the api may become only be apparent in later tasks or in the examples provided.

Once the tests are passing (congratulations!) you may wish to consider any of the following questions as a starting point for discussion:

1. Is there anything in this exercise that can be replaced by features from the Stream api (or anything introduced in Java 8)? Would this be sensible?

2. We haven't implemented equality for Validation. Would this be a useful or sensible addition? Why?

3. We haven't implemented anything with the signature `public T get()`. Why not?

4. You may have found ways of implementing parts of the api in terms of other parts. What is the minimum api surface necessary to implement Validation thus far? Are there any patterns or further abstractions you can see? Could any of this minimal functionality be further generalised? If not, why not?

5. Surely there must some things about the design of what we have so far that could be improved. Can you identify anything? If so, can you change the design, keep the current tests working, and add any additional tests we need?

6. We've implemented and done some basic testing of a powerful but pretty small api. Can you think of any more specialised (but still domain-agnostic) functionality you would like to use that can be derived from what we've seen so far?

7. We've implemented map functions for several different arities, which probably seems quite clunky. Does anything prevent us from implementing a generalised map function which works for any number of input Validations? If so, should we go ahead and implement mapN functions for larger N? How large?

Thanks to Jenny Beckett (insano10) for playtesting and feedback.