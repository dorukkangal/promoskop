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
#import "MapForBranchViewController.h"
#import <MapKit/MapKit.h>


@interface ProductWithPriceDetailViewController ()<MKMapViewDelegate>


@property (strong, nonatomic) NSMutableArray *sortableBranchesAndPricesArray;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (assign, nonatomic) NSInteger locationRequestID;
@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@property (nonatomic) BOOL isMapOnScreen;

@property (strong,nonatomic) NSDictionary* selectedBranch;

@end

@implementation ProductWithPriceDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    
    self.sortableBranchesAndPricesArray = [[[DataAccessLayer database] getBranchAndPriceDetailForProductWithId:self.productID] mutableCopy];
//    self.sortableBranchesAndPricesArray = [[[DataAccessLayer database] getBranchAndPriceDetailForProductWithId:2] mutableCopy];
    
    for (int i = 0; i < [self.sortableBranchesAndPricesArray count]; i++){
        
        NSMutableDictionary* mDict = [self.sortableBranchesAndPricesArray[i] mutableCopy];
        [mDict setObject:[NSNumber numberWithDouble:0.0] forKey:@"distance"];
        [self.sortableBranchesAndPricesArray replaceObjectAtIndex:i withObject:[mDict copy]];
    }
    
    self.isMapOnScreen = NO;
    
    self.mapView.delegate = self;
    [self startLocationRequest];
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
    return self.sortableBranchesAndPricesArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0){
        return 70;
    }
    return 130;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.row == 0) {
        
        static NSString *FirstCellIdentifier = @"TopCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:FirstCellIdentifier forIndexPath:indexPath];
        
        if (self.sortableBranchesAndPricesArray.count > 0) {
            cell.textLabel.text = self.sortableBranchesAndPricesArray[0][@"product_name"] ;
            cell.detailTextLabel.text = self.sortableBranchesAndPricesArray[0][@"Maybe some product detail"];
        }

        /*cell.imageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:@"http://www.animalspot.net/wp-content/uploads/2013/02/Rabbit.jpg"]]];*/
        
        return cell;
        
    } else {
        
        static NSString *CellIdentifier = @"ReusableCell";
        BranchWithProductPriceTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
        
        NSDictionary *resultDict = self.sortableBranchesAndPricesArray[indexPath.row-1];
        cell.storeLabel.text = resultDict[@"store_name"];
        cell.branchAddressLabel.text = resultDict[@"address"];
        cell.priceLabel.text = [NSString stringWithFormat:@"%@ TL", resultDict[@"price"]];
        double distance = [resultDict[@"distance"] floatValue];
//        NSNumberFormatter *fmt = [[NSNumberFormatter alloc] init];
//        [fmt setPositiveFormat:@"0.##"];
//        cell.distanceLabel.text = [NSString stringWithFormat:@"Appro. %@ km", [fmt stringFromNumber:self.distancesArray[indexPath.row-1]] ];
         cell.distanceLabel.text = [NSString stringWithFormat:@"Appro. %.2f km", distance ];
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
    }
}

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(userLocation.coordinate, 16000, 16000);
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

#pragma mark -
#pragma mark Navigation
- (IBAction)btnMapPressed:(id)sender {
    
    if (!self.isMapOnScreen) {

        [UIView transitionFromView:self.tableView
                            toView:self.mapView
                          duration:1
                           options:UIViewAnimationOptionTransitionFlipFromRight
                        completion:^(BOOL finished){
                            self.isMapOnScreen = YES;
                            UIButton* mapButton = (UIButton*)sender;
                            mapButton.titleLabel.text = @"List";
                        }];
    }
    else{
        [UIView transitionFromView:self.mapView
                            toView:self.tableView
                          duration:1
                           options:UIViewAnimationOptionTransitionFlipFromRight
                        completion:^(BOOL finished){
                            self.isMapOnScreen = NO;
                            UIButton* mapButton = (UIButton*)sender;
                            mapButton.titleLabel.text = @"Map";
                        }];
    }


}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    
    if([segue.identifier isEqualToString:@"MapForBranch"]){
        NSInteger row = [self.tableView indexPathForCell:sender].row;
        NSLog(@"did select row at indexpath.row: %ld",(long)row);
        self.selectedBranch = self.sortableBranchesAndPricesArray[row-1];
        MapForBranchViewController *mfbvc = (MapForBranchViewController *)segue.destinationViewController;
        mfbvc.selectedBranch = self.selectedBranch;
    }

}


@end
