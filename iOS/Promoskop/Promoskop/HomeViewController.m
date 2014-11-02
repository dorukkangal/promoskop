//
//  ViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 14/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "HomeViewController.h"
#import <AFNetworking.h>
#import "Globals.h"
#import "SearchedProductsViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "ProductWithPriceDetailViewController.h"
#import <SWRevealViewController.h>


@interface HomeViewController ()<UISearchBarDelegate, UITableViewDataSource, UITableViewDelegate, SWRevealViewControllerDelegate>


@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *productsArray;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;
@end


@implementation HomeViewController
#pragma mark view life cycle methods

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setupUI];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)viewWillAppear:(BOOL)animated{
    [self.tableView reloadData];
}

#pragma mark UI utility methods

- (void)setupUI{
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    [self setupRevealMenu];
}

- (void)setupRevealMenu{
    SWRevealViewController *revealViewController = self.revealViewController;
    revealViewController.delegate = self;
    if(revealViewController){
        [self.revealButtonItem setTarget:self.revealViewController];
        [self.revealButtonItem setAction:@selector(revealToggle:)];
        [revealViewController.view addGestureRecognizer:self.revealViewController.panGestureRecognizer];
        [revealViewController.view addGestureRecognizer:self.revealViewController.tapGestureRecognizer];
    }
}

#pragma mark UISearchBar delegate methods
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    NSLog(@"[HomeViewController] => searchBarSearchButtonClicked");
    [self performSegueWithIdentifier:@"SearchedProductsViewController" sender:searchBar];

}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    if(searchText.length >= 2 ){
        AFHTTPRequestOperationManager *operationManagaer = [AFHTTPRequestOperationManager manager];
        [operationManagaer GET:[baseURL stringByAppendingString:[NSString stringWithFormat:@"%@%@",findBySubString,searchText]] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"%@", responseObject);
            self.productsArray = (NSArray *)responseObject;
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.tableView reloadData];
            });
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"Error Description %@", [error description]);
        }];
    }
}

#pragma mark UITableViewDataSource vs UITableViewDelegate methods
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BasicCell" forIndexPath:indexPath];
    NSDictionary *dic = self.productsArray[indexPath.row];
    
    UIImageView *leftImageView = (UIImageView *)[cell.contentView viewWithTag:100];
    [leftImageView setContentMode:UIViewContentModeScaleAspectFit];
    [leftImageView sd_setImageWithURL:dic[@"product_url"]];
    
    UILabel *productNameLabel = (UILabel *)[cell.contentView viewWithTag:101];
    productNameLabel.preferredMaxLayoutWidth = self.view.frame.size.width - CGRectGetMaxX(leftImageView.frame)- 15;
    [productNameLabel setText:dic[@"product_name"]];
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.productsArray.count;
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
    [self.searchBar resignFirstResponder];
}


#pragma mark Navigation segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"SearchedProductsViewController"]){
        if([sender isKindOfClass:[UISearchBar class]]){
            NSString *searchedText = [(UISearchBar *)sender text];
            SearchedProductsViewController *searchedProductsViewController = (SearchedProductsViewController *)segue.destinationViewController;
            AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
            NSString *str =[[searchedText  uppercaseStringWithLocale:[NSLocale localeWithLocaleIdentifier:@"TR_tr"]]stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSString *requestString = [baseURL stringByAppendingString:[NSString stringWithFormat:@"%@%@",findBySubString,str]];
            [operationManager GET:requestString parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
                NSArray *responseDict = (NSArray *)responseObject;
                NSArray *products = responseDict;
                [searchedProductsViewController setFoundProducts:products];
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                NSLog(@"Error : %@", [error description]);
            }];
        }
    }
    else if([segue.identifier isEqualToString:@"ProductWithPriceDetailViewController"]){
        UITableViewCell *senderCell = (UITableViewCell *)sender;
        NSIndexPath *indexPath = [self.tableView indexPathForCell:senderCell];
        ProductWithPriceDetailViewController *productWithPriceDetailViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        [productWithPriceDetailViewController setProductID:[self.productsArray[indexPath.row][@"barcode_id"]integerValue]];
    }
}

#pragma mark SWRevealViewController delegate


- (void)revealController:(SWRevealViewController *)revealController willMoveToPosition:(FrontViewPosition)position{
    if(position == FrontViewPositionRight){
        NSLog(@"Position FronViewPositionLeftSideMost");
        self.view.userInteractionEnabled = NO;
    }
    else if(position == FrontViewPositionLeft){
        NSLog(@"Position FrontViewPositionLeft");
        self.view.userInteractionEnabled = YES;
    }
}


@end
