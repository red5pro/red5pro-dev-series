//
//  ViewController.m
//  Publisher
//
//  Created by Dominick Accattato on 6/20/17.
//  Copyright © 2017 Dominick Accattato. All rights reserved.
//

#import "ViewController.h"
#import "PublisherViewController.h"

@interface ViewController () {
    PublisherViewController *publisher;
    BOOL isPublishing;
}

@end

@implementation ViewController


- (IBAction)onPublishToggle:(id)sender {
    if(isPublishing) {
        [publisher stop];
    }
    else {
        [publisher start];
    }
    isPublishing = !isPublishing;
    [[self publishButton] setTitle:isPublishing ? @"STOP" : @"START" forState:UIControlStateNormal];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    UIViewController *controller = [storyboard instantiateViewControllerWithIdentifier:@"publishView"];
    
    CGRect frameSize = self.view.bounds;
    publisher.view.layer.frame = frameSize;
    publisher = (PublisherViewController *)controller;
    
    [self.view addSubview:publisher.view];
    [self.view sendSubviewToBack:publisher.view];
    
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [[self publishButton] setTitle:@"START" forState:UIControlStateNormal];
}

-(void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    isPublishing = NO;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
