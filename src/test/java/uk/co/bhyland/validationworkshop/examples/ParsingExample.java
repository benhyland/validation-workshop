package uk.co.bhyland.validationworkshop.examples;

import uk.co.bhyland.validationworkshop.Validation;

import java.util.stream.Collectors;

import static uk.co.bhyland.validationworkshop.Validation.failure;
import static uk.co.bhyland.validationworkshop.Validation.success;

public class ParsingExample {

    public static void main(final String... args) {

        new ParsingExample().parsingExample();

    }

    private void parsingExample() {
        final String aliceToBob = "GREETING|alice|bob|salutations";
        final String bobToBob = "GREETING|bob|bob||what does this button do?";

        Validation<String, Message> aliceToBobMessage = parseMessage(aliceToBob);
        Validation<String, Message> bobToBobMessage = parseMessage(bobToBob);

        String aliceResult = formatParseResult(aliceToBobMessage);
        String bobResult = formatParseResult(bobToBobMessage);

        System.out.println("parsingExample");
        System.out.println(aliceResult);
        System.out.println(bobResult);
    }

    private Validation<String, Message> parseMessage(String input) {

        final Validation<String, String[]> splitInput = success(input.split("|"));

        final Validation<String, MessageType> messageType = splitInput.map(si -> si[0]).flatMap(this::parseMessageType);
        final Validation<String, String> from = splitInput.map(si -> si[1]);
        final Validation<String, String> to = splitInput.map(si -> si[2]);
        final Validation<String, Body> body = splitInput.map(si -> si[3]).flatMap(this::parseBody);

        return Validation.map4(messageType, from, to, body, mt -> f -> t -> b -> new Message(mt, f, t, b));
    }

    private String formatParseResult(Validation<String, Message> result) {
        return result.fold(
                es -> "failed to parse message: " + es.stream().collect(Collectors.joining(", ")),
                m -> "successfully parsed message from " + m.from
                );
    }

    Validation<String, Body> parseBody(final String body) {
        return body.length() > 10 ? success(new Body(body)) : failure("body length too short: " + body);
    }

    Validation<String, MessageType> parseMessageType(String input) {
        try {
            return success(MessageType.valueOf(input));
        }
        catch (NullPointerException | IllegalArgumentException e) {
            return failure(e.toString() + " : " + input);
        }
    }

    private enum MessageType {
        GREETING,
        KEBAB_ORDER,
        ;
    }

    private final class Body {

        final String body;

        public Body(String body) {
            this.body = body;
        }
    }

    private final class Message {

        final MessageType messageType;
        final String from;
        final String to;
        final Body body;

        public Message(MessageType messageType, String from, String to, Body body) {

            this.messageType = messageType;
            this.from = from;
            this.to = to;
            this.body = body;
        }
    }
}
