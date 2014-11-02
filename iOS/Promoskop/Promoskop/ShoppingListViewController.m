//
//  ShoppingListViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ShoppingListViewController.h"
#import <SWRevealViewController.h>

@interface ShoppingListViewController ()
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;

@end

@implementation ShoppingListViewController

- (void)viewDidLoad{
    [self setupUI];
}

- (void)setupUI{
    [self.revealButtonItem setAction:@selector(revealToggle:)];
}
@end
