package com.finerioconnect.pfm.sdk.tecban.demobackfront.budgets.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finerioconnect.core.sdk.models.FCBudget
import com.finerioconnect.core.sdk.models.FCCategory
import com.finerioconnect.core.sdk.models.FCError
import com.finerioconnect.core.sdk.models.responses.FCBudgetsResponse
import com.finerioconnect.pfm.sdk.core.FinerioPFMAPI
import com.finerioconnect.pfm.sdk.modules.budgets.listeners.GetBudgetsListener
import com.finerioconnect.pfm.sdk.modules.categories.listeners.GetCategoriesListener
import com.finerioconnect.pfm.sdk.tecban.demobackfront.databinding.FragmentBudgetsListBinding

private const val ARG_USER_ID = "paramUserId"

class BudgetsListFragment : Fragment() {
    private var userId: Int? = null

    private lateinit var mBudgets: List<FCBudget>
    private lateinit var mCategories: List<FCCategory>

    private lateinit var mBinding: FragmentBudgetsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(ARG_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentBudgetsListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            BudgetsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_USER_ID, param1)
                }
            }
    }

    private fun setup() {
        getBudgets()
    }

    private fun getBudgets() {
        userId?.let {
            FinerioPFMAPI.shared.budgets().getAll(
                it,
                null,
                object: GetBudgetsListener {
                    override fun budgetsObtained(response: FCBudgetsResponse) {
                        mBudgets = response.budgets
                        getCategories()
                    }

                    override fun error(errors: List<FCError>) {
                        if (errors.isNotEmpty()) {
                            val (_, _, detail) = errors[0]
                            Log.e("ERROR", detail)
                        }
                    }

                    override fun severError(serverError: Throwable) {
                        Log.e("SERVER ERROR", serverError.message!!)
                    }
                }
            )
        }
    }

    private fun getCategories() {
        FinerioPFMAPI.shared.categories().getAll(
            userId,
            null,
            object : GetCategoriesListener {
                override fun categories(categories: List<FCCategory>, nextCursor: Int) {
                    mCategories = categories
                    setDataToUI()
                }

                override fun error(errors: List<FCError>) {
                    if (errors.isNotEmpty()) {
                        val (_, _, detail) = errors[0]
                        Log.e("ERROR", detail)
                    }
                }

                override fun severError(serverError: Throwable) {
                    Log.e("SERVER ERROR", serverError.message!!)
                }
            }
        )
    }

    private fun setDataToUI() = with(mBinding) {
        budgetView.setBudgets(null, mBudgets, mCategories)
    }
}