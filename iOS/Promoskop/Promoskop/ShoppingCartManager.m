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
        self.productsMutableArrayCurrentlyInShoppingCart = [self getShoppingCart];
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
    return [documentsDirectory stringByAppendingPathComponent:@"shoppingcart.plist"];
}

- (NSMutableArray *)getShoppingCart{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    return [fileManager fileExistsAtPath:self.pathForShoppingCart] ? [[NSMutableArray alloc] initWithContentsOfFile:self.pathForShoppingCart] : [NSMutableArray  array];
}

- (void) addProductToShoppingCart:(NSDictionary*)product{
    [self.productsMutableArrayCurrentlyInShoppingCart addObject:product];
}

- (void) removeProductFromShoppingCart:(NSString *)productId{
    for (NSDictionary* product in self.productsMutableArrayCurrentlyInShoppingCart) {
        if([product[@"barcode_id"] respondsToSelector:@selector(isEqualToString:)]){
            if ([product[@"barcode_id"]  isEqualToString:productId]) {
                [self.productsMutableArrayCurrentlyInShoppingCart removeObject:product];
                break;
            }
        }
        else{
            if ([[product[@"barcode_id"] stringValue] isEqualToString:productId]) {
                [self.productsMutableArrayCurrentlyInShoppingCart removeObject:product];
                break;
            }
        }
    }
}

- (BOOL) isProductInShoppingCart:(NSString *)productId{
    for (NSDictionary* product in self.productsMutableArrayCurrentlyInShoppingCart) {
        if([product[@"barcode_id"] isKindOfClass:[NSString class]]){
            if([product[@"barcode_id"] isEqualToString:productId]){
                return YES;
            }
        }
        else if([product[@"barcode_id"] isKindOfClass:[NSNumber class]]){
            if([[product[@"barcode_id"]  stringValue] isEqualToString:productId])
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

- (void)clearShoppingCart{
    [self.productsMutableArrayCurrentlyInShoppingCart removeAllObjects];
}

@end
