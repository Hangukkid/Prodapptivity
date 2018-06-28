package matthew.won.utoronto.prod;

import android.content.Context;
import android.widget.LinearLayout;

public class LinLayout extends LinearLayout {
    public LinLayout(Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        return true;
    }
}
