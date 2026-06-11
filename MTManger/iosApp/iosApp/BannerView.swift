//
//  BannerView.swift
//  iosApp
//
//  Created by 정원희 on 7/1/24.
//

import UIKit
import SwiftUI
import ComposeApp
import FirebaseCore
import GoogleMobileAds

private let testBannerAdUnitId = "ca-app-pub-3940256099942544/2934735716"
private let prodBannerAdUnitId = "ca-app-pub-2369897242309575/2080103235"

struct BannerView: UIViewControllerRepresentable {
    
    let bannerView = GoogleMobileAds.BannerView(adSize: AdSizeBanner)
    
    func makeUIViewController(context: Context) -> UIViewController {
        
        let viewController = UIViewController()
        if Const.shared.USE_SCREENSHOT_MOCK_DATA || !_isDebugAssertConfiguration() {
            bannerView.adUnitID = prodBannerAdUnitId
        } else {
            bannerView.adUnitID = testBannerAdUnitId
        }
        bannerView.rootViewController = viewController
        bannerView.delegate = context.coordinator

        viewController.view.addSubview(bannerView)
        
        return viewController
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        bannerView.load(Request())
    }
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    internal class Coordinator: NSObject, BannerViewDelegate
    {
        let parent: BannerView
        
        init(_ parent: BannerView) {
            self.parent = parent
        }
        
        // MARK: - GADBannerViewDelegate methods

        func bannerViewDidReceiveAd(_ bannerView: BannerView) {
          print("\(#function) called")
        }

        func bannerView(_ bannerView: BannerView, didFailToReceiveAdWithError error: Error) {
          print("\(#function) called \n\(error)")
        }

        func bannerViewDidRecordImpression(_ bannerView: BannerView) {
          print("\(#function) called")
        }

        func bannerViewWillPresentScreen(_ bannerView: BannerView) {
          print("\(#function) called")
        }

        func bannerViewWillDismissScreen(_ bannerView: BannerView) {
          print("\(#function) called")
        }

        func bannerViewDidDismissScreen(_ bannerView: BannerView) {
          print("\(#function) called")
        }
    }
}

#Preview {
    BannerView()
        .frame(width: AdSizeBanner.size.width,height: AdSizeBanner.size.height)

}
