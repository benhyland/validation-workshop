package uk.co.bhyland.validationworkshop;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class MotivatingExamples {

    public static void main(final String... args) {

        final MotivatingExamples examples = new MotivatingExamples();

        examples.validatingExample();
        examples.bailingEarlyValidatingExample();
        examples.applicativeCompositionExample();
        examples.orchestratingExample();
    }

    // much of the below would be extracted and written only once for a particular domain.

    public void validatingExample() {

        String input = "c b a";

        List<Function<String, Validation<String, String>>> checks = Arrays.asList(
                Checks::checkLength,
                Checks::checkAlphabetic
                // etc
        );

        final List<Validation<String, String>> individualCheckResults = checks.stream().map(f -> f.apply(input)).collect(Collectors.toList());

        final Validation<String, List<String>> combinedCheckResults = Validation.sequence(individualCheckResults);

        String message = combinedCheckResults.fold(
                s -> "validation succeeded: " + s,
                es -> "validation failed:\n" + es.stream().collect(Collectors.joining("\n"))
        );

        System.out.println(message);
        System.out.println();
    }

    public void bailingEarlyValidatingExample() {

        Validation<String, String> input= Validation.success("c b a");

        Validation<String, String> validated = input.flatMap(Checks::checkLength).flatMap(Checks::checkAlphabetic);

        String message = validated.fold(
                s -> "validation succeeded: " + s,
                es -> "validation bailed early, first failure encountered was: " + es.get(0)
        );

        System.out.println(message);
        System.out.println();
    }

    public void applicativeCompositionExample() {

        String input = "bar baz foo quux";

        final Validation<String, String> combinedCheckResults = Validation.map3(
                Checks.checkLength(input),
                Checks.checkAlphabetic(input),
                Checks.checkWordCount(input, 4),
                s1 -> s2 -> wordCount -> String.format("%s has %d words", s1, wordCount)
        );

        String message = combinedCheckResults.fold(
                s -> "validation succeeded: " + s,
                es -> "validation failed:\n" + es.stream().collect(Collectors.joining("\n"))
        );

        System.out.println(message);
        System.out.println();
    }

    public void orchestratingExample() {
        // in parallel on several targets, perform several tasks, stopping at the first failure on each target
        // the result is either a complete success or the collection of failures from every failed host
        // note that we are emulating target state with a String - not vey sensible.

        List<String> targets = Arrays.asList("targetA", "targetB", "targetC");

        Function<String, Validation<String, String>> taskA = (String target) -> success(target + " was OK on taskA");
        Function<String, Validation<String, String>> taskB = (String previousState) -> failure(previousState + ", got into trouble on taskB");
        Function<String, Validation<String, String>> taskC = (String previousState) -> failure(previousState + ", would be in trouble on taskC but the task won't be run");

        Function<String, Supplier<Validation<String, String>>> runTasks = (String target) -> () ->
                Validation.<String, String>success(target).flatMap(taskA).flatMap(taskB).flatMap(taskC);

        Function<Future<Validation<String, String>>, Validation<String, String>> tryGet = (Future<Validation<String, String>> future) -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException  e) {
                return failure(e.getMessage());
            }
        };

        // remember to force parallelism before continuing to map
        List<Future<Validation<String, String>>> futures = targets.stream().map(
                target -> CompletableFuture.supplyAsync(runTasks.apply(target))).collect(Collectors.toList());

        List<Validation<String, String>> results = futures.stream().map(tryGet).collect(Collectors.toList());

        final Validation<String, List<String>> combinedResults = Validation.sequence(results);

        String message = combinedResults.fold(
                s -> "orchestration succeeded: " + s,
                es -> "orchestration failed:\n" + es.stream().collect(Collectors.joining("\n"))
        );

        System.out.println(message);
        System.out.println();
    }

    private static class Checks {

        private Checks() {}

        public static Validation<String, String> checkLength(String input) {
            if(input.length() < 10) {
                return failure("too short");
            }
            return success(input);
        }

        public static Validation<String, String> checkAlphabetic(String input) {
            String[] words = input.split(" ");
            String[] sortedWords = Stream.of(words).sorted().toArray(String[]::new);
            if(Arrays.equals(words, sortedWords)) {
                return success(input);
            }
            return failure("input words are not alphabetic");
        }

        public static Validation<String, Integer> checkWordCount(String input, int minWordCount) {
            String[] words = input.split(" ");
            if(words.length < minWordCount) {
                return failure("needs at least " + minWordCount + " words");
            }
            return success(words.length);
        }
    }
}
