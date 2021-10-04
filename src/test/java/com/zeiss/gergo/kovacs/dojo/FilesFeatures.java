package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.zeiss.gergo.kovacs.util.Logger.Options.SPACED;

public class FilesFeatures implements BasicFunctionalities {
    private final Path basePath = Path.of("src/test/resources/");
    private final Path oneTestFile = basePath.resolve("one_test_file.txt");
    private final Path anotherTestFile = basePath.resolve("another_test_file.txt");

    private final Function<Integer, String> randomNumber = (Integer length) ->
            IntStream.rangeClosed(1, length)
                     .map(it -> generator.nextInt(10))
                     .mapToObj(Integer::toString)
                     .collect(Collectors.joining());

    private final Supplier<String> randomFileName = () -> "test_file_" + randomNumber.apply(10);
    private final Supplier<Path> randomFilePath = () -> basePath.resolve(randomFileName.get());
    private final Consumer<Path> silentDeleteFile = (Path path) -> {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @Test
    void filesMethods() {
        final var content = "Some sort of string content to save";

        final var firstFileName = randomFilePath.get();
        final var secondFileName = randomFilePath.get();
        try {
            Files.writeString(firstFileName, content + " 123", StandardOpenOption.CREATE_NEW);
            Files.writeString(secondFileName, content + " 456", StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);

            logger.pairs(
                    Files.readString(firstFileName), "reading file 1",
                    Files.readString(secondFileName), "reading file 2"
            );

            logger.pairs(
                    SPACED, Files.readString(oneTestFile), "reading test file"
            ).line();

            final var noMismatch = Files.mismatch(oneTestFile, oneTestFile);
            final var mismatchAtTheEnd = Files.mismatch(oneTestFile, anotherTestFile);

            logger.pairs(
                    noMismatch, "no mismatch",
                    mismatchAtTheEnd, "some mismatch at the end of the file"
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Stream.of(firstFileName, secondFileName)
                  .filter(Files::exists)
                  .forEach(silentDeleteFile);
        }
    }
}
