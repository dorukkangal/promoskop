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


@interface HomeViewController ()<UISearchBarDelegate, UITableViewDataSource, UITableViewDelegate>


@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *productsArray;
@end

@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    NSLog(@"[HomeViewController] => searchBarSearchButtonClicked");
    [self performSegueWithIdentifier:@"SearchedProductsViewController" sender:searchBar];

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"BasicCell" forIndexPath:indexPath];
    NSDictionary *dic = self.productsArray[indexPath.row];
    [cell.imageView sd_setImageWithURL:dic[@"product_url"]];
    [cell.textLabel setText:dic[@"product_name"]];
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.productsArray.count;
}

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

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
    [self.searchBar resignFirstResponder];
}
@end
