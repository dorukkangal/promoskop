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

@property (weak, nonatomic) IBOutlet MKMapView *mapView;

@end

@implementation MapForBranchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    

    NSLog(@"Branch: %@",self.selectedBranch);
    

    double lat =  [self.selectedBranch[@"latitude"] floatValue];
    double lon =  [self.selectedBranch[@"longitude"] floatValue];
    CLLocationCoordinate2D branchCoordinate = CLLocationCoordinate2DMake(lat, lon);
    
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(branchCoordinate, 16000, 16000);
    [self.mapView setRegion:[self.mapView regionThatFits:region] animated:NO];
    
    MKPointAnnotation *point = [[MKPointAnnotation alloc] init];

    point.coordinate = branchCoordinate;
    point.title = [NSString stringWithFormat:@"%@ TL",self.selectedBranch[@"price"]];
    point.subtitle = [NSString stringWithFormat:@"%@ : %@ ", self.selectedBranch[@"store_name"], self.selectedBranch[@"address"]];;
    [self.mapView addAnnotation:point];
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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