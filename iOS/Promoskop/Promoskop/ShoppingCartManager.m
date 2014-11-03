//
//  ShoppingCartManager.m
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 3.11.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "ShoppingCartManager.h"


@interface ShoppingCartManager ()

@property (nonatomic, strong, readwrite) NSMutableArray *productsMutableArrayCurrentlyInShoppingCart;
@property (nonatomic, strong) NSString *pathForShoppingCart;

@end

@implementation ShoppingCartManager


- (id)init{
    self = [super init];
    if(self){
        self.productsMutableArrayCurrentlyInShoppingCart = [NSMutableArray array];
    }
    return self;
}

+ (ShoppingCartManager *)manager{
    static ShoppingCartManager *m = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        m = [[ShoppingCartManager alloc]init];
    });
    return m;
}

- (NSString *)pathForShoppingCart{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    NSLog(@"Shopping cart folder: %@", documentsDirectory);
    
    return [documentsDirectory stringByAppendingPathComponent:@"shoppingcart.plist"];
}

//+ (NSArray*)getShoppingCart{
//    
//    NSString *path = [self getShoppingCartPath];
//    NSFileManager *fileManager = [NSFileManager defaultManager];
//    
//    if ([fileManager fileExistsAtPath: path])
//    {
//        return [[NSArray alloc] initWithContentsOfFile:path];
//    }
//    else
//    {
//        // If the file doesn’t exist, create an empty dictionary
//        NSArray *cart = [[NSMutableArray alloc] init];
//        return cart;
//    }
//}

- (void) addProductToShoppingCart:(NSDictionary*)product{

    [self.productsMutableArrayCurrentlyInShoppingCart addObject:product];
//    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
//    [cart addObject:product];
//    [cart writeToFile:[self getShoppingCartPath] atomically:YES];
}

- (void) removeProductFromShoppingCart:(NSInteger)productId{
    
//    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
    
    for (NSDictionary* product in self.productsMutableArrayCurrentlyInShoppingCart) {
        if ([product[@"product_id"] integerValue] == productId) {
            [self.productsMutableArrayCurrentlyInShoppingCart removeObject:product];
            break;
        }
    }
//    [cart writeToFile:[self getShoppingCartPath] atomically:YES];
}

- (BOOL) isProductInShoppingCart:(NSInteger)productId{
    
//    NSMutableArray* cart = [[self getShoppingCart] mutableCopy];
    
    for (NSDictionary* product in self.productsMutableArrayCurrentlyInShoppingCart) {
        if ([product[@"product_id"] integerValue] == productId) {
            return YES;
        }
    }
    
    return NO;
}

- (NSArray *)productsArrayCurrentInShoppingBasket{
    return [self.productsMutableArrayCurrentlyInShoppingCart copy];
}


- (BOOL)saveProductsInShoppingCart{
    return [self.productsMutableArrayCurrentlyInShoppingCart writeToFile:self.pathForShoppingCart atomically:YES];
}

@end
