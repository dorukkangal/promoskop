//
//  SearchedProductsViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 15/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "SearchedProductsViewController.h"
#import "ProductWithPriceDetailViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>

@interface SearchedProductsViewController ()<UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *productsTableView;
@end

@implementation SearchedProductsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"FoundProducts => %@",self.foundProducts);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.foundProducts.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"SearchedProducts" forIndexPath:indexPath];
    NSDictionary *dict = self.foundProducts[indexPath.row];
    UILabel *textLabel = (UILabel *)[cell.contentView viewWithTag:101];
    UIImageView *imageView = (UIImageView *)[cell.contentView viewWithTag:100];
    [textLabel setText:dict[@"name"]];
    [imageView sd_setImageWithURL:[NSURL URLWithString:dict[@"url"]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        [imageView setImage:image];
    }];
    return cell;
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailTableViewController"]){
        UITableViewCell *cell = (UITableViewCell *)sender;
        NSIndexPath *indexPath = [self.productsTableView indexPathForCell:cell];
        NSDictionary *dict = self.foundProducts[indexPath.row];
        ProductWithPriceDetailViewController *productWithPriceDetailTableViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        [productWithPriceDetailTableViewController setProductID:[dict[@"id"] integerValue]];
    }
    
}
- (void)setFoundProducts:(NSArray *)foundProducts{
    _foundProducts = foundProducts;
    [self.productsTableView reloadData];
}

@end
