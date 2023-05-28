import SwiftUI
import shared

class UIViewControllerImpl: UIViewFactory {
    
    let introScreenConnectorFactory = IntroScreenConnectorFactory()
    
    func createViewController(screenStructure: ScreenStructure<AnyObject, AnyObject, AnyObject, AnyObject>) -> UIViewController? {
        switch(screenStructure) {
        case introScreenConnectorFactory.getScreenStructure():
            return UIHostingController(rootView: IntroScreen(screenConnector: introScreenConnectorFactory.getConnector()))
        default:
            return nil
        }
    }
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = Main_iosKt.InitMainViewController(viewFactory: UIViewControllerImpl())
        window?.makeKeyAndVisible()
        return true
    }
}

