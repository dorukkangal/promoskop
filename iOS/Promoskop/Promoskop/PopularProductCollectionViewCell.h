//
//  PopularProductCollectionViewCell.h
//  Promoskop
//
//  Created by Mustafa Besnili on 08/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PopularProductCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *productImageView;
@property (weak, nonatomic) IBOutlet UILabel *productNameLabel;
@property (weak, nonatomic) IBOutlet UIButton *shoppingBasketButton;

@end
