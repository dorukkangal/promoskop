//
//  ProductWithPriceDetailTableViewController.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ProductWithPriceDetailTableViewController.h"
#import "DataAccessLayer.h"
#import "BranchWithProductPrice.h"
#import "BranchWithProductPriceTableViewCell.h"
#import <CoreLocation/CoreLocation.h>


@interface ProductWithPriceDetailTableViewController ()<CLLocationManagerDelegate>

@property (strong, nonatomic) NSArray *branchesAndPricesArray;
@property (strong, nonatomic) CLLocationManager* locationManager;

@end

@implementation ProductWithPriceDetailTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    
    self.branchesAndPricesArray = [[DataAccessLayer database] getBranchAndPriceDetailForProductWithId:4];
    
    self.locationManager.delegate = self;
    [self.locationManager requestWhenInUseAuthorization];
    [self.locationManager startUpdatingLocation];

    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return self.branchesAndPricesArray.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.row == 0) {
        
        static NSString *FirstCellIdentifier = @"TopCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:FirstCellIdentifier forIndexPath:indexPath];
        
        cell.textLabel.text = @"Product Name" ;
        cell.detailTextLabel.text = @"Maybe some product detail";
        cell.imageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://www.animalspot.net/wp-content/uploads/2013/02/Rabbit.jpg"]]];
        return cell;

    } else {
        
        static NSString *CellIdentifier = @"ReusableCell";
        BranchWithProductPriceTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
        
        BranchWithProductPrice* bwpp = [self.branchesAndPricesArray objectAtIndex:indexPath.row-1];
        cell.storeLabel.text = bwpp.storeName ;
        cell.branchAddressLabel.text = bwpp.address;
        cell.priceLabel.text = [NSString stringWithFormat:@"%ld TL", (long)bwpp.price];
        cell.distanceLabel.text = @"Appro. 0 km";
        return cell;
    }
    
    
    
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations{
    CLLocation* currentLocation = [locations lastObject];
    NSLog(@"lat%f - lon%f", currentLocation.coordinate.latitude, currentLocation.coordinate.longitude);
    //[self.locationManager stopUpdatingLocation];
}
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"failed to get location");
}

@end
