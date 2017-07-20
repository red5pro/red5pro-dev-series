//
//  PublisherViewController.m
//  Publisher
//
//  Created by Dominick Accattato on 6/20/17.
//  Copyright © 2017 Dominick Accattato. All rights reserved.
//

#import "PublisherViewController.h"
#import <R5Streaming/R5Streaming.h>

@interface PublisherViewController() {
    R5Configuration *config;
    R5Stream *stream;
}
@end

@implementation PublisherViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    config = [R5Configuration new];
    config.host = @"192.168.0.8";
    config.port = 8554;
    config.contextName = @"live";
    config.licenseKey = @"";
}

-(void)preview {
    
    NSString *cameraID = nil;
    
    NSArray *captureDeviceType = @[AVCaptureDeviceTypeBuiltInWideAngleCamera];
    AVCaptureDeviceDiscoverySession *captureDevice =
    [AVCaptureDeviceDiscoverySession
     discoverySessionWithDeviceTypes:captureDeviceType
     mediaType:AVMediaTypeVideo
     position:AVCaptureDevicePositionUnspecified];
    
    cameraID = [captureDevice.devices.lastObject localizedName];
    
    //    AVCaptureDevice *defaultCamera = [AVCaptureDevice defaultDeviceWithDeviceType:AVCaptureDeviceTypeBuiltInDualCamera mediaType:AVMediaTypeVideo position:AVCaptureDevicePositionFront];
    
    R5Camera *camera = [[R5Camera alloc] initWithDevice:captureDevice.devices.lastObject andBitRate:512];
    
    AVCaptureDevice *audioDevice= [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeAudio];
    R5Microphone *microphone = [[R5Microphone new] initWithDevice:audioDevice];
    
    R5Connection *connection = [[R5Connection new] initWithConfig:config];
    
    stream = [[R5Stream new] initWithConnection:connection];
    [stream attachVideo:camera];
    [stream attachAudio:microphone];
    
    [stream setDelegate:self];
    [self attachStream:stream];
    [self showPreview:YES];
}

-(void)start {
    [self showPreview:YES];
    [self showDebugInfo:true];
    [stream publish:@"stream1" type:R5RecordTypeLive];
}

-(void)stop {
    [stream stop];
    [stream setDelegate:nil];
    [self preview];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self preview];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self stop];
}

-(void)onR5StreamStatus:(R5Stream *)stream withStatus:(int)statusCode withMessage:(NSString *)msg {
    NSLog(@"Stream: %s - %@", r5_string_for_status(statusCode), msg);
}


@end
