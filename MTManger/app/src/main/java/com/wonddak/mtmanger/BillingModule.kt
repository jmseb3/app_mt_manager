package com.wonddak.mtmanger

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BillingModule(
    private val context: Context,
) : PurchasesUpdatedListener {

    companion object {
        const val TAG = "BillingClient"
        const val REMOVE_ADS = "remove_ad_inapp"
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private var productDetailsList: List<ProductDetails> = mutableListOf()
    private lateinit var consumeListener: ConsumeResponseListener

    var removeAddStatus = MutableStateFlow(false)

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.i(TAG, "연결 실패")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.i(TAG, "연결성공")
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    CoroutineScope(Dispatchers.Main).launch {
                        queryPurchase()
                        querySkuDetails()
                    }
                }
            }
        })
    }

    /**
     * 기존 구매 목록 처리
     */
    fun queryPurchase() {
        if (!billingClient.isReady) {
            Log.e(TAG, "queryPurchases: BillingClient is not ready")
        }
        val queryPurchasesParams =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        // Query for existing subscription products that have been purchased.
        billingClient.queryPurchasesAsync(
            queryPurchasesParams
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    purchaseList.forEach {
                        Log.i(TAG, it.products.toString())
                        if (it.products.contains(REMOVE_ADS)) {
                            if (it.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                removeAddStatus.value = true
                            }
                            Log.i(TAG, it.purchaseState.toString())
                            Log.i(TAG, it.purchaseTime.toString())
                        }
                        Log.i(TAG, "광고제거 상태 : ${removeAddStatus.value}")
                    }
                } else {
                    Log.i(TAG, "items : empty")
                }

            } else {
                Log.e(TAG, billingResult.debugMessage)
            }
        }
    }

    /**
     * 구매 가능 목록 호출
     * 필요하다면 API 구분 필요
     * */
    suspend fun querySkuDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(REMOVE_ADS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }
        productDetailsResult.productDetailsList?.let {
            productDetailsList = it
        }
        productDetailsList.forEach {
            Log.i(TAG, "getItem :$it")
        }
    }

    fun purchaseRemoveAdRequest(activity: Activity) {

        val list: MutableList<BillingFlowParams.ProductDetailsParams> = mutableListOf()
        for (productDetails in productDetailsList) {
            if (productDetails.productId == REMOVE_ADS) {
                val flowProductDetailParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
                list.add(flowProductDetailParams)
                break
            }
        }

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(list)
            .build()

        val billingResult =
            billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
        Log.d(TAG, billingResult.toString())
        Log.d(TAG, BillingResponseCode.OK.toString())
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        Log.d(TAG, "${billingResult.responseCode}")
        if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            //구매 성공
            CoroutineScope(Dispatchers.Main).launch {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            // 유저 취소 errorcode
        } else {
            // 에러
        }
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        val consumeResult = withContext(Dispatchers.IO) {
            billingClient.consumePurchase(consumeParams)
        }
        Log.d(TAG, "$consumeResult")
        if (purchase.products.contains(REMOVE_ADS)) {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                removeAddStatus.value = true
            }
            Log.i(TAG, purchase.purchaseState.toString())
            Log.i(TAG, purchase.purchaseTime.toString())
        }
        Log.i(TAG, "광고제거 상태 : ${removeAddStatus.value}")
    }
}