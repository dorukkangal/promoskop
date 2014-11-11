//
//  CustomNavigationController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "CustomNavigationController.h"

@implementation CustomNavigationController

- (void) awakeFromNib{
    [self.navigationBar setTintColor:[UIColor whiteColor]];
}

- (UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

@end
