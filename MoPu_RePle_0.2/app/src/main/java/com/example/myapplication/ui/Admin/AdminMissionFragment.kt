package com.example.myapplication.ui.My_page

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.AdminMissionSettingsBinding

class AdminMissionFragment : Fragment() {

    private var _binding: AdminMissionSettingsBinding? = null
    private val binding get() = _binding!!

    private var selectedType: String? = null  // "상시" or "돌발"
    private var selectedCategory: String? = null // 카테고리 이름 (텀블러 등)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminMissionSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTypeButtons()
        setupCategoryButtons()
        setupInputWatchers()
        updateButtonState() // 초기 상태 설정
    }

    private fun setupTypeButtons() {
        with(binding) {
            typeAlways.setOnClickListener {
                selectedType = "상시"
                typeAlways.setBackgroundColor(Color.BLACK)
                typeAlways.setTextColor(Color.WHITE)
                typeSudden.setBackgroundColor(Color.WHITE)
                typeSudden.setTextColor(Color.BLACK)

                missionCategory.visibility = View.VISIBLE
                missionSetCategory.visibility = View.VISIBLE

                missionSetPeriod.visibility = View.GONE
                inputPeriod1.visibility = View.GONE
                inputPeriod2.visibility = View.GONE
                periodText.visibility = View.GONE

                updateButtonState()
            }

            typeSudden.setOnClickListener {
                selectedType = "돌발"
                typeSudden.setBackgroundColor(Color.BLACK)
                typeSudden.setTextColor(Color.WHITE)
                typeAlways.setBackgroundColor(Color.WHITE)
                typeAlways.setTextColor(Color.BLACK)

                missionCategory.visibility = View.GONE
                missionSetCategory.visibility = View.GONE

                missionSetPeriod.visibility = View.VISIBLE
                inputPeriod1.visibility = View.VISIBLE
                inputPeriod2.visibility = View.VISIBLE
                periodText.visibility = View.VISIBLE

                updateButtonState()
            }
        }
    }

    private fun setupCategoryButtons() {
        with(binding) {
            val categoryButtons = listOf(
                categoryTumbler,
                categoryTraffiic,
                categoryRecycle
            )

            categoryButtons.forEach { button ->
                button.setOnClickListener {
                    // 전체 리셋
                    categoryButtons.forEach {
                        it.setBackgroundColor(Color.WHITE)
                        it.setTextColor(Color.BLACK)
                    }
                    // 선택된 버튼 색 변경
                    button.setBackgroundColor(Color.BLACK)
                    button.setTextColor(Color.WHITE)

                    selectedCategory = button.text.toString()
                    updateButtonState()
                }
            }
        }
    }

    private fun setupInputWatchers() {
        with(binding) {
            val inputs = listOf(
                inputTitle,
                inputDescription,
                inputPoint,
                inputPeriod1,
                inputPeriod2
            )

            inputs.forEach { input ->
                input.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        updateButtonState()
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }

    private fun updateButtonState() {
        with(binding) {
            val titleFilled = inputTitle.text.isNotEmpty()
            val descFilled = inputDescription.text.isNotEmpty()
            val pointFilled = inputPoint.text.isNotEmpty()

            val isReady = when (selectedType) {
                "상시" -> {
                    titleFilled && descFilled && pointFilled && selectedCategory != null
                }
                "돌발" -> {
                    titleFilled && descFilled && pointFilled &&
                            inputPeriod1.text.isNotEmpty() && inputPeriod2.text.isNotEmpty()
                }
                else -> false
            }

            val buttons = listOf(alwaysMission, suddenMission)
            buttons.forEach { button ->
                if (isReady) {
                    button.setBackgroundResource(R.drawable.admin_mission_button_green)
                    button.isEnabled = true
                } else {
                    button.setBackgroundResource(R.drawable.admin_mission_button_gray)
                    button.isEnabled = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
