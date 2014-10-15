//
//  DataAccessLayer.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "DataAccessLayer.h"
#import "BranchWithProductPrice.h"

@implementation DataAccessLayer

static DataAccessLayer *_database;

+ (DataAccessLayer *)database{
    if(!_database)
        _database = [[DataAccessLayer alloc] init];
    return _database;
}


- (id)init
{
    self = [super init];
    if(self)
    {
        _database = [FMDatabase databaseWithPath:[self pathForDatabase]];
    }
    return self;
}

- (NSString *)pathForDatabase
{
    NSString *documentsDir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    return [documentsDir stringByAppendingPathComponent:@"Promoskop.sqlite"];
}

- (NSArray *)getBranchAndPriceDetailForProductWithId:(NSInteger)productId{
    NSMutableArray *BranchesWithProductPrices = [NSMutableArray array];
    if([_database open])
    {
        FMResultSet *resultSet = [_database executeQuery:@"SELECT * FROM Product p left join ProductBranch pb on (p.id = pb.product_id) left join Branch b on (pb.branch_id = b.id) inner join Store s on (b.store_id = s.id) where p.id = ?" ,[NSString stringWithFormat:@"%li",(long)productId]];
        while ([resultSet next]) {
            NSInteger price = [resultSet  doubleForColumn:@"price"];
            NSString *address = [resultSet stringForColumn:@"address"];
            NSInteger lat = [resultSet doubleForColumn:@"latitude"];
            NSInteger lon = [resultSet doubleForColumn:@"longitude"];
            NSString *storeName = [resultSet stringForColumn:@"name"];
            BranchWithProductPrice *bwpp = [[BranchWithProductPrice alloc] initWithPrice:price
                                                                                 address:address                                                                                           lat:lat                                                                                     lon:lon
                                                                               storeName:storeName];
            [BranchesWithProductPrices addObject:bwpp];
        }
    }
    [_database close];
    return BranchesWithProductPrices;
}

@end
