//
//  ProductWithPriceDetailTableViewController.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ProductWithPriceDetailViewController.h"
#import "DataAccessLayer.h"
#import "BranchWithProductPriceTableViewCell.h"
#import "INTULocationManager.h"
#import <AFNetworking.h>
#import "Globals.h"
#import "MapForBranchViewController.h"
#import <MapKit/MapKit.h>
#import <SDWebImage/UIImageView+WebCache.h>

#define TABLEVIEW_FIRST_CELL_HEIGHT 70
#define TABLEVIEW_CELL_HEIGHT 130

@interface ProductWithPriceDetailViewController ()<MKMapViewDelegate>


@property (nonatomic, strong) NSMutableArray *sortableBranchesAndPricesArray;
@property (nonatomic, weak ) IBOutlet UITableView *tableView;
@property (nonatomic, assign) NSInteger locationRequestID;
@property (nonatomic,weak) IBOutlet MKMapView *mapView;
@property (nonatomic, strong) NSDictionary *responseDict;
@property (nonatomic) BOOL isMapOnScreen;
@property (nonatomic, strong) CLLocation *currentLocation;

@property (strong,nonatomic) NSDictionary* selectedBranch;

@end

@implementation ProductWithPriceDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    

    self.isMapOnScreen = NO;
    
    self.mapView.delegate = self;
    
}

- (void)startLocationRequest{
    
    __weak __typeof(self) weakSelf = self;
    
    INTULocationManager *locMgr = [INTULocationManager sharedInstance];
    self.locationRequestID = [locMgr requestLocationWithDesiredAccuracy:INTULocationAccuracyBlock
                                                                timeout:10.0
                                                   delayUntilAuthorized:YES
      block:^(CLLocation *currentLocation, INTULocationAccuracy achievedAccuracy, INTULocationStatus status){
          __typeof(weakSelf) strongSelf = weakSelf;
          
          if (status == INTULocationStatusSuccess) {
              // achievedAccuracy is at least the desired accuracy (potentially better)
              NSLog(@"Location request successful! Current Location:\n%@", currentLocation);
              self.currentLocation = currentLocation;
              for (int i = 0; i < [self.sortableBranchesAndPricesArray count]; i++){
                  NSMutableDictionary* currentDict = [self.sortableBranchesAndPricesArray[i] mutableCopy];
                  double lat =  [currentDict[@"latitude"] floatValue];
                  double lon =  [currentDict[@"longitude"] floatValue];
                  
                  CLLocation* branchLocation = [[CLLocation alloc] initWithLatitude:lat  longitude:lon];
                  CLLocationDistance distanceInMeters = [branchLocation distanceFromLocation:currentLocation];
                  double distanceInKilometers = distanceInMeters / 1000.0;
                  
                  [currentDict setObject:[NSNumber numberWithDouble:distanceInKilometers] forKey:@"distance"];
                  
                  [self.sortableBranchesAndPricesArray replaceObjectAtIndex:i withObject:[currentDict copy]];
              }
              //Sorting the array according to a key in the inner dictionary
              NSSortDescriptor* distanceDescriptor = [[NSSortDescriptor alloc] initWithKey:@"distance" ascending:YES];
              NSArray* sortDescriptors = [NSArray arrayWithObject:distanceDescriptor];
              self.sortableBranchesAndPricesArray = [[self.sortableBranchesAndPricesArray sortedArrayUsingDescriptors:sortDescriptors] mutableCopy];
          }
          else if (status == INTULocationStatusTimedOut) {
              // You may wish to inspect achievedAccuracy here to see if it is acceptable, if you plan to use currentLocation
              UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:@"Timeout"
                                                                 message:[NSString stringWithFormat:@"Location request timed out. Current Location:\n%@", currentLocation]
                                                                delegate:self
                                                       cancelButtonTitle:@"OK"
                                                       otherButtonTitles:nil];
              [theAlert show];
          }
          else {
              // An error occurred
              UIAlertView *theAlert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                 message:@"Error message."
                                                                delegate:self
                                                       cancelButtonTitle:@"OK"
                                                       otherButtonTitles:nil];
              if (status == INTULocationStatusServicesNotDetermined) {
                  theAlert.message = @"Error: User has not responded to the permissions alert.";
              } else if (status == INTULocationStatusServicesDenied) {
                  theAlert.message = @"Error: User has denied this app permissions to access device location.";
              } else if (status == INTULocationStatusServicesRestricted) {
                  theAlert.message = @"Error: User is restricted from using location services by a usage policy.";
              } else if (status == INTULocationStatusServicesDisabled) {
                  theAlert.message = @"Error: Location services are turned off for all apps on this device.";
              } else {
                  theAlert.message = @"An unknown error occurred.\n(Are you using iOS Simulator with location set to 'None'?)";
              }
              [theAlert show];
          }
          
          strongSelf.locationRequestID = NSNotFound;
                                                                  }];
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
    if (self.sortableBranchesAndPricesArray.count == 0) {
        return 2;
    }
    return self.sortableBranchesAndPricesArray.count + 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0){
        return TABLEVIEW_FIRST_CELL_HEIGHT;
    }
    return TABLEVIEW_CELL_HEIGHT;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.row == 0) {
        
        static NSString *FirstCellIdentifier = @"TopCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:FirstCellIdentifier forIndexPath:indexPath];
        

            UILabel *productNameLabel = (UILabel *)[cell.contentView viewWithTag:101];
            productNameLabel.text = self.responseDict[@"product_name"];
            UIImageView *productImageView = (UIImageView *) [cell.contentView viewWithTag:100];
            [productImageView sd_setImageWithURL:[NSURL URLWithString:self.responseDict[@"product_url"]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                [productImageView setImage:image];
            }];
//            cell.detailTextLabel.text = self.responseDict[@"product_url"];


        /*cell.imageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://www.animalspot.net/wp-content/uploads/2013/02/Rabbit.jpg"]]];*/
        
        return cell;
        
    } else {
        
        static NSString *CellIdentifier = @"ReusableCell";
        BranchWithProductPriceTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
        
        if (self.sortableBranchesAndPricesArray.count > 0) {
            NSDictionary *resultDict = self.sortableBranchesAndPricesArray[indexPath.row-1];
            cell.storeLabel.text = resultDict[@"store_name"];
            cell.branchAddressLabel.text = resultDict[@"address"];
            cell.priceLabel.text = [NSString stringWithFormat:@"%@ TL", resultDict[@"price"]];
            double distance = [resultDict[@"distance"] floatValue];
            cell.distanceLabel.text = [NSString stringWithFormat:@"Appro. %.2f km", distance ];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            cell.userInteractionEnabled = YES;
        }
        else{
            cell.storeLabel.text = @"";
            cell.branchAddressLabel.text = @"Product is not being sold in any store.";
            cell.priceLabel.text = @"";
            cell.distanceLabel.text = @"";
            cell.accessoryType = UITableViewCellAccessoryNone;
            cell.userInteractionEnabled = NO;
        }
        
        return cell;
    }
    
    
    
}

/**
 Implement the setter for locationRequestID in order to update the UI as needed.
 */
- (void)setLocationRequestID:(NSInteger)locationRequestID
{
    _locationRequestID = locationRequestID;
    
    BOOL isProcessingLocationRequest = (locationRequestID != NSNotFound);
    
    //    self.desiredAccuracyControl.enabled = !isProcessingLocationRequest;
    //    self.timeoutSlider.enabled = !isProcessingLocationRequest;
    //    self.requestCurrentLocationButton.enabled = !isProcessingLocationRequest;
    //    self.forceCompleteRequestButton.enabled = isProcessingLocationRequest;
    //    self.cancelRequestButton.enabled = isProcessingLocationRequest;
    
    if (isProcessingLocationRequest) {
        //        [self.activityIndicator startAnimating];
        NSLog(@"Location request in progress...");
    } else {
        //        [self.activityIndicator stopAnimating];
        [self.tableView reloadData];
        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(self.currentLocation.coordinate, 16000, 16000);
        
        [self.mapView setRegion:[self.mapView regionThatFits:region] animated:NO];
        
        for (NSDictionary* dict in self.sortableBranchesAndPricesArray) {
            
            MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
            double lat =  [dict[@"latitude"] floatValue];
            double lon =  [dict[@"longitude"] floatValue];
            point.coordinate = CLLocationCoordinate2DMake(lat, lon);
            point.title = [NSString stringWithFormat:@"%@ TL",dict[@"price"]];
            point.subtitle = [NSString stringWithFormat:@"%@ : %@ ", dict[@"store_name"], dict[@"address"]];;
            [self.mapView addAnnotation:point];
            
        }
    }
}


- (IBAction)flip:(id)sender {
    if (!self.isMapOnScreen) {
        
        [UIView transitionFromView:self.tableView
                            toView:self.mapView
                          duration:1
                           options:UIViewAnimationOptionTransitionFlipFromRight | UIViewAnimationOptionShowHideTransitionViews
                        completion:^(BOOL finished){
                            self.isMapOnScreen = YES;
                            UIBarButtonItem* mapButton = (UIBarButtonItem *)sender;
                            [mapButton setTitle:@"List"];
                        }];
    }
    else{
        [UIView transitionFromView:self.mapView
                            toView:self.tableView
                          duration:1
                           options:UIViewAnimationOptionTransitionFlipFromRight | UIViewAnimationOptionShowHideTransitionViews
                        completion:^(BOOL finished){
                            self.isMapOnScreen = NO;
                            UIBarButtonItem* mapButton = (UIBarButtonItem*)sender;
                            [mapButton setTitle:@"Map"];
                        }];
    }
}
#pragma mark -
#pragma mark Navigation

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    if([segue.identifier isEqualToString:@"MapForBranch"]){
        NSInteger row = [self.tableView indexPathForCell:sender].row;
        NSLog(@"did select row at indexpath.row: %ld",(long)row);
        self.selectedBranch = self.sortableBranchesAndPricesArray[row-1];
        MapForBranchViewController *mfbvc = (MapForBranchViewController *)segue.destinationViewController;
        mfbvc.selectedBranch = self.selectedBranch;
    }
}

- (void)setProductID:(NSInteger)productID{
    _productID = productID;
    AFHTTPRequestOperationManager *operationManager = [AFHTTPRequestOperationManager manager];
    [operationManager GET:[baseURL stringByAppendingString:[NSString stringWithFormat:@"%@%zd",findByID,productID]] parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"Response Object => %@" , responseObject);
        self.responseDict = (NSDictionary *)responseObject;
        [self prepareArray];
        [self startLocationRequest];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        NSLog(@"Error : %@", [error description]);
    }];

}


- (void)prepareArray{
    self.sortableBranchesAndPricesArray = [self.responseDict[@"branches_prices"] mutableCopy];
    //    self.sortableBranchesAndPricesArray = [[[DataAccessLayer database] getBranchAndPriceDetailForProductWithId:2] mutableCopy];
    for (int i = 0; i < [self.sortableBranchesAndPricesArray count]; i++){
        
        NSMutableDictionary* mDict = [self.sortableBranchesAndPricesArray[i] mutableCopy];
        [mDict setObject:[NSNumber numberWithDouble:0.0] forKey:@"distance"];
        [self.sortableBranchesAndPricesArray replaceObjectAtIndex:i withObject:[mDict copy]];
    }
    
}

@end
