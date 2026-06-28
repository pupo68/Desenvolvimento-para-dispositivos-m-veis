package br.edu.pucpr.listacompras.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import br.edu.pucpr.listacompras.R
import br.edu.pucpr.listacompras.databinding.DialogDashboardBinding

/**
 * BottomSheet dialog showing shopping list statistics
 * Displays total, purchased, pending counts, progress, and category breakdown
 */
class DashboardDialog : BottomSheetDialogFragment() {

    private var _binding: DialogDashboardBinding? = null
    private val binding get() = _binding!!

    private var totalCount: Int = 0
    private var compradoCount: Int = 0
    private var pendenteCount: Int = 0
    private var categoryBreakdown: String = ""

    companion object {
        private const val ARG_TOTAL = "arg_total"
        private const val ARG_COMPRADO = "arg_comprado"
        private const val ARG_PENDENTE = "arg_pendente"
        private const val ARG_CATEGORIES = "arg_categories"

        fun newInstance(
            total: Int,
            comprado: Int,
            pendente: Int,
            categories: String
        ): DashboardDialog {
            return DashboardDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TOTAL, total)
                    putInt(ARG_COMPRADO, comprado)
                    putInt(ARG_PENDENTE, pendente)
                    putString(ARG_CATEGORIES, categories)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalCount = it.getInt(ARG_TOTAL, 0)
            compradoCount = it.getInt(ARG_COMPRADO, 0)
            pendenteCount = it.getInt(ARG_PENDENTE, 0)
            categoryBreakdown = it.getString(ARG_CATEGORIES, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate counts
        binding.textTotalCount.text = totalCount.toString()
        binding.textCompradoCount.text = compradoCount.toString()
        binding.textPendenteCount.text = pendenteCount.toString()

        // Accessibility: announce summary for TalkBack
        val summary = getString(R.string.dashboard_accessibility_summary,
            totalCount, compradoCount, pendenteCount)
        binding.statsContainer.contentDescription = summary

        // Progress
        val progress = if (totalCount > 0) {
            ((compradoCount.toFloat() / totalCount.toFloat()) * 100).toInt()
        } else 0

        binding.progressIndicator.setProgressCompat(progress, true)
        binding.textProgressLabel.text = getString(R.string.dashboard_progress_label, progress)

        // Category breakdown
        binding.textCategoryBreakdown.text = categoryBreakdown.ifEmpty {
            getString(R.string.dashboard_no_categories)
        }

        // Close button
        binding.buttonCloseDashboard.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
