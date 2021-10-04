package com.zeiss.gergo.kovacs.dojo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringFeatures implements BasicFunctionalities{
    // JDK 13
    @Test
    void textBlock() {
        final var block = """
                """;

//        final String mustHaveANewLine = """ first line
//                """;

        final var html = """
                <html>      
                    <body>
                        <div>example text</div>
                    </body>
                </html""";

        final var fragmentShaderOld = "version 330 core\n" +
                                      "                out vec4 FragColor;\n" +
                                      "                 \n" +
                                      "                in vec4 vertexColor; // the input variable from the vertex shader (same name and same type)\n" +
                                      "                \n" +
                                      "                void main()\n" +
                                      "                {\n" +
                                      "                    FragColor = vertexColor;\n" +
                                      "                }";

        final var fragmentShaderNew = """                
                #version 330 core
                out vec4 FragColor;
                 
                in vec4 vertexColor; // the input variable from the vertex shader (same name and same type)
                                
                void main()
                {
                    FragColor = vertexColor;
                }""";

        final var explicitIndentation = """
                    easier to read
                """;

        final var tripleQuotes = """
                “To be, or not to be, that is the question”
                    - William Shakespeare, "" //OK, but how about 3 double quotes?
                    \""" //OK
                """;

        final var noCarriageReturnForYou = """
                using windows\r
                is a sin""";

        final var longTextEscapes = """
                escaping long text \
                has never been \
                easier
                """;

        final var ignoredWhiteSpaces = """   
                my personal space             
                is ignored :( 
                """;

        final var respectedWhiteSpaces = """
                my personal space                    \s
                shall not be ignored anymore!              \s
                """;

        final var formattedText = """
                easy-peasy %s %s"""
                .formatted("lemon", "squeezy");
    }

    // JDK 12
    @Test
    void indent() {
        final var unindentedString = """
                No indent for you!
                Or you!
                    Or for you!""";

        final var asIndented = unindentedString.indent(4 * 2);
    }

    // JDK 12
    @Test
    void transform() {
        final Function<String, String> removeSpaces = (s) -> s.replace(" ", "");

        final var numbersAsString = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        final Function<String, String> translateNumbers = (s) -> Arrays.stream(s.split(","))
                                                                  .map(word -> word.matches("[0-9]")
                                                                          ? numbersAsString.get(Integer.parseInt(word))
                                                                          : word
                                                                  )
                                                                  .collect(Collectors.joining(","));

        final var originalList = "ONE, TWO, THREE, 4, 5, Six, 7";

        final var withoutSpaces = originalList.transform(removeSpaces);
        final var translatedNumbers = withoutSpaces.transform(translateNumbers);
        final var lowerCase = translatedNumbers.transform(String::toLowerCase);
        logger.pairs(
                originalList, "original",
                withoutSpaces, "removing spaces",
                translatedNumbers, "removing number",
                lowerCase, "to lower case"
        );

        final var reducer = new StringReducer(List.of(removeSpaces, translateNumbers, String::toLowerCase));
        final var reducedString = originalList.transform(reducer::reduce);

        logger.pairs(
                reducedString, "Using reducer"
        );
    }

    record StringReducer(List<Function<String,String >> operations) {
        String reduce(String input) {
            var step = input;
            for (Function<String, String> op : operations) {
                step = op.apply(step);
            }
            return step;
        }
    }
}
