//
//  PopularProductCollectionViewCell.m
//  Promoskop
//
//  Created by Mustafa Besnili on 08/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import "PopularProductCollectionViewCell.h"

@implementation PopularProductCollectionViewCell

- (IBAction)addToBasket:(UIButton *)sender {
    sender.selected = !sender.selected;
    if([self.delegate respondsToSelector:@selector(addToBasketButtonTappedIn:)])
       [self.delegate addToBasketButtonTappedIn:self];
}
@end
