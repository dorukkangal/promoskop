//
//  BranchWithProductPrice.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BranchWithProductPrice : NSObject

@property (nonatomic) NSInteger price;
@property (strong, nonatomic) NSString *address;
@property (nonatomic) NSInteger lat;
@property (nonatomic) NSInteger lon;
@property (strong, nonatomic) NSString *storeName;

- (id)initWithPrice:(NSInteger)price
            address:(NSString *)address
                lat:(NSInteger)lat
                lon:(NSInteger)lon
          storeName:(NSString*)storeName;
@end
