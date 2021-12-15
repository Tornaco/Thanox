package github.tornaco.thanos.android.ops.ops

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class OpsDashboardFragment : Fragment() {

    companion object {
        fun newInstance(): OpsDashboardFragment {
            return OpsDashboardFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return LinearLayout(requireContext()).apply { setBackgroundColor(Color.BLUE) }
    }
}