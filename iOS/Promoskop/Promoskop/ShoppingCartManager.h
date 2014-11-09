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
- (void) removeProductFromShoppingCart:(NSInteger)productId;
- (BOOL) isProductInShoppingCart:(NSInteger)productId;
+ (ShoppingCartManager *)manager;
- (BOOL)saveProductsInShoppingCart;
- (void)clearShoppingCart;

@end
