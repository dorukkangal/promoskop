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
#import <CoreLocation/CoreLocation.h>
#import "INTULocationManager.h"
#import <AFNetworking.h>

@interface ShoppingListViewController ()<UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) CLLocation *currentLocation;
@end

@implementation ShoppingListViewController

- (void)viewDidLoad{
    [self setupUI];
    [self getUserLocation];
}

- (void)setupUI{
    [self.revealButtonItem setAction:@selector(revealToggle:)];
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
        [[ShoppingCartManager manager]removeProductFromShoppingCart:[deletedProduct[@"product_id"] integerValue]];
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }
}

- (IBAction)clearShoppingList:(id)sender {
    [[ShoppingCartManager manager] clearShoppingCart];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"CheapestShoppingListInGivenRadiusViewController"]){
        NSDictionary *postData = @{ @"currentLatitude" : @(self.currentLocation.coordinate.latitude), @"currentLongitude" : @(self.currentLocation.coordinate.longitude) ,
                                    @"maxDistance" : @5.0f, @"barcodeIds" : [[[ShoppingCartManager manager] productsArrayCurrentInShoppingBasket] valueForKeyPath:@"@distinctUnionOfObjects.product_id"]};
        AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
        [operationManager POST:[baseURL stringByAppendingString:calculate] parameters:postData success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"Reponse Object :%@", responseObject);
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"[ShoppingListViewController] Error : %@" , [error description]);
        }];
    }
}

@end
