//
//  DataAccessLayer.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "DataAccessLayer.h"

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

- (void)copyDatabaseIfNeeded{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if(![fileManager fileExistsAtPath:[self pathForDatabase]]){
        NSString *dbPathFromApp =  [[NSBundle mainBundle] pathForResource:@"Promoskop" ofType:@"sqlite"];
        NSError *error = nil;
        [fileManager copyItemAtPath:dbPathFromApp toPath:[self pathForDatabase] error:&error];
    }
}

- (NSString *)pathForDatabase
{
    NSString *documentsDir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    return [documentsDir stringByAppendingPathComponent:@"Promoskop.sqlite"];
}

- (NSArray *)getBranchAndPriceDetailForProductWithId:(NSInteger)productId{
    NSMutableArray *branchesWithProductPrices = [NSMutableArray array];
    if([_database open])
    {
        FMResultSet *resultSet = [_database executeQuery:@"SELECT p.name as product_name,p.url, pb.product_id,pb.branch_id, pb.price, b.name as branch_name, b.address, b.latitude,b.longitude, b.store_id, s.name as store_name FROM Product p left join ProductBranch pb on (p.id = pb.product_id) left join Branch b on (pb.branch_id = b.id) inner join Store s on (b.store_id = s.id) where p.id = ?" ,[NSString stringWithFormat:@"%li",(long)productId]];
        while ([resultSet next]) {
            NSDictionary *dictionary = [resultSet resultDictionary];
            [branchesWithProductPrices addObject:dictionary];
        }
    }
    [_database close];
    return branchesWithProductPrices;
}

- (NSArray *)searchProductWithName:(NSString *)texte{
    NSMutableArray *products = [NSMutableArray array];
    if([_database open]){
        FMResultSet *resultSet = [_database executeQuery:@"SELECT * FROM Product WHERE upper(name) like ?",[[NSString stringWithFormat:@"%%%@%%",texte] uppercaseStringWithLocale:[NSLocale localeWithLocaleIdentifier:@"TR_tr"]] ];
        while ([resultSet next]) {
            NSDictionary *dictionary = [resultSet resultDictionary];
            [products addObject:dictionary];
        }
    }
    [_database close];
    return [products copy];
}

- (NSInteger)fetchProductIdForGivenBarcode:(NSString *)scannedBarcode{
#pragma mark unimplemented method  returns static variable
    return 8;
}

@end
