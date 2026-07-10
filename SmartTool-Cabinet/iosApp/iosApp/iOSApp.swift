import SwiftUI
import FirebaseCore
import GoogleSignIn

@main
struct iOSApp: App {

    // Arranca o Firebase mal a app é criada, antes de qualquer ecrã.
    // Sem isto, o primeiro login rebenta porque o Firebase não está configurado.
    init() {
        FirebaseApp.configure()
    }

    var body: some Scene {
            WindowGroup {
                ContentView()
                    .onOpenURL { url in
                        // Deixa o GoogleSignIn processar o link de retorno do login.
                        GIDSignIn.sharedInstance.handle(url)
                    }
            }
        }
}
