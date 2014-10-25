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
#import "SMCalloutView.h"
#import <MBProgressHUD.h>
#import "ASMediaFocusManager.h"

#define TABLEVIEW_FIRST_CELL_HEIGHT 70
#define TABLEVIEW_CELL_HEIGHT 130
#define MKPINANNOTATION_CALLOUT_CONTENT_VIEW_SIZE 120

@interface ProductWithPriceDetailViewController ()<MKMapViewDelegate,SMCalloutViewDelegate,ASMediasFocusDelegate>


@property (nonatomic, strong) NSMutableArray *sortableBranchesAndPricesArray;
@property (nonatomic, weak ) IBOutlet UITableView *tableView;
@property (nonatomic, assign) NSInteger locationRequestID;
@property (nonatomic,weak) IBOutlet MKMapView *mapView;
@property (nonatomic, strong) NSDictionary *responseDict;
@property (nonatomic) BOOL isMapOnScreen;
@property (nonatomic, strong) CLLocation *currentLocation;
@property (nonatomic, strong) SMCalloutView *calloutView;
@property (nonatomic, strong) ASMediaFocusManager *mediaFocusManager;
@property (nonatomic) BOOL statusBarHidden;


@property (strong,nonatomic) NSDictionary* selectedBranch;

@end

@implementation ProductWithPriceDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    MBProgressHUD *hud =   [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.labelText = @"Fetching Data";
    hud.mode = MBProgressHUDModeIndeterminate;
    

    self.isMapOnScreen = NO;
    
    self.mapView.delegate = self;
    
    // create our custom callout view
    self.calloutView = [SMCalloutView platformCalloutView];
    self.calloutView.delegate = self;
    
    self.mediaFocusManager = [[ASMediaFocusManager alloc] init];
    self.mediaFocusManager.delegate = self;
    self.statusBarHidden = NO;

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
        
        
        [cell.imageView sd_setImageWithURL:[NSURL URLWithString:self.responseDict[@"product_url"]]placeholderImage:[UIImage imageNamed:@"placeholder"]];
//        cell.imageView.bounds = CGRectMake(0,0,75,75);
//        cell.imageView.frame = CGRectMake(0,0,75,75);
        cell.imageView.contentMode = UIViewContentModeScaleAspectFit;


        // Tells which views need to be focusable. You can put your image views in an array and give it to the focus manager.
        [self.mediaFocusManager installOnView:cell.imageView];

        
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

#pragma mark -
#pragma mark MapView Delegate Methods

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    
    if ([annotation isKindOfClass:[MKUserLocation class]]) {
        return nil;
    } else {
        
        // create a proper annotation view, be lazy and don't use the reuse identifier
        MKPinAnnotationView *view = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"reuseId"];
        
        // if we're using SMCalloutView, we don't want MKMapView to create its own callout!
        view.canShowCallout = NO;
        
        
        return view;
    }

}

- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)annotationView {
    
    // apply the MKAnnotationView's basic properties
//    self.calloutView.title = annotationView.annotation.title;
//    self.calloutView.subtitle = annotationView.annotation.subtitle;
    
    
    UIView *contentView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, MKPINANNOTATION_CALLOUT_CONTENT_VIEW_SIZE, MKPINANNOTATION_CALLOUT_CONTENT_VIEW_SIZE)];
    
    UILabel *title = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, MKPINANNOTATION_CALLOUT_CONTENT_VIEW_SIZE, 21)];
    title.text = annotationView.annotation.title;
    [contentView addSubview:title];
    
    UILabel *subtitle = [[UILabel alloc] initWithFrame:CGRectMake(0, 17, MKPINANNOTATION_CALLOUT_CONTENT_VIEW_SIZE, 100)];
    subtitle.text = annotationView.annotation.subtitle;
    subtitle.numberOfLines = 0;
    subtitle.font = [subtitle.font fontWithSize:13];
    [contentView addSubview:subtitle];
    
    
    self.calloutView.contentView = contentView;
    
    
    
    // Apply the MKAnnotationView's desired calloutOffset (from the top-middle of the view)
    self.calloutView.calloutOffset = annotationView.calloutOffset;
    
    
    // iOS 7 only: Apply our view controller's edge insets to the allowable area in which the callout can be displayed.
    if (floor(NSFoundationVersionNumber) > NSFoundationVersionNumber_iOS_6_1)
        self.calloutView.constrainedInsets = UIEdgeInsetsMake(self.topLayoutGuide.length, 0, self.bottomLayoutGuide.length, 0);
    
    if (![annotationView.annotation isKindOfClass:[MKUserLocation class]]) {
        // This does all the magic.
        [self.calloutView presentCalloutFromRect:annotationView.bounds inView:annotationView constrainedToView:self.view animated:YES];
    }

}

- (void)mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view {
    
    
    [self.calloutView dismissCalloutAnimated:YES];
}

//
// SMCalloutView delegate methods
//

- (NSTimeInterval)calloutView:(SMCalloutView *)calloutView delayForRepositionWithSize:(CGSize)offset {
    
    // When the callout is being asked to present in a way where it or its target will be partially offscreen, it asks us
    // if we'd like to reposition our surface first so the callout is completely visible. Here we scroll the map into view,
    // but it takes some math because we have to deal in lon/lat instead of the given offset in pixels.
    
    CLLocationCoordinate2D coordinate = self.mapView.centerCoordinate;
    
    // where's the center coordinate in terms of our view?
    CGPoint center = [self.mapView convertCoordinate:coordinate toPointToView:self.view];
    
    // move it by the requested offset
    center.x -= offset.width;
    center.y -= offset.height;
    
    // and translate it back into map coordinates
    coordinate = [self.mapView convertPoint:center toCoordinateFromView:self.view];
    
    // move the map!
    [self.mapView setCenterCoordinate:coordinate animated:YES];
    
    // tell the callout to wait for a while while we scroll (we assume the scroll delay for MKMapView matches UIScrollView)
    return kSMCalloutViewRepositionDelayForUIScrollView;
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
        self.mapView.hidden = NO;
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
        self.mapView.hidden = YES;
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


#pragma mark - ASMediaFocusDelegate
// Returns an image view that represents the media view. This image from this view is used in the focusing animation view. It is usually a small image.
- (UIImageView *)mediaFocusManager:(ASMediaFocusManager *)mediaFocusManager imageViewForView:(UIView *)view
{
    return (UIImageView *)view;
}

// Returns the final focused frame for this media view. This frame is usually a full screen frame.

- (CGRect)mediaFocusManager:(ASMediaFocusManager *)mediaFocusManager finalFrameForView:(UIView *)view
{
//    CGFloat navHeight = self.navigationController.navigationBar.frame.size.height;
//    CGRect viewBounds = self.view.bounds;
//    return CGRectMake(viewBounds.origin.x, viewBounds.origin.y+navHeight, viewBounds.size.width, viewBounds.size.height - navHeight);
    
    return self.view.bounds;
}

// Returns the view controller in which the focus controller is going to be added.
// This can be any view controller, full screen or not.
- (UIViewController *)parentViewControllerForMediaFocusManager:(ASMediaFocusManager *)mediaFocusManager
{
    return self;
}

// Returns an URL where the image is stored. This URL is used to create an image at full screen. The URL may be local (file://) or distant (http://).
- (NSURL *)mediaFocusManager:(ASMediaFocusManager *)mediaFocusManager mediaURLForView:(UIView *)view
{
    return [NSURL URLWithString:self.responseDict[@"product_url"]];
}

// Returns the title for this media view. Return nil if you don't want any title to appear.
- (NSString *)mediaFocusManager:(ASMediaFocusManager *)mediaFocusManager titleForView:(UIView *)view
{
    return nil;
}

- (void)mediaFocusManagerWillAppear:(ASMediaFocusManager *)mediaFocusManager
{
    [self toggleStatusBarHiddenWithAppearanceUpdate:NO];
    [self toggleNavigationBarHiddenAnimated:YES];
}

- (void)mediaFocusManagerWillDisappear:(ASMediaFocusManager *)mediaFocusManager
{
    [self toggleNavigationBarHiddenAnimated:YES];
    [self toggleStatusBarHiddenWithAppearanceUpdate:YES];
}

- (BOOL)prefersStatusBarHidden
{
    return self.statusBarHidden;
}

- (void)toggleStatusBarHiddenWithAppearanceUpdate:(BOOL)updateAppearance
{
    self.statusBarHidden = !self.statusBarHidden;
    
    if (updateAppearance) {
        [UIView animateWithDuration:UINavigationControllerHideShowBarDuration animations:^{
            [self setNeedsStatusBarAppearanceUpdate];
        }];
    }
}

- (void)toggleNavigationBarHiddenAnimated:(BOOL)animated
{
    [self.navigationController
     setNavigationBarHidden:!self.navigationController.navigationBarHidden
     animated:animated];
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
        [UIView animateWithDuration:.5f animations:^{
            self.tableView.hidden = NO;
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                dispatch_async(dispatch_get_main_queue(), ^{
                  
                    [MBProgressHUD hideHUDForView:self.view animated:YES];
                });
            });
        } completion:^(BOOL finished) {

        }];
        
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
