package util.q;

public interface RequestHandler<T> {
    void handleRequest(T request);
}
