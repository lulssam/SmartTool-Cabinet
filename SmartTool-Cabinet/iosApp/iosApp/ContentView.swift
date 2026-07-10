import UIKit
import SwiftUI
import ComposeApp
import GoogleSignIn
import FirebaseCore

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            nativeGoogleSignIn: { onResult in
                GoogleSignInHelper.signIn { idToken, accessToken in
                    _ = onResult(idToken, accessToken)
                }
            }
        )
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

/// Trata todo o login com a Google no lado nativo iOS.
/// No fim chama `onResult` com o idToken, ou com `nil` se o utilizador cancelar/falhar.
enum GoogleSignInHelper {
    static func signIn(onResult: @escaping (String?, String?) -> Void) {
        // O clientID vem do GoogleService-Info.plist, através do Firebase já configurado.
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            onResult(nil, nil); return
        }
        GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientID)

        guard let rootVC = rootViewController() else {
            onResult(nil, nil); return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: rootVC) { result, error in
            if let error = error {
                print("🔴 GoogleSignIn ERRO: \(error.localizedDescription)")
                onResult(nil, nil); return
            }

            let idToken = result?.user.idToken?.tokenString
            let accessToken = result?.user.accessToken.tokenString
            // Este é o token que o Firebase precisa.
            onResult(idToken, accessToken)
        }
    }

    /// Descobre o ecrã que está à frente, para apresentar o login por cima dele.
    private static func rootViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes
            .first { $0.activationState == .foregroundActive } as? UIWindowScene
        var top = scene?.windows.first(where: { $0.isKeyWindow })?.rootViewController
        while let presented = top?.presentedViewController {
            top = presented
        }
        return top
    }
}
