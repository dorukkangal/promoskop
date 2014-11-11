//
//  CheapestShoppingListInGivenRadiusViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 09/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "CheapestShoppingListInGivenRadiusViewController.h"
#import "CheapestShoppingListHeaderView.h"
#import "ShoppingCartManager.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "BasicCell.h"
#import <SWRevealViewController.h>

@interface CheapestShoppingListInGivenRadiusViewController()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSMutableArray *notFoundProducts;
@end

static NSString * const cellIdentifier = @"CheapestShoppingListTableViewCell";
static NSString * const headerIdentifier = @"CheapestShoppingListHeaderView";

@implementation CheapestShoppingListInGivenRadiusViewController

- (void)viewDidLoad{
    [self.tableView registerNib:[UINib nibWithNibName:@"CheapestShoppingListHeaderView" bundle:nil] forHeaderFooterViewReuseIdentifier:headerIdentifier];
    [self.tableView setTableFooterView:[UIView.alloc initWithFrame:CGRectZero]];
}

- (void)viewWillAppear:(BOOL)animated{
    [self.revealViewController.view removeGestureRecognizer:self.revealViewController.panGestureRecognizer];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    BasicCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    if(indexPath.section < self.products.count){
        NSDictionary *dictionary = self.products[indexPath.section];
        NSArray *innerProducts = dictionary[@"products"];
        NSDictionary *productDic = innerProducts[indexPath.row];
        [cell.leftImageView sd_setImageWithURL:[NSURL URLWithString:productDic[@"product_url"]]];
        [cell.descriptionLabel setText:productDic[@"product_name"]];
    }
    else if(self.products.count > 0){
        NSDictionary *dictionary = self.notFoundProducts[indexPath.row];
        [cell.descriptionLabel setText:dictionary[@"product_name"]];
        [cell.leftImageView sd_setImageWithURL:[NSURL URLWithString:dictionary[@"product_url"]]];
    }

    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if(section < self.products.count){
        NSDictionary *dic = self.products[section];
        return [dic[@"products"] count];
    }
    else if(self.products.count > 0){
        return [ShoppingCartManager manager].productsArrayCurrentInShoppingBasket.count - [self countOfProductsFoundInStore];
    }
    else
        return 0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    CheapestShoppingListHeaderView *headerView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:headerIdentifier];
    if(section < self.products.count){
        headerView.storeLogoImageView.hidden = NO;
        headerView.subTotalPrice.hidden = NO;
        NSDictionary *dic = self.products[section];
        [headerView.storeLogoImageView sd_setImageWithURL:[NSURL URLWithString:dic[@"store_logo"]]];
        NSNumber *price = dic[@"price"];
        [headerView.subTotalPrice setText:[NSString stringWithFormat:@"%.2f", [price floatValue]] ];
        [headerView.storeWithBranchNameLabel setText:dic[@"branch_name"]];
    }
    else if(section == self.products.count){
        headerView.storeLogoImageView.hidden = YES;
        headerView.subTotalPrice.hidden = YES;
        [headerView.storeWithBranchNameLabel setText:@"BULUNAMAYAN ÜRÜNLER"];
    }
    return headerView;
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 60.f;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    if(self.products.count == [ShoppingCartManager manager].productsArrayCurrentInShoppingBasket.count){
        return self.products.count;
    }
    else if(self.products.count > 0){
        return self.products.count + 1;
    }
    else return 0;
}

- (IBAction)backButtonPressed:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setProducts:(NSArray *)products{
    _products = products;
    [self prepareNotFoundProductArray];
    [self.tableView reloadData];
}

- (void)prepareNotFoundProductArray{
    NSMutableArray *foundProducts = [NSMutableArray array];
    for (NSDictionary *dic in self.products) {
        [foundProducts addObjectsFromArray:dic[@"products"]];
    }
    [self.notFoundProducts addObjectsFromArray:[ShoppingCartManager manager].productsArrayCurrentInShoppingBasket];
    for (NSDictionary *dic in foundProducts) {
        [self.notFoundProducts enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
            if([obj[@"barcode_id"] integerValue] == [dic[@"barcode_id"] integerValue]){
                *stop = YES;
                [self.notFoundProducts removeObjectAtIndex:idx];
            }
        }];
    }}

- (NSInteger)countOfProductsFoundInStore{
    NSInteger count = 0 ;
    for (NSDictionary* dic in self.products) {
        count += [dic[@"products"] count];
    }
    return count;
}

- (NSMutableArray *)notFoundProducts{
    if(!_notFoundProducts){
        _notFoundProducts = [NSMutableArray array];
    }
    return _notFoundProducts;
}

@end
