package com.finerioconnect.pfm.sdk.tecban.demobackfront

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.finerioconnect.core.sdk.core.FinerioConnectCore
import com.finerioconnect.core.sdk.shared.enums.Environment
import com.finerioconnect.core.sdk.shared.enums.LogLevel
import com.finerioconnect.pfm.sdk.core.FinerioPFMAPI
import com.finerioconnect.pfm.sdk.tecban.demobackfront.budgets.BudgetsActivity
import com.finerioconnect.pfm.sdk.tecban.demobackfront.credits.CreditsActivity
import com.finerioconnect.pfm.sdk.tecban.demobackfront.databinding.ActivityMainBinding
import com.finerioconnect.sdk.budget.core.FinerioBudgetSDK
import com.finerioconnect.sdk.credit.core.FinerioCreditSDK

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setup()
    }

    private fun setup() {
        configSDKs()
        setListeners()
    }

    private fun configSDKs() {
        val finerioConnectCore = FinerioConnectCore.shared
        finerioConnectCore.apiKey = "api_key"
        finerioConnectCore.environment = Environment.SANDBOX
        finerioConnectCore.logLevel = LogLevel.DEBUG
        finerioConnectCore.configure()

        FinerioPFMAPI.shared.init()

        val finerioCreditSDK = FinerioCreditSDK.shared
        finerioCreditSDK.configure()

        val finerioBudgetSDK = FinerioBudgetSDK.shared
        finerioBudgetSDK.configure()
    }

    private fun setListeners() = with(mBinding) {
        btGoCreditsSDK.setOnClickListener { goNextClass(CreditsActivity::class.java) }
        btGoBudgetsSDK.setOnClickListener { goNextClass(BudgetsActivity::class.java) }
    }

    private fun goNextClass(_class: Class<*>) {
        val intent = Intent(this, _class)
        startActivity(intent)
    }
}