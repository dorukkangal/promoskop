//
//  CheapestShoppingListHeaderView.h
//  Promoskop
//
//  Created by Mustafa Besnili on 09/11/14.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CheapestShoppingListHeaderView : UITableViewHeaderFooterView
@property (weak, nonatomic) IBOutlet UIView *containerView;
@property (weak, nonatomic) IBOutlet UILabel *subTotalPrice;
@property (weak, nonatomic) IBOutlet UILabel *storeWithBranchNameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *storeLogoImageView;
@end
