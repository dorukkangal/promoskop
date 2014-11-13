//
//  SearchedProductsViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 15/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "SearchedProductsViewController.h"
#import "ProductWithPriceDetailViewController.h"
#import "Globals.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import <AFNetworking.h>
#import <MBProgressHUD.h>

@interface SearchedProductsViewController ()<UITableViewDataSource,UITableViewDelegate, UISearchBarDelegate>
@property (weak, nonatomic) IBOutlet UITableView *productsTableView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@end

@implementation SearchedProductsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.interactivePopGestureRecognizer.enabled = NO;
    self.productsTableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
//    MBProgressHUD *hud =   [MBProgressHUD showHUDAddedTo:self.view animated:YES];
//    hud.labelText = @"Fetching Data";
//    hud.mode = MBProgressHUDModeIndeterminate;
}

- (void)viewWillAppear:(BOOL)animated{
    [self.searchBar becomeFirstResponder];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.foundProducts.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"SearchedProducts" forIndexPath:indexPath];
    NSDictionary *dict = self.foundProducts[indexPath.row];
    UILabel *textLabel = (UILabel *)[cell.contentView viewWithTag:101];
    UIImageView *imageView = (UIImageView *)[cell.contentView viewWithTag:100];
    [textLabel setText:dict[@"product_name"]];
    [imageView sd_setImageWithURL:[NSURL URLWithString:dict[@"product_url"]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
        [imageView setImage:image];
    }];
    return cell;
}

-(BOOL)searchBar:(UISearchBar *)searchBar shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSString *searchText =[text isEqualToString:@"\n"] ? searchBar.text : [searchBar.text stringByReplacingCharactersInRange:range withString:text];
    NSString *encodedString = [searchText stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    if(![text isEqualToString:@"\n"]){
        if(searchText.length >=  2){
            
            manager.requestSerializer = [AFHTTPRequestSerializer serializer];
            [manager GET:[baseURL stringByAppendingFormat:@"%@%@",findBySubString,encodedString] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
                NSString *absoluteString =operation.request.URL.absoluteString;
                NSRange range = [absoluteString rangeOfString:@"="];
                NSString *textFromResponse =[absoluteString substringWithRange:NSMakeRange(range.location + 1, absoluteString.length - range.location - 1)];
                if([[textFromResponse stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] isEqualToString:searchBar.text]){
                    self.foundProducts = (NSArray *)responseObject;
                    self.productsTableView.hidden = NO;
                    [self.productsTableView reloadData];
                    [MBProgressHUD hideHUDForView:self.view animated:YES];
                }
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                NSLog(@"[SearchedProductsViewController]Error : %@",[error description]);
            }];

        }
        else {
            self.foundProducts = [NSArray array];
            [self.productsTableView reloadData];
        }
    }
    else {
        [searchBar resignFirstResponder];
        NSLog(@"Operation Count :%zd",[manager.operationQueue operations].count );
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        manager.requestSerializer = [AFHTTPRequestSerializer serializer];
        [manager GET:[baseURL stringByAppendingFormat:@"%@%@",findBySubString,encodedString] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
            self.foundProducts = (NSArray *)responseObject;
            self.productsTableView.hidden = NO;
            [self.productsTableView reloadData];
            [MBProgressHUD hideHUDForView:self.view animated:YES];
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"[SearchedProductsViewController]Error : %@",[error description]);
        }];
    }

    return YES;
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
    [self.searchBar resignFirstResponder];
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailTableViewController"]){
        UITableViewCell *cell = (UITableViewCell *)sender;
        NSIndexPath *indexPath = [self.productsTableView indexPathForCell:cell];
        NSDictionary *dict = self.foundProducts[indexPath.row];
        ProductWithPriceDetailViewController *productWithPriceDetailTableViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        [productWithPriceDetailTableViewController setProductID:[dict[@"barcode_id"] integerValue]];
    }
}
- (void)setFoundProducts:(NSArray *)foundProducts{
    _foundProducts = foundProducts;
    [UIView animateWithDuration:.5f animations:^{
        self.productsTableView.hidden = NO;
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideHUDForView:self.view animated:YES];
            });
        });
    } completion:^(BOOL finished) {
      self.productsTableView.hidden = NO;
    }];
    [self.productsTableView reloadData];
}

- (IBAction)cancelButtonPressed:(id)sender {
    [self.navigationController popViewControllerAnimated:NO];
}
@end
