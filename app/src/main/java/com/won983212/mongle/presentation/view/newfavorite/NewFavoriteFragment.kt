package com.won983212.mongle.presentation.view.newfavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.won983212.mongle.R
import com.won983212.mongle.common.util.toastLong
import com.won983212.mongle.data.model.Emotion
import com.won983212.mongle.databinding.BottomSheetNewFavoriteBinding

class NewFavoriteFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<NewFavoriteViewModel>()
    private lateinit var binding: BottomSheetNewFavoriteBinding
    private var initialEmotion: Emotion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            initialEmotion = it.getSerializable(ARGUMENT_INITIAL_EMOTION) as Emotion
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomSheetNewFavoriteBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnNewFavoriteOk.setOnClickListener {
            if (!viewModel.saveFavorite()) {
                requireContext().toastLong(R.string.new_favorite_fill_blanks)
            } else {
                dismiss()
            }
        }

        binding.selectorNewFavoriteEmotion.setOnSelectedListener {
            viewModel.selectEmotion(it)
        }

        if (initialEmotion != null) {
            viewModel.selectEmotion(initialEmotion!!)
        }

        return binding.root
    }

    companion object {
        private const val ARGUMENT_INITIAL_EMOTION = "initialEmotion"

        fun newInstance(initialEmotion: Emotion) =
            NewFavoriteFragment().apply {
                arguments = bundleOf(ARGUMENT_INITIAL_EMOTION to initialEmotion)
            }
    }
}