//
//  BranchWithProductPrice.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "BranchWithProductPrice.h"

@implementation BranchWithProductPrice

- (id)initWithPrice:(NSInteger)price address:(NSString *)address lat:(NSInteger)lat lon:(NSInteger)lon storeName:(NSString*)storeName{
    self = [super init];
    if(self)
    {
        self.price = price;
        self.address = address;
        self.lat = lat;
        self.lon = lon;
        self.storeName = storeName;
    }
    return  self;
}

@end
