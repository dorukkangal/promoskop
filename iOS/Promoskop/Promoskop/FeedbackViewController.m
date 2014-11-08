//
//  FeedbackViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 05/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "FeedbackViewController.h"
#import <Colours.h>

@interface FeedbackViewController()
@end

@implementation FeedbackViewController

- (void)viewDidLoad{
    [self.navigationItem setTitle:@"Öneri Kutusu"];
}

- (IBAction)backButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:NULL];
}
@end
