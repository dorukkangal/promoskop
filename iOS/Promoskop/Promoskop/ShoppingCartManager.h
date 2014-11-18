//
//  ShoppingCartManager.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 3.11.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ShoppingCartManager : NSObject

@property (nonatomic, strong, readonly) NSArray *productsArrayCurrentInShoppingBasket;

- (void) addProductToShoppingCart:(NSDictionary*)product;
- (void) removeProductFromShoppingCart:(NSString *)productId;
- (BOOL) isProductInShoppingCart:(NSString *)productId;
+ (ShoppingCartManager *)manager;
- (BOOL)saveProductsInShoppingCart;
- (void)clearShoppingCart;

@end
