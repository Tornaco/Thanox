package github.tornaco.android.thanos.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import github.tornaco.android.thanos.util.TypefaceHelper;

/**
 * Created by Tornaco on 2018/5/15 10:59.
 * God bless no bug!
 */
public class JetBrainsMonoMediumTextView extends AppCompatTextView {
    public JetBrainsMonoMediumTextView(Context context) {
        super(context);
        init(context);
    }

    public JetBrainsMonoMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JetBrainsMonoMediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceHelper.jetbrainsMonoMedium(context));
    }
}
