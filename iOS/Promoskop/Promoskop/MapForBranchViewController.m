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
    
    

    NSLog(@"Single branch id: %d",self.branchId);
    
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
