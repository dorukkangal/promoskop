//
//  ShoppingListViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 02/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ShoppingListViewController.h"
#import <SWRevealViewController.h>
#import "Globals.h"
#import "ShoppingCartManager.h"
#import "BasicCell.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "CheapestShoppingListInGivenRadiusViewController.h"
#import <CoreLocation/CoreLocation.h>
#import "INTULocationManager.h"
#import <AFNetworking.h>
#import "ShoppingListHeaderView.h"

@interface ShoppingListViewController ()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) CLLocation *currentLocation;
@end

static NSString * const headerIdentifier = @"ShoppingListHeaderView";
NSArray * kilometerIndexArray;

@implementation ShoppingListViewController

- (void)viewDidLoad{
//    [self setupUI];
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    [self.tableView registerNib:[UINib nibWithNibName:@"ShoppingListHeaderView" bundle:nil] forHeaderFooterViewReuseIdentifier:headerIdentifier];
    [self getUserLocation];
    kilometerIndexArray = @[@2.0,@5.0,@10.0];
}

- (void)viewWillAppear:(BOOL)animated{
    [self setupUI];
    [self.navigationController.navigationBar setTintColor:[UIColor whiteColor]];
    [self.navigationItem.leftBarButtonItem setTintColor:[UIColor whiteColor]];
}

- (void)setupUI{
//    [self.revealButtonItem setAction:@selector(revealToggle:)];
    SWRevealViewController *revealViewController = self.revealViewController;
//    revealViewController.delegate = self;
    if(revealViewController){
        [self.revealButtonItem setTarget:self.revealViewController];
        [self.revealButtonItem setAction:@selector(revealToggle:)];
        [self.view addGestureRecognizer:self.revealViewController.tapGestureRecognizer];
        [self.revealViewController.view addGestureRecognizer:self.revealViewController.panGestureRecognizer];
    }
}

- (void)getUserLocation{
    INTULocationManager *locationManager = [INTULocationManager sharedInstance];
    [locationManager requestLocationWithDesiredAccuracy:INTULocationAccuracyBlock timeout:10.f block:^(CLLocation *currentLocation, INTULocationAccuracy achievedAccuracy, INTULocationStatus status) {
        self.currentLocation = currentLocation;
    }];
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

-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath{
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    if(editingStyle == UITableViewCellEditingStyleDelete){
        NSDictionary *deletedProduct = [ShoppingCartManager manager].productsArrayCurrentInShoppingBasket[indexPath.row];
        [[ShoppingCartManager manager]removeProductFromShoppingCart:[deletedProduct[@"barcode_id"] integerValue]];
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    ShoppingListHeaderView *headerView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:headerIdentifier];
    return headerView;
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 81.f;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

- (IBAction)clearShoppingList:(id)sender {

    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Sepeti Boşalt"
                                  message:@"Sepetinizi boşaltmak istediğinize emin misiniz?"
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"Evet"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [[ShoppingCartManager manager] clearShoppingCart];
                             [self.tableView reloadData];
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             
                         }];
    UIAlertAction* cancel = [UIAlertAction
                             actionWithTitle:@"İptal"
                             style:UIAlertActionStyleDefault
                             handler:^(UIAlertAction * action)
                             {
                                 [alert dismissViewControllerAnimated:YES completion:nil];
                                 
                             }];
    
    [alert addAction:ok];
    [alert addAction:cancel];
    
    [self presentViewController:alert animated:YES completion:nil];

}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"CheapestShoppingListInGivenRadiusViewController"]){
        
        ShoppingListHeaderView* header = (ShoppingListHeaderView*)[self.tableView headerViewForSection:0];
        NSNumber* distance = (NSNumber*) kilometerIndexArray[header.segmentedControl.selectedSegmentIndex];
        NSDictionary *postData = @{ @"currentLatitude" : @(self.currentLocation.coordinate.latitude), @"currentLongitude" : @(self.currentLocation.coordinate.longitude) ,
                                    @"maxDistance" : distance, @"barcodeIds" : [[[ShoppingCartManager manager] productsArrayCurrentInShoppingBasket] valueForKeyPath:@"@distinctUnionOfObjects.barcode_id"]};
        AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
        operationManager.requestSerializer = [AFJSONRequestSerializer serializer];
        [operationManager POST:[baseURL stringByAppendingString:calculate] parameters:postData success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"Reponse Object :%@", responseObject);
            CheapestShoppingListInGivenRadiusViewController *cheapestShoppingListInGivenRadius = (CheapestShoppingListInGivenRadiusViewController *)segue.destinationViewController;
            [cheapestShoppingListInGivenRadius setProducts:(NSArray *)responseObject];
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"[ShoppingListViewController] Error : %@" , [error description]);
        }];
    }
}

@end
