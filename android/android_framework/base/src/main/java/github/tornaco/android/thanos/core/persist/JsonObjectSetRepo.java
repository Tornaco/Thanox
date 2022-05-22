package github.tornaco.android.thanos.core.persist;

import android.os.Handler;
import android.util.AtomicFile;
import android.util.Log;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import github.tornaco.android.thanos.core.util.function.Predicate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.tornaco.android.thanos.core.persist.i.SetRepo;
import github.tornaco.android.thanos.core.util.FileUtils;
import com.elvishew.xlog.XLog;
import github.tornaco.android.thanos.core.util.XmlUtils;
import lombok.Cleanup;
import util.CollectionUtils;

/**
 * Created by guohao4 on 2017/12/11. Email: Tornaco@163.com
 */

public abstract class JsonObjectSetRepo<T> implements SetRepo<T> {

  private static final int FLUSH_DELAY = 5000;
  private static final int FLUSH_DELAY_FAST = 100;

  private final Gson gson = new Gson();
  private static final ExecutorService IO = Executors.newSingleThreadExecutor();
  private Handler mHandler;
  private ExecutorService mExe;

  private AtomicFile mFile;

  public JsonObjectSetRepo(File file, Handler handler, ExecutorService service) {
    this.mFile = new AtomicFile(file);
    this.mExe = service;
    this.mHandler = handler;

    try {
      if (!this.mFile.getBaseFile().exists()) {
        Files.createParentDirs(file);
      }
    } catch (IOException e) {
      XLog.w("Fail createParentDirs for: " + file + "\n" + Log.getStackTraceString(e));
    }

    XLog.d("StringSetRepo: " + name() + ", comes up @%s", file);

    reload();
  }

  private final Set<T> mStorage = new HashSet<>();

  private final Object sync = new Object();

  @Override
  public Set<T> getAll() {
    return new HashSet<>(mStorage);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void reload() {
    synchronized (sync) {
      try {

        if (!mFile.getBaseFile().exists()) {
          XLog.w("getBaseFile not exists, skip load: " + name());
          return;
        }

        if (mFile.getBaseFile().isDirectory()) {
          XLog.w("getBaseFile isDirectory, clean up: " + name());
          FileUtils.deleteDirQuiet(mFile.getBaseFile());
          mFile.delete();
        }

        @Cleanup
        InputStream inputStream = mFile.openRead();
        Set<String> h = new HashSet(XmlUtils.readSetXml(inputStream));
        Set<T> t = new HashSet<>();
        CollectionUtils.consumeRemaining(h, s -> {
          T box = gson.fromJson(s, onCreateTypeToken().getType());
          t.add(box);
        });
        mStorage.addAll(t);

      } catch (Throwable e) {
        XLog.w("Fail reload@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
      }
    }
  }

  protected abstract TypeToken onCreateTypeToken();

  @Override
  public void reloadAsync() {
    Runnable r = JsonObjectSetRepo.this::reload;
    if (mExe == null) {
      IO.execute(r);
    } else {
      mExe.execute(r);
    }
  }

  @Override
  public void flush() {
    XLog.i("flush");
    synchronized (sync) {
      try {
        Set<String> out = new HashSet<>();
        Object[] arrLocal = mStorage.toArray();
        CollectionUtils.consumeRemaining(arrLocal, o -> {
          String str = gson.toJson(o);
          out.add(str);
        });
        @Cleanup
        FileOutputStream fos = mFile.startWrite();
        XmlUtils.writeSetXml(out, fos);
        mFile.finishWrite(fos);
      } catch (Throwable e) {
        XLog.w("Fail flush@IOException: " + mFile + "\n" + Log.getStackTraceString(e));
      }
    }
  }

  private Runnable mFlusher = JsonObjectSetRepo.this::flush;

  private Runnable mFlushCaller = this::flushAsync;

  @Override
  public void flushAsync() {
    XLog.i("flush async");
    if (mExe == null) {
      IO.execute(mFlusher);
    } else {
      mExe.execute(mFlusher);
    }
  }

  @Override
  public boolean add(T s) {
    if (s == null) {
      return false;
    }
    boolean added = mStorage.add(s);
    if (added && mHandler != null) {
      mHandler.removeCallbacks(mFlushCaller);
      mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
    }
    return added;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    if (c == null) {
      return false;
    }
    boolean added = mStorage.addAll(c);
    if (added && mHandler != null) {
      mHandler.removeCallbacks(mFlushCaller);
      mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
    }
    return added;
  }

  @Override
  public boolean remove(T s) {
    if (s == null) {
      return false;
    }
    boolean removed = mStorage.remove(s);
    if (removed && mHandler != null) {
      mHandler.removeCallbacks(mFlushCaller);
      mHandler.postDelayed(mFlushCaller, FLUSH_DELAY);
    }
    return removed;
  }

  @Override
  public void removeAll() {
    mStorage.clear();
    if (mHandler != null) {
      mHandler.removeCallbacks(mFlushCaller);
      mHandler.postDelayed(mFlushCaller, FLUSH_DELAY_FAST);
    }
  }

  @Override
  public boolean has(T s) {
    return s != null && !mStorage.isEmpty() && mStorage.contains(s);
  }

  @Override
  public boolean has(T[] t) {
    if (t != null) {
      for (T tt : t) {
        if (has(tt)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String name() {
    return Files.getNameWithoutExtension(mFile.getBaseFile().getPath());
  }

  @Override
  public int size() {
    return mStorage.size();
  }

  @Override
  public T find(Predicate<T> predicate) {
    for (T element : getAll()) {
      if (predicate.test(element)) {
        return element;
      }
    }
    return null;
  }
}
