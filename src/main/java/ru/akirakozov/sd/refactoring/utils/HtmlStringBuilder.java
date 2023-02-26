package ru.akirakozov.sd.refactoring.utils;

public class HtmlStringBuilder {
    private final StringBuilder result;

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String HEADER_1_OPEN = "<h1>";
    private static final String HEADER_1_CLOSE = "</h1>";
    private static final String LINE_BREAK = "</br>";

    public HtmlStringBuilder() {
        this.result = new StringBuilder().append("<html><body>").append(LINE_SEPARATOR);
    }

    public HtmlStringBuilder appendHeader(String content) {
        result.append(HEADER_1_OPEN).append(content).append(HEADER_1_CLOSE).append(LINE_SEPARATOR);

        return this;
    }

    public HtmlStringBuilder appendWithLineBreak(String line) {
        result.append(line).append(LINE_BREAK).append(LINE_SEPARATOR);

        return this;
    }

    public HtmlStringBuilder appendNewLineString(String content) {
        result.append(content).append(LINE_SEPARATOR);

        return this;
    }

    public HtmlStringBuilder appendString(String content) {
        result.append(content);

        return this;
    }

    public String build() {
        return result.append("</body></html>").toString();
    }
}
