package cool.utils;

public class ParserPath {
    private final String path;
    private final int line;
    private final Integer stringConstId;

    public ParserPath(String path, int line, Integer stringConstId) {
        this.path = path;
        this.line = line;
        this.stringConstId = stringConstId;
    }

    public int getLine() {
        return line;
    }

    public String getPath() {
        return path;
    }

    public Integer getStringConstId() {
        return stringConstId;
    }
}
