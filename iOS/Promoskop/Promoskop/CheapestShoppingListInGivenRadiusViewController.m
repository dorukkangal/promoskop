//
//  CheapestShoppingListInGivenRadiusViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 09/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "CheapestShoppingListInGivenRadiusViewController.h"
#import "CheapestShoppingListHeaderView.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "BasicCell.h"

@interface CheapestShoppingListInGivenRadiusViewController()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@end

static NSString * const cellIdentifier = @"CheapestShoppingListTableViewCell";
static NSString * const headerIdentifier = @"CheapestShoppingListHeaderView";

@implementation CheapestShoppingListInGivenRadiusViewController

- (void)viewDidLoad{
    [self.tableView registerNib:[UINib nibWithNibName:@"CheapestShoppingListHeaderView" bundle:nil] forHeaderFooterViewReuseIdentifier:headerIdentifier];
    [self.tableView setTableFooterView:[UIView.alloc initWithFrame:CGRectZero]];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    BasicCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    NSDictionary *dictionary = self.products[indexPath.section];
    NSArray *innerProducts = dictionary[@"products"];
    NSDictionary *productDic = innerProducts[indexPath.row];
    [cell.leftImageView sd_setImageWithURL:[NSURL URLWithString:productDic[@"product_url"]]];
    [cell.descriptionLabel setText:productDic[@"product_name"]];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSDictionary *dic = self.products[section];
    return [dic[@"products"] count];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    CheapestShoppingListHeaderView *headerView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:headerIdentifier];
    NSDictionary *dic = self.products[section];
    [headerView.storeLogoImageView sd_setImageWithURL:[NSURL URLWithString:dic[@"store_logo"]]];
    NSNumber *price = dic[@"price"];
    [headerView.subTotalPrice setText:[NSString stringWithFormat:@"%.2f", [price floatValue]] ];
    [headerView.storeWithBranchNameLabel setText:dic[@"branch_name"]];
    return headerView;
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 60.f;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return self.products.count;
}

- (IBAction)backButtonPressed:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setProducts:(NSArray *)products{
    _products = products;
    [self.tableView reloadData];
}

@end
