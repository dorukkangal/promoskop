//
//  MapForBranchesViewController.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 16.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "MapForBranchViewController.h"
#import <MapKit/MapKit.h>

@interface MapForBranchViewController ()

@property (weak, nonatomic) IBOutlet UILabel *storeName;
@property (weak, nonatomic) IBOutlet UILabel *storeAddress;
@property (weak, nonatomic) IBOutlet MKMapView *mapView;
@property(strong,nonatomic) MKPointAnnotation *point;

@end

@implementation MapForBranchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    

    NSLog(@"Branch: %@",self.selectedBranch);
    self.storeName.text = [NSString stringWithFormat:@"%@-%@",self.selectedBranch[@"store_name"],self.selectedBranch[@"branch_name"]] ;
    self.storeAddress.text = self.selectedBranch[@"branch_address"];
    
    double lat =  [self.selectedBranch[@"latitude"] floatValue];
    double lon =  [self.selectedBranch[@"longitude"] floatValue];
    CLLocationCoordinate2D branchCoordinate = CLLocationCoordinate2DMake(lat, lon);
    
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(branchCoordinate, 16000, 16000);
    [self.mapView setRegion:[self.mapView regionThatFits:region] animated:NO];
    
    
    self.point = [[MKPointAnnotation alloc] init];
    
    self.point.coordinate = branchCoordinate;
    self.point.title = [NSString stringWithFormat:@"%@ TL",self.selectedBranch[@"price"]];
    double distance = [self.selectedBranch[@"distance"] floatValue];
    self.point.subtitle = [NSString stringWithFormat:@"Appro. %.2f km", distance ];
    [self.mapView addAnnotation:self.point];
    [self.mapView selectAnnotation:[[self.mapView annotations] lastObject] animated:NO];
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)directionsToHerePressed:(id)sender {
    
    MKPlacemark* placemark = [[MKPlacemark alloc] initWithCoordinate:self.point.coordinate addressDictionary:nil];
    
    MKMapItem *mapItem = [[MKMapItem alloc] initWithPlacemark:placemark];
    mapItem.name = [NSString stringWithFormat:@"%@-%@",self.selectedBranch[@"store_name"],self.selectedBranch[@"branch_name"]] ;
    [mapItem openInMapsWithLaunchOptions:@{MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeWalking}];
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
