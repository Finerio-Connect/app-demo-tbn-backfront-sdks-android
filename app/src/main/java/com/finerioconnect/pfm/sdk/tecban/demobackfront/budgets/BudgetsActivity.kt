package com.finerioconnect.pfm.sdk.tecban.demobackfront.budgets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finerioconnect.pfm.sdk.tecban.demobackfront.R
import com.finerioconnect.pfm.sdk.tecban.demobackfront.budgets.fragments.BudgetsListFragment
import com.finerioconnect.pfm.sdk.tecban.demobackfront.databinding.ActivityBudgetsBinding
import com.finerioconnect.pfm.sdk.tecban.demobackfront.shared.Constants

class BudgetsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityBudgetsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBudgetsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setup()
    }

    private fun setup() {
        showFragment()
    }

    private fun showFragment() {
        val userId = Constants.Commons.USER_ID
        val fragment: Fragment = BudgetsListFragment.newInstance(userId)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment)
            .addToBackStack(null).commit()
    }
}