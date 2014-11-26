package uk.co.bhyland.validationworkshop.examples;

import uk.co.bhyland.validationworkshop.Validation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class OrchestrationExample {

    private final RemoteExecutor remoteExecutor;
    private final ExecutorService executorService;

    public OrchestrationExample(RemoteExecutor remoteExecutor, ExecutorService executorService) {

        this.remoteExecutor = remoteExecutor;
        this.executorService = executorService;
    }

    public static void main(final String... args) {

        final OrchestrationExample orchestrationExample = new OrchestrationExample(new RemoteExecutor(), Executors.newFixedThreadPool(5));
        orchestrationExample.orchestrationExample();
    }

    private void orchestrationExample() {

        final List<String> hostnames = Arrays.asList("host1", "host2", "host3", "host4");

        final Function<String, Validation<String, Result>> task = getTask("nproc");

        List<Future<Validation<String, Result>>> futureTasks = scheduleTasks(hostnames, task);

        final List<Validation<String, Result>> completedTasks = Validation.mapInputs(futureTasks, this::tryRunRemoteTask);
        final Validation<String, List<String>> output = formatCompletedTasks(completedTasks);

        final String message = formatOutput(output);

        System.out.println(message);
    }

    private Function<String, Validation<String, Result>> getTask(String command) {
        return (String host) -> remoteExecutor.executeOnHost(host, command);
    }

    private List<Future<Validation<String,Result>>> scheduleTasks(List<String> hostnames, Function<String, Validation<String, Result>> task) {
        return hostnames.stream().collect(Collectors.mapping(
                host -> executorService.submit(() -> task.apply(host)),
                Collectors.toList())
        );
    }

    private Validation<String, List<String>> formatCompletedTasks(List<Validation<String, Result>> completedResults) {
        return Validation.traverse(completedResults, result -> "task " + result.task + " had result " + result.value + " on host " + result.host);
    }

    private String formatOutput(Validation<String, List<String>> output) {
        return output.fold(
                results -> "all tasks completed successfully:\n" + results.stream().collect(Collectors.joining("\n")),
                errors -> "errors encountered:\n" + errors.stream().collect(Collectors.joining("\n"))
        );
    }

    private Validation<String, Result> tryRunRemoteTask(Future<Validation<String, Result>> futureTask) {
        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            return failure(e.getMessage());
        }
    }

    private static final class Result {

        final String task;
        final String host;
        final String value;

        private Result(String task, String host, String value) {
            this.task = task;
            this.host = host;
            this.value = value;
        }
    }

    private static final class RemoteExecutor {
        private Validation<String, Result> executeOnHost(String host, String command) {
            if(!"nproc".equals(command)) {
                return failure("don't know how to run command: " + command);
            }

            if("host2".equals(host)) {
                return failure("Can't contact host: " + host);
            }

            return success(new Result(command, host, "24"));
        }
    }
}