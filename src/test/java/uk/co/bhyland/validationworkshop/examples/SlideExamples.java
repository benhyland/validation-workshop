package uk.co.bhyland.validationworkshop.examples;

import uk.co.bhyland.validationworkshop.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SlideExamples {

    private static class SomeThing {}
    private static class SomeOtherThing extends SomeThing {}
    private static class ServiceException extends Exception {}
    private static class RemoteException extends Exception {}
    private static class Either<A,B> {
        public void ifLeft(Consumer<A> errorHandler) {}
        public void ifRight(Consumer<B> valueHandler) {}
    }

    private static class Repository {
        SomeThing getById(int id) { return null; }
        Optional<SomeThing> findById(int id) { return null; }
    }

    private static class Service {
        SomeThing performDangerousActionForId(int id) throws ServiceException { return null; }
        Either<ServiceException, SomeThing> tryDangerousActionForId(int id) { return null; }

        SomeOtherThing useSomeThing(SomeThing thing) throws ServiceException { return null; }
        Validation<Exception,SomeOtherThing> tryUseSomeThing(SomeThing thing) { return null; }
    }

    private static class RemoteAgent {
        SomeThing lookupSomeThingOnHost(String host) throws RemoteException { return null; }
        Validation<Exception,SomeThing> tryLookupSomeThingOnHost(String host) { return null; }
    }



    Repository repository = new Repository();
    Service service = new Service();
    RemoteAgent remoteAgent = new RemoteAgent();

    private void doSomeThing(SomeThing thing) {}
    private void doSomeOtherThings(List<SomeOtherThing> things) {}
    private void handleError(Exception e) {}
    private void handleAllErrors(List<Exception> es) {}



    public void nullExample() {

        SomeThing result = repository.getById(42);

        if(result != null) {
            doSomeThing(result);
        }

    }

    public void exceptionExample() {

        try {
            SomeThing result = service.performDangerousActionForId(42);
            doSomeThing(result);
        }
        catch (ServiceException e) {
            handleError(e);
        }

    }

    public void eitherExample() {

        Either<ServiceException,SomeThing> result = service.tryDangerousActionForId(42);

        result.ifLeft(this::handleError);
        result.ifRight(this::doSomeThing);

    }


    public void multipleFailureExample() {

    List<String> hosts = Arrays.asList("host1", "host2", "host3");
    List<SomeOtherThing> results = new ArrayList<>();
    List<Exception> errors = new ArrayList<>();

    for (String host : hosts) {
        try {
            SomeThing firstResult = remoteAgent.lookupSomeThingOnHost(host);
            SomeOtherThing secondResult = service.useSomeThing(firstResult);
            results.add(secondResult);
        } catch (RemoteException | ServiceException e) {
            errors.add(e);
        }
    }
    if(!errors.isEmpty()) {
        doSomeOtherThings(results);
    }

    }


    public void validationExample() {

        List<String> hosts = Arrays.asList("host1", "host2", "host3");

        List<Validation<Exception, SomeThing>> firstResults = Validation.mapInputs(hosts, remoteAgent::tryLookupSomeThingOnHost);
        List<Validation<Exception, SomeOtherThing>> secondResults = Validation.flatMapInputs(firstResults, service::tryUseSomeThing);
        Validation<Exception, List<SomeOtherThing>> result = Validation.sequence(secondResults);

        result.ifSuccess(this::doSomeOtherThings);
        result.ifFailure(this::handleAllErrors);

    }
}
