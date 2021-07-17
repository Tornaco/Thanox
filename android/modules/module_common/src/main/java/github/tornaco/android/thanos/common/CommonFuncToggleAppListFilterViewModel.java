package github.tornaco.android.thanos.common;

import android.app.Application;
import androidx.annotation.NonNull;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import rx2.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Setter;

public class CommonFuncToggleAppListFilterViewModel extends CommonAppListFilterViewModel {

    @Setter
    private OnAppItemSelectStateChangeListener selectStateChangeListener;

    public CommonFuncToggleAppListFilterViewModel(@NonNull Application application) {
        super(application);
    }


    void selectAll() {
        disposables.add(Observable.fromIterable(listModels)
                .doOnNext(listModel -> selectStateChangeListener.onAppItemSelectionChanged(listModel.appInfo, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Rxs.EMPTY_CONSUMER, Rxs.ON_ERROR_LOGGING, this::start));
    }

    void unSelectAll() {
        disposables.add(Observable.fromIterable(listModels)
                .doOnNext(listModel -> selectStateChangeListener.onAppItemSelectionChanged(listModel.appInfo, false))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Rxs.EMPTY_CONSUMER, Rxs.ON_ERROR_LOGGING, this::start));
    }

}
