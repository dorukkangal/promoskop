//
//  PopularProductCollectionViewCell.h
//  Promoskop
//
//  Created by Mustafa Besnili on 08/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@class PopularProductCollectionViewCell;

@protocol CustomCollectionCellDelegate <NSObject>

- (void)addToBasketButtonTappedIn:(PopularProductCollectionViewCell *)cell;

@end

@interface PopularProductCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *productImageView;
@property (weak, nonatomic) IBOutlet UILabel *productNameLabel;
@property (weak, nonatomic) IBOutlet UIButton *shoppingBasketButton;
@property (weak, nonatomic) id<CustomCollectionCellDelegate> delegate;
- (IBAction)addToBasket:(id)sender;
@end
