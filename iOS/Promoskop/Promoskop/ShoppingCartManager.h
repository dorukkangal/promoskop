//
//  ShoppingCartManager.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 3.11.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ShoppingCartManager : NSObject


#warning It's here so that cey learns smthing, eueuheuehe:-)
// That's called an NSArray property backed by NSMutableArray,
@property (nonatomic, strong, readonly) NSArray *productsArrayCurrentInShoppingBasket;

- (void) addProductToShoppingCart:(NSDictionary*)product;
- (void) removeProductFromShoppingCart:(NSInteger)productId;
- (BOOL) isProductInShoppingCart:(NSInteger)productId;
+ (ShoppingCartManager *)manager;

@end
