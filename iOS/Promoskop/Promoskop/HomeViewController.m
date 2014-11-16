//
//  ViewController.m
//  Promoskop
//
//  Created by Mustafa Besnili on 14/10/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "HomeViewController.h"
#import <AFNetworking.h>
#import "BasicCell.h"
#import <Colours.h>
#import "Globals.h"
#import "PopularProductCollectionViewCell.h"
#import "SearchedProductsViewController.h"
#import <SDWebImage/UIImageView+WebCache.h>
#import "ProductWithPriceDetailViewController.h"
#import <SWRevealViewController.h>
#import <AFNetworking.h>
#import <MBProgressHUD.h>
#import "ShoppingCartManager.h"


static NSString * const popularProductCollectionViewCell = @"PopularProductCollectionViewCell";
static NSString * const popularProductReusableViewCell = @"PopularProductReusableViewCell";

@interface HomeViewController ()<UISearchBarDelegate, SWRevealViewControllerDelegate, UIGestureRecognizerDelegate , UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, CustomCollectionCellDelegate, UIAlertViewDelegate>

@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (nonatomic, strong) NSArray *productsArray;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *revealButtonItem;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@end


@implementation HomeViewController
#pragma mark view life cycle methods

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
    [operationManager GET:[baseURL stringByAppendingString:config] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *currentVersion = [NSBundle mainBundle].infoDictionary[@"CFBundleShortVersionString"];
        NSString *serverAppVersion = responseObject[@"app_version"];
        if(![currentVersion isEqualToString:serverAppVersion]){
            [self warnUserToUpdate];
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"[HomeViewController] Error : %@" , [error description]);
    }];
    

    NSDictionary *popularProductsContent = [NSDictionary dictionaryWithContentsOfFile:[self path]];
    NSDate *createdDate = popularProductsContent[@"createdAt"];
    NSTimeInterval timeInterval = [[NSDate date] timeIntervalSinceDate:createdDate];
    if(timeInterval >= 15 * 60 || !popularProductsContent){
        [self requestPopularProducts];
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.mode = MBProgressHUDModeIndeterminate;
        hud.labelText = @"Ürünler getiriliyor";
    }
    else {
        self.productsArray = popularProductsContent[@"products"];
    }
    [self setupUI];
}

-(void)warnUserToUpdate{
    UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Info" message:@"Yeni sürümde güzel güzel değişiklikler yaptık. Update pls." delegate:self cancelButtonTitle:@"Daha Sonra :-(" otherButtonTitles:@"Tamam", nil];
    [alertView show];
}

- (NSString *)path{
    NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *path = [searchPaths[0] stringByAppendingPathComponent:@"PopularProducts.plist"];
    return path;
}

- (void)viewWillAppear:(BOOL)animated{
    [self.revealViewController.view addGestureRecognizer:self.revealViewController.panGestureRecognizer];
}

- (void)viewWillDisappear:(BOOL)animated{
    [self.revealViewController.view removeGestureRecognizer:self.revealViewController.panGestureRecognizer];
}

#pragma mark UI utility methods

- (void)requestPopularProducts{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager GET:[NSString stringWithFormat:@"%@%@15",baseURL,getPopularProducts] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        self.productsArray = (NSArray *)responseObject;
        NSDictionary *dic = @{ @"createdAt" : [NSDate date] , @"products" : self.productsArray  };
        [dic writeToFile:[self path] atomically:YES];
        
        [self.collectionView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"[HomeViewController]Error :%@",[error description]);
    }];
}

-(void)addToBasketButtonTappedIn:(PopularProductCollectionViewCell *)cell{
    NSIndexPath *indexPath = [self.collectionView indexPathForCell:cell];
    NSDictionary *productDictionary = self.productsArray[indexPath.item];
    if(![[ShoppingCartManager manager]isProductInShoppingCart:[productDictionary[@"barcode_id"]integerValue]]){
        [[ShoppingCartManager manager]addProductToShoppingCart:productDictionary];
    }
    else {
        [[ShoppingCartManager manager]removeProductFromShoppingCart:[productDictionary[@"barcode_id"] integerValue]];
    }
    [self.collectionView reloadItemsAtIndexPaths:@[indexPath]];
}

- (void)setupUI{
    [self.searchBar setBarTintColor:[UIColor blueberryColor]];
    self.navigationController.interactivePopGestureRecognizer.delegate = self;
    [self setupRevealMenu];
}

- (void)setupRevealMenu{
    SWRevealViewController *revealViewController = self.revealViewController;
    revealViewController.delegate = self;
    if(revealViewController){
        [self.revealButtonItem setTarget:self.revealViewController];
        [self.revealButtonItem setAction:@selector(revealToggle:)];
        [self.view addGestureRecognizer:self.revealViewController.tapGestureRecognizer];
        [self.revealViewController.view addGestureRecognizer:self.revealViewController.panGestureRecognizer];
    }
}

#pragma mark UISearchBar delegate methods
-(BOOL)searchBarShouldBeginEditing:(UISearchBar *)searchBar{
    [searchBar resignFirstResponder];
    [self performSegueWithIdentifier:@"SearchedProductsViewController" sender:searchBar];
    return NO;
}

#pragma mark UICollectionViewDataSource vs UICollectionViewDelegate methods

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    PopularProductCollectionViewCell *cell = (PopularProductCollectionViewCell *) [collectionView dequeueReusableCellWithReuseIdentifier:popularProductCollectionViewCell forIndexPath:indexPath];
    cell.delegate =self;
    NSDictionary *product = self.productsArray[indexPath.item];
    [cell.productImageView sd_setImageWithURL:[NSURL URLWithString:product[@"product_url"]]];
    [cell.productNameLabel setText:product[@"product_name"]];
    [cell.productNameLabel setPreferredMaxLayoutWidth:134.f];
    if([[ShoppingCartManager manager]isProductInShoppingCart:[product[@"barcode_id"] integerValue]])
       [cell.shoppingBasketButton setSelected:YES];
    else
        [cell.shoppingBasketButton setSelected:NO];
    return cell;
}

-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath{
    
    UICollectionReusableView *reusableView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:popularProductReusableViewCell forIndexPath:indexPath];

    if(kind == UICollectionElementKindSectionHeader && indexPath.section == 0){
        UIImageView *imageView = (UIImageView *)[reusableView viewWithTag:200];
        [imageView setImage:[UIImage imageNamed:@"popular_products"]];
        UILabel *label = (UILabel *)[reusableView viewWithTag:201];
        [label setText:@"Popüler Ürünler"];
    }
    return reusableView;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return self.productsArray.count;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    if(IS_IPHONE_4_OR_LESS || IS_IPHONE_5)
        return UIEdgeInsetsMake(6, 6, 6, 6);
    else
        return UIEdgeInsetsMake(6, 25, 25, 25);
}

-(void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath{
    
}

- (void)scrollViewWillBeginDecelerating:(UIScrollView *)scrollView{
    [self.searchBar resignFirstResponder];
}

#pragma mark Navigation segue
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([segue.identifier isEqualToString:@"ProductWithPriceDetailViewController"]){
        PopularProductCollectionViewCell *popularProductCollectionViewCell = (PopularProductCollectionViewCell *)sender;
        NSIndexPath *itemIndexPath = [self.collectionView indexPathForCell:popularProductCollectionViewCell];
        ProductWithPriceDetailViewController *productWithPriceDetailViewController = (ProductWithPriceDetailViewController *)segue.destinationViewController;
        [productWithPriceDetailViewController setProductID:[self.productsArray[itemIndexPath.item][@"barcode_id"] integerValue]];
    }
}

#pragma mark SWRevealViewController delegate


- (void)revealController:(SWRevealViewController *)revealController willMoveToPosition:(FrontViewPosition)position{
    if(position == FrontViewPositionRight){
        self.collectionView.userInteractionEnabled = NO;
        self.searchBar.userInteractionEnabled = NO;
    }
    else if(position == FrontViewPositionLeft){
        self.collectionView.userInteractionEnabled = YES;
        self.searchBar.userInteractionEnabled = YES;
    }
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
    return YES;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldBeRequiredToFailByGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
    return [gestureRecognizer isKindOfClass:UIScreenEdgePanGestureRecognizer.class];
}

#pragma mark UIAlertView delegate

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex == 0){

    }
    else if(buttonIndex == 1) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"itms://itunes.com/apps/tercihrehberi"]];
    }
}

@end
