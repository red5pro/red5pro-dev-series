//
//  ViewController.swift
//  PublisherSwift
//
//  Created by Dominick Accattato on 6/20/17.
//  Copyright © 2017 Dominick Accattato. All rights reserved.
//

import UIKit
import R5Streaming

class ViewController: UIViewController, R5StreamDelegate {
    
    var currentView : R5VideoViewController? = nil
    var publishStream : R5Stream? = nil

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func onR5StreamStatus(_ stream: R5Stream!, withStatus statusCode: Int32, withMessage msg: String!) {
        NSLog("Status: %s ", r5_string_for_status(statusCode))
    }
    
    func getConfig()->R5Configuration{
        // Set up the configuration
        let config = R5Configuration()
        config.host = "192.168.0.8"
        config.port = 8554
        config.contextName = "live"
        config.`protocol` = 1;
        config.buffer_time = 0.5
        config.licenseKey = ""
        return config
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        super.viewDidAppear(animated)
        
        AVAudioSession.sharedInstance().requestRecordPermission { (gotPerm: Bool) -> Void in
            
        };
        
        setupDefaultR5VideoViewController()
        
        // Set up the configuration
        let config = getConfig()
        // Set up the connection and stream
        let connection = R5Connection(config: config)
        
        setupPublisher(connection!)
        // show preview and debug info
        // self.publishStream?.getVideoSource().fps = 2;
        self.currentView!.attach(publishStream!)
        
        self.publishStream!.publish("stream1", type: R5RecordTypeLive)
        
    }
    
    func setupDefaultR5VideoViewController() -> R5VideoViewController{
        
        let r5View : R5VideoViewController = getNewR5VideoViewController(self.view.frame);
        self.addChildViewController(r5View);
        
        view.addSubview(r5View.view)
        r5View.setFrame(self.view.bounds)
        
        r5View.showPreview(true)
        
        r5View.showDebugInfo(true)
        
        currentView = r5View;
        
        return currentView!
    }
    
    func getNewR5VideoViewController(_ rect : CGRect) -> R5VideoViewController{
        
        let view : UIView = UIView(frame: rect)
        
        let r5View : R5VideoViewController = R5VideoViewController();
        r5View.view = view;
        
        return r5View;
    }
    
    func setupPublisher(_ connection: R5Connection){
        
        self.publishStream = R5Stream(connection: connection)
        self.publishStream!.delegate = self
        
        // Attach the video from camera to stream
        let videoDevice = AVCaptureDevice.defaultDevice(withDeviceType: .builtInWideAngleCamera, mediaType: AVMediaTypeVideo, position: .front)
        
        let camera = R5Camera(device: videoDevice, andBitRate: 750)
        camera?.width = 640
        camera?.height = 360
        camera?.orientation = 90
        self.publishStream!.attachVideo(camera)
        
        // Attach the audio from microphone to stream
        let audioDevice = AVCaptureDevice.defaultDevice(withMediaType: AVMediaTypeAudio)
        let microphone = R5Microphone(device: audioDevice)
        microphone?.bitrate = 32
        microphone?.device = audioDevice;
        NSLog("Got device %@", audioDevice!)
        self.publishStream!.attachAudio(microphone)
    }



}

