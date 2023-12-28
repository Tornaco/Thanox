package github.tornaco.android.thanos.core.persist;

import java.util.Collection;
import java.util.Set;

import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.util.function.Predicate;

@Keep
public class UserStringSetRepo {
    private final StringSetRepo repo;
    private final int userId;

    public UserStringSetRepo(StringSetRepo repo, int userId) {
        this.repo = repo;
        this.userId = userId;
    }

    public StringSetRepo getRepo() {
        return repo;
    }

    public int getUserId() {
        return userId;
    }

    public Set<String> getAll() {
        return repo.getAll();
    }

    public void reload() {
        repo.reload();
    }

    public void reloadAsync() {
        repo.reloadAsync();
    }

    public void flush() {
        repo.flush();
    }

    public void flushAsync() {
        repo.flushAsync();
    }

    public boolean add(String s) {
        return repo.add(s);
    }

    public boolean addAll(Collection<? extends String> c) {
        return repo.addAll(c);
    }

    public boolean remove(String s) {
        return repo.remove(s);
    }

    public void removeAll() {
        repo.removeAll();
    }

    public boolean has(String s) {
        return repo.has(s);
    }

    public boolean has(String[] t) {
        return repo.has(t);
    }

    public String name() {
        return repo.name();
    }

    public int size() {
        return repo.size();
    }

    public String find(Predicate<String> predicate) {
        return repo.find(predicate);
    }
}
