//
//  ShoppingCartManager.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 3.11.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ShoppingCartManager.h"

@implementation ShoppingCartManager

+ (NSString*)getShoppingCartPath{
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    NSLog(@"Shopping cart folder: %@", documentsDirectory);
    
    return [documentsDirectory stringByAppendingPathComponent:@"shoppingcart.plist"];
    
}

+ (NSArray*)getShoppingCart{
    
    NSString *path = [self getShoppingCartPath];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    if ([fileManager fileExistsAtPath: path])
    {
        return [[NSArray alloc] initWithContentsOfFile:path];
    }
    else
    {
        // If the file doesn’t exist, create an empty dictionary
        NSArray *cart = [[NSMutableArray alloc] init];
        return cart;
    }
    
    
   
}

+ (void) addProductToShoppingCart:(NSDictionary*)product{
    
    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
    [cart addObject:product];
    [cart writeToFile:[self getShoppingCartPath] atomically:YES];
    
}

+ (void) removeProductFromShoppingCart:(NSInteger)productId{
    
    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
    
    for (NSDictionary* product in cart) {
        if ([product[@"product_id"] integerValue] == productId) {
            [cart removeObject:product];
            break;
        }
    }
    
    [cart writeToFile:[self getShoppingCartPath] atomically:YES];
    
}

+ (BOOL) isProductInShoppingCart:(NSInteger)productId{
    
    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
    
    for (NSDictionary* product in cart) {
        if ([product[@"product_id"] integerValue] == productId) {
            
            return YES;
        }
    }
    
    return NO;
}


@end
