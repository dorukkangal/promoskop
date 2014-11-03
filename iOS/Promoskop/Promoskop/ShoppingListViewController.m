//
//  ShoppingListViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ShoppingListViewController.h"
#import <SWRevealViewController.h>
#import "ShoppingCartManager.h"
#import "BasicCell.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface ShoppingListViewController ()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end

@implementation ShoppingListViewController

- (void)viewDidLoad{
    [self setupUI];
}

- (void)setupUI{
    [self.revealButtonItem setAction:@selector(revealToggle:)];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    BasicCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BasicCell" forIndexPath:indexPath];
    NSDictionary *product = [ShoppingCartManager manager].productsArrayCurrentInShoppingBasket[indexPath.row];
    [cell.leftImageView sd_setImageWithURL:[NSURL URLWithString:product[@"product_url"]]];
    [cell.descriptionLabel setText:product[@"product_name"]];
    return cell;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return  [ShoppingCartManager manager].productsArrayCurrentInShoppingBasket.count;
}
@end
