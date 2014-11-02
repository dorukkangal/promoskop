//
//  BranchDetailTableViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 26/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "BranchDetailTableViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "MapForBranchViewController.h"

@interface BranchDetailTableViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *branchImageView;
@property (weak, nonatomic) IBOutlet UILabel *workingHoursLabel;
@property (weak, nonatomic) IBOutlet UILabel *addressLabel;
@property (weak, nonatomic) IBOutlet UILabel *telephoneNumberLabel;
@property (weak, nonatomic) IBOutlet UILabel *branchNameLabel;
@end

@implementation BranchDetailTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.branchImageView sd_setImageWithURL:[NSURL URLWithString:self.branchDetails[@"store_logo"]]];
    [self.branchNameLabel setText:[NSString stringWithFormat:@"%@-%@",self.branchDetails[@"store_name"], self.branchDetails[@"branch_name"]]];
    [self.addressLabel setText:self.branchDetails[@"branch_address"]];
    [self.workingHoursLabel setText:@"Mon - Sun : 10:00 AM - 10:00 PM"];
    [self.telephoneNumberLabel setText:@"(0532) 412 3387"];
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"MapForBranchViewController"]){
        MapForBranchViewController *mapForBranchViewController = (MapForBranchViewController *)segue.destinationViewController;
        mapForBranchViewController.selectedBranch = self.branchDetails;
    }
}

- (IBAction)backButtonPressed:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

@end
