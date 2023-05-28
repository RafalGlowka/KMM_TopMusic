//
//  IntroScreen.swift
//  iosApp
//
//  Created by Rafal on 15/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Lottie
import shared

struct SnackBar {
    let message : String
    let messageAction: String?
    
    init(message: String = "", messageAction: String? = nil) {
        self.message = message
        self.messageAction = messageAction
    }
}

struct IntroScreen: View {
    private var connector :IntroScreenConnector
    
    @State private var snackbar : SnackBar? = nil
    
    init(screenConnector: IntroScreenConnector) {
        connector = screenConnector
    }
       
    var body: some View {
        VStack(alignment: .center){
            Spacer()
            LottieView(filename: "music", loopMode: LottieLoopMode.loop)
                .aspectRatio(1, contentMode: .fit)
            Text(MainRes.shared.string.appName)
                .font(.largeTitle)
            Spacer()
            let _snackbar = snackbar
            if (_snackbar != nil) {
                HStack {
                    Text(_snackbar!.message)
                        .foregroundColor(Color.white)
                    Spacer()
                    if (_snackbar!.messageAction != nil) {
                        Button(_snackbar!.messageAction!, action: {
                            snackbar = nil
                            connector.onSnackbarAction()
                            
                        })
                        .foregroundColor(Color.blue)
                    }
                }
                .padding(20)
                .background(Color.gray)
            }
        }.onAppear {
            connector.onSnackbarShow = { message, action in
                snackbar = SnackBar(message: message, messageAction: action)
            }

            connector.onShow()
        }
    }
}

struct IntroScreen_Previews: PreviewProvider {
    static var previews: some View {
        IntroScreen(screenConnector: IntroScreenConnector())
    }
}
