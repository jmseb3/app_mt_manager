package com.wonddak.mtmanger

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
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
    private suspend fun queryPurchase() {
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
            CoroutineScope(Dispatchers.Main).launch {
                if (billingResult.responseCode == BillingResponseCode.OK) {
                    if (purchaseList.isNotEmpty()) {
                        purchaseList.forEach {
                            handlePurchase(it)
                        }
                    } else {
                        Log.i(TAG, "items : empty")
                    }

                } else {
                    Log.e(TAG, billingResult.debugMessage)
                }
            }
        }
    }

    /**
     * 구매 가능 목록 호출
     * 필요하다면 API 구분 필요
     * */
    private suspend fun querySkuDetails() {
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
            "상품 주문 취소"
        } else {
            // 에러
            "구매 요청 실패"
        }
    }

    //소비성 결제인경우
    //현재 앱은 해당사항이 없다.
    private suspend fun makeConsume(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        val consumeResult = withContext(Dispatchers.IO) {
            billingClient.consumePurchase(consumeParams)
        }
        Log.d(TAG, "$consumeResult")
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        Log.i(TAG, purchase.products.toString())
        if (purchase.products.contains(REMOVE_ADS)) {
            handleRemoveAd(purchase)
        }
    }

    private suspend fun handleRemoveAd(purchase: Purchase) {

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                //확인이 되지 않은 경우
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { billingResult ->
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            removeAddStatus.value = true
                        }
                    }
                }
            } else {
                removeAddStatus.value = true
            }
        }
        Log.i(TAG, "광고제거 상태 : ${removeAddStatus.value}")
    }
}