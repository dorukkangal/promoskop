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


@interface HomeViewController ()<UISearchBarDelegate>


@property (weak, nonatomic) IBOutlet UIButton *btnGo;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

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

- (IBAction)btnGoPressed:(id)sender {
    //Pass to the other view
}


- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    NSLog(@"[HomeViewController] => searchBarSearchButtonClicked");
    [self performSegueWithIdentifier:@"SearchedProductsViewController" sender:searchBar];

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
@end
