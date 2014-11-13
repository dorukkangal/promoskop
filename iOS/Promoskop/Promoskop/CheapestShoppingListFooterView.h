//
//  ShoppingListFooterView.h
//  Promoskop
//
//  Created by Ceyhun OZUGUR on 13.11.2014.
//  Copyright (c) 2014 Mustafa Besnili. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CheapestShoppingListFooterView : UITableViewHeaderFooterView
@property (weak, nonatomic) IBOutlet UILabel *totalPrice;
@property (weak, nonatomic) IBOutlet UILabel *totalOnlyMigrosPrice;
@property (weak, nonatomic) IBOutlet UILabel *totalOnlyCarrrefourPrice;
@property (weak, nonatomic) IBOutlet UILabel *totalGain;

@end
