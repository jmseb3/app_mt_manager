package com.wonddak.mtmanger

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class BillingModule(
    private val context : Context,
) :PurchasesUpdatedListener {

    companion object{
        const val TAG = "BillingClient"
        const val REMOVE_ADS = "remove_ad_inapp"
    }

    val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    private var productDetailsList: List<ProductDetails> = mutableListOf()
    private lateinit var consumeListener : ConsumeResponseListener

    var removeAddStatus = MutableStateFlow(false)

    init {
        billingClient.startConnection(object :BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.i(TAG,"연결 실패")
            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                Log.i(TAG,"연결성공")
                CoroutineScope(Dispatchers.Main).launch {
                    queryPurchase()
                    querySkuDetails()
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
        // Query for existing subscription products that have been purchased.
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    purchaseList.forEach {
                        Log.i(TAG,it.products.toString())
                        if (it.products.contains(REMOVE_ADS)) {
                            removeAddStatus.value = true

                        }
                        Log.i(TAG,"광고제거 상태 : $removeAddStatus")
                    }
                } else {
                    Log.i(TAG, "items : empty") }

            } else {
                Log.e(TAG, billingResult.debugMessage)
            }
        }
    }
    /**
     * 구매 가능 목록 호출
     * 필요하다면 API 구분 필요
     * */
    fun querySkuDetails(){
        val tempParam = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(REMOVE_ADS)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()


        billingClient.queryProductDetailsAsync(tempParam) { billingResult, mutableList ->
            productDetailsList = mutableList
            productDetailsList.forEach {
                Log.i(TAG,it.toString())
            }
        }
    }

    fun getPay(activity: Activity) {

        val list : MutableList<BillingFlowParams.ProductDetailsParams> = mutableListOf()

        productDetailsList.forEach { product ->
            if(product.productId == REMOVE_ADS) {
                val flowProductDetailParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(product)
                    .build()

                list.add(flowProductDetailParams)
            }
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(list)
            .build()

        val responseCode = billingClient.launchBillingFlow(activity, flowParams).responseCode
        Log.d(TAG, responseCode.toString())
        Log.d(TAG, BillingClient.BillingResponseCode.OK.toString())
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        Log.d(TAG, "???? ${billingResult.responseCode}")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.d(TAG,"구매 성공")
                //ToastUtil.showShortToast(this, "구매 성공")
                // 거래 성공 코드
                // ?


//                handlePurchase(purchase)

                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.consumeAsync(consumeParams, consumeListener)

            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            //ToastUtil.showShortToast(this, "유저 취소")
            // 유저 취소 errorcode
        }
    }

}