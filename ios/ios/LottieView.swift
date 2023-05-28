//
//  LottieView.swift
//  iosApp
//
//  Created by Rafal on 16/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Lottie
import SwiftUI

struct LottieView: UIViewRepresentable {
    let animationView = LottieAnimationView()
    var filename = "music"
    var loopMode: LottieLoopMode = LottieLoopMode.playOnce
    
    func makeUIView(context: Context) -> some UIView {
        let view = UIStackView()
        
        let animation = LottieAnimation.named(filename)
        animationView.animation = animation
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode = loopMode
        animationView.play()
        
        animationView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(animationView)
        
        NSLayoutConstraint.activate([
//            animationView.heightAnchor.constraint(equalTo: view.heightAnchor, multiplier: 1),
            animationView.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 1),
        ])
        return view
    }
    
    func updateUIView(_ uiView: UIViewType, context: Context) {
    }
}
