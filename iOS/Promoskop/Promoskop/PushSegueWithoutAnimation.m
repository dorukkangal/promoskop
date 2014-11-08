//
//  PushSegueWithoutAnimation.m
//  Promoskop
//
//  Created by Mustafa Besnili on 08/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "PushSegueWithoutAnimation.h"

@implementation PushSegueWithoutAnimation
- (void)perform{
    [[self.sourceViewController navigationController]pushViewController:self.destinationViewController animated:NO];
}
@end
