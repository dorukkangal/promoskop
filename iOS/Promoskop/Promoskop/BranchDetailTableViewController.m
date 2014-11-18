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
@property (strong, nonatomic) IBOutletCollection(UILabel) NSArray *labelCollections;
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
    
    for (UILabel *label in self.labelCollections) {
        [label setAttributedText:[[NSAttributedString alloc] initWithString:label.text attributes:@{NSUnderlineStyleAttributeName : @(NSUnderlineStyleSingle)}]];
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"MapForBranchViewController"]){
        MapForBranchViewController *mapForBranchViewController = (MapForBranchViewController *)segue.destinationViewController;
        mapForBranchViewController.selectedBranch = self.branchDetails;
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.section == 0 && (indexPath.row == 1 || indexPath.row == 3)){
        [self performSegueWithIdentifier:@"MapForBranchViewController" sender:nil];
    }
}

@end
