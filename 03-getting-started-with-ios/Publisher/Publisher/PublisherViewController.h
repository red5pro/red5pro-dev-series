//
//  PublisherViewController.h
//  Publisher
//
//  Created by Dominick Accattato on 6/20/17.
//  Copyright © 2017 Dominick Accattato. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <R5Streaming/R5Streaming.h>

@interface PublisherViewController : R5VideoViewController<R5StreamDelegate>

- (void) start;
- (void) stop;

@end
