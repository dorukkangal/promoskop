//
//  BranchWithProductPriceTableViewCell.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 15.10.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BranchWithProductPriceTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *storeLabel;
@property (weak, nonatomic) IBOutlet UILabel *branchAddressLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UILabel *distanceLabel;

@end
