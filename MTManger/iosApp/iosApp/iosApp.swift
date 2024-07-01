import UIKit
import SwiftUI
import ComposeApp
import FirebaseCore
import GoogleMobileAds
import AppTrackingTransparency

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        
        // Use Firebase library to configure APIs.
        FirebaseApp.configure()
        // Initialize the Google Mobile Ads SDK.
        GADMobileAds.sharedInstance().start(completionHandler: nil)
        
        initAdView()
        
        window = UIWindow(frame: UIScreen.main.bounds)
        if let window = window {
            window.rootViewController = MainKt.MainViewController()
            window.makeKeyAndVisible()
        }
        return true
    }
    
    func initAdView() {
        AdMobHelper.shared.doInit {
            return UIHostingController(rootView: BannerView()
                .frame(width: GADAdSizeBanner.size.width,height: GADAdSizeBanner.size.height)
            )
        }
    }
}


