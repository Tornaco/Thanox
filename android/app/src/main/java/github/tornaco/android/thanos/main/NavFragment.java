package github.tornaco.android.thanos.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elvishew.xlog.XLog;

class NavFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((FragmentAttachListener) context).onFragmentAttach(this);
        XLog.d("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.d("onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        XLog.d("onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLog.d("onDestroy");
    }

    interface FragmentAttachListener {
        void onFragmentAttach(NavFragment fragment);
    }
}
