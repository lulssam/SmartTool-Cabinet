import SwiftUI
import FirebaseCore 

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
        }
    }
}
