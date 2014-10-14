//
//  DataAccessLayer.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FMDatabase.h"

@interface DataAccessLayer : NSObject
{
    FMDatabase *_database;
}
+ (DataAccessLayer *)database;
- (NSArray *)getBranchAndPriceDetailForProductWithId:(NSInteger)productId;

@end
