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

import SwiftUI
import GoogleMobileAds

struct BannerView: UIViewControllerRepresentable {
    
    let bannerView = GADBannerView(adSize: GADAdSizeBanner)
    
    func makeUIViewController(context: Context) -> UIViewController {
        
        let viewController = UIViewController()
        bannerView.adUnitID = "ca-app-pub-3940256099942544/2934735716"
        bannerView.rootViewController = viewController
        bannerView.delegate = context.coordinator

        viewController.view.addSubview(bannerView)
        
        return viewController
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        bannerView.load(GADRequest())
    }
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    internal class Coordinator: NSObject, GADBannerViewDelegate
    {
        let parent: BannerView
        
        init(_ parent: BannerView) {
            self.parent = parent
        }
        
        // MARK: - GADBannerViewDelegate methods

        func bannerViewDidReceiveAd(_ bannerView: GADBannerView) {
          print("\(#function) called")
        }

        func bannerView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: Error) {
          print("\(#function) called \n\(error)")
        }

        func bannerViewDidRecordImpression(_ bannerView: GADBannerView) {
          print("\(#function) called")
        }

        func bannerViewWillPresentScreen(_ bannerView: GADBannerView) {
          print("\(#function) called")
        }

        func bannerViewWillDismissScreen(_ bannerView: GADBannerView) {
          print("\(#function) called")
        }

        func bannerViewDidDismissScreen(_ bannerView: GADBannerView) {
          print("\(#function) called")
        }
    }
}

#Preview {
    BannerView()
        .frame(width: GADAdSizeBanner.size.width,height: GADAdSizeBanner.size.height)

}


