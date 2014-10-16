//
//  MapForBranchesViewController.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 16.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "MapForBranchesViewController.h"
#import <MapKit/MapKit.h>

@interface MapForBranchesViewController ()
@property (strong, nonatomic) IBOutlet MKMapView *mapView;

@end

@implementation MapForBranchesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    
    if (self.singleBranchId > -1) {
        NSLog(@"Single branch id: %d",self.singleBranchId);
    }
    else{

        //for (NSDictionary* dict in self.branchesAndPricesArray) {
            //normally will get all branches colorize the ones with green that have the product
        //}
        
    }

    
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
