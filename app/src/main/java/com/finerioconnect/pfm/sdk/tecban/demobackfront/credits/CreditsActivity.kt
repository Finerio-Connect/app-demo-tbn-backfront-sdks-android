package com.finerioconnect.pfm.sdk.tecban.demobackfront.credits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.finerioconnect.core.sdk.models.FCError
import com.finerioconnect.core.sdk.models.FCFinancialEntity
import com.finerioconnect.core.sdk.models.responses.FCCreditsResponse
import com.finerioconnect.pfm.sdk.core.FinerioPFMAPI
import com.finerioconnect.pfm.sdk.modules.accounts.listeners.GetFinancialEntitiesListener
import com.finerioconnect.pfm.sdk.modules.credits.listeners.GetCreditsListener
import com.finerioconnect.pfm.sdk.tecban.demobackfront.databinding.ActivityCreditsBinding
import com.finerioconnect.pfm.sdk.tecban.demobackfront.shared.Constants

class CreditsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCreditsBinding

    private lateinit var mFinancialEntities: List<FCFinancialEntity>
    private lateinit var mCreditsResponse: FCCreditsResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setup()
    }

    private fun setup() {
        getFinancialEntities()
    }

    private fun getFinancialEntities() {
        val isBankAgreggation = true

        FinerioPFMAPI.shared.accounts().FinancialEntity().getAll(
            isBankAgreggation,
            object : GetFinancialEntitiesListener {
                override fun financialEntitiesObtained(financialEntities: List<FCFinancialEntity>) {
                    mFinancialEntities = financialEntities
                    getCredits()
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

    private fun getCredits() {
        val userId = Constants.Commons.USER_ID
        val cursor = null

        FinerioPFMAPI.shared.creditsClient().getList(
            userId,
            cursor,
            object : GetCreditsListener {
                override fun creditsObtained(response: FCCreditsResponse) {
                    mCreditsResponse = response
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
        creditView.setCredits(mCreditsResponse, mFinancialEntities)
    }
}