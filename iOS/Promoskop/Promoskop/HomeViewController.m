//
//  ViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 14/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "HomeViewController.h"
#import "DataAccessLayer.h"
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
            [searchedProductsViewController setFoundProducts:[[DataAccessLayer database]searchProductWithName:searchedText]];
        }
    }
}
@end
